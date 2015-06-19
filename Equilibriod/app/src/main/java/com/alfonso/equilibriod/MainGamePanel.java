package com.alfonso.equilibriod;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by alfonso on 15/06/15.
 */
public class MainGamePanel  extends SurfaceView implements SurfaceHolder.Callback{

    private  static final String TAG = MainGamePanel.class.getSimpleName();
    private MainThread thread;

    /**
     *
     * @param context
     */
    public MainGamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        thread=new MainThread(getHolder(),this);

        setFocusable(true);
    }

    /**
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    /**
     *
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     *
     * @param holder
     */
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

    /**
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(event.getY() > getHeight()-50){
                thread.setRunning(false);
                ((Activity)getContext()).finish();
            }else{
                Log.d(TAG,"Coords x="+event.getX()+ " y="+event.getY());
            }
        }


        return  super.onTouchEvent(event);
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected  void onDraw(Canvas canvas){

    }
}
