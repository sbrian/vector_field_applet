package vf.funcs;
import vf.ConfigurableValuesCallback;


public class DielectricCylinderInFieldE extends CylinderInField
{
	public DielectricCylinderInFieldE(ConfigurableValuesCallback vectorFunctionInfo)
	{
		super(vectorFunctionInfo);
		conducting = false;
		showD = false;
	}

	public String getName()
	{
		return "dielec cyl in field";
	}
	
	public ConfigurableValues[] getConfigurableValues()
	{
		return ConfigurableValues.build("Cylinder Size", 40,
				"Dielectric Strength", 60);
	}
}