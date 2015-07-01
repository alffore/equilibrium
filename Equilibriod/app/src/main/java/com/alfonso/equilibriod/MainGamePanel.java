package com.alfonso.equilibriod;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.alfonso.equilibriod.modelo.HModelD;


/**
 * Created by alfonso on 15/06/15.
 */
public class MainGamePanel  extends SurfaceView implements SurfaceHolder.Callback{

    private  static final String TAG = MainGamePanel.class.getSimpleName();
    private MainThread thread;


    public HModelD hm;

    //public TModelD tm;

    private String avgFps;


    /**
     *
     * @param context
     */
    public MainGamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        thread=new MainThread(getHolder(),this);

        setFocusable(true);

        hm = new HModelD(context);
        //tm = new TModelD(context);
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

                hm.agregaPesoManoC(event.getX(),event.getY(),20.00);

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

    public void render(Canvas canvas){
        if(canvas!=null) {
            canvas.drawColor(Color.WHITE);
            hm.draw(canvas);
            //tm.draw(canvas);
            displayFps(canvas, avgFps);
        }
    }

    public void setAvgFps(String avgFps){
        this.avgFps=avgFps;
    }

    private void displayFps(Canvas canvas, String fps){
        if(canvas != null && fps!=null){
            Paint paint = new Paint();
            paint.setColor(Color.DKGRAY);
            canvas.drawText(fps,this.getWidth()-50,20,paint);
        }
    }
}
