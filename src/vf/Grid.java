package vf;

import vf.math.VecMath;

public class Grid
{
	private static final int gridsize = 80;
	private GridElement grid[][];
	
	public Grid()
	{
		grid = new GridElement[gridsize + 1][gridsize + 1];
	}
	
	public GridElement[][] getGrid()
	{
		return grid;
	}
	
	public void resetGrid()
	{
		for (int y = 0; y != gridsize + 1; y++)
			for (int x = 0; x != gridsize + 1; x++)
			{
				grid[x][y] = new GridElement();
				grid[x][y].curl = grid[x][y].div = grid[x][y].height = 0;
			}
	}
	
	public void setupGrid()
	{
		double zval = 2.0 / gridsize;
		for (int y = 0; y != gridsize; y++)
			for (int x = 0; x != gridsize; x++)
			{
				GridElement ge = grid[x][y];
				double vecx = grid[x + 1][y].height - ge.height;
				double vecy = grid[x][y + 1].height - ge.height;
				ge.normdot = (vecx + vecy + zval)
						* (1 / 1.73)
						/ java.lang.Math.sqrt(vecx * vecx + vecy * vecy + zval
								* zval);
			}
		for (int x = 0; x != gridsize + 1; x++)
		{
			grid[gridsize][x] = grid[gridsize - 1][x];
			grid[x][gridsize] = grid[x][gridsize - 1];
		}
	}
	
	public int getGridSize()
	{
		return gridsize;
	}
	
	public GridElement getGridElement(int x, int y)
	{
		return grid[x][y];
	}
	
	public double getHeight(int x, int y)
	{
		return grid[x][y].height;
	}
	
	public boolean isVisible(int x, int y)
	{
		return grid[x][y].visible;
	}
	
	public void setAllVisible()
	{
		for (int x = 0; x != gridsize; x++)
			for (int y = 0; y != gridsize; y++)
				grid[x][y].visible = true;
	}
	
	public FloatPair interpPoint(int x1, int y1, int x2,
			int y2, double pval)
	{
		FloatPair pos = new FloatPair();
		GridElement ep1 = grid[x1][y1];
		GridElement ep2 = grid[x2][y2];
		double interp2 = (pval - ep1.height) / (ep2.height - ep1.height);
		double interp1 = 1 - interp2;
		pos.x = (x1 * interp1 + x2 * interp2) * 2. / gridsize - 1;
		pos.y = (y1 * interp1 + y2 * interp2) * 2. / gridsize - 1;
		return pos;
	}
	
	public boolean spanning(int x1, int y1, int x2,
			int y2, double pval)
	{
		GridElement ep1 = grid[x1][y1];
		GridElement ep2 = grid[x2][y2];
		if (ep1.height == ep2.height)
			return false;
		return !((ep1.height < pval && ep2.height < pval) || (ep1.height > pval && ep2.height > pval));
	}
	
	public int calcImin(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, double mult)
	{
		GridElement ep1 = grid[x1][y1];
		GridElement ep2 = grid[x2][y2];
		GridElement ep3 = grid[x3][y3];
		GridElement ep4 = grid[x4][y4];
		double pmin = VecMath.min(VecMath.min(ep1.height, ep2.height), VecMath.min(ep3.height,
				ep4.height));
		if (pmin < -5)
			pmin = -5;
		return (int) (pmin / mult);
	}
	
	public int calcImax(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, double mult)
	{
		GridElement ep1 = grid[x1][y1];
		GridElement ep2 = grid[x2][y2];
		GridElement ep3 = grid[x3][y3];
		GridElement ep4 = grid[x4][y4];
		double pmax = VecMath.max(VecMath.max(ep1.height, ep2.height), VecMath.max(ep3.height,
				ep4.height));
		if (pmax > 5)
			pmax = 5;
		return (int) (pmax / mult);
	}
	
	public double getDoubleHeight(double x, double y)
	{
		x = (x + 1) * (gridsize / 2);
		y = (y + 1) * (gridsize / 2);
		int ix = (int) x;
		int iy = (int) y;
		if (ix >= gridsize || iy >= gridsize)
			return grid[ix][iy].height;
		double fracx = x - ix;
		double fracy = y - iy;
		return grid[ix][iy].height * (1 - fracx) * (1 - fracy)
				+ grid[ix + 1][iy].height * fracx * (1 - fracy)
				+ grid[ix][iy + 1].height * (1 - fracx) * fracy
				+ grid[ix + 1][iy + 1].height * fracx * fracy;
	}
}
