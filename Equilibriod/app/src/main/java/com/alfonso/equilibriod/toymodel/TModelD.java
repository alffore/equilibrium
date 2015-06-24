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

    int width;
    int height;


    public TModelD(Context context) {
        this.context = context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        creaNS();
    }


    private void creaNS() {
        na = new NodoD();
        na.x = 100;
        na.y = 100;
        na.vx=10;
        na.m = 100;


        nb = new NodoD();
        nb.x = 150;
        nb.y = 400;
        nb.vx=-100;
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
        this.enCaja();
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

    private void enCaja(){
        if(na.x>=width/2 || na.x<=-width/2){
            na.vx=-na.vx;
        }

        if(nb.x>=width/2 || nb.x<=-width/2){
            nb.vx=-nb.vx;
        }

        if(na.y<=-height/2 || nb.y<=-height/2){
            g.ay=9.81;
            na.x = 100;
            na.y = 100;
            nb.x = 150;
            nb.y = 400;

        }

        if(na.y>=height/2 || nb.y>=height/2){
            g.ay=-9.81;
            na.x = 100;
            na.y = 100;
            nb.x = 150;
            nb.y = 400;
        }
    }

    public void draw(Canvas canvas) {


        Paint paint = new Paint();
        paint.setColor(Color.BLACK);


        canvas.drawLine((float) na.x + width / 2, (float) na.y + height / 2, (float) nb.x + width / 2, (float) nb.y + height / 2, paint);

        paint.setColor(Color.RED);
        canvas.drawCircle((float) na.x + width / 2, (float) na.y + height / 2, 50, paint);
        //canvas.drawLine((float) na.x+w/2, (float) na.y+h/2, (float) (na.x+w/2+na.ax), (float) (na.y+h/2+na.ay), paint);

        paint.setColor(Color.BLUE);
        canvas.drawCircle((float) nb.x + width / 2,(float) nb.y + height / 2,25,paint);
    }
}
