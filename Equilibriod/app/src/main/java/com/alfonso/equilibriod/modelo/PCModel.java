package com.alfonso.equilibriod.modelo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by alfonso on 5/07/15.
 */
public class PCModel {

    private static final String TAG = PCModel.class.getSimpleName();
    private double T;

    public ArrayList<PiedraCaja> apc;

    double pesoMAX;
    Context context;

    int width;
    int height;

    public NodoD g;

    public double kvel;


    /**
     * @param context
     */
    public PCModel(Context context) {
        T = 0;
        pesoMAX = 10000;
        this.context = context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        g = new NodoD();
        g.ax = 0;
        g.ay = -9.81 ;

        kvel = 1.0;

        apc = new ArrayList<>();

    }

    /**
     *
     */
    private void calculaAceleracion() {
        for (PiedraCaja pc : apc) {

            pc.ax = g.ax - kvel * pc.vx;
            pc.ay = g.ay - kvel * pc.vy;
        }

    }

    /**
     *
     * @param h
     */
    private void calculaVelocidad(double h) {
        calculaAceleracion();
        for (PiedraCaja pc : apc) {
            pc.vx += h * pc.ax;
            pc.vy += h * pc.ay;
        }
    }

    /**
     *
     * @param h
     */
    public void calPos(double h) {

        T += h;
        calculaVelocidad(h);
        for (PiedraCaja pc : apc) {
            pc.x += h * pc.vx;
            pc.y += h * pc.vy;
        }

    }


    /**
     *
     */
    public void creaPesosC() {

        double pesoacum = 0;

        while (pesoacum < pesoMAX) {

            int peso = randInt(100, 1000);

            pesoacum += peso;

            PiedraCaja pc = new PiedraCaja();
            pc.m = peso;
            pc.vx = 0;
            pc.vy = 0;
            pc.ax = 0;
            pc.ay = 0;

            pc.x = randInt(20-width/2, width - 20);
            pc.y = randInt(height, height*2);

            apc.add(pc);

        }

        Log.d(TAG, "Cantidad de pesos creados: " + apc.size());
    }


    /**
     * @param canvas
     */
    public void draw(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2.0f);

        for (PiedraCaja pc : apc) {

            canvas.drawCircle((float) pc.x + width / 2, (float) (-1 * pc.y + height / 2), 15, paint);

        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     * http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }
}
