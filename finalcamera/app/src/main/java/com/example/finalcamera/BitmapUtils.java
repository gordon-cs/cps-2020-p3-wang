




package com.example.finalcamera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


      class BitmapUtils {


    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";


    /**
     * Resamples the captured photo to fit the screen for better memory usage.
     *
     * @param context   The application context.
     * @param photoPath The path of the photo to be resampled.
     * @return The resampled bitmap
     */


    /*  The logic for this method is from AdvancedAndroid_Emojify
     */
    static Bitmap resamplePho(Context context, String photoPath) {

        // Get device screen size information
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        int targetH = metrics.heightPixels;
        int targetW = metrics.widthPixels;

        // Get the dimensions of the original bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

         bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(photoPath);
    }

    /**
     * Creates the temporary image file in the cache directory.
     *
     * @return The temporary image file.
     * @throws IOException Thrown if there is an error creating the file
     */
    static File createNewTempPhoFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File saveDir = context.getExternalCacheDir();

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                saveDir      /* directory */
        );
    }

    /**
     * Deletes image file for a given path.
     *
     * @param  context The application context.
     * @param phoPath The path of the photo to be deleted.
     */
    static boolean deletePhoFile( Context context ,String phoPath) {

        // Get the file
        File file= new File(phoPath);

        // If there is an error deleting the file, show a Toast

        boolean deleted;
         if (deleted=false)
         { String error= " Can't delete ";

         }
         return deleted;
    }

    /**
     * Helper method for adding the photo to the system photo gallery so it can be accessed
     * from other apps.
     *
     * @param phoPath The path of the saved image
     */
    private static void systemgalleryAddPho(Context context, String phoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(phoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


    /**
     * Helper method for saving the image.
     *
     * @param context The application context.
     * @param photo   The image to be saved.
     * @return The path of the saved image.
     */
    static String savePhoto(Context context, Bitmap photo) {

        String savedphotoPath = null;

        // Create the new file in the external storage
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/MyCamera");
        boolean success=true;

        if (storageDir.exists()==false) {
            success = storageDir.mkdirs();
        }

        // Save the new Bitmap
        boolean ifcondition= success;

        if (ifcondition) {
            File imageFile = new File(storageDir, imageFileName);
            savedphotoPath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            systemgalleryAddPho(context, savedphotoPath);

            // Show a Toast with the save location
            // String savedMessage = context.getString(R.string.saved_message, savedImagePath);

        }

        return savedphotoPath;
    }

    /**
     *  method for sharing an photo.
     *
     * @param context   The image context.
     * @param photoPath The path of the photo be shared.
     */
    static void sharePhoto(Context context, String photoPath) {
        // Create the share intent and start the share activity
        File photoFile = new File(photoPath);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri photoURI = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, photoFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        context.startActivity(shareIntent);
    }


}