package vf.funcs;
import vf.ConfigurableValuesCallback;


public class ChargedPlateDipole extends ChargedPlatePair
{
	public String getName()
	{
		return "charged plate dipole";
	}

	public ChargedPlateDipole(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		dipole = -1;
	}

}