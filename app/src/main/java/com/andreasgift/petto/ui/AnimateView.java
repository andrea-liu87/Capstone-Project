package com.andreasgift.petto.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.andreasgift.petto.R;

/**
 * Created by : AndreasGift
 * This view will generate the pet animation.
 */
public class AnimateView extends SurfaceView implements Runnable {

    private Thread gameThread = null;
    private final SurfaceHolder ourHolder;
    private volatile boolean isPlaying;

    private Canvas canvas;
    private final Paint paint;

    private final Bitmap[] bitmapCats = new Bitmap[2];
    private final Bitmap bitmapHappyCat;

    private boolean isMoving = false;

    private final float walkSpeed = 20;
    private final int timeMilisPerFrame = 250;

    private float catXPosition = 50;

    private final int bitmapWidth;
    public final int bitmapHeight;

    private int currentBitmapArray = 0;

    public final float screenWidth = getScreenWidth();


    public AnimateView (Context context){
        super(context);
        this.ourHolder = getHolder();
        this.paint = new Paint();
        bitmapCats[0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.cat_walk1);
        bitmapCats[1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.cat_walk2);
        bitmapHappyCat = BitmapFactory.decodeResource(this.getResources(), R.drawable.cat_happy1);

        bitmapWidth = bitmapCats[0].getWidth();
        bitmapHeight = bitmapCats[0].getHeight();
    }

    @Override
    public void run() {
        while(isPlaying){
            updateNextPosition();
            draw();
            try {
                Thread.sleep(timeMilisPerFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * As the pet is animated, this method will return bitmap for the next frame
     * @return  Bitmap for next animation frame
     */
    private Bitmap getNextBitmap(){
        if(isMoving) {
            currentBitmapArray++;
            if (currentBitmapArray >= bitmapCats.length) {
                currentBitmapArray = 0;
            }
        }
        return bitmapCats[currentBitmapArray];
    }


    /**
     * If the pet is in moving position, this method will return the next x coordinate
     */
    private void updateNextPosition() {
        if (isMoving) {
            if (catXPosition >= screenWidth){
                catXPosition = -bitmapWidth /2;
            } else {
            catXPosition = catXPosition + walkSpeed;
            }
        }
    }


    /**
     * Draw canvas method
     */
    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Bitmap bitmap = getNextBitmap();

            canvas.drawBitmap(bitmap,
                    catXPosition, 0, paint);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Draw happy cat when attribute icon is clicked
     */
    public void drawHappyCat() {

        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Bitmap bitmap = bitmapHappyCat;

            canvas.drawBitmap(bitmap,
                    catXPosition, 0, paint);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }



    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Detect screen touch for animation purpose
     * @param motionEvent
     * @return Whether screen is touched
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isMoving = true;
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }
        return true;
    }


    private float getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;}


    private float getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
