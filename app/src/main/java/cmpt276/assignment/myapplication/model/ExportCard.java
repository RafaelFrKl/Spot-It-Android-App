/**
 * This class handles the logic to save Cards as bitmaps and export them as jpg images in the gallery
 */
package cmpt276.assignment.myapplication.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class ExportCard {
    //https://stackoverflow.com/questions/50438361/converting-cardview-from-recyclerview-to-a-bitmap/50438660
    public static Bitmap getBitmapFromCardView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    //https://stackoverflow.com/questions/7887078/android-saving-file-to-external-storage
    public static void saveToGallery(Bitmap bitmapImage) {
        //Directory
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + "/saved_images");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //File name
        Random generator = new Random();
        int n = 100000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";

        File file = new File (directory, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            System.out.println("IMAGE SAVED AT " + Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).getPath() + "/saved_images with name" + fname);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
