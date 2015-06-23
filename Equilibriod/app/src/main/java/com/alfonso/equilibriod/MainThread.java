package com.alfonso.equilibriod;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.concurrent.CancellationException;

/**
 * Created by alfonso on 15/06/15.
 */
public class MainThread extends Thread {

    private  static final String TAG = MainThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;
    private boolean running;

    public void setRunning(boolean running){
        this.running=running;
    }

    /**
     *
     * @param surfaceHolder
     * @param gamePanel
     */
    public  MainThread(SurfaceHolder surfaceHolder,MainGamePanel gamePanel){
        super();
        this.surfaceHolder=surfaceHolder;
        this.gamePanel=gamePanel;
    }


    @Override
    public void run(){
        Canvas canvas;
        long tickCount =0L;
        double h=0.001;

        Log.d(TAG, "Empezando el loop del juego");

        while(running){
            canvas=null;
            try {
                tickCount++;
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {


                    //actualiza el estado
                    //this.gamePanel.hm.calPos(h);
                    gamePanel.tm.calPos(h);

                    //pinta el estado a pantalla
                    this.gamePanel.onDraw(canvas);
                }
            }finally {
                //en caso de excepcion la superficie
                if(canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }

        Log.d(TAG, "El loop del juego se ejecuto: "+tickCount+" veces");
    }

}
