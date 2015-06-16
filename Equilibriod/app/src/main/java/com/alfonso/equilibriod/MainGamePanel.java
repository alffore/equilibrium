package com.alfonso.equilibriod;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by alfonso on 15/06/15.
 */
public class MainGamePanel  extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;

    public MainGamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        thread=new MainThread(getHolder(),this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry =true;

        while(retry){
            try{

                thread.join();
                retry=false;

            }catch (InterruptedException e){
                //tratando de apagar la tarea
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return  super.onTouchEvent(event);
    }

    @Override
    protected  void onDraw(Canvas canvas){

    }
}
