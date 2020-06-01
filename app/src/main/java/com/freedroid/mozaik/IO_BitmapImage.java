package com.freedroid.mozaik;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class IO_BitmapImage {



    public static Bitmap readImage(Context context, String name, int maxSize) {
        File source = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            source = new File(Objects.requireNonNull(context).getExternalCacheDir(), name);
        }

        return getResizedBitmap(BitmapFactory.decodeFile(source.getPath()), maxSize);//affiche une image compressée pour ne pas trop charger l'application. Cette image est extraite avec BitmapFactory.decodeFile(
    }

    public static Bitmap readImage(File source, int maxSize) {
        return getResizedBitmap(BitmapFactory.decodeFile(source.getPath()), maxSize);//affiche une image compressée pour ne pas trop charger l'application. Cette image est extraite avec BitmapFactory.decodeFile(
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

    public static File saveImage (Context context, Bitmap bitmapSource, String name, boolean a4) throws IOException {
        File dest = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            if(!a4)dest = new File(Objects.requireNonNull(context).getExternalCacheDir(), name);
            else dest = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), name);

            try(FileOutputStream fos = new FileOutputStream(dest)){
                bitmapSource.compress(Bitmap.CompressFormat.JPEG, 100, fos);         //CompressFormat.JPEG autorise à reduire la qualité contrairement à .PNG
                fos.flush();
            } catch (IOException e) { e.printStackTrace(); }

        }

        return dest;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888); //ARGB_8888 Alpha Red Green Blue et chaque pixel enregistré dans 4 octets
        Canvas canvas = new Canvas(returnedBitmap);     //on attribue le Canvas au Bitmap créé
        Drawable bgDrawable = view.getBackground();     //on utilise la couleur du BG de la vue pour le BG de notre drawable
        if (bgDrawable!=null) bgDrawable.draw(canvas);  //on applique ce BG si il existe
        else canvas.drawColor(Color.WHITE);             //sinon on peint en blanc

        view.draw(canvas);                              //enfin on applique la vue au Canvas

        return returnedBitmap;
    }

    public static void deletePhotos(Context context, File[] sourcesFiles){
        int nbrFiles = 0;
        int delFiles = 0;
        for(int i = 0; i < sourcesFiles.length; i++){ //supprimer les fichiers photo enregistrés par l'application
            if(sourcesFiles[i] != null){
                nbrFiles++;
                if(sourcesFiles[i].delete()) delFiles++;
            }
        }
        int dif = nbrFiles - delFiles;
        if(dif != 0){
            Intent intent = new Intent(context, DialogInfo.class);
            String str = "Attention "+dif+" fichiers temporaires n'ont pas été supprimés\nVous pouvez le faire manuellement dans le dossier de cache de l'application:\nAndroid/data/com.freedroid.mozaik/cache";
            intent.putExtra("text", str);
            intent.putExtra("needResult", false);
            context.startActivity(intent);
        }
    }
}