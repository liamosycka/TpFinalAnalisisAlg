/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import utiles.Aleatorio;
import utiles.TecladoIn;
import EstructurasCatedra.*;
import java.io.BufferedReader;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Nacho
 */
public class GeneradorRutas {

	static List<Ciudad> ciudades = new ArrayList<Ciudad>();
	static Grafo grafo;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		int op, op2;
		do {
			op = menu1();

			if (op == 1) {
				generarArchivo();
			} else if (op == 2) {
				do {
					op2 = menu2();
					switch (op2) {
					case 1:
						generarGrafo();
						break;
					case 2:
						caminos();
						break;
					case 3:
						caminosDinamico();
						break;
					case 4:
						System.out.println(grafo.toString());
						grafo.debug();
						// caminoDinamico();
						break;
					default:
						break;
					}
				} while (op2 != 0);

			}

		} while (op != 0);
	}

	public static void generarArchivo() {
		Ciudad unaCiudad;
		int i;

		// Usando las ciudades cargadas, se generan las rutas entre las ciudades
		// calculando la distancia en Km usando latitud y longitud de cada destino
		// Se genera una cantidad aleatoria de rutas, de 1 al cantRutas
		// las rutas generadas son a los destinos mas cercanos
		// System.out.println("Hasta cuántas conexiones por ciudad?");
		 //int cantRutas = TecladoIn.readLineInt();
		int cantRutas;

		File f = new File("datos.txt"); // file to be delete
		f.delete();
		precargaCiudades();
		cantRutas = Math.min(99999, ciudades.size() - 1);
		//cantRutas=Math.min(cantRutas,ciudades.size()-1);

		// guardams las ciudades en el archivo
		ListIterator<Ciudad> itc = ciudades.listIterator();
		while (itc.hasNext()) {
			unaCiudad = itc.next();
			guardar("C:" + unaCiudad.getCiudad() + "," + unaCiudad.getPais());
		}

		ListIterator<Ruta> itr;
		ArrayList<Ruta> rutas;
		itc = ciudades.listIterator();
		int cantRutasTmp;
		Ruta unaRuta;
		while (itc.hasNext()) {
			unaCiudad = itc.next();
			rutas = generarRutasDesde(unaCiudad);
			itr = rutas.listIterator();
			i = 0;
			// cantRutasTmp = Aleatorio.intAleatorio(1, cantRutas);
			cantRutasTmp = cantRutas;
			// System.out.println("Generando " + cantRutasTmp + " ruta desde " +
			// unaCiudad.getCiudad());
			while (itr.hasNext() && i < cantRutasTmp) {
				unaRuta = itr.next();
				guardar("R:" + unaRuta.getDesde().getCiudad() + "," + unaRuta.getHasta().getCiudad() + ","
						+ unaRuta.getDistancia());
				i++;
			}
		}

	}

	public static ArrayList generarRutasDesde(Ciudad ciu) {
		ArrayList<Ruta> rutas = new ArrayList<>();
		Ciudad unaCiudad;
		int distancia;

		ListIterator<Ciudad> itr = ciudades.listIterator();
		while (itr.hasNext()) {
			unaCiudad = itr.next();
			if (ciu.compareTo(unaCiudad) > 0) {
				distancia = (int) Math.ceil(Math.sqrt(Math.pow(unaCiudad.getLatitud() - ciu.getLatitud(), 2)
						+ Math.pow(unaCiudad.getLongitud() - ciu.getLongitud(), 2)) * 112);
				rutas.add(new Ruta(ciu, unaCiudad, distancia));
			}
		}
		Collections.sort(rutas);

		return rutas;
	}

	public static void guardar(String cadena) {
		try {
			String ruta = "datos.txt";
			File file = new File(ruta);
			// Si el archivo no existe es creado
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(cadena + "\n");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int menu1() {
		int op;
		String[] opciones = { "Generar archivo de rutas", "Cargar archivo en grafo y probar", };

		System.out.println();
		System.out.println("------------------  MENÚ  ---------------------");
		for (int i = 0; i < opciones.length; i++) {
			System.out.println((i + 1) + ".- " + opciones[i]);
		}
		System.out.println("0.- Salir");
		System.out.println("-----------------------------------------------");

		do {
			op = TecladoIn.readLineInt();
			if (op < 0 || op > opciones.length) {
				System.out.println("El valor ingresado no es válido");
			}
		} while (op < 0 || op > opciones.length);
		return op;
	}

	public static int menu2() {
		int op;
		String[] opciones = { "Cargar archivo en grafo", "Buscar camino más corto", "Buscar mas corto dinamico",
				"Imprimir grafo" };

		System.out.println();
		System.out.println("------------------  MENÚ  ---------------------");
		for (int i = 0; i < opciones.length; i++) {
			System.out.println((i + 1) + ".- " + opciones[i]);
		}
		System.out.println("0.- Salir");
		System.out.println("-----------------------------------------------");

		do {
			op = TecladoIn.readLineInt();
			if (op < 0 || op > opciones.length) {
				System.out.println("El valor ingresado no es válido");
			}
		} while (op < 0 || op > opciones.length);
		return op;
	}

	public static void precargaCiudades() {
		// las 6 ciudades iniciales deben estar:
		// - Se parte desde Ushuaia
		// - Se llega a Cartagena de Indias
		// - Se debe pasar por el Salar de Uyuni
		// - Se debe pasar por Jericoacoara
		// - Se debe pasar por el Glaciar Perito Moreno
		// - Se debe pasar por Machu Pichu
		// el resto de las ciudades se pueden sacar para simplificar el problema

		/*
		 * ciudades.add(new Ciudad("Ciudad de Mendoza", "Argentina", -32.8908, -68.8272,
		 * false)); ciudades.add(new Ciudad("Córdoba", "Argentina", -31.4135, -64.1811,
		 * false));
		 * 
		 */
		ciudades.add(new Ciudad("Glaciar Perito Moreno", "Argentina", -73.0481, 50.4732, true));
		/*
		 * ciudades.add(new Ciudad("Rosario", "Argentina", -32.9468, -60.6393, false));
		 * ciudades.add(new Ciudad("San Miguel de Tucumán", "Argentina", -26.8241,
		 * -65.2226, false));
		 */
		ciudades.add(new Ciudad("Ushuaia", "Argentina", -54.0872, -68.3040, false));
		/*
		 * ciudades.add(new Ciudad("Cochabamba", "Bolivia", -17.3895, -66.1568, false));
		 * ciudades.add(new Ciudad("La Paz", "Bolivia", -16.5000, -68.1500, false));
		 * ciudades.add(new Ciudad("Laguna Verde", "Bolivia", -13.7000, -61.4333,
		 * false)); ciudades.add(new Ciudad("Oruro", "Bolivia", -17.9833, -67.1500,
		 * false)); ciudades.add(new Ciudad("Potosí", "Bolivia", -19.5836, -65.7531,
		 * false)); ciudades.add(new Ciudad("Santa Cruz de la Sierra", "Bolivia",
		 * -17.7863, -63.1812, false)); ciudades.add(new Ciudad("Sucre", "Bolivia",
		 * -19.0333, -65.2627, false));
		 * 
		 * 
		 * ciudades.add(new Ciudad("Tarija", "Bolivia", -21.5355, -64.7296, false));
		 * ciudades.add(new Ciudad("Amazonas", "Brasil", -64.5000, -3.7500, false));
		 * ciudades.add(new Ciudad("Belo Horizonte", "Brasil", -19.9208, -43.9378,
		 * false)); ciudades.add(new Ciudad("Brasilia", "Brasil", -15.7797, -47.9297,
		 * false)); ciudades.add(new Ciudad("Curitiba", "Brasil", -25.4278, -49.2731,
		 * false)); ciudades.add(new Ciudad("Fernando de Noronha", "Brasil", -3.8507,
		 * -32.4200, false)); ciudades.add(new Ciudad("Fortaleza", "Brasil", -3.7172,
		 * -38.5431, false));
		 */
		ciudades.add(new Ciudad("Jericoacoara", "Brasil", -2.8714, -40.4916, true));
		/*
		 * ciudades.add(new Ciudad("Ouro Preto", "Brasil", -20.3856, -43.5035, false));
		 * ciudades.add(new Ciudad("Río de Janeiro", "Brasil", -22.9064, -43.1822,
		 * false)); ciudades.add(new Ciudad("Salvador", "Brasil", -12.9711, -38.5108,
		 * false)); ciudades.add(new Ciudad("São Paulo", "Brasil", -23.5475, -46.6361,
		 * false)); ciudades.add(new Ciudad("Antofagasta", "Chile", -23.6524, -70.3954,
		 * false)); ciudades.add(new Ciudad("Desierto de Atacama", "Chile", -24.5000,
		 * -69.2500, false)); ciudades.add(new Ciudad("Puente Alto", "Chile", -33.6117,
		 * -70.5758, false)); ciudades.add(new Ciudad("Santiago de Chile", "Chile",
		 * -33.4569, -70.6483, false)); ciudades.add(new Ciudad("Talcahuano", "Chile",
		 * -36.7249, -73.1168, false)); ciudades.add(new Ciudad("Torres del Paine",
		 * "Chile", -51.0503, -72.8295, false)); ciudades.add(new Ciudad("Valparaíso",
		 * "Chile", -33.0360, -71.6296, false)); ciudades.add(new
		 * Ciudad("Viña del Mar", "Chile", -33.0246, -71.5518, false));
		 * ciudades.add(new Ciudad("Barranquilla", "Colombia", 10.9685, -74.7813,
		 * false)); ciudades.add(new Ciudad("Bogotá", "Colombia", 4.6097, -74.0817,
		 * false)); ciudades.add(new Ciudad("Bucaramanga", "Colombia", 7.1254, -73.1198,
		 * false)); ciudades.add(new Ciudad("Cali", "Colombia", 3.4372, -76.5225,
		 * false));
		 * 
		 * ciudades.add(new Ciudad("Cúcuta", "Colombia", 7.8939, -72.5078, false));
		 * ciudades.add(new Ciudad("Medellín", "Colombia", 6.2518, -75.5636, false));
		 * ciudades.add(new Ciudad("Pereira", "Colombia", 4.8133, -75.6961, false));
		 */
		ciudades.add(new Ciudad("Tayrona", "Colombia", 11.3000, -74.1667, false));
		ciudades.add(new Ciudad("Cartagena de Indias", "Colombia", 10.3997, -75.5144, false));
		/*
		 * ciudades.add(new Ciudad("Valle Cocora", "Colombia", 4.6411, -75.5414,
		 * false)); ciudades.add(new Ciudad("Bosque Nuboso Monteverde", "Costa Rica",
		 * 10.3000, -84.8167, false)); ciudades.add(new Ciudad("Baños", "Ecuador",
		 * -1.3964, -78.4247, false)); ciudades.add(new Ciudad("Isla Galápagos",
		 * "Ecuador", -0.7402, 90.3138, false)); ciudades.add(new Ciudad("Asunción",
		 * "Paraguay", -25.3007, -57.6359, false)); ciudades.add(new Ciudad("Capiatá",
		 * "Paraguay", -25.3552, -57.4454, false)); ciudades.add(new
		 * Ciudad("Ciudad del Este", "Paraguay", -25.5097, -54.6111, false));
		 * ciudades.add(new Ciudad("Lambaré", "Paraguay", -25.3468, -57.6065, false));
		 * ciudades.add(new Ciudad("San Lorenzo", "Paraguay", -25.3397, -57.5088,
		 * false)); ciudades.add(new Ciudad("Arequipa", "Peru", -16.3989, -71.5350,
		 * false)); ciudades.add(new Ciudad("Chiclayo", "Peru", -6.7714, -79.8409,
		 * false)); ciudades.add(new Ciudad("El Callao", "Peru", -12.0566, -77.1181,
		 * false)); ciudades.add(new Ciudad("Huancayo", "Peru", -12.0651, -75.2049,
		 * false)); ciudades.add(new Ciudad("Iquitos", "Peru", -3.7491, -73.2538,
		 * false)); ciudades.add(new Ciudad("Lima", "Peru", -12.0432, -77.0282, false));
		 * ciudades.add(new Ciudad("Trujillo", "Peru", -8.1160, -79.0300, false));
		 * ciudades.add(new Ciudad("Las Piedras", "Uruguay", -34.7302, -56.2192,
		 * false));
		 */
		ciudades.add(new Ciudad("Maldonado", "Uruguay", -34.9000, -54.9500, false));
		/*
		 * ciudades.add(new Ciudad("Montevideo", "Uruguay", -34.9033, -56.1882, false));
		 * ciudades.add(new Ciudad("Paysandú", "Uruguay", -32.3214, -58.0756, false));
		 * ciudades.add(new Ciudad("Punta del Este", "Uruguay", -34.9475, -54.9338,
		 * false)); ciudades.add(new Ciudad("Rivera", "Uruguay", -30.9053, -55.5508,
		 * false)); ciudades.add(new Ciudad("Salto", "Uruguay", -31.3833, -57.9667,
		 * false)); ciudades.add(new Ciudad("Tacuarembó", "Uruguay", -31.7169,
		 * -55.9811, false)); ciudades.add(new Ciudad("Barquisimeto", "Venezuela",
		 * 10.0647, -69.3570, false)); ciudades.add(new Ciudad("Caracas", "Venezuela",
		 * 10.4880, -66.8792, false)); ciudades.add(new Ciudad("Maracaibo", "Venezuela",
		 * 10.6666, -71.6124, false)); ciudades.add(new Ciudad("Maracay", "Venezuela",
		 * 10.2353, -67.5911, false)); ciudades.add(new Ciudad("Valencia", "Venezuela",
		 * 10.1620, -68.0077, false));
		 */
		ciudades.add(new Ciudad("Machu Pichu", "Peru", -13.1631, -72.5456, true));
		ciudades.add(new Ciudad("Salar de Uyuni", "Bolivia", -20.1338, -67.4891, true));
	}

	public static void generarGrafo() {
		Ciudad unaCiudad, otraCiudad;

		grafo = new Grafo(ciudades.size());
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File("datos.txt");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String[] arrOfStr;
			String linea;
			while ((linea = br.readLine()) != null) {
				if (linea.charAt(0) == 'C') {
					arrOfStr = linea.substring(2).split(",", 5);
					unaCiudad = new Ciudad(arrOfStr[0], arrOfStr[1]);
					grafo.insertarVertice(unaCiudad);
				} else {
					arrOfStr = linea.substring(2).split(",", 3);
					unaCiudad = new Ciudad(arrOfStr[0], "");
					otraCiudad = new Ciudad(arrOfStr[1], "");

					grafo.insertarArco(unaCiudad, otraCiudad, Integer.parseInt(arrOfStr[2]));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static Camino caminos() {

		Ciudad unaCiudad, otraCiudad;
		Camino c = null;
		String[] str = { "Salar de Uyuni", "Machu Pichu", "Glaciar Perito Moreno", "Jericoacoara" };

		otraCiudad = new Ciudad("Cartagena de Indias", "");
		unaCiudad = new Ciudad("Ushuaia", "");
		LinkedList obligatorios = new LinkedList();
		for (String s : str) {
			obligatorios.add(new Ciudad(s, ""));
		}
		if (grafo.existeCamino(unaCiudad, otraCiudad)) {
			c = grafo.caminoMenorDistancia(unaCiudad, otraCiudad, obligatorios);
			if (!c.esVacio()) {
				System.out.println("Camino con menos Km: " + c);
			} else {
				System.out.println("No hay un camino que pase por las 4 ciudades.");
			}
		} else {
			System.out.println("No existe un camino entre las ciudades");
		}
		

		return c;
	}

	public static Camino caminosDinamico() {

		Ciudad ciudadComienzo, ciudadFin;
		Camino c = null;
		String[] str = { "Salar de Uyuni", "Machu Pichu", "Glaciar Perito Moreno", "Jericoacoara" };

		ciudadFin = new Ciudad("Cartagena de Indias", "");
		ciudadComienzo = new Ciudad("Ushuaia", "");
		LinkedList obligatorios = new LinkedList();
		for (String s : str) {
			obligatorios.add(new Ciudad(s, ""));
		}
		if (grafo.existeCamino(ciudadComienzo, ciudadFin)) {
			//c = grafo.caminoMenorDistanciaDinamico(ciudadComienzo, ciudadFin, obligatorios);
			c = grafo.newHope(ciudadComienzo, ciudadFin, obligatorios);
			
			if (!c.esVacio()) {
				System.out.println("Camino con menos Km: " + c);
				c=grafo.newHope(new Ciudad("Tayrona",""), ciudadFin, obligatorios);
				System.out.println("camino 2: "+c);
				c=grafo.newHope(new Ciudad("Maldonado",""), ciudadFin, obligatorios);
				System.out.println("camino 3: "+c);
			} else {
				System.out.println("No hay un camino que pase por las 4 ciudades.");
			}
		} else {
			System.out.println("No existe un camino entre las ciudades");
		}
		
		return c;
	}

}
