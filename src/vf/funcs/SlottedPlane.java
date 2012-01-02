package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.Complex;

public class SlottedPlane extends VecFunction
{
	public String getName()
	{
		return "slotted conducting plane";
	}

	Complex z, z2, z3;

	public double getDivOffset()
	{
		return -17.3;
	}

	public double getDivRange()
	{
		return 2.5;
	}

	public SlottedPlane(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		z = new Complex();
		z2 = new Complex();
		z3 = new Complex();
	}

	public void getField(double result[], double y[])
	{
		double a = (getValue1() + 1) / 101.;
		z.set(y[0], y[1]);
		if (y[1] >= -.01 && y[1] <= .01 && (y[0] < -a || y[0] > a))
		{
			setBoundCheck(true);
			if (z.b == 0)
				z.b = -1e-8;
		}
		z2.set(z);
		z2.square();
		z2.add(-a * a);
		z3.set(z2);
		z3.pow(.5);
		if (z3.b < 0)
			z3.mult(-1);
		z3.add(z.a, z.b);
		result[2] = z3.b * 2;
		z2.pow(-.5);
		if (z2.b > 0)
			z2.mult(-1);
		z2.mult(z);
		result[1] = -(1 + z2.a) * .003;
		result[0] = -(z2.b) * .003;
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Slot Size", 30);
	}
}