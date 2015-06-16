package com.alfonso.equilibriod;

import android.util.Log;
import android.view.SurfaceHolder;

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



    public  MainThread(SurfaceHolder surfaceHolder,MainGamePanel gamePanel){
        super();
        this.surfaceHolder=surfaceHolder;
        this.gamePanel=gamePanel;
    }


    @Override
    public void run(){

        long tickCount =0L;

        Log.d(TAG, "Empezando el loop del juego");

        while(running){
            tickCount++;
            //actualiza el estado
            //pinta el estado a pantalla
        }

        Log.d(TAG, "El loop del juego se ejecuto"+tickCount+" veces");
    }

}
