package vf.awt;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;

public class VecDemoCanvas extends Canvas
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -942607699339021770L;
	VecDemoUpdate pg;

	public VecDemoCanvas(VecDemoUpdate p)
	{
		pg = p;
	}

	public Dimension getPreferredSize()
	{
		return new Dimension(300, 400);
	}

	public void update(Graphics g)
	{
		pg.updateVecDemo(g);
	}

	public void paint(Graphics g)
	{
		pg.updateVecDemo(g);
	}
};