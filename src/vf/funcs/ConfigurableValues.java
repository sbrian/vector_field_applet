package vf.funcs;

public class ConfigurableValues
{

	private int initialValue;
	private String name;

	public ConfigurableValues(String name, int initialValue)
	{
		super();
		this.initialValue = initialValue;
		this.name = name;
	}

	public int getInitialValue()
	{
		return initialValue;
	}

	public String getName()
	{
		return name;
	}

	public static ConfigurableValues[] build(String name1, int initialValue1)
	{
		return new ConfigurableValues[] { new ConfigurableValues(name1,
				initialValue1) };
	}

	public static ConfigurableValues[] build(String name1, int initialValue1,
			String name2, int initialValue2)
	{
		return new ConfigurableValues[] {
				new ConfigurableValues(name1, initialValue1),
				new ConfigurableValues(name2, initialValue2) };
	}

	public static ConfigurableValues[] build(String name1, int initialValue1,
			String name2, int initialValue2, String name3, int initialValue3)
	{
		return new ConfigurableValues[] {
				new ConfigurableValues(name1, initialValue1),
				new ConfigurableValues(name2, initialValue2),
				new ConfigurableValues(name3, initialValue3) };
	}
}
