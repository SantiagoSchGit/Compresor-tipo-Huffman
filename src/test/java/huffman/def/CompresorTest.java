package huffman.def;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import huffman.util.HuffmanTree;
import imple.Factory;

class CompresorTest
{

private static final String FILENAME = "test.x";
	
	@AfterEach
	public void afterEach()
	{
		File f = new File(FILENAME);
		f.delete();
	}
	
	@Test
	public void testConvertirListaEnArbolYGenerarCodigos() {
	  try {
	      // Frase a analizar
	      String frase = "COMO COME COCORITO COME COMO COSMONAUTA";

	      // Crear archivo de prueba
	      String filename = "test_frase_huffman.txt";
	      java.nio.file.Files.write(java.nio.file.Paths.get(filename), frase.getBytes());

	      // Paso 1: Contar ocurrencias
	      Compresor c = Factory.getCompresor();
	      HuffmanTable[] tablaOcurrencias = c.contarOcurrencias(filename);

	      // Validar tabla de ocurrencias
	      System.out.println("Tabla de ocurrencias:");
	      for (int i = 0; i < tablaOcurrencias.length; i++) {
	          if (tablaOcurrencias[i].getN() > 0) {
	              char caracter = (char) i;
	              System.out.println(caracter + ": " + tablaOcurrencias[i].getN());
	          }
	      }

	      // Paso 2: Crear la lista enlazada
	      List<HuffmanInfo> lista = c.crearListaEnlazada(tablaOcurrencias);

	      // Validar lista enlazada
	      System.out.println("\nLista enlazada:");
	      for (HuffmanInfo nodo : lista) {
	          char caracter = (char) nodo.getC();
	          System.out.println(caracter + ": " + nodo.getN());
	      }

	      // Paso 3: Construir el árbol de Huffman
	      HuffmanInfo root = c.convertirListaEnArbol(lista);

	      // Validar árbol (frecuencia total)
	      int frecuenciaTotal = frase.length(); // Incluye los espacios
	      assertEquals(frecuenciaTotal, root.getN());

	      // Paso 4: Generar códigos Huffman
	      c.generarCodigosHuffman(root, tablaOcurrencias);

	      // Validar códigos Huffman generados
	      System.out.println("\nCódigos Huffman generados:");
	      for (int i = 0; i < tablaOcurrencias.length; i++) {
	          if (tablaOcurrencias[i].getN() > 0) {
	              char caracter = (char) i;
	              int ocurrencias = tablaOcurrencias[i].getN();
	              String codigoHuffman = tablaOcurrencias[i].getCod();
	              System.out.println(caracter + " (" + ocurrencias + "): [" + codigoHuffman + "]");
	          }
	      }

	  } catch (Exception e) {
	      e.printStackTrace();
	      throw new RuntimeException(e);
	  }
	}
	
//	@Test
//	void porbarContarOcurrencias()
//	{
//		FileOutputStream fos;
//		try
//		{
//			fos=new FileOutputStream(FILENAME);
//			fos.write('A');
//			fos.write('A');
//			fos.write('A');
//			fos.write('A');
//			fos.write('B');
//			fos.write('B');
//			fos.write('B');
//			fos.write('C');
//			fos.write('C');
//			fos.write('D');
//			
//			Compresor com = Factory.getCompresor();
//			HuffmanTable[] arr = com.contarOcurrencias(FILENAME);
//			
//			for(int i = 0; i < arr.length; i++){
//				
//				if(arr[i].getN() > 0){
//					System.out.println((char) i + ": " + arr[i].getN());
//				}
//			}
//			fos.close();
//		}
//		
//		catch(FileNotFoundException e){}
//		catch(IOException e){}
//	}
//
//	
//	@Test
//	void probarCrearListaEnlazada()
//	{
//		FileOutputStream fos;
//		try
//		{
//			fos=new FileOutputStream(FILENAME);
//			fos.write('A');
//			fos.write('A');
//			fos.write('A');
//			fos.write('A');
//			fos.write('B');
//			fos.write('B');
//			fos.write('B');
//			fos.write('C');
//			fos.write('C');
//			fos.write('D');
//			
//			Compresor com = Factory.getCompresor();
//			HuffmanTable[] arr = com.contarOcurrencias(FILENAME);
//			List<HuffmanInfo> huf = com.crearListaEnlazada(arr);
//			
//			for(int i = 0; i < huf.size(); i++){
//				
//				HuffmanInfo aux = huf.get(i);
//				System.out.println((char)aux.getC() + ": " + aux.getN());
//			}
//			fos.close();
//		}
//		
//		catch(FileNotFoundException e){}
//		catch(IOException e){}
//
//	}
//	
//	@Test
//	void probraCrearArbol()
//	{
//		try{
//			FileOutputStream fos;
//			
//			fos=new FileOutputStream(FILENAME);
//			fos.write('A');
//			fos.write('A');
//			fos.write('A');
//			fos.write('A');
//			fos.write('B');
//			fos.write('B');
//			fos.write('B');
//			fos.write('C');
//			fos.write('C');
//			fos.write('D');
//			
//			Compresor com = Factory.getCompresor();
//			HuffmanTable[] arr = com.contarOcurrencias(FILENAME);
//			List<HuffmanInfo> huf = com.crearListaEnlazada(arr);
//			HuffmanInfo root = com.convertirListaEnArbol(huf);
//			
//			// instancio 
//			HuffmanTree ht = new HuffmanTree(root);
//			
//			// tendrá el código Huffman de cada hoja
//			StringBuffer cod = new StringBuffer();
//			
//			HuffmanInfo hoja = ht.next(cod);
//			while( hoja!=null )
//			{
//				System.out.println(hoja+": ["+cod+"]");
//				hoja = ht.next(cod);
//			}
//			fos.close();
//		}
//		
//		catch(FileNotFoundException e){}
//		catch(IOException e){}
//
//	}
//	
	@Test
	void probraGenerarCodigosHuffman()
	{
		try{
			FileOutputStream fos;
			
			fos=new FileOutputStream(FILENAME);
			fos.write('A');
			fos.write('A');
			fos.write('A');
			fos.write('A');
			fos.write('B');
			fos.write('B');
			fos.write('B');
			fos.write('C');
			fos.write('C');
			fos.write('D');
			
			Compresor com = Factory.getCompresor();
			HuffmanTable[] arr = com.contarOcurrencias(FILENAME);
			List<HuffmanInfo> huf = com.crearListaEnlazada(arr);
			HuffmanInfo root = com.convertirListaEnArbol(huf);
			
			com.generarCodigosHuffman(root,arr);
			for(int i = 0; i < arr.length; i++){	
				if(arr[i].getN() > 0){
					System.out.println((char) i + ": " + arr[i].getCod());
				}
			}
			fos.close();
		}
		
		catch(FileNotFoundException e){}
		catch(IOException e){}

	}
	
	@SuppressWarnings("resource")
	@Test
	void probraEscribirEncabezadoYContenido()
	{
		try{
			FileOutputStream fos;
			
			fos=new FileOutputStream(FILENAME);
			fos.write('A');
			fos.write('A');
			fos.write('A');
			fos.write('A');
			fos.write('B');
			fos.write('B');
			fos.write('B');
			fos.write('C');
			fos.write('C');
			fos.write('D');
			
			Compresor com = Factory.getCompresor();
			HuffmanTable[] arr = com.contarOcurrencias(FILENAME);
			List<HuffmanInfo> huf = com.crearListaEnlazada(arr);
			HuffmanInfo root = com.convertirListaEnArbol(huf);	
			com.generarCodigosHuffman(root,arr);

			com.escribirEncabezado(FILENAME,arr);
			com.escribirContenido(FILENAME,arr);
			
			FileInputStream f;		
			f=new FileInputStream(FILENAME + ".huf");
			
			assertEquals(4,f.read()); //Cantidad de chars diferentes
			assertEquals('A',f.read());
			assertEquals(1,f.read());
			assertEquals(0,f.read()); //0000000
			assertEquals('B',f.read());
			assertEquals(2,f.read());
			assertEquals(128,f.read()); //1000000
			assertEquals('C',f.read());
			assertEquals(3,f.read());
			assertEquals(224,f.read()); //1110000
			assertEquals('D',f.read());
			assertEquals(3,f.read());
			assertEquals(192,f.read()); //1100000
			
			assertEquals(0,f.read()); //Son 10 letras el original
			assertEquals(0,f.read());
			assertEquals(0,f.read());
			assertEquals(10,f.read());
			
			//00001010 10111111 110
			assertEquals(10,f.read()); //00001010
			assertEquals(191,f.read()); //10111111
			assertEquals(192,f.read()); //11000000
		
			fos.close();
		}
		
		catch(FileNotFoundException e){}
		catch(IOException e){}

	}
	

}
