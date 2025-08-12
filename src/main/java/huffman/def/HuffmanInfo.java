package huffman.def;

import java.util.Comparator;

public class HuffmanInfo
{
	private int c;
	private int n;
	private HuffmanInfo left = null;
	private HuffmanInfo right = null;
	
	public HuffmanInfo(){setC(0); setN(0);};
	public HuffmanInfo(int c,int n)
	{
		setC(c);
		setN(n);
	}
	
	public int getC()
	{
		return c;
	}
	public void setC(int c)
	{
		this.c=c;
	}
	public int getN()
	{
		return n;
	}
	public void setN(int n)
	{
		this.n=n;
	}
	public HuffmanInfo getLeft()
	{
		return left;
	}
	public void setLeft(HuffmanInfo left)
	{
		this.left=left;
	}
	public HuffmanInfo getRight()
	{
		return right;
	}
	public void setRight(HuffmanInfo right)
	{
		this.right=right;
	}
	
	@Override
	public String toString()
	{
		return (char)c+" ("+n+")";
	}
	
	public static Comparator<HuffmanInfo> comparadorPorN = new Comparator<HuffmanInfo>() {
        @Override
        public int compare(HuffmanInfo a, HuffmanInfo b) {
            return a.getN()==b.getN()?a.getC()-b.getC():a.getN()-b.getN();
        }
    };
}
