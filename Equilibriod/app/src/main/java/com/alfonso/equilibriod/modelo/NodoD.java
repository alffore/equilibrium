package com.alfonso.equilibriod.modelo;

/**
 * Created by alfonso on 20/06/15.
 */
public class NodoD {

    public String snom;
    public double x;
    public double y;


    public double ax;
    public double ay;

    public double vx;
    public double vy;

    //masa del nodo
    public double m;

    //k del resorte en nodo
    public double k;

    //angulo de referencia para resortes en articulaciones
    public double angulo0;


    public NodoD(){
        x=0;
        y=0;

        vx=0;
        vy=0;

        ax=0;
        ay=0;

        angulo0=Math.PI;

        k=0;
    }
}
