package EstructurasCatedra;

import java.util.LinkedList;
import java.util.List;

public class Grafo {

	private NodoVertice inicio;
	private int cantidad;
	private ItemMatriz[][] matrizDist;
	private int idVert;
	private int[][] m;
	private int[][] caminos;

	public Grafo(int cantNodos) {
		cantNodos++; // sumo 1 porque no se considera la posicion 0,0 de la matriz
		inicio = null;
		cantidad = 0;
		matrizDist = new ItemMatriz[cantNodos][cantNodos];
		idVert = 1;
		m = new int[cantNodos - 1][cantNodos - 1];
		caminos = new int[cantNodos - 1][cantNodos - 1];
	}

	public boolean insertarVertice(Object elem) {
		boolean exito = true;
		NodoVertice aux = new NodoVertice(elem);
		if (inicio == null) {
			inicio = aux;
			cantidad++;
		} else {
			NodoVertice temp = inicio;
			int i = 1;
			boolean excep = false;
			while (temp.getSigVertice() != null && !excep) {
				if (temp.getElem().equals(elem)) {
					excep = true;
				} else {
					temp = temp.getSigVertice();
				}
			}
			if (temp.getElem().equals(elem)) {
				excep = true;
			}
			if (excep) {
				exito = false;
			} else {
				temp.setSigVertice(aux);
				cantidad++;
			}
		}
		if (exito) {
			aux.setID(idVert++);

		}
		return exito;
	}

	public boolean insertarArco(Object vert1, Object vert2, Object etiqueta) {
		NodoVertice aux1 = buscarNodoVertice(inicio, vert1);
		boolean excep = true, exito = true;
		if (aux1 != null) {
			NodoVertice aux2 = buscarNodoVertice(inicio, vert2);
			if (aux2 != null) {
				NodoVertice temp2 = new NodoVertice(vert2);
				if (recuperarArco(aux1.getAdyacente(), temp2) == null) {
					if (aux1.getAdyacente() != null) {
						NodoAdyacente auxAD1 = aux1.getAdyacente();
						while (auxAD1.getSigAdyacente() != null) {
							auxAD1 = auxAD1.getSigAdyacente();
						}
						auxAD1.setSigAdyacente(new NodoAdyacente(aux2, etiqueta));
					} else {
						aux1.setAdyacente(new NodoAdyacente(aux2, etiqueta));
					}
					if (aux2.getAdyacente() != null) {
						NodoAdyacente auxAD2 = aux2.getAdyacente();
						while (auxAD2.getSigAdyacente() != null) {
							auxAD2 = auxAD2.getSigAdyacente();
						}
						auxAD2.setSigAdyacente(new NodoAdyacente(aux1, etiqueta));
					} else {
						aux2.setAdyacente(new NodoAdyacente(aux1, etiqueta));
					}
					excep = false;
				}
				if (exito) {
					/*
					 * Si el arco ha podido ser insertado con exito, inserto en la matriz la
					 * distancia que hay entre los 2 nodos del arco insertado
					 */
					m[aux1.getID() - 1][aux2.getID() - 1] = (int) etiqueta;
					m[aux2.getID() - 1][aux1.getID() - 1] = (int) etiqueta;

				}
			}
		}
		if (excep) {
			exito = false;
		}
		// Suponiendo que no se ingresen mas de un arco entre los mismos dos nodos:

		return exito;
	}

	public void debug() {
		for (ItemMatriz[] i : matrizDist) {
			for (ItemMatriz x : i) {
				System.out.print(x + " | ");
			}
			System.out.println();
		}
	}

	public void debugMatFloyd() {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				System.out.print(m[i][j] + " | ");
			}
			System.out.println();

		}
	}

	public void debugMatCaminosFloyd() {
		for (int i = 0; i < caminos.length; i++) {
			for (int j = 0; j < caminos.length; j++) {
				System.out.print(caminos[i][j] + " | ");
			}
			System.out.println();

		}
	}

	public boolean eliminarVertice(Object vert) {
		int ind = elimCompleto(inicio, new NodoVertice(vert), 0);
		boolean exito = true;
		if (ind == 1) {
			inicio = inicio.getSigVertice();
		}
		if (ind == 0) {
			exito = false;
		}
		return exito;
	}

	private int elimCompleto(NodoVertice aux, NodoVertice busq, int enc) {
		int ret = 0;
		if (aux != null) {
			if (enc == 1) {
				eliminarArco(aux, aux.getAdyacente(), busq);
				elimCompleto(aux.getSigVertice(), busq, enc);
			} else {
				if (aux.getElem().equals(busq.getElem())) {
					elimCompleto(aux.getSigVertice(), busq, 1);
					ret = 1;
				} else {
					ret = elimCompleto(aux.getSigVertice(), busq, enc);
					if (ret == 1 || ret == 2) {
						eliminarArco(aux, aux.getAdyacente(), busq);
						if (ret == 1) {
							aux.setSigVertice(aux.getSigVertice().getSigVertice());
							ret = 2;
						}
					}
				}
			}
		}
		return ret;
	}

	public boolean eliminarArco(Object vert1, Object vert2) {
		boolean exito = true;
		NodoVertice aux1 = buscarNodoVertice(inicio, vert1);
		boolean excep = true;
		if (aux1 != null) {
			NodoVertice aux2 = buscarNodoVertice(inicio, vert2);
			if (aux2 != null) {
				NodoVertice temp2 = new NodoVertice(vert2);
				if (recuperarArco(aux1.getAdyacente(), temp2) != null) {
					eliminarArco(aux1, aux1.getAdyacente(), new NodoVertice(vert2));
					eliminarArco(aux2, aux2.getAdyacente(), new NodoVertice(vert1));
					excep = false;
				}
			}
		}
		if (excep) {
			exito = false;
		}
		return exito;
	}

	private void eliminarArco(NodoVertice inicio, NodoAdyacente aux, NodoVertice nodo) {
		if (aux != null) {
			NodoAdyacente sig = aux;
			if (sig.getVerticeAdy().getElem().equals(nodo.getElem())) {
				inicio.setAdyacente(sig.getSigAdyacente());
				sig = null;
			} else {
				while (sig != null) {
					if (sig.getVerticeAdy().getElem().equals(nodo.getElem())) {
						aux.setSigAdyacente(sig.getSigAdyacente());
						sig = null;
					} else {
						aux = sig;
						sig = sig.getSigAdyacente();
					}
				}
			}
		}
	}

	public int cantidadDeCaminos(Object v1, Object v2) {
		Lista aux;
		aux = this.caminosSimples(v1, v2);
		return aux.longitud();
	}

	public Lista caminosSimples(Object v1, Object v2) {
		Lista ret = new Lista();
		NodoVertice vert = buscarNodoVertice(inicio, v1);
		if (vert != null) {
			Lista aux = new Lista();
			caminosSimples(aux, ret, vert, new NodoVertice(v2));
		}
		ret.invertirLista();
		return ret;
	}

	private void caminosSimples(Lista aux, Lista ret, NodoVertice vert, NodoVertice v2) {
		aux.insertar(vert.getElem(), 1);

		if (vert.getElem().equals(v2.getElem())) {
			Lista aux2 = aux.clone();
			aux2.invertirLista();
			ret.insertar(aux2, 1);
		} else {
			NodoAdyacente temp = vert.getAdyacente();
			while (temp != null) {
				if (aux.localizar(temp.getVerticeAdy().getElem()) == -1) {
					caminosSimples(aux, ret, temp.getVerticeAdy(), v2);
				}
				temp = temp.getSigAdyacente();
			}
		}
		aux.eliminar(1);
	}

	public Lista caminoMasCorto(Object v1, Object v2) {
		Lista ret = new Lista();
		NodoVertice temp = buscarNodoVertice(inicio, v1);
		if (temp != null) {
			ret = caminoCorto(new Lista(), ret, temp, new NodoVertice(v2));
		}
		ret.invertirLista();
		return ret;
	}

	private Lista caminoCorto(Lista aux, Lista ret, NodoVertice vert, NodoVertice v2) {
		aux.insertar(vert.getElem(), 1);

		// System.out.println(aux.toString());
		if (vert.getElem().equals(v2.getElem())) {
			if (ret.esVacia()) {
				ret = aux.clone();
			} else {
				if (aux.longitud() < ret.longitud()) {
					ret.vaciar();
					ret = aux.clone();
				}
			}
		} else {
			NodoAdyacente adyancente = vert.getAdyacente();
			while (adyancente != null) {
				if (aux.localizar(adyancente.getVerticeAdy().getElem()) == -1) {
					if (ret.esVacia()) {
						ret = caminoCorto(aux, ret, adyancente.getVerticeAdy(), v2);
					} else {
						if (ret.longitud() > aux.longitud() + 1) {
							ret = caminoCorto(aux, ret, adyancente.getVerticeAdy(), v2);
						}
					}
				}
				adyancente = adyancente.getSigAdyacente();
			}
		}
		aux.eliminar(1);
		return ret;
	}

	public Lista caminoMasLargo(Object v1, Object v2) {
		Lista ret = new Lista();
		NodoVertice temp = buscarNodoVertice(inicio, v1);
		if (temp != null) {
			ret = caminoLargo(new Lista(), ret, temp, new NodoVertice(v2));
		}
		ret.invertirLista();
		return ret;
	}

	private Lista caminoLargo(Lista aux, Lista ret, NodoVertice vert, NodoVertice v2) {
		aux.insertar(vert.getElem(), 1);
		if (vert.getElem().equals(v2.getElem())) {
			if (aux.longitud() > ret.longitud()) {
				ret.vaciar();
				ret = aux.clone();
			}
		} else {
			NodoAdyacente temp = vert.getAdyacente();
			while (temp != null) {
				if (aux.localizar(temp.getVerticeAdy().getElem()) == -1) {
					ret = caminoLargo(aux, ret, temp.getVerticeAdy(), v2);
				}
				temp = temp.getSigAdyacente();
			}
		}
		aux.eliminar(1);
		return ret;
	}

	public boolean existeCamino(Object v1, Object v2) {
		NodoVertice temp = buscarNodoVertice(inicio, v1);
		if (temp != null) {
			Lista lis = new Lista();
			NodoVertice nv2 = new NodoVertice(v2);
			lis.vaciar();
			return caminoInt(temp, nv2, lis);
		} else {
			return false;
		}
	}

	private boolean caminoInt(NodoVertice aux, NodoVertice v2, Lista lis) {
		if (aux.equals(v2)) {
			return true;
		} else {
			lis.insertar(aux, 1);
			NodoAdyacente temp = aux.getAdyacente();
			boolean enc = false;
			while (!enc && temp != null) {
				if (lis.localizar(temp.getVerticeAdy()) == -1) {
					enc = caminoInt(temp.getVerticeAdy(), v2, lis);
				}
				temp = temp.getSigAdyacente();
			}
			return enc;
		}
	}

	public int cantidadVertices() {
		return cantidad;
	}

	public Lista obtenerVertices() {
		Lista ret = new Lista();
		NodoVertice aux = inicio;
		while (aux != null) {
			visitar(aux, ret);
			aux = aux.getSigVertice();
		}
		return ret;
	}

	private void visitar(NodoVertice aux, Lista visit) {
		if (visit.localizar(aux.getElem()) == -1) {
			visit.insertar(aux.getElem(), 1);
			NodoAdyacente temp = aux.getAdyacente();
			while (temp != null) {
				visitar(temp.getVerticeAdy(), visit);
				temp = temp.getSigAdyacente();
			}
		}
	}

	public boolean actualizarEtiqueta(Object nombre1, Object nombre2, Object etiqueta) {
		boolean exito = false;
		NodoVertice aux1 = buscarNodoVertice(inicio, nombre1);
		if (aux1 != null) {
			NodoAdyacente temp = aux1.getAdyacente();
			while (temp != null && !(temp.getVerticeAdy().getElem().equals(nombre2))) {
				temp = temp.getSigAdyacente();
			}
			if (temp != null) {
				temp.setEtiqueta(etiqueta);
				exito = true;
			}
		}
		return exito;
	}

	public NodoAdyacente recuperarArco(NodoAdyacente aux, NodoVertice vert) {
		if (aux != null) {
			if (aux.getVerticeAdy().getElem().equals(vert.getElem())) {
				return aux;
			} else {
				return recuperarArco(aux.getSigAdyacente(), vert);
			}
		} else {
			return null;
		}
	}

	private NodoVertice buscarNodoVertice(NodoVertice aux, Object elem) {

		if (aux != null) {
			if (aux.getElem().equals(elem)) {
				return aux;
			} else {
				return buscarNodoVertice(aux.getSigVertice(), elem);
			}
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		String cadena = "";
		NodoVertice aux = inicio;
		while (aux != null) {
			cadena = toStringInt(aux, cadena);
			aux = aux.getSigVertice();
			cadena = cadena + "\n";
		}
		if (cadena.equals("")) {
			cadena = "Grafo Vacío";
		}
		return cadena;
	}

	private String toStringInt(NodoVertice aux, String cadena) {
		cadena = cadena + "Vertice: " + aux.getElem() + ".";
		NodoAdyacente temp = aux.getAdyacente();
		if (temp != null) {
			cadena = cadena + "Vertices Adyacentes: ";
		}
		while (temp != null) {
			cadena = cadena + temp.getVerticeAdy().getElem() + "(" + temp.getEtiqueta() + ")" + " - ";
			temp = temp.getSigAdyacente();
		}
		return cadena;
	}

	public Camino caminoMenorDistancia(Object origen, Object destino, LinkedList<Ciudad> obligatorios) {
		// Se busca el camino más corto en distancia entre el origen y destino
		NodoVertice vertOr, vertDes;
		boolean existenTodos = true;

		vertOr = this.buscarNodoVertice(inicio, origen);
		vertDes = this.buscarNodoVertice(inicio, destino);
		// TODO Modificado
		for (Ciudad c : obligatorios) {
			existenTodos &= (this.buscarNodoVertice(inicio, c)) != null;
			if (!existenTodos) {
				break;
			}
		}
		// --
		Lista lsVisitados = new Lista();
		Camino camino = new Camino();
		// Se usa un arreglo por tema de referencia, para poder modificarlo según mi
		// conveniencia.
		int[] distanciaMinima = { Integer.MAX_VALUE };
		if (vertOr != null && vertDes != null && existenTodos) {
			System.out.println("Hace llamada al metodo privado");
			camino.setListaDeNodos(caminoMenorDistanciaAux(vertOr, destino, lsVisitados, 0, camino.getListaDeNodos(),
					distanciaMinima, obligatorios));
			camino.setDistancia(distanciaMinima[0]);
		}
		return camino;
	}

	private Lista caminoMenorDistanciaAux(NodoVertice n, Object dest, Lista lsVisitados, int distActual, Lista lsCamino,
			int[] distMin, LinkedList<Ciudad> obligatorios) {
		//
		lsVisitados.insertar(n.getElem(), lsVisitados.longitud() + 1);
		if (n.getElem().equals(dest) && lsVisitados.incluye(obligatorios)) {
			/**
			 * Como "lsVisitados" va a sufrir modificaciones se decide clonarla, porque si
			 * sólo se le asigna va a sufrir modificaciones, "lsCamino" va a sufrir las
			 * mismas modificaciones (referencia).
			 */
			lsCamino = lsVisitados.clone();
			distMin[0] = distActual;

		} else {
			NodoAdyacente ady = n.getAdyacente();
			while (ady != null) {
				distActual += (int) ady.getEtiqueta();
				if (distActual < distMin[0]) {
					if (lsVisitados.localizar(ady.getVerticeAdy().getElem()) < 0) {
						lsCamino = caminoMenorDistanciaAux(ady.getVerticeAdy(), dest, lsVisitados, distActual, lsCamino,
								distMin, obligatorios);
					}
				}
				// Se resta porque voy a ir por otro camino,y este no continen al adyacente
				// anterior.
				distActual -= (int) ady.getEtiqueta();
				ady = ady.getSigAdyacente();
			}
		}
		lsVisitados.eliminar(lsVisitados.longitud());

		return lsCamino;
	}

	/////////////////// NUEVO INTENTO XDXD

	public Camino menorDistanciaDinamico(Object origen, Object destino, LinkedList<Ciudad> obligatorios) {
		// Se busca el camino más corto en distancia entre el origen y destino
		NodoVertice vertOr, vertDes;
		boolean existenTodos = true;

		vertOr = this.buscarNodoVertice(inicio, origen);
		vertDes = this.buscarNodoVertice(inicio, destino);

		// TODO Modificado
		for (Ciudad c : obligatorios) {
			existenTodos &= (this.buscarNodoVertice(inicio, c)) != null;
			if (!existenTodos) {
				break;
			}
		}
		// --
		Lista lsVisitados = new Lista();
		Camino camino = new Camino();
		// Se usa un arreglo por tema de referencia, para poder modificarlo según mi
		// conveniencia.

		int[] distanciaMinima = { Integer.MAX_VALUE };
		if (vertOr != null && vertDes != null && existenTodos) {

			ItemMatriz valorEnMatriz = matrizDist[vertOr.getID()][vertDes.getID()];
			// veo si ya hay guardado algun valor para lo q se est� solicitando
			if (valorEnMatriz != null) {

				// pregunto si el valor que esta guardado corresponde a una distancia que haya
				// pasado por todos los obligatorios
				if (valorEnMatriz.isVerificado()) {

					Lista ls = new Lista();
					ls.insertar(origen, 1);
					ls.insertar(destino, 2);
					System.out.println("no voy al aux");
					camino.setListaDeNodos(ls);
					camino.setDistancia(valorEnMatriz.getDistancia());
				} else {
					// el valor almacenado es resultado de calculos intermedios y no es una
					// distancia que haya pasado por todos los obligatorios
					// por lo tanto hay que calcularlo
					System.out.println("llamado recursivo por valor no verif");
					camino.setListaDeNodos(menorDistanciaDinamicoAux(vertOr, vertOr, destino, lsVisitados, 0,
							camino.getListaDeNodos(), distanciaMinima, obligatorios));
					camino.setDistancia(distanciaMinima[0]);
					ItemMatriz nuevoItem = new ItemMatriz(camino.getDistancia());
					nuevoItem.setVerificado(true);
					matrizDist[vertOr.getID()][vertDes.getID()] = nuevoItem;
					matrizDist[vertDes.getID()][vertOr.getID()] = nuevoItem;
				}

			} else {
				// no existe ninguna entrada en la matriz, hay que calcularlo
				System.out.println("llamado recursivo por valor null");
				camino.setListaDeNodos(menorDistanciaDinamicoAux(vertOr, vertOr, destino, lsVisitados, 0,
						camino.getListaDeNodos(), distanciaMinima, obligatorios));
				camino.setDistancia(distanciaMinima[0]);
				ItemMatriz nuevoItem = new ItemMatriz(camino.getDistancia());
				nuevoItem.setVerificado(true);
				matrizDist[vertOr.getID()][vertDes.getID()] = nuevoItem;
				matrizDist[vertDes.getID()][vertOr.getID()] = nuevoItem;
			}

		}

		return camino;
	}

	private Lista menorDistanciaDinamicoAux(NodoVertice n, NodoVertice origen, Object dest, Lista lsVisitados,
			int distActual, Lista lsCamino, int[] distMin, LinkedList<Ciudad> obligatorios) {
		ItemMatriz itemActual, nuevoItem;
		NodoVertice vertDestino = buscarNodoVertice(inicio, dest);

		lsVisitados.insertar(n.getElem(), lsVisitados.longitud() + 1);
		if (n.getElem().equals(dest) && lsVisitados.incluye(obligatorios)) {

			lsCamino = lsVisitados.clone();
			distMin[0] = distActual;

		} else {
			boolean continuar = true;
			NodoAdyacente ady = n.getAdyacente();
			while (ady != null && continuar) {

				distActual += (int) ady.getEtiqueta();
				if (distActual < distMin[0]) {
					if (lsVisitados.localizar(ady.getVerticeAdy().getElem()) < 0) {
						/*
						 * Primero verifico si hay una distancia almacenada en la matriz desde el nodo
						 * en el que estoy parado hacia el destino
						 */

						// itemActual = matrizDist[n.getID()][vertDestino.getID()];
						itemActual = matrizDist[ady.getVerticeAdy().getID()][vertDestino.getID()];
						if (itemActual != null) {
							if (itemActual.isVerificado()) {
								/*
								 * en este momento ya hay un camino que pasa por todos los nodos desde donde
								 * estoy parado hasta el destino por lo tanto no es necesario que lo recorra, ya
								 * que se que existe y que en la matriz esta la distancia minima
								 */
								distActual += itemActual.getDistancia();
								continuar = false;
								distMin[0] = distActual;
								Lista ls = new Lista();
								ls.insertar(origen.getElem(), 1);
								ls.insertar(dest, 2);
								lsCamino = ls;
							} else {
								// la entrada en la matriz no es valida, no me sirve la informacion

								lsCamino = menorDistanciaDinamicoAux(ady.getVerticeAdy(), origen, dest, lsVisitados,
										distActual, lsCamino, distMin, obligatorios);
							}
						} else {

							// no hay ninguna entrada en la matriz, por lo tanto no puedo ahorrarme el
							// camino.

							lsCamino = menorDistanciaDinamicoAux(ady.getVerticeAdy(), origen, dest, lsVisitados,
									distActual, lsCamino, distMin, obligatorios);

						}

					}
				}
				// Se resta porque voy a ir por otro camino,y este no continen al adyacente
				// anterior.
				distActual -= (int) ady.getEtiqueta();
				ady = ady.getSigAdyacente();
			}
		}
		lsVisitados.eliminar(lsVisitados.longitud());

		return lsCamino;
	}

	public Camino floyd(Object origen, Object destino) {
		Camino camino = new Camino();
		System.out.println("Antes de floyd");
		this.debugMatFloyd();
		this.calcularMatrizFloyd();
		System.out.println("dpss de floyd");
		this.debugMatFloyd();
		NodoVertice vertOr = buscarNodoVertice(inicio, origen);
		NodoVertice vertDes = buscarNodoVertice(inicio, destino);
		//camino.setListaDeNodos(obtenerCaminoFloyd(vertOr, vertDes));
		camino.setDistancia(obtenerDistanciaFloyd(vertOr, vertDes));
		
		return camino;

	}

	private void calcularMatrizFloyd() {
		for (int k = 0; k < m.length; k++) {
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < m.length; j++) {

					if (m[i][j] > (m[i][k] + m[k][j])) {

						m[i][j] = m[i][k] + m[k][j];
						caminos[i][j] = k;

					}
				}
			}
		}

	}

	private Lista obtenerCaminoFloyd(NodoVertice vertOr, NodoVertice vertDes) {
		System.out.println("entra a metodo con while");
		Lista lsCamino = new Lista();
		int i = vertOr.getID() - 1;
		int j = vertDes.getID() - 1;
		int k = 1;
		System.out.println(caminos[i][j]);
		while (caminos[i][j] != i) {

			lsCamino.insertar(caminos[i][j], k);
			j = caminos[i][j];
			k++;
		}
		System.out.println("sale del while");
		return lsCamino;
	}

	private int obtenerDistanciaFloyd(NodoVertice vertOr, NodoVertice vertDes) {
		return m[vertOr.getID() - 1][vertDes.getID() - 1];

	}
}
