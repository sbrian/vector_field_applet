package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;

public class CylinderAndLineCharge extends VecFunction
{
	public CylinderAndLineCharge(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
	}

	public String getName()
	{
		return "cyl + line charge";
	}

	double getCylRadius()
	{
		return (getValue1() + 1) / 110.;
	}

	double getSeparation()
	{
		return getValue2() / 100.;
	}

	double getCylPos()
	{
		return getSeparation() / 2;
	}

	double getPointPos()
	{
		return -getSeparation() / 2 - getCylRadius();
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Cylinder Size", 30, "Separation", 30,
				"Cylinder Potential", 50);
	}

	public void setupFrame()
	{
		q = -.0003;
		a = getCylRadius();
		b = getSeparation() + a;
		spos = getCylPos();
		imagePos = spos - a * a / b;
		double r2_0 = spos + a - imagePos;
		double r3_0 = spos + a - getPointPos();
		cq = a * a * (q / (r2_0 * r2_0) - q / (r3_0 * r3_0));
		pot0 = -cq * java.lang.Math.log(a) + q * java.lang.Math.log(r2_0) - q
				* java.lang.Math.log(r3_0);
		cq -= (getValue3() / 50. - 1) * .0006
				/ java.lang.Math.log(a);
	}

	double q, a, b, spos, imagePos, cq, pot0;

	public void calcDivergence()
	{
		int i;
		double pos[] = getRkK1();
		double res[] = getRkK2();
		double a1 = getCylRadius() + .001;
		int x, y;
		for (x = 0; x != getGridSize(); x++)
			for (y = 0; y != getGridSize(); y++)
				getGrid()[x][y].div = 0;
		// TODO: Getting array out of bounds here when separation or cylinder size gets too large
		getGrid()[doubleToGrid(getPointPos())][doubleToGrid(0)].div = -getReverse();
		for (i = 0; i != 200; i++)
		{
			double th = 2 * VecMath.pi * i / 200.;
			double costh = java.lang.Math.cos(th);
			double sinth = java.lang.Math.sin(th);
			pos[0] = costh * a1 + getCylPos();
			pos[1] = sinth * a1;
			pos[2] = 0;
			
			// TODO: Is this okay?  Used to be curfunc
			this.getField(res, pos);
			getGrid()[doubleToGrid(costh * a
					+ getCylPos())][doubleToGrid(sinth * a)].div += (costh
					* res[0] + sinth * res[1])
					* 60 * getReverse();
		}
	}

	public void getField(double result[], double y[])
	{
		double x1 = y[0] - spos;
		double r1 = VecMath.distance(x1, y[1]);
		final int mult = 4000;
		double y0 = y[0];
		double y1 = y[1];
		if (r1 < a)
		{
			y0 = spos + a;
			y1 = 0;
			x1 = r1 = a;
			setBoundCheck(true);
		}
		double x2 = y0 - imagePos;
		double r2 = VecMath.distance(x2, y1);
		double x3 = y0 - getPointPos();
		double r3 = VecMath.distance(x3, y1);
		double chargeSize = .001;
		if (r3 < chargeSize)
			setBoundCheck(true);
		double a1 = cq / (r1 * r1);
		double a2 = -q / (r2 * r2);
		double a3 = q / (r3 * r3);
		result[0] = x1 * a1 + x2 * a2 + x3 * a3;
		result[1] = y1 * (a1 + a2 + a3);
		result[2] = mult
				* (-pot0 - cq * java.lang.Math.log(r1) + q
						* java.lang.Math.log(r2 + 1e-20) - q
						* java.lang.Math.log(r3 + 1e-20));
		if (r1 == a)
			result[0] = result[1] = 0;
	}
}