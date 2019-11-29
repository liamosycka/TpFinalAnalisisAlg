package EstructurasCatedra;



public class Camino {

    private int distancia;
    private Lista listaDeNodos;

    public Camino() {
        distancia = 0;
        listaDeNodos = new Lista();
    }
    
    public boolean esVacio() {
        return listaDeNodos.esVacia();
    }

    public Camino(Lista listaDeNodos, int dist) {
        this.listaDeNodos = listaDeNodos;
        this.distancia = dist;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int dis) {
        this.distancia = dis;
    }

    public Lista getListaDeNodos() {
        return listaDeNodos;
    }

    public void setListaDeNodos(Lista listaDeNodos) {
        this.listaDeNodos = listaDeNodos;
    }

    public int get_Cant_De_Nodos() {
        return this.listaDeNodos.longitud();
    }

    @Override
    public String toString() {
        String cadena = "";
        Ciudad ciudad;
        for (int i = 1; i <= listaDeNodos.longitud(); i++) {
            ciudad = (Ciudad) listaDeNodos.recuperar(i);
            cadena += ciudad.getCiudad() + " -> ";
        }
        cadena += " con una distancia total de " + distancia + " km.";

        return cadena;
    }   
}
