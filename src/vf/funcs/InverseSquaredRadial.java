package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;

public class InverseSquaredRadial extends VecFunction
{
	public InverseSquaredRadial(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
	}

	public String getName()
	{
		return "point charge";
	}

	public void getField(double result[], double y[])
	{
		double r = VecMath.distance(y);
		if (r < chargeSize)
			setBoundCheck(true);
		double r3 = r * r * r;
		double q = .0003 / r3;
		result[0] = -y[0] * q;
		result[1] = -y[1] * q;
		result[2] = -.3 / r;
	}

	static final double chargeSize = .001;
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return null;
	}
}