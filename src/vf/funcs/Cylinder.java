package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;


public class Cylinder extends VecFunction
{
	public Cylinder(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
	}

	public String getName()
	{
		return "conducting cylinder";
	}

	double getCylRadius()
	{
		return (getValue1() + 1) / 110.;
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Cylinder Size", 30,
				"Cylinder Potential", 1);
	}

	public void getField(double result[], double y[])
	{
		double a = getCylRadius();
		double farpt = 4;
		double pot = 2 * (getValue2() / 50. - 1);
		final int mult = 4000;
		double cq = -pot
				/ (mult * (java.lang.Math.log(a) - java.lang.Math.log(farpt)));
		double pot0 = mult * cq * java.lang.Math.log(farpt);
		double y0 = y[0];
		double y1 = y[1];
		double r1 = VecMath.distance(y0, y1);
		if (r1 < a)
		{
			result[0] = result[1] = 0;
			result[2] = pot;
			setBoundCheck(true);
			return;
		}
		double a1 = 5 * cq / (r1 * r1);
		result[0] = y0 * a1;
		result[1] = y1 * a1;
		result[2] = pot0 - cq * mult * java.lang.Math.log(r1);
	}

	public void calcDivergence()
	{
		double a = getCylRadius();
		int i;
		for (i = 0; i != 100; i++)
		{
			double th = 2 * VecMath.pi * i / 100.;
			double xx = java.lang.Math.cos(th) * a;
			double yy = java.lang.Math.sin(th) * a;
			getGrid()[doubleToGrid(xx)][
					doubleToGrid(yy)].div -= getReverse() / 20.;
		}
	}


}