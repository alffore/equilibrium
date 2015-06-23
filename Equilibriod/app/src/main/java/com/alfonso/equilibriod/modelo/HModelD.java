package com.alfonso.equilibriod.modelo;

import android.content.Context;


/**
 * Created by alfonso on 20/06/15.
 */
public class HModelD extends HModel {

    private static final String TAG = HModelD.class.getSimpleName();
    public NodoD g;
    public double t;

    public NodoD xuni;
    public NodoD yuni;

    public double kvel;

    /**
     * @param context
     */
    public HModelD(Context context) {
        super(context);

        g = new NodoD();
        g.x = 0;
        g.y = -9.81;



        xuni = new NodoD();
        xuni.x = 1;
        xuni.y = 0;

        yuni = new NodoD();
        yuni.x = 0;
        yuni.y = 1;

        kvel = 0.01;

    }


    /**
     * Método que calcula el producto punto entre 2 segmentos
     *
     * @param s1 Segmento 1
     * @param s2 Segmento 2
     * @return regresa el producto punto entre 2 segmentos
     */
    private double prodS(Segmento s1, Segmento s2) {
        return prod(s1.n, s2.n);
    }

    /**
     * Calcula el producto punto entre 2 nodos
     *
     * @param n1
     * @param n2
     * @return
     */
    private double prod(NodoD n1, NodoD n2) {
        return n1.x * n2.x + n1.y * n2.y;
    }


    /**
     * Método que invierte segmentos, rota 180 grados
     *
     * @param seg
     * @return Regresa un nodo rotado PI radianes
     */
    private NodoD invS(Segmento seg) {

        return rotS(seg, Math.PI);
    }

    /**
     * Método que rota un angulo arbitrario un Segmento y regresa un vector en la dirección
     *
     * @param seg
     * @param angulo
     * @return
     */
    private NodoD rotS(Segmento seg, double angulo) {
        NodoD naux = new NodoD();

        naux.x = seg.obtenLongitud() * Math.cos(seg.obtenAngulo() + angulo);
        naux.y = seg.obtenLongitud() * Math.sin(seg.obtenAngulo() + angulo);

        return naux;
    }

    /**
     * Método que calcula a nueva posición de los nodos con un paso de tiempo h
     *
     * @param h
     */
    public void calPos(double h) {
        t += h;
        calVel(h);

        for (Object onodo : mNod.values()) {
            NodoD nd = (NodoD) onodo;

            nd.x += h * nd.vx;
            nd.y += h * nd.vy;

        }

        //actualiza la información en segmentos

        for (Object oseg : mSeg.values()) {
            ((Segmento) oseg).actualiza();
        }
    }


    private void calVel(double h) {
        calAcel();
        for (Object onodo : mNod.values()) {
            NodoD nd = (NodoD) onodo;

            nd.vx += h * nd.ax;
            nd.vy += h * nd.ay;

        }

    }


    // cálculo de las aceleraciones instantaneas por nodo

    private void calAcel() {

        calcula_an8();
        calcula_an10();

        calcula_an7();
        calcula_an9();

        calcula_an6();

        calcula_an3();

        calcula_an2();
        calcula_an4();

        calcula_an5();
        calcula_an1();


        //sumamos el efecto de gravedad para todos los nodos excepto el 1 y el 5 y efecto de disipación de energía
        for (Object onodo : mNod.values()) {
            NodoD nd = (NodoD) onodo;
            if (!nd.snom.equals("1") && !nd.snom.equals("5")) {
                nd.ax += g.ax - kvel * nd.vx;
                nd.ay += g.ay - kvel * nd.vy;
            }
        }

    }

    private void calcula_an1() {

        NodoD n1 = (NodoD) mNod.get("1");

        n1.ax = 0;
        n1.ay = 0;
    }

    private void calcula_an2() {

        double factor;
        double gamma;


        NodoD n2 = (NodoD) mNod.get("2");
        NodoD n1 = (NodoD) mNod.get("1");
        NodoD n3 = (NodoD) mNod.get("3");

        Segmento s12 = (Segmento) mSeg.get("12");
        Segmento s23 = (Segmento) mSeg.get("23");
        Segmento s36 = (Segmento) mSeg.get("36");
        Segmento s34 = (Segmento) mSeg.get("34");


        factor = s12.k * (s12.longitud0 - s12.obtenLongitud()) / (n2.m * s12.obtenLongitud());
        n2.ax = factor * s12.n.x;
        n2.ay = factor * s12.n.y;


        factor = s23.k * (s23.longitud0 - s23.obtenLongitud()) / (n2.m * s23.obtenLongitud());
        n2.ax += -factor * s23.n.x;
        n2.ay += -factor * s23.n.y;

        /*gamma = Math.acos(prod(s12.n, xuni) / s12.obtenLongitud());
        NodoD rot_s12 = this.rotS(s12, -Math.PI / 2);
        factor = n1.k * (gamma - n1.angulo0) / (n2.m * s12.obtenLongitud());
        n2.ax += factor * rot_s12.x;
        n2.ay += factor * rot_s12.y;

        gamma = Math.acos(prod(invS(s23), s36.n) / (s23.obtenLongitud() * s36.obtenLongitud()));
        NodoD rot_s23 = rotS(s23, Math.PI / 2);
        factor = n3.k * (gamma - n3.angulo0) / (n2.m * s23.obtenLongitud());
        n2.ax += factor * rot_s23.x;
        n2.ay += factor * rot_s23.y;

        gamma = Math.acos(prod(invS(s23), s34.n) / (s23.obtenLongitud() * s34.obtenLongitud()));
        factor = n3.k * (gamma - n3.angulo0) / (n2.m * s23.obtenLongitud());
        n2.ax += -factor * rot_s23.x;
        n2.ay += -factor * rot_s23.y;*/
    }

    private void calcula_an3() {

        double factor;
        double gamma;

        NodoD n3 = (NodoD) mNod.get("3");
        NodoD n6 = (NodoD) mNod.get("6");
        Segmento s36 = (Segmento) mSeg.get("36");
        Segmento s23 = (Segmento) mSeg.get("23");
        Segmento s34 = (Segmento) mSeg.get("34");
        Segmento s67 = (Segmento) mSeg.get("67");
        Segmento s69 = (Segmento) mSeg.get("69");

        factor = s36.k * (s36.longitud0 - s36.obtenLongitud()) / (n3.m * s36.obtenLongitud());
        n3.ax = -factor * s36.n.x;
        n3.ay = -factor * s36.n.y;

        factor = s23.k * (s23.longitud0 - s23.obtenLongitud()) / (n3.m * s23.obtenLongitud());
        n3.ax += factor * s23.n.x;
        n3.ay += factor * s23.n.y;

        factor = s34.k * (s34.longitud0 - s34.obtenLongitud()) / (n3.m * s34.obtenLongitud());
        n3.ax += -factor * s34.n.x;
        n3.ay += -factor * s34.n.y;

        /*gamma = Math.acos(prodS(s36, s67) / (s36.obtenLongitud() * s67.obtenLongitud()));
        NodoD rot_36 = rotS(s36, Math.PI / 2);
        factor = n6.k * (gamma - n6.angulo0) / (n3.m * s36.obtenLongitud());
        n3.ax += factor * rot_36.x;
        n3.ay += factor * rot_36.y;

        gamma = Math.acos((prodS(s36, s69) / (s36.obtenLongitud() * s69.obtenLongitud())));
        factor = n6.k * (gamma - n6.angulo0) / (n3.m * s36.obtenLongitud());
        n3.ax += -factor * rot_36.x;
        n3.ay += -factor * rot_36.y;*/


    }

    private void calcula_an4() {

        double factor;
        double gamma;

        NodoD n4 = (NodoD) mNod.get("4");
        NodoD n5 = (NodoD) mNod.get("5");
        NodoD n3 = (NodoD) mNod.get("3");

        Segmento s34 = (Segmento) mSeg.get("34");
        Segmento s45 = (Segmento) mSeg.get("45");
        Segmento s36 = (Segmento) mSeg.get("36");
        Segmento s23 = (Segmento) mSeg.get("23");


        factor = s34.k * (s34.longitud0 - s34.obtenLongitud()) / (n4.m * s34.obtenLongitud());
        n4.ax += factor * s34.n.x;
        n4.ay += factor * s34.n.y;

        factor = s45.k * (s45.longitud0 - s45.obtenLongitud()) / (n4.m * s45.obtenLongitud());
        n4.ax += -factor * s45.n.x;
        n4.ay += -factor * s45.n.y;

        /*gamma = Math.acos(prod(invS(s45), xuni) / s45.obtenLongitud());
        NodoD rot_s45 = this.rotS(s45, Math.PI / 2);
        factor = n5.k * (gamma - n5.angulo0) / (n4.m * s45.obtenLongitud());
        n4.ax += factor * rot_s45.x;
        n4.ay += factor * rot_s45.y;

        gamma = Math.acos(prod(s34.n, s36.n) / (s34.obtenLongitud() * s36.obtenLongitud()));
        NodoD rot_s34 = rotS(s34, Math.PI / 2);
        factor = n3.k * (gamma - n3.angulo0) / (n4.m * s34.obtenLongitud());
        n4.ax += factor * rot_s34.x;
        n4.ay += factor * rot_s34.y;

        gamma = Math.acos(prod(invS(s23), s34.n) / (s23.obtenLongitud() * s34.obtenLongitud()));
        factor = n3.k * (gamma - n3.angulo0) / (n4.m * s23.obtenLongitud());
        n4.ax += -factor * rot_s34.x;
        n4.ay += -factor * rot_s34.y;*/
    }

    private void calcula_an5() {
        NodoD n5 = (NodoD) mNod.get("5");

        n5.ax = 0;
        n5.ay = 0;
    }

    private void calcula_an6() {
        double factor;
        double gamma;

        NodoD n6 = (NodoD) mNod.get("6");
        NodoD n7 = (NodoD) mNod.get("7");
        NodoD n9 = (NodoD) mNod.get("9");
        NodoD n3 = (NodoD) mNod.get("3");
        Segmento s36 = (Segmento) mSeg.get("36");
        Segmento s67 = (Segmento) mSeg.get("67");
        Segmento s69 = (Segmento) mSeg.get("69");
        Segmento s78 = (Segmento) mSeg.get("78");
        Segmento s910 = (Segmento) mSeg.get("910");
        Segmento s34 = (Segmento) mSeg.get("34");
        Segmento s23 = (Segmento) mSeg.get("23");


        factor = s36.k * (s36.longitud0 - s36.obtenLongitud()) / (n6.m * s36.obtenLongitud());
        n6.ax = factor * s36.n.x;
        n6.ay = factor * s36.n.y;

        factor = s67.k * (s67.longitud0 - s67.obtenLongitud()) / (n6.m * s67.obtenLongitud());
        n6.ax += -factor * s67.n.x;
        n6.ay += -factor * s67.n.y;

        factor = s69.k * (s69.longitud0 - s69.obtenLongitud()) / (n6.m * s69.obtenLongitud());
        n6.ax += -factor * s69.n.x;
        n6.ay += -factor * s69.n.y;

       /* gamma = Math.acos(prod(invS(s67), s78.n) / (s67.obtenLongitud() * s78.obtenLongitud()));
        factor = n7.k * (gamma - n7.angulo0) / (n6.m * s67.obtenLongitud());
        NodoD rot_67 = this.rotS(s67, Math.PI / 2);
        n6.ax += factor * rot_67.x;
        n6.ay += factor * rot_67.y;

        gamma = Math.acos(prod(invS(s69), s910.n) / (s69.obtenLongitud() * s910.obtenLongitud()));
        factor = n9.k * (gamma - n9.angulo0) / (n6.m * s69.obtenLongitud());
        NodoD rot_69 = this.rotS(s69, -Math.PI / 2);
        n6.ax += factor * rot_69.x;
        n6.ay += factor * rot_69.y;

        gamma = Math.acos(prod(s34.n, s36.n) / (s34.obtenLongitud() * s36.obtenLongitud()));
        NodoD rot_s36 = rotS(s36, -Math.PI / 2);
        factor = n3.k * (gamma - n3.angulo0) / (n6.m * s34.obtenLongitud());
        n6.ax += factor * rot_s36.x;
        n6.ay += factor * rot_s36.y;

        gamma = Math.acos(prod(invS(s23), s36.n) / (s23.obtenLongitud() * s36.obtenLongitud()));
        factor = n3.k * (gamma - n3.angulo0) / (n6.m * s23.obtenLongitud());
        n6.ax += -factor * rot_s36.x;
        n6.ay += -factor * rot_s36.y;*/

    }

    private void calcula_an7() {
        double factor;
        double gamma;

        NodoD n7 = (NodoD) mNod.get("7");
        Segmento s67 = (Segmento) mSeg.get("67");
        Segmento s78 = (Segmento) mSeg.get("78");
        Segmento s36 = (Segmento) mSeg.get("36");


        factor = s67.k * (s67.longitud0 - s67.obtenLongitud()) / (n7.m * s67.obtenLongitud());
        n7.ax = factor * s67.n.x;
        n7.ay = factor * s67.n.y;


        factor = s78.k * (s78.longitud0 - s78.obtenLongitud()) / (n7.m * s78.obtenLongitud());
        n7.ax += -factor * s78.n.x;
        n7.ay += -factor * s78.n.y;

       /* gamma = Math.acos(prodS(s36, s67) / (s36.obtenLongitud() * s67.obtenLongitud()));
        factor = n7.k * (gamma - n7.angulo0) / (n7.m * s67.obtenLongitud());
        NodoD rot_67 = this.rotS(s67, Math.PI / 2);
        n7.ax += factor * rot_67.x;
        n7.ay += factor * rot_67.y;*/

    }

    private void calcula_an9() {
        double factor;
        double gamma;

        NodoD n9 = (NodoD) mNod.get("9");
        NodoD n6 = (NodoD) mNod.get("6");
        Segmento s69 = (Segmento) mSeg.get("69");
        Segmento s910 = (Segmento) mSeg.get("910");
        Segmento s36 = (Segmento) mSeg.get("36");

        factor = s910.k * (s910.longitud0 - s910.obtenLongitud()) / (n9.m * s910.obtenLongitud());
        n9.ax = -factor * s910.n.x;
        n9.ay = -factor * s910.n.y;

        factor = s69.k * (s69.longitud0 - s69.obtenLongitud()) / (n9.m * s69.obtenLongitud());
        n9.ax += factor * s69.n.x;
        n9.ay += factor * s69.n.y;

        /*gamma = Math.acos((prodS(s36, s69) / (s36.obtenLongitud() * s69.obtenLongitud())));
        NodoD rot_69 = rotS(s69, Math.PI / 2);
        factor = n6.k * (gamma - n6.angulo0) / (n9.m * s69.obtenLongitud());
        n9.ax += -factor * rot_69.x;
        n9.ay += -factor * rot_69.y;*/

    }

    private void calcula_an8() {
        double factor;
        double gamma;

        NodoD n8 = (NodoD) mNod.get("8");
        NodoD n7 = (NodoD) mNod.get("7");
        Segmento s78 = (Segmento) mSeg.get("78");
        Segmento s67 = (Segmento) mSeg.get("67");

        factor = s78.k * (s78.longitud0 - s78.obtenLongitud()) / (n8.m * s78.obtenLongitud());
        n8.ax = factor * s78.n.x;
        n8.ay = factor * s78.n.y;

        /*gamma = Math.acos(prod(invS(s67), s78.n) / (s67.obtenLongitud() * s78.obtenLongitud()));
        factor = n7.k * (gamma - n7.angulo0) / (n8.m * s78.obtenLongitud());
        NodoD rot_78 = this.rotS(s78, Math.PI / 2);
        n8.ax += factor * rot_78.x;
        n8.ay += factor * rot_78.y;*/

    }

    private void calcula_an10() {
        double factor;
        double gamma;

        NodoD n10 = (NodoD) mNod.get("10");
        NodoD n9 = (NodoD) mNod.get("9");
        Segmento s910 = (Segmento) mSeg.get("910");
        Segmento s69 = (Segmento) mSeg.get("69");

        factor = s910.k * (s910.longitud0 - s910.obtenLongitud()) / (n10.m * s910.obtenLongitud());
        n10.ax = factor * s910.n.x;
        n10.ay = factor * s910.n.y;

       /* gamma = Math.acos(prod(invS(s69), s910.n) / (s69.obtenLongitud() * s910.obtenLongitud()));
        factor = n9.k * (gamma - n9.angulo0) / (n10.m * s69.obtenLongitud());
        NodoD rot_910 = this.rotS(s910, -Math.PI / 2);
        n10.ax += factor * rot_910.x;
        n10.ay += factor * rot_910.y;*/
    }
}
