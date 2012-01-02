package vf.funcs;
import vf.ConfigurableValuesCallback;
import vf.math.VecMath;

public class InverseRadialQuad extends VecFunction
{
	public InverseRadialQuad(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
	}

	public String getName()
	{
		return "quad lines";
	}

	public void getField(double result[], double y[])
	{
		double sep = gridToDouble(getGridSize() / 2
						+ getValue1()
						* getGridSize() / 200);
		double xx1 = y[0] + sep;
		double xx2 = y[0] - sep;
		double yy1 = y[1] + sep;
		double yy2 = y[1] - sep;
		double r1 = VecMath.distance(xx1, yy1);
		double r2 = VecMath.distance(xx2, yy1);
		double r3 = VecMath.distance(xx1, yy2);
		double r4 = VecMath.distance(xx2, yy2);
		if (r1 < getLineWidth()
				|| r2 < getLineWidth()
				|| r3 < getLineWidth()
				|| r4 < getLineWidth())
			setBoundCheck(true);
		double q = .0003;
		result[0] = q
				* (-xx1 / (r1 * r1) - xx2 / (r4 * r4) + xx2 / (r2 * r2) + xx1
						/ (r3 * r3));
		result[1] = q
				* (-yy1 / (r1 * r1) - yy2 / (r4 * r4) + yy1 / (r2 * r2) + yy2
						/ (r3 * r3));
		result[2] = .2 * (+java.lang.Math.log(r1 + 1e-20)
				- java.lang.Math.log(r2 + 1e-20)
				- java.lang.Math.log(r3 + 1e-20) + java.lang.Math
				.log(r4 + 1e-20));
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Line Separation", 30);
	}
}