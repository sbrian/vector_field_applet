package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;

public class InverseSquaredRadialDouble extends InverseSquaredRadial
{
	public InverseSquaredRadialDouble(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		// TODO Auto-generated constructor stub
	}

	public String getName()
	{
		return "point charge double";
	}

	double sign2;

	public void getField(double result[], double y[])
	{
		double sep = gridToDouble(getGridSize() / 2
						+ getValue1()
						* getGridSize() / 200);
		double xx1 = y[0] - sep;
		double xx2 = y[0] + sep;
		double r1 = VecMath.distance(xx1, y[1]);
		if (r1 < chargeSize)
			setBoundCheck(true);
		double r2 = VecMath.distance(xx2, y[1]);
		if (r2 < chargeSize)
			setBoundCheck(true);
		double q = .0003;
		double rq1 = q / (r1 * r1 * r1);
		double rq2 = q / (r2 * r2 * r2) * sign2;
		result[0] = -xx1 * rq1 - xx2 * rq2;
		result[1] = -y[1] * rq1 - y[1] * rq2;
		result[2] = -.05 / r1 - .05 * sign2 / r2;
		if (sign2 == -1)
			result[2] *= 2;
	}

	public void setup()
	{
		sign2 = 1;
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Charge Separation", 30);
	}

};