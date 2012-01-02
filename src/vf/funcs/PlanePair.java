package vf.funcs;
import vf.ConfigurableValuesCallback;

public class PlanePair extends ConductingPlate
{
	public String getName()
	{
		return "conducting planes w/ gap";
	}

	public PlanePair(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		plate = false;
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Gap Size", 20);
	}

	public double getDivOffset()
	{
		return -17;
	}
};