package src;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import EstructurasCatedra.Camino;
import EstructurasCatedra.Ciudad;
import EstructurasCatedra.Lista;

class GeneradorRutasTest {

	@Test
	public void caminoMenorDistancia() {

		String[] str = { "Ushuaia", "Glaciar Perito Moreno", "Jericoacoara", "Salar de Uyuni", "Machu Pichu",
				"Cartagena de Indias" };
		Lista listaCiudadesCorrectas = new Lista();
		boolean verif = true;
		for (String s : str) {
			listaCiudadesCorrectas.insertar(new Ciudad(s, ""), listaCiudadesCorrectas.longitud() + 1);
		}
		Camino c;

		GeneradorRutas.generarArchivo();
		GeneradorRutas.generarGrafo();
		c = GeneradorRutas.caminos();
		Lista listaDeNodos = c.getListaDeNodos();

		for (int i = 1; i <= Math.max(listaCiudadesCorrectas.longitud(), listaDeNodos.longitud()); i++) {
			if (i - 1 < str.length) {
				verif &= ((Ciudad) listaDeNodos.recuperar(i)).getCiudad()
						.equals(((Ciudad) listaCiudadesCorrectas.recuperar(i)).getCiudad());
			} else {
				verif = false;
			}
		}

		assertTrue(verif);

	}

	@Test
	public void caminoMenorDistanciaDinamico() {
		GeneradorRutas.generarArchivo();
		GeneradorRutas.generarGrafo();

		Camino c = GeneradorRutas.caminos();

		int dist = c.getDistancia();

		Camino cDin = GeneradorRutas.caminosDinamico();

		assertTrue(dist == cDin.getDistancia());

	}

	@Test
	public void testTiempos() {
		GeneradorRutas.generarArchivo();
		GeneradorRutas.generarGrafo();
		double t1 = System.currentTimeMillis();
		for (int i = 0; i < 9999; i++) {
			GeneradorRutas.caminos();
		}
		GeneradorRutas.caminos();

		double t2 = System.currentTimeMillis();
		System.out.println("**TIEMPO imperativo=" + (t2 - t1));

		double t3 = System.currentTimeMillis();
		for (int i = 0; i < 999999; i++) {
			GeneradorRutas.caminosDinamico();
		}
		GeneradorRutas.caminosDinamico();

		double t4 = System.currentTimeMillis();
		System.out.println("**TIEMPO dinamico=" + (t4 - t3));
	}

}
