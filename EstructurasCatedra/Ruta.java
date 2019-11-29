/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EstructurasCatedra;


/**
 *
 * @author Nacho
 */
public class Ruta implements Comparable{
    Ciudad desde;
    Ciudad hasta;
    int Distancia;

    public void setDesde(Ciudad desde) {
        this.desde = desde;
    }

    public void setHasta(Ciudad hasta) {
        this.hasta = hasta;
    }

    public void setDistancia(int Distancia) {
        this.Distancia = Distancia;
    }

    public Ciudad getDesde() {
        return desde;
    }

    public Ciudad getHasta() {
        return hasta;
    }

    public int getDistancia() {
        return Distancia;
    }

    public Ruta(Ciudad desde, Ciudad hasta, int Distancia) {
        this.desde = desde;
        this.hasta = hasta;
        this.Distancia = Distancia;
    }

    @Override
    public int compareTo(Object t) {
        return this.Distancia - ((Ruta) t).Distancia;
    }
}
