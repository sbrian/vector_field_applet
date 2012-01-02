package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;

public class InverseRadialDouble extends VecFunction
{
	public InverseRadialDouble(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		sign = 1;
	}

	public String getName()
	{
		return "line charge double";
	}

	double sign;

	public void getField(double result[], double y[])
	{
		double sep = gridToDouble(getGridSize() / 2
						+ getValue1()
						* getGridSize() / 200);
		double xx1 = y[0] - sep;
		double xx2 = y[0] + sep;
		double r1 = VecMath.distance(xx1, y[1]);
		double r2 = VecMath.distance(xx2, y[1]);
		if (r1 < getLineWidth()
				|| r2 < getLineWidth())
			setBoundCheck(true);
		double q = .0002;
		double r1s = 1 / (r1 * r1);
		double r2s = 1 / (r2 * r2 * sign);
		result[0] = q * (-xx1 * r1s - xx2 * r2s);
		result[1] = q * (-y[1] * r1s - y[1] * r2s);
		result[2] = .2 * (java.lang.Math.log(r1 + 1e-20) + sign
				* java.lang.Math.log(r2 + 1e-20));
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Line Separation", 30);
	}
}