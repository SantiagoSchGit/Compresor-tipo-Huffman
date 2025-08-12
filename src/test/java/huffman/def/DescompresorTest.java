package huffman.def;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import huffman.util.HuffmanTree;
import imple.Factory;

class DescompresorTest
{
	private static final String FILENAME = "test.x";
	
	@AfterEach
	public void afterEach()
	{
		File f = new File(FILENAME);
		f.delete();
	}
	
//	@Test
//	void probarRecrearArbol()
//	{
//
//		try{
//			FileOutputStream fos = new FileOutputStream(FILENAME + ".huf");
//			fos.write(4); //Van a ser 4 diferentes
//			fos.write('A');
//			fos.write(1); //El codigo de A es de 1 de largo
//			fos.write(128); //Empieza con 1 que es lo que quiero
//			fos.write('B');
//			fos.write(2); //El codigo de A es de 2 de largo
//			fos.write(64); //Empieza con 01 que es lo que quiero
//			fos.write('C');
//			fos.write(3); //El codigo de A es de 3 de largo
//			fos.write(0); //Empieza con 000 que es lo que quiero
//			fos.write('D');
//			fos.write(3); //El codigo de A es de 3 de largo
//			fos.write(32); //Empieza con 001 que es lo que quiero
//			
//			//11110101 01000000 001
//			fos.write(245); //11110101
//			fos.write(64); // 01000000
//			fos.write(32); // 001 y el resto 0
//			
//			fos.write(0);
//			fos.write(0);
//			fos.write(0);
//			fos.write(10);
//			
//			fos.close(); //Cierro este archivo comprimido
//			
//			Descompresor des = Factory.getDescompresor();
//			HuffmanInfo root= new HuffmanInfo();
//			des.recomponerArbol(FILENAME, root);
//			
//			// instancio 
//			HuffmanTree ht = new HuffmanTree(root);
//			
//			// tendrá el código Huffman de cada hoja
//			StringBuffer cod = new StringBuffer();
//			
//			//Test que nos dio el profe
//			HuffmanInfo hoja = ht.next(cod);
//			while( hoja!=null )
//			{
//				System.out.println(hoja+": ["+cod+"]");
//				hoja = ht.next(cod);
//			}
//			
//		}
//		
//		catch(IOException e){}
//	}
	
	@Test
	void probarDescomprimirArchivo()
	{
		
		FileOutputStream fos;
		try
		{
			fos=new FileOutputStream(FILENAME + ".huf");
			
			fos.write(4); //Van a ser 4 diferentes
			fos.write('A');
			fos.write(1); //El codigo de A es de 1 de largo
			fos.write(128); //Empieza con 1 que es lo que quiero
			fos.write('B');
			fos.write(2); //El codigo de A es de 2 de largo
			fos.write(64); //Empieza con 01 que es lo que quiero
			fos.write('C');
			fos.write(3); //El codigo de A es de 3 de largo
			fos.write(0); //Empieza con 000 que es lo que quiero
			fos.write('D');
			fos.write(3); //El codigo de A es de 3 de largo
			fos.write(32); //Empieza con 001 que es lo que quiero
			
			fos.write(0);
			fos.write(0);
			fos.write(0);
			fos.write(10); //Le digo que el archivo original tenia 10 chars
			
			//11110101 01000000 001
			fos.write(245); //11110101
			fos.write(64); // 01000000
			fos.write(32); // 001 y el resto 0
			
			fos.close();
			
			Descompresor des = Factory.getDescompresor();
			HuffmanInfo arbol= new HuffmanInfo();
			long largo = des.recomponerArbol(FILENAME, arbol);
			des.descomprimirArchivo(arbol, largo, FILENAME);
			
			FileInputStream f = new FileInputStream(FILENAME);
			
			
			System.out.println((char)f.read()); //El primero deberia ser A
			
			assertEquals('A',f.read());
			assertEquals('A',f.read());
			assertEquals('A',f.read());
			assertEquals('B',f.read());
			assertEquals('B',f.read());
			assertEquals('B',f.read());
			assertEquals('C',f.read());
			assertEquals('C',f.read());
			assertEquals('D',f.read());
			
			System.out.println(f.read()); //Deberia ser -1 porque termino
			
			f.close();
		}
		
		catch(FileNotFoundException e){}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

