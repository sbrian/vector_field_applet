package vf.math;

import vf.Particle;

public class VecMath
{
	public static final double pi = 3.14159265358979323846;
	
	public static double cosh(double a)
	{
		return .5 * (java.lang.Math.exp(a) + java.lang.Math.exp(-a));
	}

	public static double sinh(double a)
	{
		return .5 * (java.lang.Math.exp(a) - java.lang.Math.exp(-a));
	}
	
	public static double distance(Particle p)
	{
		return distance(p.pos[0], p.pos[1]);
	}

	public static double distance(double y[])
	{
		return distance(y[0], y[1]);
	}

	public static double distance(double x, double y)
	{
		return java.lang.Math.sqrt(x * x + y * y + .000000001);
	}
	
	public static void rotateParticleAdd(double result[], double y[], double mult, double cx,
			double cy)
	{
		result[0] += -mult * (y[1] - cy);
		result[1] += mult * (y[0] - cx);
	}

	public static void rotateParticle(double result[], double y[], double mult)
	{
		result[0] = -mult * y[1];
		result[1] = mult * y[0];
		result[2] = 0;
	}
	
	
	public static int min(int a, int b)
	{
		return (a < b) ? a : b;
	}

	public static int max(int a, int b)
	{
		return (a > b) ? a : b;
	}
	

	public static double min(double a, double b)
	{
		return (a < b) ? a : b;
	}

	public static double max(double a, double b)
	{
		return (a > b) ? a : b;
	}
}
