package vf;

public interface ConfigurableValuesCallback
{	
	public int getValue1();
	
	public int getValue2();
	
	public int getValue3();
	
	public int getReverse();
	
	public void setBoundCheck(boolean boundCheck);
	
	public boolean getBoundCheck();
	
	public double[] getRkK1();
	
	public double[] getRkK2();
	
	public double[] getRkK3();
	
	public GridElement[][] getGrid();
	
	public int getGridSize();
	
	public double getLineWidth();
}
