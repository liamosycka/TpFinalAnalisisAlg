package EstructurasCatedra;


public class Nodo {

    private Object elem;
    private Nodo enlace;

    public Nodo() {
        this.elem = null;
    }

    public Nodo (Object elem){
        this.elem = elem;
    }

    public Nodo (Object elem, Nodo siguiente){
        this.elem = elem;
        this.enlace = siguiente;
    }

    public Object getElem (){
        return this.elem;
    }

    public Nodo getEnlace (){
        return this.enlace;
    }

    public void setElem (Object elem){
        this.elem = elem;
    }

    public void setEnlace (Nodo siguiente){
        this.enlace = siguiente;
    }

}

