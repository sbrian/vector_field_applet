package vf;

public class GridElement
{
	public double height, div, curl, normdot, vecX, vecY;
	public boolean visible, valid;
	
	static final int FC_FIELD = 0;
	static final int FC_POTENTIAL = 1;
	static final int FC_NONE = 2;
	static final int FC_DIV = 3;
	static final int FC_CURL = 4;
	
	private final double floorBrightMult = 2;
	
	public int computeNormDotColor(int fc, double levelHeight, double divOffset, double divRange)
	{
		return computeColor(normdot, fc, levelHeight, divOffset, divRange);
	}
	
	public int computeColor(double c, int fc, double levelHeight, double divOffset, double divRange)
	{
		if (c < 0)
			c = 0;
		if (c > 1)
			c = 1;
		c = .5 + c * .5;
		double value = 0;
		double range = 10;
		double offset = 4;
		switch (fc)
		{
		case FC_FIELD:
			value = vecX * vecX + vecY * vecY;
			offset = 10;
			range = 16;
			if (!valid)
				return 0xFF000080;
			break;
		case FC_POTENTIAL:
			value = height - levelHeight;
			offset = 1;
			range = 2;
			break;
		case FC_CURL:
			value = curl;
			offset = 4;
			range = 10;
			break;
		case FC_DIV:
			value = div;
			offset = divOffset;
			range = divRange;
			break;
		case FC_NONE:
			if (!valid)
				return 0xFF000080;
			break;
		}
		value *= floorBrightMult;
		double redness = (value < 0) ? (java.lang.Math.log(-value) + offset)
				/ range : 0;
		double grnness = (value > 0) ? (java.lang.Math.log(value) + offset)
				/ range : 0;
		if (redness > 1)
			redness = 1;
		if (grnness > 1)
			grnness = 1;
		if (grnness < 0)
			grnness = 0;
		if (redness < 0)
			redness = 0;
		double grayness = (1 - (redness + grnness)) * c;
		double gray = .6;
		int r = (int) ((c * redness + gray * grayness) * 255);
		int g = (int) ((c * grnness + gray * grayness) * 255);
		int b = (int) ((gray * grayness) * 255);
		return (255 << 24) | (r << 16) | (g << 8) | b;
	}
};