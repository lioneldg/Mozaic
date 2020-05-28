package com.freedroid.mozaik;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class IO_BitmapImage {



    public static Bitmap readImage(Context context, String name, int maxSize) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            File source = new File(Objects.requireNonNull(context).getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name);
            return getResizedBitmap(BitmapFactory.decodeFile(source.getPath()), maxSize);//affiche une image compressée pour ne pas trop charger l'application. Cette image est extraite avec BitmapFactory.decodeFile(
        } else return null;
    }

    public static Bitmap readImage(File source, int maxSize) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            return getResizedBitmap(BitmapFactory.decodeFile(source.getPath()), maxSize);//affiche une image compressée pour ne pas trop charger l'application. Cette image est extraite avec BitmapFactory.decodeFile(
        } else return null;
    }

    private static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static File saveImage (Context context, Bitmap bitmapSource, String name, int quality){
        File dest = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            dest = new File(Objects.requireNonNull(context).getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name);
            try(FileOutputStream fos = new FileOutputStream(dest)){
                bitmapSource.compress(Bitmap.CompressFormat.JPEG, quality, fos);         //CompressFormat.JPEG autorise à reduire la qualité contrairement à .PNG
                fos.flush();
            } catch (IOException e) { e.printStackTrace();
                Log.d("ERR",""+e.getMessage());}
        }
        else Toast.makeText(context, "Impossible d'accéder à la mémoire externe!!!", Toast.LENGTH_LONG).show();

        return dest;
    }
}