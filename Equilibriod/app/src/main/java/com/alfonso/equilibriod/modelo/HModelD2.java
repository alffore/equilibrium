package com.alfonso.equilibriod.modelo;

import android.content.Context;

/**
 * Clase que genera un modelo dinamico del
 * Created by alfonso on 10/07/15.
 */
public class HModelD2 extends HModelD {

    private static final String TAG = HModelD2.class.getSimpleName();

    /**
     * @param context
     */
    public HModelD2(Context context) {
        super(context);
    }


    /**
     * Método que calcula la aceleración por Nodo en un recorrido loop
     */
    protected void calAcel() {

        for (Object onodo : mNod.values()) {
            NodoD ncal = (NodoD) onodo;

            ncal.ax = 0;
            ncal.ay = 0;

            if (ncal.id != 1 && ncal.id != 5) {
                for (Object oseg : mSeg.values()) {
                    Segmento segmento = (Segmento) oseg;

                    if (segmento.na.equals(ncal)) {
                        calculaAcelNaxSeg(ncal, segmento);
                    } else if (segmento.nb.equals(ncal)) {
                        calculaAcelNbxSeg(ncal, segmento);
                    }

                }

                //sumamos el efecto de gravedad y disipación de energía para todos los nodos excepto el 1 y el 5

                ncal.ax += g.ax - kvel * ncal.vx;
                ncal.ay += g.ay - kvel * ncal.vy;
            }
        }


    }

    /**
     * @param ncal
     * @param s
     */
    protected void calculaAcelNaxSeg(NodoD ncal, Segmento s) {

        double factor = s.k * (s.obtenLongitud() - s.longitud0) / (ncal.m * s.obtenLongitud());

        ncal.ax += factor * s.n.x;
        ncal.ay += factor * s.n.y;


    }

    /**
     * @param ncal
     * @param s
     */
    protected void calculaAcelNbxSeg(NodoD ncal, Segmento s) {
        double factor = s.k * (s.longitud0 - s.obtenLongitud()) / (ncal.m * s.obtenLongitud());

        ncal.ax += factor * s.n.x;
        ncal.ay += factor * s.n.y;

    }


}
