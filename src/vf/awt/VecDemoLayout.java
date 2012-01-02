package vf.awt;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.TextComponent;


public class VecDemoLayout implements LayoutManager
{
	public VecDemoLayout()
	{
	}

	public void addLayoutComponent(String name, Component c)
	{
	}

	public void removeLayoutComponent(Component c)
	{
	}

	public Dimension preferredLayoutSize(Container target)
	{
		return new Dimension(500, 500);
	}

	public Dimension minimumLayoutSize(Container target)
	{
		return new Dimension(100, 100);
	}

	public void layoutContainer(Container target)
	{
		int barwidth = 0;
		int i;
		for (i = 1; i < target.getComponentCount(); i++)
		{
			Component m = target.getComponent(i);
			if (m.isVisible())
			{
				Dimension d = m.getPreferredSize();
				if (d.width > barwidth)
					barwidth = d.width;
			}
		}
		Insets insets = target.insets();
		int targetw = target.size().width - insets.left - insets.right;
		int cw = targetw - barwidth;
		int targeth = target.size().height - (insets.top + insets.bottom);
		target.getComponent(0).move(insets.left, insets.top);
		target.getComponent(0).resize(cw, targeth);
		cw += insets.left;
		int h = insets.top;
		for (i = 1; i < target.getComponentCount(); i++)
		{
			Component m = target.getComponent(i);
			if (m.isVisible())
			{
				Dimension d = m.getPreferredSize();
				if (m instanceof DecentScrollbar || m instanceof TextComponent)
					d.width = barwidth;
				if (m instanceof Choice && d.width > barwidth)
					d.width = barwidth;
				if (m instanceof Label)
				{
					h += d.height / 5;
					d.width = barwidth;
				}
				m.move(cw, h);
				m.resize(d.width, d.height);
				h += d.height;
			}
		}
	}
};