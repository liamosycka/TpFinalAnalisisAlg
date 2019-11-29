package EstructurasCatedra;


import java.util.LinkedList;

public class Lista {

    private Nodo cabecera;
    private int longitud;

    // primer elemento de la lista = 1, no cero,..
    public void Lista() {
        cabecera = null;
        longitud = 0;

    }

    public boolean insertar(Object elem, int pos) {
        boolean exito;
        if (pos < 1 || pos > this.longitud + 1) {
            exito = false;
        } else {
            //Caso UNO: primer posicion (funciona para lista vacia)
            if (pos == 1) {
                this.cabecera = new Nodo(elem, this.cabecera);
            } else {
                // Caso DOS; posicion intermedia y final
                int i = 1;
                Nodo nodoAux = this.cabecera;
                while (i < pos - 1) {
                    //Convierto nodoAux en el nodo anterior a la posicion a insertar
                    nodoAux = nodoAux.getEnlace();
                    i++;
                }
                //Creo nodo nuevo con el elemento, enlazo al siguiente y luego
                Nodo nodoNuevo = new Nodo(elem, nodoAux.getEnlace());

                //Enlazo el nodo anterior al nodo nuevo
                nodoAux.setEnlace(nodoNuevo);
            }
            this.longitud = this.longitud + 1;
            exito = true;
        }
        return exito;
    }

    public boolean eliminar(int pos) {
        /* Borra el elemento de la posición pos, por lo que la cantidad de elementos de la lista disminuye en uno. Para
        una eliminación exitosa, la lista no debe estar vacía y la posición recibida debe ser 1  pos  longitud(lista).
        Devuelve verdadero si se pudo eliminar correctamente y falso en caso contrario.
        Error: Si la posicion es invalida devuelve -1 , cod error*/
        boolean exito;
        if (pos < 1 || pos > this.longitud()) {
            exito = false;
        } else {
            if (pos == 1) {
                this.cabecera = this.cabecera.getEnlace();
            } else {
                int i = 1;
                Nodo nodoAnterior = this.cabecera;
                // 'nodoAux' apuntará a el elemento anterior a borrar
                while (i < pos - 1) {
                    i++;
                    nodoAnterior = nodoAnterior.getEnlace();
                }
                // 'nodoAux2' será el elemento siguiente al que borraremos
                Nodo nodoAux2 = nodoAnterior.getEnlace().getEnlace();
                //Modificamos el enlace
                nodoAnterior.setEnlace(nodoAux2);
            }
            //la longitud de la lista disminuye y la op. fue exitosa
            this.longitud = this.longitud - 1;
            exito = true;
        }
        return exito;
    }

    public int localizar(Object elem) {
        // Devuelve la posición en la que se encuentra la primera ocurrencia de elem dentro de la lista. En caso de
        //no encontrarlo devuelve -1.
        int posicion;
        posicion = 1;
        Nodo nodoAux;
        nodoAux = this.cabecera;
        while (nodoAux != null && !nodoAux.getElem().equals(elem)) {
            posicion++;
            nodoAux = nodoAux.getEnlace();
        }
        if (nodoAux == null) { //Si no lo encontró
            posicion = -1;
        }
        return posicion;
    }

    public Object recuperar(int pos) {
        // Devuelve el elemento de la posición pos. La precondición es que la posición sea válida.
        Object retorna;
        if (pos < 1 || pos > this.longitud()) {
            //Codigo de error
            retorna = Integer.MAX_VALUE;
        } else {
            int i = 1;
            Nodo nodoAux = this.cabecera;
            while (i < pos) {
                nodoAux = nodoAux.getEnlace();
                i++;
            }
            retorna = nodoAux.getElem();
        }
        return retorna;
    }

    public int longitud() {
        return this.longitud;
    }

    public boolean esVacia() { //retorna true si es vacía
        return (this.longitud == 0);
    }

    public void vaciar() { //vacia lista
        this.cabecera = null;
        this.longitud = 0;
    }

    public void eliminarApariciones(Object x) {
        if (this.longitud > 0) {
            while (this.cabecera != null && this.cabecera.getElem().equals(x)) {
                this.cabecera = this.cabecera.getEnlace();
            }
            Nodo aux = this.cabecera;
            Nodo aux2 = aux;
            while (aux != null && aux2 != null) {
                aux2 = aux.getEnlace();
                while (aux2 != null && aux2.getElem().equals(x)) {
                    aux2 = aux2.getEnlace();
                }
                aux.setEnlace(aux2);
                aux = aux2;
                if (aux2 != null) {
                    aux2 = aux2.getEnlace();
                }
            }

        }

    }

    public void invertirLista() {
        if (cabecera != null) {

            Nodo aux2 = cabecera;
            Nodo aux = cabecera.getEnlace();

            while (cabecera != null && aux != null) {
                cabecera.setEnlace(aux.getEnlace());
                aux.setEnlace(aux2);
                aux2 = aux;
                aux = cabecera.getEnlace();
            }
            cabecera = aux2;
        }
    }

    public void insertarProm() {
        Object prom = 0;
        Object iter = 0;
        if (!this.esVacia()) {
            Nodo aux = this.cabecera;
            if (aux.getEnlace() == null) {
                prom = aux.getElem();
                Nodo nuevo = new Nodo(prom);
                aux.setEnlace(nuevo);
            } else {
                while (aux.getEnlace() != null) {
                    prom = (int) prom + (int) aux.getElem();
                    iter = (int) iter + 1;
                    aux = aux.getEnlace();
                }
                prom = (int) prom + (int) aux.getElem();
                iter = (int) iter + 1;
                prom = (int) prom / (int) iter;
                Nodo nuevo = new Nodo(prom);
                aux.setEnlace(nuevo);
            }
        }
    }

    public boolean incluye(LinkedList l  ){
        boolean res = true;
        for (Object o: l) {
            res &= localizar(o) > 0;
            if(!res) break;
        }
        return res;
    }

    @Override
    public String toString() {
        String res = "| ";
        if (this.longitud >= 1) {
            Nodo nodoAux = this.cabecera;
            int i = 1;
            while (nodoAux != null) {
                //cada tipo de dato tiene su propio método toString() definido
                res += nodoAux.getElem().toString() + " | ";
                nodoAux = nodoAux.getEnlace();
                i++;
            }
        } else {
            res = "Lista vacía";
        }
        return res;
    }

    @Override
    public Lista clone() {
        Lista listaCopia = new Lista();

        if (this.longitud > 0) { //cuando la lista no es vacia
            listaCopia.longitud = this.longitud;
            Nodo original = this.cabecera.getEnlace(); //primer elem. copia
            Nodo copia = new Nodo(this.cabecera.getElem()); //
            listaCopia.cabecera = copia; //
            while (original != null) {
                Nodo nodoNuevo = new Nodo(original.getElem());
                copia.setEnlace(nodoNuevo);
                copia = nodoNuevo;
                original = original.getEnlace();
            }
        }
        return listaCopia;
    }
}
