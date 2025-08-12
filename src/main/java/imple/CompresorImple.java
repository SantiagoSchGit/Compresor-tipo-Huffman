package imple;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import huffman.def.BitWriter;
import huffman.def.Compresor;
import huffman.def.HuffmanInfo;
import huffman.def.HuffmanTable;
import huffman.util.Console;
import huffman.util.HuffmanTree;
import huffman.util.IntUtil;

public class CompresorImple implements Compresor
{
	
	BitWriter bitWriter = Factory.getBitWriter();
	int cantHojasProcentaje = 0;
	
	@Override
	public HuffmanTable[] contarOcurrencias(String filename)
	{
		Console consola= null;
		boolean tru = true;
		consola = consola.get(tru );
		
		boolean bPorcentaje = false; 
		
		consola.print("Estado de contar las ocurrencias [          ]");
		consola.skipBkp(11);
		
		
		HuffmanTable[] tablaContador = new HuffmanTable[256];
		for(int i=0; i<256; i++)
		{
			HuffmanTable aux=new HuffmanTable();
			tablaContador[i]=aux;
		}
		
		try(FileInputStream fis = new FileInputStream(filename);)
		{
			BufferedInputStream bis = new BufferedInputStream(fis);
			File file = new File(filename);
			
			int letra;
			letra = bis.read();
			
			long charEncontrados = 0;
			long largoTexto = file.length();
			int i = 1;
			
			while(letra != -1) {
				long porcentajeActual = ((charEncontrados * 100) / largoTexto);
				
				if((10.00*i) <= porcentajeActual){
					consola.print("#");
					i++;
				}
				
				if(tablaContador[letra].getN() == 0) {
					cantHojasProcentaje++;
				}
				
				tablaContador[letra].increment();
				charEncontrados++;
				letra = bis.read();
			}

			while(i <= 9) {
				consola.print("#");
				i++;
			}
			consola.print("#");
			consola.skipFwd();
			consola.println();
		}
		
		catch(Exception e){e.printStackTrace();}
		
		return tablaContador;
	}

	@Override
	public List<HuffmanInfo> crearListaEnlazada(HuffmanTable[] arr)
	{

		List<HuffmanInfo> listaHuffman=new LinkedList<>();
		
		for(int i = 0; arr.length > i; i++) {
			
			if(arr[i].getN() > 0) {
				HuffmanInfo aux = new HuffmanInfo(i, arr[i].getN());
				listaHuffman.add(aux);
			}
		}
		
		Collections.sort(listaHuffman,HuffmanInfo.comparadorPorN);

		return listaHuffman;
	}

	@Override
	public HuffmanInfo convertirListaEnArbol(List<HuffmanInfo> lista)
	{

		for(int i = 1; lista.size() > 1; i++) {
			
			HuffmanInfo huffman2 = lista.remove(0);
			HuffmanInfo huffman1 = lista.remove(0);
			
			HuffmanInfo rama = new HuffmanInfo(255+i,huffman1.getN()+huffman2.getN());
			
			rama.setLeft(huffman1);
			rama.setRight(huffman2);
			
			lista.add(rama);
			Collections.sort(lista,HuffmanInfo.comparadorPorN);
		}
		
		return lista.remove(0);
	}

	@Override
	public void generarCodigosHuffman(HuffmanInfo root, HuffmanTable[] arr)
	{

		StringBuffer code = new StringBuffer();
		HuffmanTree arbol=new HuffmanTree(root);
		
		Console consola= null;
		boolean tru = true;
		consola = consola.get(tru );

		boolean bPorcentaje = false; 
		
		consola.print("Estado de creacion de codigos [          ]");
		consola.skipBkp(11);
		
		int i = 0;
		int j = 1;
		
		HuffmanInfo x=arbol.next(code);
		while(x!=null)
		{

			long porcentajeActual = ((i * 100) / cantHojasProcentaje);
			
			
			if((10.00*j) <= porcentajeActual){
				consola.print("#");
				j++;
			}
			
			String codeString=code.toString();
			arr[x.getC()].setCod(codeString);

			x=arbol.next(code);
			i++;
		}
		
		while(j <= 9) {
			consola.print("#");
			j++;
		}
		consola.print("#");
		consola.skipFwd();
		consola.println();
	}

	@Override
	public long escribirEncabezado(String filename, HuffmanTable[] arr)
	{

		try
		{
			
			
			FileOutputStream fos = new FileOutputStream(filename + ".huf");
			bitWriter.using(fos);
			
			Console consola= null;
			boolean tru = true;
			consola = consola.get(tru );

			boolean bPorcentaje = false; 
			
			consola.print("Estado del encabezado [          ]");
			consola.skipBkp(11);
			
			int contadorDeChars = 0;
			int contadorDeLetras = 0;
			int k = 1;
			
			for(int i=0; i < arr.length; i++){
				
				if(arr[i].getN()>0){
					contadorDeChars++;
					
					long porcentajeActual = ((contadorDeChars * 100) / cantHojasProcentaje);
					
					if((10.00*k) <= porcentajeActual  && k < 10){
						consola.print("#");
						k++;
					}
				}
				contadorDeLetras = contadorDeLetras + arr[i].getN();
			}
			
			while(k <= 9) {
				consola.print("#");
				k++;
			}
			
			consola.print("#");
			consola.skipFwd();
			consola.println();
			
			if(contadorDeChars == 256) {
				fos.write(0);
			}
			else {
				fos.write(contadorDeChars);
			}
			
			for(int i=0; i < arr.length; i++){
				
				if(arr[i].getN() > 0) {
					
					fos.write(i);
					fos.write(arr[i].getCod().length());
					
					String codigo = arr[i].getCod();
					for(int j=0; j < arr[i].getCod().length(); j++)
					{ 
						int bit = codigo.charAt(j)-'0';
						bitWriter.writeBit(bit); 
					}
					
					bitWriter.flush();
				}
			}
			
			IntUtil.write(contadorDeLetras,4,fos);

		}
		
		catch(Exception e){e.printStackTrace();}
		
		File archivo = new File(filename+".huf");
		return (int)archivo.length();
	}

	@Override
	public void escribirContenido(String filename, HuffmanTable[] arr)
	{

		try(FileInputStream fis = new FileInputStream(filename);)
		{
			BufferedInputStream bis = new BufferedInputStream(fis);
			File file = new File(filename);
			
			Console consola= null;
			boolean tru = true;
			consola = consola.get(tru );

			boolean bPorcentaje = false; 
			int contadorDeChars = 0;
			long largoTexto = file.length();
			
			consola.print("Estado del cuerpo [          ]");
			consola.skipBkp(11);
			
			
			int letra = bis.read();
			contadorDeChars++;
			int j = 1;
			
			while(letra != -1)
			{
				String code = arr[letra].getCod();
				for(int i=0; i < code.length(); i++)
				{
					int bit = code.charAt(i)-'0';
					bitWriter.writeBit(bit);
				}
				
				long porcentajeActual = ((contadorDeChars * 100) / largoTexto);
				
				
				if((10.00*j) < porcentajeActual && j < 10){
					consola.print("#");
					j++;
				}
				
				letra=bis.read();
				contadorDeChars++;
			}

			bitWriter.flush();

			while(j <= 9) {
				consola.print("#");
				j++;
			}
			consola.print("#");
			consola.skipFwd();
			consola.println();
			
			fis.close();
			bis.close();
		}
		
		catch(Exception e){e.printStackTrace();}
		
	}

}
