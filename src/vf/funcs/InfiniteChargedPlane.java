package vf.funcs;
import vf.ConfigurableValuesCallback;

public class InfiniteChargedPlane extends VecFunction
{
	public InfiniteChargedPlane(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
	}

	public String getName()
	{
		return "infinite plane";
	}

	public void getField(double result[], double y[])
	{
		double alpha = .0004;
		if (y[1] > -.01 && y[1] < .01)
			setBoundCheck(true);
		result[0] = 0;
		result[1] = (y[1] <= 0) ? alpha : -alpha;
		result[2] = java.lang.Math.abs(y[1]) - 1;
	}

	public boolean checkBoundsWithForce()
	{
		return false;
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return null;
	}
}