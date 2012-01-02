package vf;
import java.awt.Color;

public class Particle
{
	public double pos[];
	public double vel[];
	public double lifetime;
	public double phi, theta, phiv, thetav;
	public double stepsize;
	public Color color;

	public Particle()
	{
		pos = new double[3];
		vel = new double[3];
		stepsize = 1;
	}
};