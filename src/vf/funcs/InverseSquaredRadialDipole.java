package vf.funcs;
import vf.ConfigurableValuesCallback;


public class InverseSquaredRadialDipole extends InverseSquaredRadialDouble
{
	public InverseSquaredRadialDipole(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		// TODO Auto-generated constructor stub
	}

	public String getName()
	{
		return "dipole";
	}

	public void setup()
	{
		super.setup();
		sign2 = -1;
	}
}