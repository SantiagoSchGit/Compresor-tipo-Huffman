package imple;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import huffman.def.BitWriter;

public class BitWriterImple implements BitWriter 
{
	FileOutputStream fos;
	BufferedOutputStream bos;
	
	Queue<Integer> byteActual = new LinkedList<>();
	
    @Override
    public void using(OutputStream os)
    {
    	fos = (FileOutputStream)os;
    	bos = new BufferedOutputStream(fos);
    }

    @Override
    public void writeBit(int bit) 
    {
    	
    	byteActual.add(bit);
    	
    	if(byteActual.size() == 8) {
    		
    		int byteASubir = 0;
    		
    		while(byteActual.size() > 0){
                int actualBit = byteActual.poll();
                byteASubir = ((byteASubir << 1) | actualBit);
            }
    		
    		try
			{
				bos.write(byteASubir);
			}
			catch(Exception e){e.printStackTrace();}
    	}
    }
        
    @Override
    public void flush() 
    {
    	
    	if(byteActual.size() > 0) {
    		
    		while(byteActual.size() < 8) {
    			byteActual.add(0);
    		}
    		
    		int byteASubir = 0;
    		
    		while(byteActual.size() > 0){
                int actualBit = byteActual.poll();
                byteASubir = ((byteASubir << 1) | actualBit);
            }
    		
    		try
			{
				bos.write(byteASubir);
			}
			catch(Exception e){e.printStackTrace();}
    	}
    	
    	try
		{
			bos.flush();
		}
		catch(IOException e){e.printStackTrace();}
    }
}
