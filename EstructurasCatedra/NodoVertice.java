package EstructurasCatedra;

public class NodoVertice {


    private Object elem;
    private NodoVertice sigVertice;
    private NodoAdyacente verticeAdy;
    private int id;


    public NodoVertice(){
        elem=null;
        sigVertice=null;
        verticeAdy=null;
        id = 0;
    }
    public NodoVertice(Object nuevoVert){
        elem=nuevoVert;
        sigVertice=null;
        verticeAdy=null;
        id = 0;
    }
    
    public NodoVertice(Object nuevoVert, int id){
        elem=nuevoVert;
        sigVertice=null;
        verticeAdy=null;
        this.id = id;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    public int getID(){
        return id;
    }

    public Object getElem(){
        return elem;
    }
    public NodoAdyacente getAdyacente(){
        return verticeAdy;
    }
    public NodoVertice getSigVertice(){
        return sigVertice;
    }


    public void setElem(Object nuevoVert){
        elem=nuevoVert;
    }
    public void setAdyacente(NodoAdyacente nuevoAdy){
        verticeAdy=nuevoAdy;
    }
    public void setSigVertice(NodoVertice nuevoVert){
        sigVertice=nuevoVert;
    }


    public boolean equals(NodoVertice aux){
        return (this.elem.equals(aux.getElem()));
    }

}
