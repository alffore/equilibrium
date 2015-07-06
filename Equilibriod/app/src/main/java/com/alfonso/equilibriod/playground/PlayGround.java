package com.alfonso.equilibriod.playground;


import android.content.Context;
import android.graphics.Canvas;

import com.alfonso.equilibriod.modelo.HModelD;
import com.alfonso.equilibriod.modelo.PiedraModel;

/**
 * Created by alfonso on 5/07/15.
 */
public class PlayGround {

    Context context;


    HModelD hm;
    PiedraModel pcm;

    /**
     * @param context
     */
    public PlayGround(Context context) {

        this.context = context;

        hm = new HModelD(context);
        pcm = new PiedraModel(context);

        pcm.creaPesosC();
    }

    /**
     *
     * @param h
     */
    public void simulaA(double h) {
        hm.calPos(h);
        pcm.calPos(h*30);
    }


    public void interaccion(double x, double y){

        pcm.muevePC(x,y);
    }




    /**
     * @param canvas
     */
    public void draw(Canvas canvas) {
        hm.draw(canvas);
        pcm.draw(canvas);
    }
}
