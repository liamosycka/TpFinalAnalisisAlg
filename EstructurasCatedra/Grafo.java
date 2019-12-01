package EstructurasCatedra;

import java.util.LinkedList;
import java.util.List;

public class Grafo {

	private NodoVertice inicio;
	private int cantidad;
	private ItemMatriz[][] matrizDist;
	private int idVert;
	ItemMatriz[][] matrizCamValidos;
	private int[][] matrizUnica;

	public Grafo(int cantNodos) {
		cantNodos++; // sumo 1 porque no se considera la posicion 0,0 de la matriz
		inicio = null;
		cantidad = 0;
		matrizDist = new ItemMatriz[cantNodos][cantNodos];
		idVert = 1;
		matrizCamValidos = new ItemMatriz[matrizDist.length][matrizDist.length];
		matrizUnica = new int[cantNodos][cantNodos];
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
					// matrizDist[aux1.getID()][aux2.getID()] = new ItemMatriz(0);
					// matrizDist[aux2.getID()][aux1.getID()] = new ItemMatriz((int) etiqueta);
					matrizUnica[aux1.getID()][aux1.getID()] = -1;
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

	public void debugMatUnica() {
		for (int i = 0; i < matrizDist.length; i++) {
			for (int j = 0; j < matrizDist.length; j++) {
				System.out.print(matrizDist[i][j] + " | ");
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

	public Camino caminoMenorDistanciaDinamico(Object origen, Object destino, LinkedList<Ciudad> obligatorios) {
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
		Lista caminoAux = new Lista();
		caminoAux.insertar(origen, 1);
		caminoAux.insertar(destino, 2);

		// --
		Lista lsVisitados = new Lista();
		Camino camino = new Camino();
		// Se usa un arreglo por tema de referencia, para poder modificarlo según mi
		// conveniencia.
		int[] distanciaMinima = { Integer.MAX_VALUE };
		if (vertOr != null && vertDes != null && existenTodos) {
			if (matrizCamValidos[vertOr.getID()][vertDes.getID()] != null) {
				camino.setDistancia(matrizCamValidos[vertOr.getID()][vertDes.getID()].getDistancia());
				camino.setListaDeNodos(caminoAux);
			} else {
				System.out.println("\nHace llamada al metodo privado\n");
				camino.setListaDeNodos(caminoMenorDistanciaDinamicoAux(vertOr, vertOr, destino, lsVisitados, 0,
						camino.getListaDeNodos(), distanciaMinima, obligatorios));
				camino.setDistancia(distanciaMinima[0]);

			}

//			ItemMatriz itM = matrizCamValidos[vertOr.getID()][vertDes.getID()];
//			if (itM != null) {
//				itM.setDistancia(camino.getDistancia());
//			} else {
//				itM = new ItemMatriz(camino.getDistancia());
//				itM.setVerificado(true);
//			}
		}
		return camino;
	}

	private Lista caminoMenorDistanciaDinamicoAux(NodoVertice n, NodoVertice verOrigen, Object dest, Lista lsVisitados,
			int distActual, Lista lsCamino, int[] distMin, LinkedList<Ciudad> obligatorios) {
		ItemMatriz itM;
		lsVisitados.insertar(n.getElem(), lsVisitados.longitud() + 1);
		if (n.getElem().equals(dest) && lsVisitados.incluye(obligatorios)) {

			itM = new ItemMatriz(distActual);
			itM.setVerificado(true);
			ItemMatriz itemActual = matrizCamValidos[verOrigen.getID()][n.getID()];
			if (itemActual == null) {
				matrizCamValidos[verOrigen.getID()][n.getID()] = itM;
			} else {
				if (distActual < itemActual.getDistancia()) {
					matrizCamValidos[verOrigen.getID()][n.getID()] = itM;
				}
			}

			lsCamino = lsVisitados.clone();
			distMin[0] = distActual;

		} else {
			NodoAdyacente ady = n.getAdyacente();
			while (ady != null) {

				if (distActual + (int) ady.getEtiqueta() < distMin[0]) {

					if (lsVisitados.localizar(ady.getVerticeAdy().getElem()) < 0) {
						// esto es para guardar la distancia entre el origen y el nodo al q voy a ir
						distActual += (int) ady.getEtiqueta();
						itM = matrizDist[verOrigen.getID()][n.getAdyacente().getVerticeAdy().getID()];
						if (itM != null) {
							if (!itM.isVerificado()) {
								itM.setDistancia(distActual);
								itM.setVerificado(true);
								// inverso
								itM = matrizDist[n.getAdyacente().getVerticeAdy().getID()][verOrigen.getID()];
								itM.setDistancia(distActual);
								itM.setVerificado(true);
							} else {
								if (itM.getDistancia() > distActual) {
									itM.setDistancia(distActual);
									// inverso
									itM = matrizDist[n.getAdyacente().getVerticeAdy().getID()][verOrigen.getID()];
									itM.setDistancia(distActual);
								}
							}
						} else {
							itM = new ItemMatriz(distActual);
							itM.setVerificado(true);
						}
						// esto es para guardar la dist entre el nodo que estoy parado y al q voy a ir
						itM = matrizDist[n.getID()][n.getAdyacente().getVerticeAdy().getID()];
						if (itM != null) {
							if (!itM.isVerificado()) {
								itM.setDistancia((int) ady.getEtiqueta());
								itM.setVerificado(true);
								// El inverso
								itM = matrizDist[n.getAdyacente().getVerticeAdy().getID()][n.getID()];
								itM.setDistancia((int) ady.getEtiqueta());
								itM.setVerificado(true);
							} else {
								if (itM.getDistancia() > (int) ady.getEtiqueta()) {
									itM.setDistancia((int) ady.getEtiqueta());
									itM = matrizDist[n.getAdyacente().getVerticeAdy().getID()][n.getID()];
									itM.setDistancia((int) ady.getEtiqueta());
								}
							}
						} else {
							itM = new ItemMatriz(distActual);
							itM.setVerificado(true);
						}
						lsCamino = caminoMenorDistanciaDinamicoAux(ady.getVerticeAdy(), verOrigen, dest, lsVisitados,
								distActual, lsCamino, distMin, obligatorios);
						distActual -= (int) ady.getEtiqueta();
					}

				}

				// Se resta porque voy a ir por otro camino,y este no continen al adyacente
				// anterior.
				ady = ady.getSigAdyacente();
			}
		}
		lsVisitados.eliminar(lsVisitados.longitud());

		return lsCamino;
	}

	////////////////////// probando con una matriz///////
	/*
	 * public Camino caminoMenorDistanciaDinamico(Object origen, Object destino,
	 * LinkedList<Ciudad> obligatorios) { //Se busca el camino más corto en
	 * distancia entre el origen y destino NodoVertice vertOr, vertDes; boolean
	 * existenTodos = true;
	 * 
	 * vertOr = this.buscarNodoVertice(inicio, origen); vertDes =
	 * this.buscarNodoVertice(inicio, destino); // TODO Modificado for (Ciudad c :
	 * obligatorios) { existenTodos &= (this.buscarNodoVertice(inicio, c)) != null;
	 * if (!existenTodos) { break; } } Lista caminito = new Lista();
	 * caminito.insertar(origen, 1); caminito.insertar(destino, 2);
	 * 
	 * // -- Lista lsVisitados = new Lista(); Camino camino = new Camino(); //Se usa
	 * un arreglo por tema de referencia, para poder modificarlo según mi
	 * conveniencia. int[] distanciaMinima = {Integer.MAX_VALUE}; if (vertOr != null
	 * && vertDes != null && existenTodos) {
	 * 
	 * /////////////////////////////////////////////////////////////////////////
	 * ItemMatriz itM=matrizDist[vertOr.getID()][vertDes.getID()]; if
	 * (itM!=null&&itM.isVerificado()) { camino.setDistancia(itM.getDistancia()); }
	 * else { camino.setListaDeNodos(caminoMenorDistanciaDinamicoAux(vertOr, vertOr,
	 * destino, lsVisitados, 0, camino.getListaDeNodos(), distanciaMinima,
	 * obligatorios)); camino.setDistancia(distanciaMinima[0]); }
	 * camino.setListaDeNodos(caminito); } return camino; }
	 * 
	 * private Lista caminoMenorDistanciaDinamicoAux(NodoVertice n, NodoVertice
	 * verOrigen, Object dest, Lista lsVisitados, int distActual, Lista lsCamino,
	 * int[] distMin, LinkedList<Ciudad> obligatorios) { // ItemMatriz itM;
	 * 
	 * lsVisitados.insertar(n.getElem(), lsVisitados.longitud() + 1); if
	 * (n.getElem().equals(dest)) { if(lsVisitados.incluye(obligatorios)) { itM=new
	 * ItemMatriz(distActual); itM.setVerificado(true); ItemMatriz
	 * itemActual=matrizDist[verOrigen.getID()][n.getID()];
	 * if(itM.getDistancia()<=itemActual.getDistancia()) {
	 * matrizDist[verOrigen.getID()][n.getID()]=itM; }
	 * 
	 * 
	 * lsCamino = lsVisitados.clone(); distMin[0] = distActual;
	 * 
	 * }else { matrizDist[verOrigen.getID()][n.getID()]=new
	 * ItemMatriz(Integer.MAX_VALUE); }
	 * 
	 * 
	 * 
	 * } else { NodoAdyacente ady = n.getAdyacente(); while (ady != null) {
	 * 
	 * if (distActual + (int) ady.getEtiqueta() < distMin[0]) {
	 * 
	 * if (lsVisitados.localizar(ady.getVerticeAdy().getElem()) < 0) { //esto es
	 * para guardar la distancia entre el origen y el nodo al q voy a ir distActual
	 * += (int) ady.getEtiqueta(); itM =
	 * matrizDist[verOrigen.getID()][n.getAdyacente().getVerticeAdy().getID()]; if
	 * (itM != null) { if (!itM.isVerificado()) { itM.setDistancia(distActual);
	 * itM.setVerificado(true); // Inverso itM =
	 * matrizDist[n.getAdyacente().getVerticeAdy().getID()][verOrigen.getID()];
	 * itM.setDistancia(distActual); itM.setVerificado(true); } else { if
	 * (itM.getDistancia() > distActual) { itM.setDistancia(distActual); // Inverso
	 * itM =
	 * matrizDist[n.getAdyacente().getVerticeAdy().getID()][verOrigen.getID()];
	 * itM.setDistancia(distActual); } } } else { itM = new ItemMatriz(distActual);
	 * itM.setVerificado(true); } //esto es para guardar la dist entre el nodo que
	 * estoy parado y al q voy a ir itM =
	 * matrizDist[n.getID()][n.getAdyacente().getVerticeAdy().getID()]; if (itM !=
	 * null) { if (!itM.isVerificado()) { itM.setDistancia((int) ady.getEtiqueta());
	 * itM.setVerificado(true); // El inverso itM =
	 * matrizDist[n.getAdyacente().getVerticeAdy().getID()][n.getID()];
	 * itM.setDistancia((int) ady.getEtiqueta()); itM.setVerificado(true); } else {
	 * if (itM.getDistancia() > (int) ady.getEtiqueta()) { itM.setDistancia((int)
	 * ady.getEtiqueta()); itM =
	 * matrizDist[n.getAdyacente().getVerticeAdy().getID()][n.getID()];
	 * itM.setDistancia((int) ady.getEtiqueta()); } } } else { itM = new
	 * ItemMatriz(distActual); itM.setVerificado(true); } lsCamino =
	 * caminoMenorDistanciaDinamicoAux(ady.getVerticeAdy(), verOrigen, dest,
	 * lsVisitados, distActual, lsCamino, distMin, obligatorios); distActual -=
	 * (int) ady.getEtiqueta(); }
	 * 
	 * }
	 * 
	 * //Se resta porque voy a ir por otro camino,y este no continen al adyacente
	 * anterior. ady = ady.getSigAdyacente(); } }
	 * lsVisitados.eliminar(lsVisitados.longitud());
	 * 
	 * return lsCamino; }
	 */

	/////////////////// NUEVO INTENTO XDXD

	public Camino newHope(Object origen, Object destino, LinkedList<Ciudad> obligatorios) {
		// Se busca el camino más corto en distancia entre el origen y destino
		NodoVertice vertOr, vertDes;
		boolean existenTodos = true;

		vertOr = this.buscarNodoVertice(inicio, origen);
		vertDes = this.buscarNodoVertice(inicio, destino);
		System.out.println("Ushuaia: " + vertOr.getID());
		System.out.println("CARTAGEN: " + vertDes.getID());
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
				System.out.println("EL VALOR EN MATRIZ NO ES NULL");
				// pregunto si el valor que esta guardado corresponde a una distancia que haya
				// pasado por todos los obligatorios
				if (valorEnMatriz.isVerificado()) {
					System.out.println("EL VALOR ESTA VERIFICADO");
					Lista ls = new Lista();
					ls.insertar(origen, 1);
					ls.insertar(destino, 2);
					System.out.println("no voy al aux");
					camino.setListaDeNodos(ls);
					camino.setDistancia(valorEnMatriz.getDistancia());
				} else {
					System.out.println("Valor no verificado, valor: " + valorEnMatriz.getDistancia());
					// el valor almacenado es resultado de calculos intermedios y no es una
					// distancia que haya pasado por todos los obligatorios
					// por lo tanto hay que calcularlo
					// LLAMADA A CALCULAR
					// System.out.println("\n VOY AL METODO AUX YA QUE EL VALOR NO ES VALIDO\n");
					System.out.println("llamado recursivo por valor no verif");
					camino.setListaDeNodos(newHopeAux(vertOr, vertOr, destino, lsVisitados, 0, camino.getListaDeNodos(),
							distanciaMinima, obligatorios));
					camino.setDistancia(distanciaMinima[0]);
					System.out.println(camino.getDistancia());
					ItemMatriz nuevoItem = new ItemMatriz(camino.getDistancia());
					nuevoItem.setVerificado(true);
					matrizDist[vertOr.getID()][vertDes.getID()] = nuevoItem;
					matrizDist[vertDes.getID()][vertOr.getID()] = nuevoItem;
				}

			} else {
				System.out.println("EL VALOR EN MATRIZ ES NULL");
				// no existe ninguna entrada en la matriz, hay que calcularlo
				// LLAMADA A CALCULAR
				// System.out.println("\n VOY AL METODO AUX YA QUE NO HAY ENTRADA EN LA
				// MATRIZ\n");
				System.out.println("llamado recursivo por valor null");
				camino.setListaDeNodos(newHopeAux(vertOr, vertOr, destino, lsVisitados, 0, camino.getListaDeNodos(),
						distanciaMinima, obligatorios));
				camino.setDistancia(distanciaMinima[0]);
				System.out.println(camino.getDistancia());
				ItemMatriz nuevoItem = new ItemMatriz(camino.getDistancia());
				nuevoItem.setVerificado(true);
				matrizDist[vertOr.getID()][vertDes.getID()] = nuevoItem;
				matrizDist[vertDes.getID()][vertOr.getID()] = nuevoItem;
			}

		}

		return camino;
	}

	private Lista newHopeAux(NodoVertice n, NodoVertice origen, Object dest, Lista lsVisitados, int distActual,
			Lista lsCamino, int[] distMin, LinkedList<Ciudad> obligatorios) {
		ItemMatriz itemActual, nuevoItem;
		NodoVertice vertDestino = buscarNodoVertice(inicio, dest);

		//
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

						//itemActual = matrizDist[n.getID()][vertDestino.getID()];
						itemActual=matrizDist[ady.getVerticeAdy().getID()][vertDestino.getID()];
						if (itemActual != null) {
							if (itemActual.isVerificado()) {
								/*
								 * en este momento ya hay un camino que pasa por todos los nodos desde donde
								 * estoy parado hasta el destino por lo tanto no es necesario que lo recorra, ya
								 * que se que existe y que en la matriz esta la distancia minima
								 */
								System.out.println("NODO ACTUAL : "+n.getID());
								System.out.println("hay entrada valida entre : " + n.getAdyacente().getVerticeAdy().getID() + " y "
										+ vertDestino.getID() + " con dist: " + itemActual.getDistancia());
								System.out.println("dist actual : "+distActual);
								distActual += itemActual.getDistancia();
								continuar = false;
								distMin[0]=distActual;
								Lista ls = new Lista();
								ls.insertar(origen.getElem(), 1);
								ls.insertar(dest, 2);
								lsCamino = ls;
							} else {
								// System.out.println("entrada en matriz no valida");
								// la entrada en la matriz no es valida, no me sirve la informacion
								nuevoItem = new ItemMatriz((int) ady.getEtiqueta());

								// guardo la distancia entre donde estoy parado y el nodo que sigue
								if (matrizDist[n.getID()][ady.getVerticeAdy().getID()] != null) {
									if (!matrizDist[n.getID()][ady.getVerticeAdy().getID()].isVerificado()) {
										matrizDist[n.getID()][ady.getVerticeAdy().getID()] = nuevoItem;
										matrizDist[ady.getVerticeAdy().getID()][n.getID()] = nuevoItem;
									}
								} else {
									matrizDist[n.getID()][ady.getVerticeAdy().getID()] = nuevoItem;
									matrizDist[ady.getVerticeAdy().getID()][n.getID()] = nuevoItem;
								}
								// ahora guardo la distancia entre el origen y donde voy a ir
								itemActual = matrizDist[origen.getID()][ady.getVerticeAdy().getID()];
								if (itemActual != null) {
									if (!itemActual.isVerificado()) {
										if (distActual < itemActual.getDistancia()) {
											nuevoItem = new ItemMatriz(distActual);
											matrizDist[origen.getID()][ady.getVerticeAdy().getID()] = nuevoItem;
											matrizDist[ady.getVerticeAdy().getID()][origen.getID()] = nuevoItem;

										}

									}
								} else {
									nuevoItem = new ItemMatriz(distActual);
									matrizDist[origen.getID()][ady.getVerticeAdy().getID()] = nuevoItem;
									matrizDist[ady.getVerticeAdy().getID()][origen.getID()] = nuevoItem;
								}

								// System.out.println("\n LLAMADO RECURSIVO PORQUE LA ENTRADA NO ES VALIDA\n");
								lsCamino = newHopeAux(ady.getVerticeAdy(), origen, dest, lsVisitados, distActual,
										lsCamino, distMin, obligatorios);
							}
						} else {
							// System.out.println("no hay entrada en la matriz");

							// no hay ninguna entrada en la matriz, por lo tanto no puedo ahorrarme el
							// camino.
							nuevoItem = new ItemMatriz((int) ady.getEtiqueta());

							// guardo la distancia entre donde estoy parado y el nodo que sigue
							if (matrizDist[n.getID()][ady.getVerticeAdy().getID()] != null) {
								if (!matrizDist[n.getID()][ady.getVerticeAdy().getID()].isVerificado()) {
									matrizDist[n.getID()][ady.getVerticeAdy().getID()] = nuevoItem;
									matrizDist[ady.getVerticeAdy().getID()][n.getID()] = nuevoItem;
								}
							} else {
								matrizDist[n.getID()][ady.getVerticeAdy().getID()] = nuevoItem;
								matrizDist[ady.getVerticeAdy().getID()][n.getID()] = nuevoItem;
							}
							// ahora guardo la distancia entre el origen y donde voy a ir
							nuevoItem = new ItemMatriz(distActual);
							matrizDist[origen.getID()][ady.getVerticeAdy().getID()] = nuevoItem;
							matrizDist[ady.getVerticeAdy().getID()][origen.getID()] = nuevoItem;
							// System.out.println("\n LLAMADO RECURSIVO PORQUE NO HAY ENTRADA EN MATRIZ\n");
							lsCamino = newHopeAux(ady.getVerticeAdy(), origen, dest, lsVisitados, distActual, lsCamino,
									distMin, obligatorios);

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

	//////////////////////////////////////////////////////

	public void borrarDatos() {
		this.inicio = null;
		cantidad = 0;
	}

}
