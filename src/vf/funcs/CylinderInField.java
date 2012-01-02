package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;

public class CylinderInField extends VecFunction
{
	public CylinderInField(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		conducting = true;
		showD = false;
	}

	public String getName()
	{
		return "cylinder in field";
	}

	boolean conducting, showD;
	double a;

	public void setupFrame()
	{
		a = getValue1() / 100.;
	}

	public void getField(double result[], double y[])
	{
		double a2 = a * a;
		double r = VecMath.distance(y[0], y[1]);
		double e1 = getValue2() / 10. + 1;
		double dimult = (conducting) ? 1 : (e1 - 1) / (e1 + 1);
		double fmult = .0006;
		final double potmult = 3;
		if (r < a)
		{
			result[0] = result[1] = result[2] = 0;
			if (conducting)
				setBoundCheck(true);
			else
				result[0] = (showD) ? e1 * fmult * (1 - dimult) : fmult
						* (1 - dimult);
			result[2] = -potmult * (1 - dimult) * y[0];
			return;
		}
		double costh = y[0] / r;
		double sinth = y[1] / r;
		double r_2 = 1 / (r * r);
		double er = (1 + dimult * a2 * r_2) * costh * fmult;
		double eth = -(1 - dimult * a2 * r_2) * sinth * fmult;
		er /= r;
		result[0] = y[0] * er - eth * sinth;
		result[1] = y[1] * er + eth * costh;
		result[2] = -potmult * (1 - dimult * a2 * r_2) * y[0];
	}

	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Cylinder Size", 40);
	}
	
	public void calcDivergence()
	{
		int i;
		double pos[] = getRkK1();
		double res[] = getRkK2();
		double a1 = a + .001;
		int x, y;
		for (x = 0; x != getGridSize(); x++)
			for (y = 0; y != getGridSize(); y++)
				getGrid()[x][y].div = 0;
		for (i = 0; i != 200; i++)
		{
			double th = 2 * VecMath.pi * i / 200.;
			double costh = java.lang.Math.cos(th);
			double sinth = java.lang.Math.sin(th);
			pos[0] = costh * a1;
			pos[1] = sinth * a1;
			pos[2] = 0;
			this.getField(res, pos);
			double rx = res[0];
			double ry = res[1];
			double a2 = a - .001;
			pos[0] = costh * a2;
			pos[1] = sinth * a2;
			pos[2] = 0;
			this.getField(res, pos);
			getGrid()[doubleToGrid(costh * a)][
					doubleToGrid(sinth * a)].div += (costh * (rx - res[0]) + sinth
					* (ry - res[1]))
					* 4e2 * getReverse();
		}
	}


}