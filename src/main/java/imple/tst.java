package imple;

import java.io.File;
import java.io.IOException;
import java.util.List;

import huffman.def.Compresor;
import huffman.def.Descompresor;
import huffman.def.HuffmanInfo;
import huffman.def.HuffmanTable;
import huffman.util.Console;

public class tst
{
	public static void main(String[] args) {
		Console console = Console.get();
		boolean continuar = true;

		while (continuar) { // bucle para que el usuario decida cunado sale de la consola. 
			try {
				// Pedir al usuario que seleccione un archivo
				console.println("Seleccione el archivo:");
				String rutaArchivo = console.fileExplorer(); //funcion que te deja selecionar el archivo 

				if (rutaArchivo == null) {
					console.println("No se seleccionó ningún archivo.");
					continuar = preguntarSalir(console);
					continue;
				}

				File archivo = new File(rutaArchivo);
				if (!archivo.exists()) {
					console.println("El archivo seleccionado no existe: " + rutaArchivo);
					continuar = preguntarSalir(console);
					continue;
				}

				// Decidir si comprimir o descomprimir
				if (rutaArchivo.endsWith(".huf")) {
					// Descomprimir
					console.println("\n=== Inicio del Proceso de Descompresión ===");

					String rutaSalida = rutaArchivo.substring(0, rutaArchivo.length() - 4); // Eliminar ".huf"
					descomprimirArbol(rutaArchivo, rutaSalida, console);

					console.println("\nDescompresión finalizada. Archivo descomprimido generado: " + rutaSalida);

				} else {
					// Comprimir
					console.println("\n=== Inicio del Proceso de Compresión ===");

					comprimirArchivo(rutaArchivo, console);

					console.println("\nCompresión finalizada. Archivo comprimido generado: " + rutaArchivo + ".huf");
				}

				// Preguntar si desea continuar con otro archivo
				continuar = preguntarSalir(console);

			} catch (Exception e) {
				console.println("Ocurrió un error:");
				e.printStackTrace();
				continuar = preguntarSalir(console);
			}
		}

		console.closeAndExit();
	}

	// Método Descomprimir
	private static void descomprimirArbol(String rutaArchivo, String rutaSalida, Console console) throws IOException {
		Descompresor descompresor = Factory.getDescompresor();

		// Reconstruir el árbol de Huffman desde el archivo comprimido
		HuffmanInfo arbolReconstruido = new HuffmanInfo();
		long longitudOriginal = descompresor.recomponerArbol(rutaArchivo, arbolReconstruido);
		
		//Depuración
		console.println(longitudOriginal);
		console.println(arbolReconstruido.getC());
		console.println(arbolReconstruido.getN());

		// Descomprimir el archivo con barra de progreso
		console.println("Descomprimiendo archivo...");

		// Descomprimir el archivo
		descompresor.descomprimirArchivo(arbolReconstruido, longitudOriginal, rutaSalida);
	}

	// Método Comprimir
	private static void comprimirArchivo(String rutaArchivo, Console console) {
		Compresor compresor = Factory.getCompresor();

		// Generar la tabla de ocurrencias
		HuffmanTable[] tablaOcurrencias = compresor.contarOcurrencias(rutaArchivo);

		// Crear lista enlazada y construir el árbol
		List<HuffmanInfo> lista = compresor.crearListaEnlazada(tablaOcurrencias);
		HuffmanInfo root = compresor.convertirListaEnArbol(lista);

		// Generar códigos Huffman
		compresor.generarCodigosHuffman(root, tablaOcurrencias);

		// Escribir el archivo comprimido con barra de progreso
		console.println("Escribiendo archivo comprimido...");

		// Escribir el archivo comprimido
		compresor.escribirEncabezado(rutaArchivo, tablaOcurrencias);
		compresor.escribirContenido(rutaArchivo, tablaOcurrencias);
	}

	// Método para preguntar si salir o continuar
	private static boolean preguntarSalir(Console console) {
		console.println("\n¿Desea seleccionar otro archivo? (Y/N):");
		String respuesta = console.readlnString().toUpperCase();

		//verifica que la letra ingresada sea Y o N
		while (!respuesta.equals("Y") && !respuesta.equals("N")) {
			console.println("Entrada no válida. Por favor ingrese 'Y' para continuar o 'N' para salir:");
			respuesta = console.readlnString().toUpperCase();
		}

		return respuesta.equals("Y");
	}

	// Metodo para la barra de progreso
	public static void mostrarBarraDeProgreso(Console console, int progreso, int total, String mensaje) {
	    int anchoBarra = 50; // Tamaño de la barra de progreso
	    int porcentaje = (progreso * 100) / total;
	    int cantidadHashes = (progreso * anchoBarra) / total;

	    
	    StringBuilder barra = new StringBuilder("[");
	    for (int i = 0; i < cantidadHashes; i++) {
	        barra.append("#");
	    }
	    for (int i = cantidadHashes; i < anchoBarra; i++) {
	        barra.append(" ");
	    }
	    barra.append("] ").append(porcentaje).append("% ").append(mensaje);

	    try {
	        // Calcula cuánto texto hay actualmente en la consola
	        int textoActual = console._getLenght();

	        // Si el progreso es cero, imprime la barra inicialmente
	        if (progreso == 1) {
	            console.print(barra.toString());
	        } else {
	            // Retrocede de manera segura para sobrescribir la barra
	            console.skipBkp(Math.min(textoActual, barra.length()));
	            console.print(barra.toString());
	        }
	    } catch (IllegalArgumentException e) {
	        // Captura el error y muestra detalles para depuración
	        System.err.println("Error al actualizar la barra de progreso: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
}
