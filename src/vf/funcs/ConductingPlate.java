package vf.funcs;

import vf.ConfigurableValuesCallback;
import vf.math.Complex;

public class ConductingPlate extends VecFunction
{
	public String getName()
	{
		return "conducting plate";
	}

	Complex z, z2;
	boolean plate;
	double a, base;

	public ConductingPlate(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		z = new Complex();
		z2 = new Complex();
		plate = true;
	}

	public void setupFrame()
	{
		a = (getValue1() + 1) / 100.;
		z.set(0, 1 / a);
		z.arcsin();
		base = z.b;
	}

	public void getField(double result[], double y[])
	{
		if (y[1] >= -.02 && y[1] <= .02)
		{
			if ((plate && y[0] >= -a && y[0] <= a)
					|| (!plate && (y[0] >= a || y[0] <= -a)))
				setBoundCheck(true);
		}
		z.set(y[0] / a, y[1] / a);
		if (y[1] < 0 && plate)
			z.b = -z.b;
		z2.set(z);
		z2.arcsin();
		result[2] = (plate) ? z2.b / base - 1 : -z2.a * .6;
		z.square();
		z.mult(-1);
		z.add(1);
		z.pow(-.5);
		z.mult(1 / a);
		if (plate)
		{
			result[1] = z.a * -.0007;
			result[0] = z.b * -.0007;
			if (y[1] <= 0)
				result[1] = -result[1];
		}
		else
		{
			result[0] = z.a * .0007;
			result[1] = -z.b * .0007;
			if (y[1] == 0)
				result[1] = -result[1];
		}
	}

	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Plate Size", 60);
	}
	
	public double getDivOffset()
	{
		return -17.3;
	}

	public double getDivRange()
	{
		return 2.5;
	}


}