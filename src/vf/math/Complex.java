package vf.math;

public class Complex
{
	public double a, b;

	public Complex()
	{
		a = b = 0;
	}

	public void set(double aa, double bb)
	{
		a = aa;
		b = bb;
	}

	public void set(Complex c)
	{
		set(c.a, c.b);
	}

	public void add(double r)
	{
		a += r;
	}

	public void add(double r, double i)
	{
		a += r;
		b += i;
	}

	public void square()
	{
		set(a * a - b * b, 2 * a * b);
	}

	public void mult(double c, double d)
	{
		set(a * c - b * d, a * d + b * c);
	}

	public void mult(double c)
	{
		a *= c;
		b *= c;
	}

	public void mult(Complex c)
	{
		mult(c.a, c.b);
	}

	public void recip()
	{
		double n = a * a + b * b;
		set(a / n, -b / n);
	}

	public void pow(double p)
	{
		double arg = java.lang.Math.atan2(b, a);
		arg *= p;
		double abs = java.lang.Math.pow(a * a + b * b, p * .5);
		set(abs * java.lang.Math.cos(arg), abs * java.lang.Math.sin(arg));
	}

	public void sin()
	{
		set(VecMath.cosh(b) * java.lang.Math.sin(a), java.lang.Math.cos(a) * VecMath.sinh(b));
	}

	public void cos()
	{
		set(VecMath.cosh(b) * java.lang.Math.cos(a), java.lang.Math.sin(a) * VecMath.sinh(b));
	}

	public void log()
	{
		set(java.lang.Math.log(a * a + b * b), java.lang.Math.atan2(b, a));
	}

	public void arcsin()
	{
		Complex z2 = new Complex();
		z2.set(a, b);
		z2.square();
		z2.mult(-1);
		z2.add(1);
		z2.pow(.5);
		mult(0, 1);
		add(z2.a, z2.b);
		log();
		mult(0, -1);
	}
}