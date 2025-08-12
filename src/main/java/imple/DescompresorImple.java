package imple;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.Queue;

import huffman.def.BitReader;
import huffman.def.Descompresor;
import huffman.def.HuffmanInfo;
import huffman.def.HuffmanTable;
import huffman.util.Console;

public class DescompresorImple implements Descompresor
{

	FileInputStream fis = null;
	BufferedInputStream bis = null;
	BitReader bitReader = Factory.getBitReader();
	
	@Override
	public long recomponerArbol(String filename, HuffmanInfo arbol)
	{

		long largoTexto = 0;
		try
		{
			
			
			fis = new FileInputStream(filename + ".huf");
			bis = new BufferedInputStream(fis);
			bitReader.using(bis);
			
			Queue<HuffmanTable> huffmanList = new LinkedList<>();
			Queue<Character> charList = new LinkedList<>();
			
			int cantChars = bis.read();
			if(cantChars == 0) {
				cantChars = 256;
			}
			
			for(int i = 0; i < cantChars; i++) {
				
				int letra = bis.read();
				int largo = bis.read();
				
				String code = "";
				for(int j = 0; j < largo; j++){
					int bit = bitReader.readBit();
					code = code + (char)(bit + '0');
				}
				bitReader.flush();
				
				HuffmanTable aux = new HuffmanTable();
				aux.setCod(code);
				huffmanList.add(aux);
				charList.add( (char) letra);
			}
			
			largoTexto = 
		    ((bis.read() & 0xFF) << 24) |
		    ((bis.read() & 0xFF) << 16) |
		    ((bis.read() & 0xFF) << 8)  |
		    (bis.read() & 0xFF);
			
			while(huffmanList.size() > 0) {
				
				char c = (char) charList.poll();
				HuffmanTable huf = huffmanList.poll();
				String path = huf.getCod();
				
				HuffmanInfo posicion = arbol;
				for(int i = 0; i < path.length(); i++){
					
					if(path.charAt(i) == '0'){
						
						if(posicion.getLeft() == null){	
							posicion.setLeft(new HuffmanInfo());
						}
						posicion = posicion.getLeft();
					}
					
					if(path.charAt(i) == '1'){ //Si la accion es 1 voy a la derecha
						
						if(posicion.getRight() == null){ //Si no existe elemento a la derecha	
							posicion.setRight(new HuffmanInfo()); //Inicializo un elemento
						}
						posicion = posicion.getRight(); //Me muevo ahi
					}
				}
				
				posicion.setC(c);
			}
		}
		
		catch(Exception e){e.printStackTrace();}
		
		return largoTexto;
	}

	@Override
	public void descomprimirArchivo(HuffmanInfo root, long largoTexto, String filename)
	{
		
		Console consola= Console.get();
		consola.setPaused(false);

		boolean bPorcentaje = false; 
		
		consola.print("Estado de la descompresion [          ]").skipBkp(11);
		
		
		try(FileOutputStream fos = new FileOutputStream(filename);)
		{
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			int charEncontrados = 0;
			HuffmanInfo posicion = root;
			int i = 1;
			
			while(charEncontrados < largoTexto) {
				
				long porcentajeActual = ((charEncontrados * 100) / largoTexto);
				
				

				
				int accion = bitReader.readBit();
				
				if(accion == 0){
					posicion = posicion.getLeft();
				}
				
				if(accion == 1){
					posicion = posicion.getRight();
				}
				
				if(posicion.getLeft() == null && posicion.getRight() == null){ //Si no tiene hijos es una hoja
					
					bos.write(posicion.getC());
					posicion = root;
					charEncontrados++;
					if(porcentajeActual == (10.00*i)){
						consola.print("#");
						i++;
					}
				}
			}
			
			bos.flush();
			
			while(i <= 9) {
				consola.print("#");
				i++;
			}
			consola.print("#");
			consola.skipFwd();
			consola.println();
			consola.println();
			
		}
		
		catch(Exception e){e.printStackTrace();}
	}

}
