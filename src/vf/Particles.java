package vf;

import java.awt.Color;
import java.util.Random;

import vf.math.VecMath;

public class Particles
{
	static final int densitygridsize = 16;
	static final double densitygroupsize = 2. / densitygridsize;
	
	private int density[][];
	private Random random;
	private Particle particles[];
	
	private int maxParticleCount;
	
	private int rediscount;
	
	private FieldVector vectors[];
	
	public Particles(int maxParticleCount)
	{
		this.maxParticleCount = maxParticleCount;
		Color particleColors[] = new Color[27];
		int i;
		for (i = 0; i != 27; i++)
			particleColors[i] = new Color(((i % 3) + 1) * 85,
					((i / 3) % 3 + 1) * 85, ((i / 9) % 3 + 1) * 85);
		random = new Random();
		particles = new Particle[maxParticleCount];
		for (i = 0; i != maxParticleCount; i++)
		{
			particles[i] = new Particle();
			particles[i].color = particleColors[i % 27];
		}
		density = new int[densitygridsize][densitygridsize];
	}
	
	public Particle getParticle(int index)
	{
		return particles[index];
	}
	
	public void clearVectors()
	{
		vectors = null;
	}
	
	public void resetDensityGroups(int pcount)
	{
		int i, j, k;
		for (i = 0; i != densitygridsize; i++)
			for (j = 0; j != densitygridsize; j++)
				density[i][j] = 0;
		for (i = 0; i != pcount; i++)
		{
			Particle p = particles[i];
			addToDensityGroup(p);
		}
		for (; i != maxParticleCount; i++)
		{
			Particle p = particles[i];
			p.lifetime = -100;
		}
	}

	public int addToDensityGroup(Particle p)
	{
		int a = (int) ((p.pos[0] + 1) * (densitygridsize / 2));
		int b = (int) ((p.pos[1] + 1) * (densitygridsize / 2));
		int n = 0;
		try
		{
			n = ++density[a][b];
			if (n > maxParticleCount)
				System.out.print(a + " " + b + " " + density[a][b] + "\n");
		}
		catch (Exception e)
		{
			System.out.print(p.pos[0] + " " + p.pos[1] + "\n");
			e.printStackTrace();
		}
		return n;
	}

	public void removeFromDensityGroup(Particle p)
	{
		int a = (int) ((p.pos[0] + 1) * (densitygridsize / 2));
		int b = (int) ((p.pos[1] + 1) * (densitygridsize / 2));
		try
		{
			if (--density[a][b] < 0)
				System.out.print(a + " " + b + " " + density[a][b] + "\n");
		}
		catch (Exception e)
		{
			System.out.print(p.pos[0] + " " + p.pos[1] + "\n");
			e.printStackTrace();
		}
	}

	public void positionParticle(Particle p, boolean redistribute)
	{
		int x, y;
		int bestx = 0, besty = 0;
		int best = 10000;
		int randaddx = getrand(densitygridsize);
		int randaddy = getrand(densitygridsize);
		for (x = 0; x != densitygridsize; x++)
			for (y = 0; y != densitygridsize; y++)
			{
				int ix = (randaddx + x) % densitygridsize;
				int iy = (randaddy + y) % densitygridsize;
				if (density[ix][iy] <= best)
				{
					bestx = ix;
					besty = iy;
					best = density[ix][iy];
				}
			}
		p.pos[0] = bestx * densitygroupsize + getrand(100) * densitygroupsize
				/ 100.0 - 1;
		p.pos[1] = besty * densitygroupsize + getrand(100) * densitygroupsize
				/ 100.0 - 1;
		p.lifetime = redistribute ? 500 : 5000;
		p.stepsize = 1;
		p.theta = (getrand(101) - 50) * VecMath.pi / 50.;
		p.phi = (getrand(101) - 50) * VecMath.pi / 50.;
		int j;
		for (j = 0; j != 3; j++)
			p.vel[j] = 0;
	}



	public void resetParticles(int pcount)
	{
		int i, j;
		for (i = 0; i != pcount; i++)
		{
			Particle p = particles[i];
			for (j = 0; j != 2; j++)
			{
				p.pos[j] = getrand(200) / 100.0 - 1;
				p.vel[j] = 0;
			}
			p.pos[2] = 0;
			p.lifetime = i * 2;
			p.stepsize = 1;
		}
		resetDensityGroups(pcount);
	}
	
	public void kickParticles(int particleCount)
	{
		int i, j;
		for (i = 0; i != particleCount; i++)
		{
			Particle p = particles[i];
			for (j = 0; j != 2; j++)
				p.vel[j] += (getrand(100) / 99.0 - .5) * .04;
		}
	}
	
	private int getrand(int x)
	{
		int q = random.nextInt();
		if (q < 0)
			q = -q;
		return q % x;
	}
	
	public void redistributeIfNeeded(int bestd, int pcount)
	{
		int maxd = (6 * pcount / (densitygridsize * densitygridsize));
		if ( bestd > maxd)
			redistribute(bestd, pcount);
	}
	
	private void redistribute(int mostd, int pcount)
	{
		if (mostd < 5)
			return;
		rediscount++;
		int maxd = (6 * pcount / (densitygridsize * densitygridsize));
		int i;
		int pn = 0;
		for (i = rediscount % 4; i < pcount; i += 4)
		{
			Particle p = particles[i];
			int a = (int) ((p.pos[0] + 1) * (densitygridsize / 2));
			int b = (int) ((p.pos[1] + 1) * (densitygridsize / 2));
			if (density[a][b] <= maxd)
				continue;
			p.lifetime = -1;
			pn++;
		}
	}
}
