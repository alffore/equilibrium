package com.alfonso.equilibriod.modelo;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

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
     * @param canvas
     */
    public void draw(Canvas canvas){



        Paint paint = new Paint();
        paint.setColor(Color.BLACK);




        for(Object oseg: mSeg.values()){
            Segmento seg=(Segmento) oseg;

            canvas.drawLine((float)seg.na.x+width/2,-(float)seg.na.y+height/2,(float)seg.nb.x+width/2,-(float)seg.nb.y+height/2,paint);
        }
    }

}
