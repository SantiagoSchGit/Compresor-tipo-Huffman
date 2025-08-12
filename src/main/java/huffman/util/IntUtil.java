package huffman.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IntUtil
{
	public static int read(int b, InputStream is) throws IOException
	{
		int value=0;

		for(int i=0; i<b; i++)
		{
			int byteRead=is.read();
			if(byteRead==-1)
			{
				throw new IOException("End of stream reached before reading enough bytes");
			}
			value=(value<<8)|(byteRead&0xFF);
		}

		return value;
	}

	public static void write(int v, int b, OutputStream os) throws IOException
	{
		for(int i=b-1; i>=0; i--)
		{
			os.write((v>>(i*8))&0xFF);
		}
	}	
}
