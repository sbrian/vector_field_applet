package vf.awt;
import java.awt.Label;


public class AuxBar
{
	public DecentScrollbar bar;
	public Label label;

	public AuxBar(Label l, DecentScrollbar b)
	{
		label = l;
		bar = b;
	}
	
	public DecentScrollbar getScrollbar()
	{
		return bar;
	}
};