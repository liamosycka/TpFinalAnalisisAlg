/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EstructurasCatedra;

public class ItemMatriz {
    
    private int distancia;
    private boolean verificado;
    
    public ItemMatriz(int valor){
        distancia = valor;
        verificado = false;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }
    
    public String toString(){
        String res;
        res = distancia + "";
        if(verificado) res += "(V)"; else res += "(F)";
        return res;
    }
    
}
