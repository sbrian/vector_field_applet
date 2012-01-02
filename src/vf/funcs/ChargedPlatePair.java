package vf.funcs;
import vf.ConfigurableValuesCallback;


public class ChargedPlatePair extends ChargedPlate
{
	public String getName()
	{
		return "charged plate pair";
	}

	public boolean useRungeKutta()
	{
		return false;
	}

	double dipole;

	public ChargedPlatePair(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		dipole = 1;
	}

	public void getField(double result[], double y[])
	{
		double sep = getValue2() / 100.;
		if ((y[1] >= -.01 + sep && y[1] <= .01 + sep || y[1] >= -.01 - sep
				&& y[1] <= .01 - sep)
				&& y[0] >= -a && y[0] <= a)
			setBoundCheck(true);
		double a1 = -a - y[0];
		double a2 = a - y[0];
		double y1 = y[1] - sep;
		double y12 = y1 * y1;
		if (y12 == 0)
			y12 = 1e-8;
		double y2 = y[1] + sep;
		double y22 = y2 * y2;
		if (y22 == 0)
			y22 = 1e-8;
		double q = .0003 / a;
		result[0] = .5
				* q
				* (java.lang.Math.log((y12 + a2 * a2) / (y12 + a1 * a1)) + dipole
						* java.lang.Math.log((y22 + a2 * a2) / (y22 + a1 * a1)));
		result[1] = q
				* (java.lang.Math.atan(a1 / y1) - java.lang.Math.atan(a2 / y1) + dipole
						* (java.lang.Math.atan(a1 / y2) - java.lang.Math
								.atan(a2 / y2)));
		result[2] = .4 * (getPot(a1, a2, y1) + dipole * getPot(a1, a2, y2)) / a;
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
			y = doubleToGrid(sep);
			getGrid()[x][y].div = -getReverse();
			y = doubleToGrid(-sep);
			getGrid()[x][y].div = -dipole
					* getReverse();
		}
	}

	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Sheet Size", 60,
				"Sheet Separation", 33);
	}

	public boolean checkBounds(double y[], double oldY[])
	{
		double size = getValue1() / 100.;
		double sep = getValue2() / 100.;
		if (y[0] >= -size && y[0] <= size)
		{
			if (y[1] > sep)
			{
				if (oldY[1] < sep)
					return true;
			}
			else if (y[1] < -sep)
			{
				if (oldY[1] > -sep)
					return true;
			}
			else if (oldY[1] > sep || oldY[1] < -sep)
				return true;
		}
		return false;
	}

}