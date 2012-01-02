package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;

public class InverseRadial extends VecFunction
{
	public InverseRadial(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
	}

	double lineLen;

	public String getName()
	{
		return "charged line";
	}

	public void getField(double result[], double y[])
	{
		double r = VecMath.distance(y[0], y[1]);
		if (r < getLineWidth())
			setBoundCheck(true);
		double r2 = r * r;
		result[0] = -.0002 * y[0] / r2;
		result[1] = -.0002 * y[1] / r2;
		result[2] = .4 * java.lang.Math.log(r + 1e-300);
	}

	public void setup()
	{
		lineLen = 1;
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return null;
	}
}