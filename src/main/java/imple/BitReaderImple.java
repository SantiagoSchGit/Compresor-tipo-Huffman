package imple;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import huffman.def.BitReader;

public class BitReaderImple implements BitReader
{
	InputStream is;
	
	Queue<Integer> bitsGuardados = new LinkedList<>();
	
	@Override
	public void using(InputStream is)
	{		
		this.is =is;
	}

	@Override
	public int readBit()
	{
		if(bitsGuardados.size() == 0) {
			try
			{
				int byteLeido = is.read();
				
				if(byteLeido != -1) {
					
					for (int i = 7; i >= 0; i--) { 
						int bit = (byteLeido >> i) & 1; 
						bitsGuardados.add(bit); 
					}
				}
				else {
					return -1;
				}
			}
			catch(Exception e){e.printStackTrace();}
		}
		
		return bitsGuardados.poll();
	}
	
	@Override
	public void flush()
	{
		while(bitsGuardados.size()>0) {
			bitsGuardados.poll();
		}
	}
}
