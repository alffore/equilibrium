package com.alfonso.equilibriod.toymodel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.alfonso.equilibriod.modelo.NodoD;
import com.alfonso.equilibriod.modelo.Segmento;

/**
 * Created by alfonso on 23/06/15.
 */
public class TModelD {

    Context context;
    NodoD g;

    NodoD na;
    NodoD nb;

    Segmento s;


    double t;


    public TModelD(Context context) {
        this.context = context;
        creaNS();
    }


    private void creaNS() {
        na = new NodoD();
        na.x = 100;
        na.y = 100;
        na.m = 100;


        nb = new NodoD();
        nb.x = 150;
        nb.y = 400;
        nb.m = 120;

        s = new Segmento(na, nb);
        s.k = 400;
        s.longitud0 = 540;

        g = new NodoD();

        g.ax = 0;
        g.ay = -9.81;


    }


    public void calPos(double h) {
        t += h;
        calVel(h);

        na.x += h * na.vx;
        na.y += h * na.vy;

        nb.x += h * nb.vx;
        nb.y += h * nb.vy;

        s.actualiza();
    }


    private void calVel(double h) {

        calAcel();

        na.vx += h * na.ax;
        na.vy += h * na.ay;

        nb.vx += h * nb.ax;
        nb.vy += h * nb.ay;


    }

    private void calAcel() {
        double factor;

        factor = s.k * (s.longitud0 - s.obtenLongitud()) / (na.m * s.obtenLongitud());
        na.ax = factor * s.n.x;
        na.ay = factor * s.n.y;


        factor = s.k * (s.longitud0 - s.obtenLongitud()) / (nb.m * s.obtenLongitud());
        nb.ax = -factor * s.n.x;
        nb.ay = -factor * s.n.y;


        //campo gravitacional

        na.ax += g.ax;
        na.ay += g.ay;

        nb.ax += g.ax;
        nb.ay += g.ay;

    }

    public void draw(Canvas canvas) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);


        canvas.drawLine((float) na.x + w / 2, (float) na.y + h / 2, (float) nb.x + w / 2, (float) nb.y + h / 2, paint);

        paint.setColor(Color.RED);

        canvas.drawLine((float) na.x+w/2, (float) na.y+h/2, (float) (na.x+w/2+na.ax), (float) (na.y+h/2+na.ay), paint);
    }
}
