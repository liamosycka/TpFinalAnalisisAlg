
package EstructurasCatedra;


public class NodoAdyacente {


    private NodoVertice verticeAdy;
    private NodoAdyacente sigAdy;
    private Object etiqueta;


    public NodoAdyacente(){
        sigAdy=null;
        verticeAdy=null;
        etiqueta=null;
    }
    public NodoAdyacente(NodoVertice nodoVert, Object nuevaEtiqueta){
        sigAdy=null;
        verticeAdy=nodoVert;
        etiqueta=nuevaEtiqueta;
    }


    public Object getEtiqueta(){
        return etiqueta;
    }

    public NodoAdyacente getSigAdyacente() {
        return sigAdy;
    }
    public NodoVertice getVerticeAdy(){
        return verticeAdy;
    }


    public void setSigAdyacente(NodoAdyacente nuevoAdy){
        sigAdy=nuevoAdy;
    }
    public void setVerticeAdy(NodoVertice nuevoVert){
        verticeAdy=nuevoVert;
    }

    public void setEtiqueta(Object etiqueta) {
        this.etiqueta = etiqueta;
    }
}
