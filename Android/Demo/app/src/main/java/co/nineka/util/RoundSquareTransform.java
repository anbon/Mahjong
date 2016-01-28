package co.nineka.util;

/**
 * Created by x51811danny on 2015/11/17.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.squareup.picasso.Transformation;

import co.nineka.App;

public class RoundSquareTransform implements Transformation {
    private final static String TAG = "RoundSquareTransform";
    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap bitmap;
        if(source.getWidth()>source.getHeight())
            bitmap = ScalePic(source, App.heightPixels);
        else
            bitmap = ScalePic( source, App.widthPixels);

        source.recycle();
        ///////////////////////////////////////////////////////////////
        /*int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();*/
        return bitmap;
    }

    @Override
    public String key() {
        return "RoundSquare";
    }

    private Bitmap ScalePic(Bitmap bitmap,int phone)
    {

        //縮放比例預設為1
        float mScale = 1 ;
        Log.i(TAG, "phone = " + phone);
        boolean isLandScape = bitmap.getWidth() > bitmap.getHeight();
        int size = isLandScape ? bitmap.getHeight() : bitmap.getWidth();
        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if(bitmap.getWidth() > App.widthPixels )
        {
            Log.i(TAG,"圖片寬度大於手機寬度 " + "Size = " + size);
            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();
            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);


            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    isLandScape?  (( bitmap.getWidth() - size) / 2) : 0,
                    isLandScape? 0 :  (( bitmap.getHeight() - size) / 2),
                    size,
                    size,
                    mMat,
                    false);
            return getRoundedCornerBitmap(
                    mScaleBitmap, (App.widthPixels<1080)?180.0f:300.0f);

        }
        else {
            Log.i(TAG, "圖片寬度小於手機寬度 " + "Size = " + size);
            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);
            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    isLandScape? (( bitmap.getWidth() - size) / 2) : 0,
                    isLandScape? 0 : (( bitmap.getHeight() - size) / 2),
                    size,
                    size,
                    mMat,
                    false);
            float round = (App.widthPixels<1080)?180.0f:300.0f;
            int phoneSize = App.widthPixels;
            Log.v(TAG,"round = "+round);
            round = round * ((float)size/(float)phoneSize);

            /*if(about_photo.getWidth()>size) {
                round = 30.0f * ((float) size / (float) about_photo.getWidth());
                about_photo.setImageBitmap(getRoundedCornerBitmap(
                        mScaleBitmap, round));
            }*/

            return (getRoundedCornerBitmap(mScaleBitmap, round ));

        }
    }
    //圓角轉換函式，帶入Bitmap圖片及圓角數值則回傳圓角圖，回傳Bitmap再置入ImageView
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
