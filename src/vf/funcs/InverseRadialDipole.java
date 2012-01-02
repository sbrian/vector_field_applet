package vf.funcs;
import vf.ConfigurableValuesCallback;


public class InverseRadialDipole extends InverseRadialDouble
{
	public InverseRadialDipole(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		sign = 1;
	}

	public String getName()
	{
		return "dipole lines";
	}
}