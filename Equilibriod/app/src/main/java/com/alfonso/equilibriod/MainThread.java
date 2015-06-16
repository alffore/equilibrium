package com.alfonso.equilibriod;

import android.view.SurfaceHolder;

/**
 * Created by alfonso on 15/06/15.
 */
public class MainThread extends Thread {


    private boolean running;

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;

    public  MainThread(SurfaceHolder surfaceHolder,MainGamePanel gamePanel){
        super();
        this.surfaceHolder=surfaceHolder;
        this.gamePanel=gamePanel;
    }



    public void setRunning(boolean running){
        this.running=running;
    }

    @Override
    public void run(){
        while(running){
            //actualiza el estado
            //pinta el estado a pantalla
        }
    }

}
