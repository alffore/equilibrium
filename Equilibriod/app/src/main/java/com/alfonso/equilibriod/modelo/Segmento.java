package com.alfonso.equilibriod.modelo;


/**
 * Clase que define un segmento
 * Created by alfonso on 17/06/15.
 */
public class Segmento {

    public NodoD na;
    public NodoD nb;
    public double longitud0;

    public NodoD n;

    private double angulo;

    public String snom;

    public double k;

    /**
     * @param na
     * @param nb
     */
    public Segmento(NodoD na, NodoD nb) {
        this.na = na;
        this.nb = nb;

        n = new NodoD();

        actualiza();

        angulo = this.obtenAngulo();
    }

    public void actualiza() {
        n.x = nb.x - na.x;
        n.y = nb.y - na.y;
    }

    /**
     * @return
     */
    public double obtenLongitud() {

        return Math.sqrt(Math.pow(n.x, 2) + Math.pow(n.y, 2));

    }

    /**
     * Metodo que regresa el angulo del segmento
     *
     * @return
     */
    public double obtenAngulo() {
        angulo = Math.acos(n.x / this.obtenLongitud());
        return angulo;
    }
}
