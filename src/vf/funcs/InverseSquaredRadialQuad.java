package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;

public class InverseSquaredRadialQuad extends InverseSquaredRadial
{
	public InverseSquaredRadialQuad(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
	}

	public String getName()
	{
		return "quadrupole";
	}

	public void getField(double result[], double y[])
	{
		double sep = gridToDouble(getGridSize() / 2
						+ getValue1()
						* getGridSize() / 200);
		double xx1 = y[0] - sep;
		double xx2 = y[0] + sep;
		double yy1 = y[1] - sep;
		double yy2 = y[1] + sep;
		double r1 = VecMath.distance(xx1, yy1);
		double r2 = VecMath.distance(xx2, yy1);
		double r3 = VecMath.distance(xx1, yy2);
		double r4 = VecMath.distance(xx2, yy2);
		if (r1 < chargeSize || r2 < chargeSize || r3 < chargeSize
				|| r4 < chargeSize)
			setBoundCheck(true);
		double q = .0003;
		double rq1 = q / (r1 * r1 * r1);
		double rq2 = q / (r2 * r2 * r2);
		double rq3 = q / (r3 * r3 * r3);
		double rq4 = q / (r4 * r4 * r4);
		result[0] = -xx1 * rq1 - xx2 * rq4 + xx2 * rq2 + xx1 * rq3;
		result[1] = -yy1 * rq1 - yy2 * rq4 + yy1 * rq2 + yy2 * rq3;
		result[2] = .05 * (-1 / r1 + 1 / r2 + 1 / r3 - 1 / r4);
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Charge Separation", 30);
	}
}