package vf.funcs;

import vf.ConfigurableValuesCallback;
import vf.GridElement;

public abstract class VecFunction
{
	private VecFunction()
	{
		// block this constructor
	}

	public VecFunction(ConfigurableValuesCallback vectorFunctionInfo)
	{
		this.vectorFunctionInfo = vectorFunctionInfo;
	}

	protected ConfigurableValuesCallback vectorFunctionInfo;

	public abstract String getName();

	public boolean nonGradient()
	{
		return false;
	}

	public boolean useRungeKutta()
	{
		return true;
	}

	public boolean useAdaptiveRungeKutta()
	{
		return true;
	}

	public boolean checkBoundsWithForce()
	{
		return true;
	}

	public boolean checkBounds(double y[], double oldY[])
	{
		return false;
	}

	public abstract void getField(double result[], double y[]);

	public boolean redistribute()
	{
		return true;
	}

	public void setup()
	{
	}

	public void setupFrame()
	{
	}

	public void finishFrame()
	{
	}

	public void actionPerformed()
	{
	}

	public void calcDivergence()
	{
	}

	public double getLevelHeight()
	{
		return 0;
	}

	public void setGrid(GridElement ge, int x, int y, double rk_k1[], double rk_k2[], double rk_k3[])
	{
		double xx[] = rk_k1;
		double res[] =  rk_k2;
		double res1[] =  rk_k3;
		xx[0] = (x * 2. / getGridSize()) - 1;
		xx[1] = (y * 2. / getGridSize()) - 1;
		xx[2] = 0;
		vectorFunctionInfo.setBoundCheck(false);
		getField(res, xx);
		ge.vecX = getReverse() * res[0] * 70;
		ge.vecY = getReverse() * res[1] * 70;
		ge.height = getReverse() * res[2] * .625;
		ge.valid = !getBoundCheck();
		double xorig0 = xx[0];
		xx[0] += 1e-8;
		getField(res1, xx);
		ge.div = res1[0] - res[0];
		ge.curl = res1[1] - res[1];
		xx[0] = xorig0;
		xx[1] += 1e-8;
		getField(res1, xx);
		ge.div = (ge.div + res1[1] - res[1]) * 1e10
				* getReverse();
		ge.curl = (ge.curl - (res1[0] - res[0])) * 1e10
				* getReverse();
	}

	public double getDivOffset()
	{
		return 4;
	}

	public double getDivRange()
	{
		return 11;
	}
	
	public int doubleToGrid(double x)
	{
		return (int) ((x + 1) * getGridSize() / 2);
	}

	public double gridToDouble(int x)
	{
		return (x * 2. / getGridSize() ) - 1;
	}
	
	public abstract ConfigurableValues[] getConfigurableValues();
	
	protected final int getValue1()
	{
		return vectorFunctionInfo.getValue1();
	}
	
	protected final int getValue2()
	{
		return vectorFunctionInfo.getValue2();
	}
	
	protected final int getValue3()
	{
		return vectorFunctionInfo.getValue3();
	}
	
	protected final int getReverse()
	{
		return vectorFunctionInfo.getReverse();
	}
	
	protected void setBoundCheck(boolean boundCheck)
	{
		vectorFunctionInfo.setBoundCheck(boundCheck);
	}
	
	protected boolean getBoundCheck()
	{
		return vectorFunctionInfo.getBoundCheck();
	}
	
	protected int getGridSize()
	{
		return vectorFunctionInfo.getGridSize();
	}
	
	protected double getLineWidth()
	{
		return vectorFunctionInfo.getLineWidth();
	}
	
	protected double[] getRkK1()
	{
		return vectorFunctionInfo.getRkK1();
	}

	protected double[] getRkK2()
	{
		return vectorFunctionInfo.getRkK2();
	}

	protected double[] getRkK3()
	{
		return vectorFunctionInfo.getRkK3();
	}
	
	protected GridElement[][] getGrid()
	{
		return vectorFunctionInfo.getGrid();
	}
	
}