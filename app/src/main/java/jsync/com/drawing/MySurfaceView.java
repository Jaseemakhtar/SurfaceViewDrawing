package jsync.com.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.display.DisplayManager;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

/**
 * Created by jass on 2/7/18.
 */

public class MySurfaceView extends SurfaceView implements Runnable{
    private SurfaceHolder holder;
    private Context context;
    private Thread t;
    private Canvas canvas;
    private float circleR = 30;
    private float x = circleR * 2,  y = circleR * 2;
    private float xSpeed = 1;
    private float ySpeed = 1;
    private VelocityTracker velocityTracker;
    private boolean isRunning = true;
    private boolean isDragging = false;
    Ball ball1 = new Ball(5,5);

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    private void init(){
        holder = getHolder();
        this.context = context;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action){

            case MotionEvent.ACTION_DOWN:
                isRunning = false;

                if (velocityTracker == null){
                    velocityTracker = VelocityTracker.obtain();
                }else {
                    velocityTracker.clear();
                }
                return true;

            case MotionEvent.ACTION_UP:
                isRunning = true;
                isDragging = false;
                return true;

            case MotionEvent.ACTION_MOVE:

                float mouseX = event.getX();
                float mouseY = event.getY();

                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);

                if (mouseX >= x && mouseX <= x+circleR+circleR && mouseY >= y && mouseY <= y+circleR+circleR){
                    isDragging = true;
                }else if (isDragging){
                    x = mouseX;
                    y = mouseY;

                    xSpeed = velocityTracker.getXVelocity() / 100;
                    ySpeed = velocityTracker.getYVelocity() / 100;

                }

                return true;

        }
        return false;
    }

    public MySurfaceView(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        init();
    }


    @Override
    public void run() {

        while (true){

            if (!holder.getSurface().isValid())
                continue;

            if (isRunning) {
                canvas = holder.lockCanvas();

                float widthC = canvas.getWidth();
                float heightC = canvas.getHeight();

                if (x >= widthC - circleR || x <= circleR) {
                    xSpeed *= -1;
                }
                if (y >= heightC - circleR || y <= circleR) {
                    ySpeed *= -1;
                }

                x = xSpeed + x;
                y = ySpeed + y;

                canvas.drawColor(Color.BLACK);
                Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setAntiAlias(true);
                canvas.drawCircle(x, y, circleR, paint);
                holder.unlockCanvasAndPost(canvas);

            }else if(isDragging){
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.BLACK);
                Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setAntiAlias(true);
                canvas.drawCircle(x, y, circleR, paint);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void resume(){

        t = new Thread(this);
        t.start();

    }

    public void pause(){
        while (true){
            try {
                t.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        t = null;
    }

}
