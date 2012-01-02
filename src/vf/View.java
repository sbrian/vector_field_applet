package vf;

import java.awt.Rectangle;

import vf.math.VecMath;

public class View implements Cloneable
{
	private double viewAngle;
	private double viewZoom = 1.6;
	private double viewHeight = 2;
	private double viewDistance = 5;
	private double scalex, scaley;
	static final int MODE_VIEW_ZOOM = 1;
	static final int MODE_LINE_INT = 2;
	static final int MODE_SURF_INT = 3;
	static final int MODE_VIEW_ROTATE = 0;

	public double getViewAngle()
	{
		return viewAngle;
	}

	public void setViewAngle(double viewAngle)
	{
		this.viewAngle = viewAngle;
	}

	public double getViewZoom()
	{
		return viewZoom;
	}

	public void setViewZoom(double viewZoom)
	{
		this.viewZoom = viewZoom;
	}

	public double getViewAngleCos()
	{
		return java.lang.Math.cos(viewAngle);
	}

	public double getViewAngleSin()
	{
		return java.lang.Math.sin(viewAngle);
	}

	public double getViewHeight()
	{
		return viewHeight;
	}

	public void setViewHeight(double viewHeight)
	{
		this.viewHeight = viewHeight;
	}

	public double getViewDistance()
	{
		return viewDistance;
	}

	public void setViewDistance(double viewDistance)
	{
		this.viewDistance = viewDistance;
	}
	
	public EditViewResult editView(int x, int y, boolean isFlat, int mode,
			int dragStartX, int dragStartY, View originalView)
	{
		EditViewResult editViewResult = new EditViewResult();
		if (mode ==View.MODE_VIEW_ROTATE)
		{
			if (isFlat)
				return editViewResult;
			viewAngle = (dragStartX - x) / 40. + originalView.getViewAngle();
			while (viewAngle < 0)
				viewAngle += 2 * VecMath.pi;
			while (viewAngle >= 2 * VecMath.pi)
				viewAngle -= 2 * VecMath.pi;
			
			viewHeight = -(dragStartY - y) / 10. +  originalView.getViewHeight();
			if (getViewHeight() > 9)
				viewHeight = 9;
			if (getViewHeight() < -9)
				viewHeight = -9;
			editViewResult.setBackgroundChanged(true);
			editViewResult.setNeedsRepaint(true);
			return editViewResult;
		}
		if (mode == View.MODE_VIEW_ZOOM)
		{
			if (isFlat)
				return editViewResult;
			viewZoom = (x - dragStartX) / 40. + originalView.getViewHeight();
			if (getViewZoom() < .1)
				viewZoom = .1;
			editViewResult.setBackgroundChanged(true);
			editViewResult.setNeedsRepaint(true);
			return editViewResult;
		}
		if (mode == View.MODE_LINE_INT
				|| mode == View.MODE_SURF_INT)
		{
			editViewResult.setResetIntegralXAndY(true);
			editViewResult.setNeedsRepaint(true);
			return editViewResult;
		}
		throw new RuntimeException();
		
	}
	
	void map3d(double x, double y, double z, int xpoints[], int ypoints[],
			int pt, Rectangle view, boolean isFlat, double levelHeight)
	{
		if (isFlat)
		{
			xpoints[pt] = view.x + (int) ((x + 1) * view.width / 2);
			ypoints[pt] = view.y + (int) ((1 - y) * view.height / 2);
			return;
		}
		if (z < -1000)
			z = -1000;
		if (z > 1000)
			z = 1000;
		double realx = x * getViewAngleCos() + y * getViewAngleSin();
		double realy = z - getViewHeight();
		double realz = y * getViewAngleCos() - x * getViewAngleSin() + getViewDistance();
		scalex = getViewZoom() * (view.width / 4) * getViewDistance();
		scaley = scalex;
		int yoff = (int) (scaley * (getViewHeight() - levelHeight) / getViewDistance());
		xpoints[pt] = view.x + view.width / 2 + (int) (scalex * realx / realz);
		ypoints[pt] = view.y + view.height / 2 - yoff
				- (int) (scaley * realy / realz);
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
