package com.alfonso.equilibriod.modelo;

import com.alfonso.equilibriod.modelo.Nodo;

/**
 * Created by alfonso on 17/06/15.
 */
public class Segmento {

    public Nodo na;
    public Nodo nb;
    public double longitud;

    public double angulo;

    public String snom;


    public Segmento(Nodo na,Nodo nb){
        this.na=na;
        this.nb=nb;
        longitud=-1.0;
        angulo=0;
    }



    public double obtenLongitud(){
        if(longitud==-1.0){
            return Math.sqrt(Math.pow(na.x-nb.x,2)+Math.pow(na.y-nb.y,2));
        }else{
            return longitud;
        }
    }

}
