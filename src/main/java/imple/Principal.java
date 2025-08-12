package imple;
import java.io.File;
import java.util.List;
import huffman.def.Compresor;
import huffman.def.Descompresor;
import huffman.def.HuffmanInfo;
import huffman.def.HuffmanTable;
import huffman.util.Console;

public class Principal
{

	@SuppressWarnings("static-access")
	public static String pedirNombreArchivo(){
		
		Console consola= null;
		boolean tru = true;
		consola = consola.get(tru );

		consola.println("Por favor ingrese el archivo: ");
        String filenameBruto = consola.fileExplorer();
        
        if(filenameBruto == null) {
        	consola.closeAndExit();
        }
        
		return filenameBruto;
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
        
		Console consola= null;
		boolean tru = true;
		consola = consola.get(tru );
		String seguir = "1";
		while(seguir.equals("1")) {
			
			String filenameBruto = pedirNombreArchivo();
			File archivo = new File(filenameBruto);

			
	        if(archivo.exists()){
	        	if(filenameBruto.endsWith(".huf")){
	                String filename = filenameBruto.substring(0, filenameBruto.length() - 4);
	                
	                Descompresor descompresor = Factory.getDescompresor();
	                HuffmanInfo root = new HuffmanInfo();
	                long largo= descompresor.recomponerArbol(filename,root);
	                descompresor.descomprimirArchivo(root,largo,filename);
	                
	                consola.println("Se termino de descomprimir el archivo: " + filenameBruto);
	            } 
	            
	            else{
	                
	            	String filename = filenameBruto;
	            	
	            	Compresor compresor = Factory.getCompresor();
	            	HuffmanTable[] arrayHuffmanTable = compresor.contarOcurrencias(filename);
	            	List<HuffmanInfo> listaHuffmanInfo = compresor.crearListaEnlazada(arrayHuffmanTable);
	            	HuffmanInfo root = compresor.convertirListaEnArbol(listaHuffmanInfo);
	            	compresor.generarCodigosHuffman(root,arrayHuffmanTable);
	            	compresor.escribirEncabezado(filename,arrayHuffmanTable);
	            	compresor.escribirContenido(filename,arrayHuffmanTable);
	            	
	            	consola.println();
	            	consola.print("Se termino de comprimir el archivo: " + filenameBruto);
	   			 	consola.println();
	            }
	        }
	        
	        else {
	        	consola.print("El archivo \"" + filenameBruto + "\" no existe.");
				 consola.println();
	        }
			consola.print("¿Desea seguir?");
			 consola.println();
			consola.print("1: Si");
			 //consola.skipFwd(3);
			 consola.println();
			consola.print("Cualquier otra tecla: No ");
	        consola.println();
			seguir = consola.readString();
	        consola.println();
			consola.println();
		}
		
		consola.closeAndExit();
	}
	
}
