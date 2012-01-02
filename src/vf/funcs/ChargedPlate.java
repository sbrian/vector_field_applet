package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.Complex;

public class ChargedPlate extends ConductingPlate
{
	Complex cz;

	public ChargedPlate(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		cz = new Complex();
	}

	public String getName()
	{
		return "charged plate";
	}

	public double getDivOffset()
	{
		return 4;
	}

	public double getDivRange()
	{
		return 11;
	}

	double getPot(double a1, double a2, double y)
	{
		cz.set(y, -a1);
		cz.mult(y, a2);
		cz.log();
		double b1 = cz.b;
		cz.set(y, a1);
		cz.mult(y, -a2);
		cz.log();
		double y2 = y * y;
		if (y2 == 0)
			y2 = 1e-8;
		return .3 * (2 * (a1 - a2) + (b1 - cz.b) * y + a2
				* java.lang.Math.log(a2 * a2 + y2) - a1
				* java.lang.Math.log(a1 * a1 + y2));
	}

	public void calcDivergence()
	{
		double sep = getValue2() / 100.;
		int x, y;
		for (x = 0; x != getGridSize(); x++)
		{
			double xx = gridToDouble(x);
			if (xx < -a || xx > a)
				continue;
			getGrid()[x][getGridSize() / 2].div = -getReverse();
		}
	}

	public void getField(double result[], double y[])
	{
		if (y[1] >= -.01 && y[1] <= .01 && (y[0] >= -a && y[0] <= a))
			setBoundCheck(true);
		double a1 = -a - y[0];
		double a2 = a - y[0];
		double y2 = y[1] * y[1];
		if (y2 == 0)
			y2 = 1e-8;
		double q = .0003 / a;
		result[0] = .5 * q
				* java.lang.Math.log((y2 + a2 * a2) / (y2 + a1 * a1));
		result[1] = q
				* (java.lang.Math.atan(a1 / y[1]) - java.lang.Math.atan(a2
						/ y[1]));
		result[2] = .4 * getPot(a1, a2, y[1]) / a;
	}

}
