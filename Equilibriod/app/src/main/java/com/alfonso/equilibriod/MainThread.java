package com.alfonso.equilibriod;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.text.DecimalFormat;
import java.util.concurrent.CancellationException;

/**
 * Created by alfonso on 15/06/15.
 */
public class MainThread extends Thread {

    private static final String TAG = MainThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;
    private boolean running;


    private final static int MAX_PFS = 50; //fps deseables
    private final static int MAX_FRAME_SKIPS = 5; //cantidad de frames perdidos
    private final static int FRAME_PERIOD = 1000 / MAX_PFS; //periodo de frame

    //cosas de estadística
    private DecimalFormat df = new DecimalFormat("0.##"); //formateo de 2 decimales

    private final static int STAT_INTERVAL = 1000; //ms

    //calculo historico
    private final static int FPS_HISTORY_NR = 10;

    //dato de ultimo registro
    private long lastStatusStore = 0l;
    //status del contador de tiempo
    private long statusIntervalTimer = 0l;
    //cantidad total de frames perdidos
    private long totalFramesSkipped = 0l;
    //cantidad de frames perdidos en un ciclo (1 seg)
    private long framesSkippedPerStatCycle = 0l;

    //cantidad de frames rendereados en un intervalo
    private int frameCountPerStatCycle = 0;
    private long totalFrameCount = 0l;
    //el valor ultimo FPS
    private double fpsStore[];
    //cantidad de veces que la estadistica se leyo
    private long statsCount = 0;
    //promedio de FPS desde que el juego empezo
    private double averageFps = 0.0;

    /**
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @param surfaceHolder
     * @param gamePanel
     */
    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    /**
     *
     */
    @Override
    public void run() {
        Canvas canvas;

        double h = 0.01;

        Log.d(TAG, "Empezando el loop del juego");
        initTimingElements();

        long beginTime; //tiempo cuando el ciclo empieza
        long timeDiff; //tiempo que tardo el ciclo en ejecutarse
        int sleepTime; // ms para dormir (<0 si empezamos)
        int framesSkipped; //numero de frames perdidos

        sleepTime = 0;

        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;

                    //actualiza el estado
                    gamePanel.hm.calPos(h);
                    //gamePanel.tm.calPos(h);

                    //pinta el estado a pantalla
                    this.gamePanel.render(canvas);

                    //calcula la diferencia de tiempo que tomo el calculo y pintado
                    timeDiff = System.currentTimeMillis() - beginTime;
                    //calculamos el tiempo para dormir
                    sleepTime = (int) (FRAME_PERIOD - timeDiff);

                    if (sleepTime > 0) {
                        try {
                            //duerme la tarea para salvar bateria
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                    }

                    //en caso de problemas actualizamos sin pintar
                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        gamePanel.hm.calPos(h);
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                    }

                    if (framesSkipped > 0) {
                        Log.d(TAG, "Skipped: " + framesSkipped);
                    }

                    //estadisticas
                    framesSkippedPerStatCycle += framesSkipped;
                    storeStats();

                }
            } finally {
                //en caso de excepcion la superficie
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }


    }


    private void storeStats() {
        frameCountPerStatCycle++;
        totalFrameCount++;

        //checa el tiempo actual
        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {

            //calcula el FPS actual por intervalo
            double actualFps = (double) (frameCountPerStatCycle / (STAT_INTERVAL / 1000));

            //guarda el ultimo fps en el array
            fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

            //aumentamos la veces de este calculo
            statsCount++;
            double totalFps = 0.0;
            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                totalFps += fpsStore[i];
            }

            //obtenemos el promedio
            if (statsCount < FPS_HISTORY_NR) {
                //en caso de los primeros 10
                averageFps = totalFps / statsCount;
            } else {
                averageFps = totalFps / FPS_HISTORY_NR;
            }

            //guardando el numero total de frames perdidos
            totalFramesSkipped += framesSkippedPerStatCycle;

            //reset los contadores despues de un status record (1 seg)
            framesSkippedPerStatCycle = 0;
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;

            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;
            Log.d(TAG, "<FPS>: " + df.format(averageFps));
            gamePanel.setAvgFps("FPS: " + df.format(averageFps));

        }
    }


    /**
     *
     */
    private void initTimingElements() {
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
        }
        Log.d(TAG + ".initElements()", "Inicializado");
    }
}
