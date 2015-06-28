package com.alfonso.equilibriod.modelo;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;

import com.alfonso.equilibriod.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alfonso on 16/06/15.
 */
public class HModel {

    private static final String TAG = HModel.class.getSimpleName();

    protected Map mSeg;
    protected Map mNod;

    Context context;

    Resources res;

    int width;
    int height;


    /**
     * @param context
     */
    public HModel(Context context) {

        this.context = context;

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        mSeg = new HashMap();
        mNod = new HashMap();


        res = context.getResources();


        incializaNodos();
        incializaSegmentos();

        escalaModelo();
    }

    /**
     * Este método incializa la posicion de los nodos
     */
    private void incializaNodos() {


        try {

            InputStream is = res.openRawResource(R.raw.config_nodo);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                NodoD naux = creaNodo(readString);

                mNod.put(naux.snom, naux);

                //Log.d(TAG,readString);

                readString = buffreader.readLine();
            }

            isr.close();
            is.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * @param scadena
     * @return
     */
    private NodoD creaNodo(String scadena) {
        String[] sv = scadena.split(",");

        NodoD naux = new NodoD();

        naux.snom = sv[0];
        naux.x = Double.parseDouble(sv[1]);
        naux.y = Double.parseDouble(sv[2]);
        naux.m = Double.parseDouble(sv[3]);
        naux.k = Double.parseDouble(sv[4]);
        naux.angulo0 =Double.parseDouble(sv[5])*Math.PI;

        return naux;
    }

    /**
     *
     */
    private void incializaSegmentos() {

        try {
            InputStream is = res.openRawResource(R.raw.config_seg);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                Segmento saux = creaSegmento(readString);

                mSeg.put(saux.snom, saux);

                readString = buffreader.readLine();
            }

            isr.close();
            is.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     *
     * @param scadena
     * @return
     */
    private Segmento creaSegmento(String scadena) {

        String[] sv = scadena.split(",");

        Segmento saux = new Segmento((NodoD) mNod.get(sv[1]), (NodoD) mNod.get(sv[2]));

        saux.snom = sv[0];
        saux.longitud0 = Double.parseDouble(sv[3]);
        saux.k = Double.parseDouble(sv[4]) ;

        return saux;
    }

    /**
     *
     */
    private void escalaModelo(){

        Segmento s78 = (Segmento) mSeg.get("78");
        Segmento s67 = (Segmento) mSeg.get("67");

        double longitud0_total=2*(s67.longitud0+s78.longitud0);
        double factorE=width/(2*longitud0_total);

        for(Object oseg: mSeg.values()){
            Segmento s=(Segmento)oseg;
            s.longitud0*=factorE;
        }
        Log.d(TAG,"Factor de Escala: "+factorE);
    }

    /**
     *
     * @param canvas
     */
    public void draw(Canvas canvas){

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        paint.setStrokeWidth(2.0f);
        //pinta los segmentos
        for(Object oseg: mSeg.values()){
            Segmento seg=(Segmento) oseg;

            canvas.drawLine((float)seg.na.x+width/2,(float)(-seg.na.y+height/2),(float)seg.nb.x+width/2,(float)(-seg.nb.y+height/2),paint);
        }

        //pinta manos
        paint.setColor(Color.BLUE);
        NodoD nmano=(NodoD)mNod.get("8");
        canvas.drawCircle((float) nmano.x + width / 2, (float) (-1*nmano.y + height / 2), 15, paint);

        paint.setColor(Color.GREEN);
        nmano=(NodoD)mNod.get("10");
        canvas.drawCircle((float) nmano.x + width / 2, (float) (-1*nmano.y + height / 2), 15, paint);

        //pinta Cabeza
        Segmento s36 =(Segmento)mSeg.get("36");
        NodoD n3= (NodoD)mNod.get("3");
        double x=n3.x+s36.n.x*1.2;
        double y=n3.y+s36.n.y*1.2;

        paint.setColor(Color.RED);
        canvas.drawCircle((float) (x + width / 2), (float) (-y + height / 2), 40, paint);

        //pinta pies
        paint.setColor(Color.BLACK);
        NodoD npie=(NodoD)mNod.get("1");
        canvas.drawCircle((float) npie.x + width / 2, (float) (-npie.y + height / 2), 10, paint);

        npie=(NodoD)mNod.get("5");
        canvas.drawCircle((float) npie.x + width / 2, (float) (-npie.y + height / 2), 10, paint);
    }

}
