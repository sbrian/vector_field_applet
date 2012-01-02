package vf;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Vector;

import vf.awt.AuxBar;
import vf.awt.DecentScrollbar;
import vf.awt.DecentScrollbarListener;
import vf.awt.VecDemoCanvas;
import vf.awt.VecDemoLayout;
import vf.awt.VecDemoUpdate;
import vf.funcs.ChargedPlate;
import vf.funcs.ChargedPlateDipole;
import vf.funcs.ChargedPlatePair;
import vf.funcs.ConductingPlate;
import vf.funcs.ConfigurableValues;
import vf.funcs.Cylinder;
import vf.funcs.CylinderAndLineCharge;
import vf.funcs.CylinderInField;
import vf.funcs.DielectricCylinderInFieldE;
import vf.funcs.InfiniteChargedPlane;
import vf.funcs.InverseRadial;
import vf.funcs.InverseRadialDipole;
import vf.funcs.InverseRadialDouble;
import vf.funcs.InverseRadialQuad;
import vf.funcs.InverseSquaredRadial;
import vf.funcs.InverseSquaredRadialDipole;
import vf.funcs.InverseSquaredRadialDouble;
import vf.funcs.InverseSquaredRadialQuad;
import vf.funcs.PlanePair;
import vf.funcs.SlottedPlane;
import vf.funcs.VecFunction;
import vf.math.VecMath;

class VecDemoFrame extends Frame implements ComponentListener, ActionListener,
		MouseMotionListener, MouseListener, ItemListener,
		DecentScrollbarListener, VecDemoUpdate, ConfigurableValuesCallback
{
	private static final long serialVersionUID = -4447336773448217912L;
	private static final int maxParticleCount = 2500;
	private View viewHolder = new View();
	private Particles particlesHolder;
	
	private Grid gridHolder;
	
	private Dimension winSize;
	private Rectangle viewMain, viewAxes;
	private Image dbimage;
	private Image backimage;
	private MemoryImageSource imageSource;
	private int pixels[];
	private VecDemo applet;

	private AuxBar auxBars[];
	private DecentScrollbar partCountBar;
	private DecentScrollbar strengthBar;
	private int reverse;
	private boolean boundCheck;
	
	private double rk_k1[] = new double[6];
	private double rk_k2[] = new double[6];
	private double rk_k3[] = new double[6];
	

	public final double lineWidth = .001;
	private double wooft = 0;
	
	
	
	private VecDemoCanvas cv;
	private Checkbox stoppedCheck;
	private Button resetButton;
	private Button kickButton;
	private Checkbox reverseCheck;
	private Button infoButton;
	private Choice functionChooser;
	private Choice dispChooser;
	
	static final int DISP_PART_VELOC = 0;
	static final int DISP_PART_FORCE = 1;
	static final int DISP_VECTORS = 2;
	static final int DISP_NONE = 3;
	static final int DISP_CURLERS = 4;
	
	private Label partCountLabel;
	private Label textFieldLabel;
	private Label strengthLabel;

	private double fieldStrength, barFieldStrength;
	private Color darkYellow = new Color(144, 144, 0);

	private Label vecDensityLabel;
	private DecentScrollbar vecDensityBar;
	private Label potentialLabel;
	private DecentScrollbar potentialBar;
	private Choice modeChooser;
	private Choice floorColorChooser;
	private Choice floorLineChooser;
	private TextField textFields[];

	private int xpoints[];
	private int ypoints[];
	

	private Checkbox flatCheck;
	private boolean isFlat;

	private int integralX = -1, integralY;
	private int vectorSpacing = 16;
	private int currentStep;
	private boolean parseError;
	private Color fieldColors[];
	

	private boolean functionChanged;
	private boolean backgroundChanged;
	private boolean draggingView;
	private int dragStartX, dragStartY;
	private View dragStartView;
	private Vector<VecFunction> functionList;
	private VecFunction curfunc;
	private int pause = 20;
	static final int MOT_VELOCITY = 0;
	static final int MOT_FORCE = 1;
	static final int MOT_CURLERS = 2;
	static final int MOT_EQUIPOTENTIAL = 3;

	static final int FL_NONE = 0;
	static final int FL_GRID = 1;
	static final int FL_EQUIP = 2;
	static final int FL_LINES = 3;
	private boolean useBufferedImage = false;
	
	private int shadowBufferTop[], shadowBufferBottom[], shadowBufferTop2[],
			shadowBufferBottom2[];
	
	

	
	private double rk_Y[] = new double[6];
	private double rk_Yhalf[] = new double[6];
	private double rk_oldY[] = new double[6];
	private double ls_fieldavg[] = new double[3];
	
	
	private double oldY[];

	private double rk_k4[] = new double[6];
	private double rk_yn[] = new double[6];
	
	
	static final double root2 = 1.4142135623730950488016887242096981;

	

	
	private static int frames = 0;
	private static long firsttime = 0;
	
	private long lastTime;
	private double timeStep, partMult;
	private boolean slowDragView = true;
	
	public static final Class[] VECTOR_FUNCTIONS = new Class[]{
		InverseRadial.class,
		InverseRadialDouble.class,
		InverseRadialDipole.class,
		InverseRadialQuad.class,
		InverseSquaredRadial.class,
		InverseSquaredRadialDouble.class,
		InverseSquaredRadialDipole.class,
		InverseSquaredRadialQuad.class,
		ConductingPlate.class,
		ChargedPlate.class,
		ChargedPlatePair.class,
		ChargedPlateDipole.class,
		InfiniteChargedPlane.class,
		Cylinder.class,
		CylinderAndLineCharge.class,
		CylinderInField.class,
		DielectricCylinderInFieldE.class,
		SlottedPlane.class,
		PlanePair.class
	};
	

	public String getAppletInfo()
	{
		return "VecDemo by Paul Falstad";
	}

	public VecDemoFrame(VecDemo a)
	{
		super("2-D Electrostatic Fields Applet v1.4");
		applet = a;
	}

	public void init()
	{
		try
		{
			_init();
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void _init() throws IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException
	{
		try
		{
			String param = applet.getParameter("PAUSE");
			if (param != null)
				pause = Integer.parseInt(param);
		}
		catch (Exception e)
		{
		}
		String jv = System.getProperty("java.class.version");
		double jvf = new Double(jv).doubleValue();
		if (jvf >= 48)
			useBufferedImage = true;
		
		particlesHolder = new Particles(maxParticleCount);
		
		functionList = new Vector<VecFunction>();
		
		
		for( Class<VecFunction> cls : VECTOR_FUNCTIONS )
		{
			VecFunction vf = cls.getConstructor(
					new Class[] { ConfigurableValuesCallback.class }).newInstance(
					new Object[] { this });
			functionList.addElement(vf);
		}
		
		
		xpoints = new int[4];
		ypoints = new int[4];
		
		setLayout(new VecDemoLayout());
		cv = new VecDemoCanvas(this);
		cv.addComponentListener(this);
		cv.addMouseMotionListener(this);
		cv.addMouseListener(this);
		add(cv);
		functionChooser = new Choice();
		int i;
		for (i = 0; i != functionList.size(); i++)
			functionChooser.add("Setup: "
					+ ((VecFunction) functionList.elementAt(i)).getName());
		add(functionChooser);
		functionChooser.addItemListener(this);
		floorColorChooser = new Choice();
		floorColorChooser.add("Color: field magnitude");
		floorColorChooser.add("Color: potential");
		floorColorChooser.add("Color: none");
		floorColorChooser.add("Color: charge");
		floorColorChooser.addItemListener(this);
		add(floorColorChooser);
		floorLineChooser = new Choice();
		floorLineChooser.add("Floor: no lines");
		floorLineChooser.add("Floor: grid");
		floorLineChooser.add("Floor: equipotentials");
		floorLineChooser.add("Floor: field lines");
		floorLineChooser.addItemListener(this);
		add(floorLineChooser);
		floorLineChooser.select(FL_EQUIP);
		flatCheck = new Checkbox("Flat View");
		flatCheck.addItemListener(this);
		add(flatCheck);
		dispChooser = new Choice();
		dispChooser.addItemListener(this);
		setupDispChooser(true);
		add(dispChooser);
		modeChooser = new Choice();
		modeChooser.add("Mouse = Adjust Angle");
		modeChooser.add("Mouse = Adjust Zoom");
		modeChooser.add("Mouse = Line Integral");
		modeChooser.add("Mouse = Surface Integral");
		modeChooser.addItemListener(this);
		add(modeChooser);
		stoppedCheck = new Checkbox("Stopped");
		stoppedCheck.addItemListener(this);
		add(stoppedCheck);
		reverseCheck = new Checkbox("Reverse");
		reverseCheck.addItemListener(this);
		add(reverseCheck);
		resetButton = new Button("Reset");
		add(resetButton);
		resetButton.addActionListener(this);
		kickButton = new Button("Kick");
		add(kickButton);
		kickButton.addActionListener(this);
		kickButton.disable();
		add(strengthLabel = new Label("Field Strength", Label.CENTER));
		add(strengthBar = new DecentScrollbar(this, 80, 1, 120));
		add(partCountLabel = new Label("Number of Particles", Label.CENTER));
		add(partCountBar = new DecentScrollbar(this, 500, 1, maxParticleCount));
		add(vecDensityLabel = new Label("Vector Density", Label.CENTER));
		add(vecDensityBar = new DecentScrollbar(this, 32, 2, 64));
		add(potentialLabel = new Label("Potential", Label.CENTER));
		add(potentialBar = new DecentScrollbar(this, 250, 0, 1000));
		Label lb;
		auxBars = new AuxBar[3];
		add(lb = new Label("Aux 1", Label.CENTER));
		DecentScrollbar aux1Bar;
		add(aux1Bar = new DecentScrollbar(this, 0, 0, 100));
		auxBars[0] = new AuxBar(lb, aux1Bar);
		add(lb = new Label("Aux 2", Label.CENTER));
		DecentScrollbar aux2Bar;
		add(aux2Bar = new DecentScrollbar(this, 0, 0, 100));
		auxBars[1] = new AuxBar(lb, aux2Bar);
		add(lb = new Label("Aux 3", Label.CENTER));
		DecentScrollbar aux3Bar;
		add(aux3Bar = new DecentScrollbar(this, 0, 0, 100));
		auxBars[2] = new AuxBar(lb, aux3Bar);
		textFields = new TextField[2];
		for (i = 0; i != 2; i++)
		{
			add(textFields[i] = new TextField());
			textFields[i].addActionListener(this);
		}
		fieldColors = new Color[513];
		int grayLevel = 76;
		for (i = 0; i != 256; i++)
		{
			int rb = grayLevel + (128 - grayLevel) * i / 255;
			int g = grayLevel + (255 - grayLevel) * i / 255;
			int col = (255 << 24) | (g << 8) | (rb << 16) | (rb);
			fieldColors[i] = new Color(col);
		}
		for (i = 0; i != 256; i++)
		{
			int col = (255 << 24) | (255 << 8) | ((i / 2 + 128) * (0x10001));
			fieldColors[i + 256] = new Color(col);
		}
		fieldColors[512] = fieldColors[511];
		add(new Label("http://www.falstad.com", Label.CENTER));
		reinit();
		cv.setBackground(Color.black);
		cv.setForeground(Color.lightGray);
		resize(650, 500);
		handleResize();
		Dimension screen = getToolkit().getScreenSize();
		Dimension x = getSize();
		setLocation((screen.width - x.width) / 2,
				(screen.height - x.height) / 2);
		functionChanged();
		dispChooserChanged();
		show();
		
		
	}

	private void handleResize()
	{
		Dimension d = winSize = cv.getSize();
		if (winSize.width == 0)
			return;
		dbimage = createImage(d.width, d.height);
		viewMain = new Rectangle(winSize);
		viewAxes = new Rectangle(winSize.width - 100, 0, 100, 100);
		backgroundChanged = true;
		pixels = null;
		if (useBufferedImage)
		{
			try
			{
				Class biclass = Class.forName("java.awt.image.BufferedImage");
				Class dbiclass = Class.forName("java.awt.image.DataBufferInt");
				Class rasclass = Class.forName("java.awt.image.Raster");
				Constructor cstr = biclass.getConstructor(new Class[] {
						int.class, int.class, int.class });
				backimage = (Image) cstr.newInstance(new Object[] {
						new Integer(d.width), new Integer(d.height),
						new Integer(BufferedImage.TYPE_INT_RGB) });
				Method m = biclass.getMethod("getRaster", null);
				Object ras = m.invoke(backimage, null);
				Object db = rasclass.getMethod("getDataBuffer", null).invoke(
						ras, null);
				pixels = (int[]) dbiclass.getMethod("getData", null).invoke(db,
						null);
			}
			catch (Exception ee)
			{
				System.out.println("BufferedImage failed");
			}
		}
		if (pixels == null)
		{
			pixels = new int[d.width * d.height];
			int i;
			for (i = 0; i != d.width * d.height; i++)
				pixels[i] = 0xFF000000;
			imageSource = new MemoryImageSource(d.width, d.height, pixels, 0,
					d.width);
			imageSource.setAnimated(true);
			imageSource.setFullBufferUpdates(true);
			backimage = cv.createImage(imageSource);
		}
	}



	private void generateFunction()
	{
		int x, y;
		
		if ( gridHolder == null )
			gridHolder = new Grid();
		
		curfunc.setupFrame();

		gridHolder.resetGrid();

		double mu, xx, xx2, yy, yy2, r, r1, r2, r3, r4;
		double levelheight = curfunc.getLevelHeight();
		for (x = 0; x != gridHolder.getGridSize() + 1; x++)
			for (y = 0; y != gridHolder.getGridSize() + 1; y++)
			{
				GridElement ge = gridHolder.getGridElement(x, y);
				curfunc.setGrid(ge, x, y, rk_k1, rk_k2, rk_k3);
			}
		
		curfunc.calcDivergence();
		
		gridHolder.setupGrid();
		

		functionChanged = false;
		backgroundChanged = true;
	}





	private void reinit()
	{
		handleResize();
		resetParticles();
		functionChanged = backgroundChanged = true;
	}

	private void centerString(Graphics g, String s, int y)
	{
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, (winSize.width - fm.stringWidth(s)) / 2, y);
	}

	private void drawBackground()
	{
		if (isFlat)
		{
			int x, y, gridsize = gridHolder.getGridSize();
			for (y = 0; y < gridHolder.getGridSize(); y++)
				for (x = 0; x < gridHolder.getGridSize(); x++)
				{
					GridElement ge = gridHolder.getGridElement(x, y);
					int nx = x * winSize.width / gridsize;
					int ny = winSize.height - (y + 1) * winSize.height
							/ gridsize;
					int nx1 = (x + 1) * winSize.width / gridsize;
					int ny1 = winSize.height - y * winSize.height / gridsize;
					int col = ge.computeColor(0, floorColorChooser.getSelectedIndex(),
								curfunc.getLevelHeight(), curfunc.getDivOffset(), curfunc.getDivRange());
					fillRectangle(nx, ny, nx1, ny1, col);
					ge.visible = true;
				}
			drawFloor();
			functionChanged = backgroundChanged = false;
			if (imageSource != null)
				imageSource.newPixels();
			return;
		}
		int x, y;
		int xdir, xstart, xend;
		int ydir, ystart, yend;
		int sc = gridHolder.getGridSize();
		if (viewHolder.getViewAngleCos() < 0)
		{
			ystart = sc;
			yend = 0;
			ydir = -1;
		}
		else
		{
			ystart = 0;
			yend = sc;
			ydir = 1;
		}
		if (viewHolder.getViewAngleSin() < 0)
		{
			xstart = 0;
			xend = sc;
			xdir = 1;
		}
		else
		{
			xstart = sc;
			xend = 0;
			xdir = -1;
		}
		boolean xFirst = (-viewHolder.getViewAngleSin() * xdir > viewHolder.getViewAngleCos() * ydir);
		shadowBufferBottom = new int[winSize.width];
		shadowBufferTop = new int[winSize.width];
		shadowBufferBottom2 = new int[winSize.width];
		shadowBufferTop2 = new int[winSize.width];
		for (x = 0; x != winSize.width; x++)
		{
			shadowBufferBottom[x] = shadowBufferBottom2[x] = 0;
			shadowBufferTop[x] = shadowBufferTop2[x] = winSize.height - 1;
		}
		for (x = 0; x != winSize.width * winSize.height; x++)
			pixels[x] = 0xFF000000;
		int goffx = (xdir == 1) ? 0 : -1;
		int goffy = (ydir == 1) ? 0 : -1;
		int gridsize = gridHolder.getGridSize();
		for (x = xstart; x != xend; x += xdir)
		{
			for (y = ystart; y != yend; y += ydir)
			{
				if (!xFirst)
					x = xstart;
				for (; x != xend; x += xdir)
				{
					double nx = x * (2.0 / gridsize) - 1;
					double ny = y * (2.0 / gridsize) - 1;
					double nx1 = (x + xdir) * (2.0 / gridsize) - 1;
					double ny1 = (y + ydir) * (2.0 / gridsize) - 1;
					map3d(nx, ny, gridHolder.getHeight(x, y), xpoints, ypoints, 0);
					map3d(nx1, ny, gridHolder.getHeight(x + xdir, y), xpoints, ypoints,
							1);
					map3d(nx, ny1, gridHolder.getHeight(x, y + ydir), xpoints, ypoints,
							2);
					map3d(nx1, ny1, gridHolder.getHeight(x + xdir, y + ydir), xpoints,
							ypoints, 3);
					GridElement ge = gridHolder.getGridElement(x + goffx, y + goffy);
					int col = ge.computeNormDotColor(floorColorChooser
							.getSelectedIndex(), curfunc.getLevelHeight(),
							curfunc.getDivOffset(), curfunc.getDivRange());
					fillTriangle(xpoints[0], ypoints[0], xpoints[1],
							ypoints[1], xpoints[3], ypoints[3], col);
					fillTriangle(xpoints[0], ypoints[0], xpoints[2],
							ypoints[2], xpoints[3], ypoints[3], col);
					int cx = (xpoints[0] + xpoints[3]) / 2;
					int cy = (ypoints[0] + ypoints[3]) / 2;
					boolean vis = false;
					if (cx >= 0 && cx < winSize.width
							&& cy <= shadowBufferTop[cx] && cy >= 0)
						vis = true;
					ge.visible = vis;
					if (xFirst)
						break;
				}
				if (!xFirst)
				{
					int i;
					for (i = 0; i != winSize.width; i++)
					{
						shadowBufferTop[i] = shadowBufferTop2[i];
						shadowBufferBottom[i] = shadowBufferBottom2[i];
					}
				}
			}
			if (!xFirst)
				break;
			int i;
			for (i = 0; i != winSize.width; i++)
			{
				shadowBufferTop[i] = shadowBufferTop2[i];
				shadowBufferBottom[i] = shadowBufferBottom2[i];
			}
		}
		drawFloor();
		functionChanged = backgroundChanged = false;
		if (imageSource != null)
			imageSource.newPixels();
	}

	private void drawFloor()
	{
		int x, y, gridsize = gridHolder.getGridSize();
		switch (floorLineChooser.getSelectedIndex())
		{
		case FL_NONE:
			break;
		case FL_GRID:
			for (x = 0; x != gridsize; x++)
				for (y = 0; y != gridsize; y += 10)
				{
					double nx = x * (2.0 / gridsize) - 1;
					double nx1 = (x + 1) * (2.0 / gridsize) - 1;
					double ny = y * (2.0 / gridsize) - 1;
					if (gridHolder.isVisible(x, y))
					{
						map3d(nx, ny,  gridHolder.getHeight(x, y), xpoints, ypoints, 0);
						map3d(nx1, ny, gridHolder.getHeight(x + 1, y), xpoints, ypoints,
								1);
						drawLine(xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
					}
					if ( gridHolder.isVisible(y, x) )
					{
						map3d(ny, nx, gridHolder.getHeight(y, x), xpoints, ypoints, 0);
						map3d(ny, nx1, gridHolder.getHeight(y, x + 1), xpoints, ypoints,
								1);
						drawLine(xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
					}
				}
			break;
		case FL_EQUIP:
			if (!curfunc.nonGradient())
				renderEquips();
			break;
		case FL_LINES:
			genLines();
			break;
		}
	}

	private void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int col)
	{
		if (x1 > x2)
		{
			if (x2 > x3)
			{
				int ay = interp(x1, y1, x3, y3, x2);
				fillTriangle1(x3, y3, x2, y2, ay, col);
				fillTriangle1(x1, y1, x2, y2, ay, col);
			}
			else if (x1 > x3)
			{
				int ay = interp(x1, y1, x2, y2, x3);
				fillTriangle1(x2, y2, x3, y3, ay, col);
				fillTriangle1(x1, y1, x3, y3, ay, col);
			}
			else
			{
				int ay = interp(x3, y3, x2, y2, x1);
				fillTriangle1(x2, y2, x1, y1, ay, col);
				fillTriangle1(x3, y3, x1, y1, ay, col);
			}
		}
		else
		{
			if (x1 > x3)
			{
				int ay = interp(x2, y2, x3, y3, x1);
				fillTriangle1(x3, y3, x1, y1, ay, col);
				fillTriangle1(x2, y2, x1, y1, ay, col);
			}
			else if (x2 > x3)
			{
				int ay = interp(x2, y2, x1, y1, x3);
				fillTriangle1(x1, y1, x3, y3, ay, col);
				fillTriangle1(x2, y2, x3, y3, ay, col);
			}
			else
			{
				int ay = interp(x3, y3, x1, y1, x2);
				fillTriangle1(x1, y1, x2, y2, ay, col);
				fillTriangle1(x3, y3, x2, y2, ay, col);
			}
		}
	}

	private int interp(int x1, int y1, int x2, int y2, int x)
	{
		if (x1 == x2)
			return y1;
		if (x < x1 && x < x2 || x > x1 && x > x2)
			System.out.print("interp out of bounds\n");
		return (int) (y1 + ((double) x - x1) * (y2 - y1) / (x2 - x1));
	}

	private void fillTriangle1(int x1, int y1, int x2, int y2, int y3, int col)
	{
		int dir = (x1 > x2) ? -1 : 1;
		int x = x1;
		if (x < 0)
		{
			x = 0;
			if (x2 < 0)
				return;
		}
		if (x >= winSize.width)
		{
			x = winSize.width - 1;
			if (x2 >= winSize.width)
				return;
		}
		if (y2 > y3)
		{
			int q = y2;
			y2 = y3;
			y3 = q;
		}
		while (x != x2 + dir)
		{
			int ya = interp(x1, y1, x2, y2, x);
			int yb = interp(x1, y1, x2, y3, x);
			if (ya < 0)
				ya = 0;
			if (yb >= winSize.height)
				yb = winSize.height - 1;
			if (shadowBufferTop2[x] > ya)
				shadowBufferTop2[x] = ya;
			if (shadowBufferBottom2[x] < yb)
				shadowBufferBottom2[x] = yb;
			int sb1 = shadowBufferTop[x];
			int sb2 = shadowBufferBottom[x];
			if (!(ya >= sb1 && yb <= sb2))
			{
				for (; ya <= yb; ya++)
				{
					if (ya < sb1 || ya > sb2)
						pixels[x + ya * winSize.width] = col;
				}
			}
			x += dir;
			if (x < 0 || x >= winSize.width)
				return;
		}
	}

	private void fillRectangle(int x1, int y1, int x2, int y2, int col)
	{
		int x, y;
		for (y = y1; y < y2; y++)
			for (x = x1; x < x2; x++)
				pixels[x + y * winSize.width] = col;
	}

	private void drawLine(int x1, int y1, int x2, int y2)
	{
		if (x1 == x2 && y1 == y2)
			return;
		if (abs(y2 - y1) > abs(x2 - x1))
		{
			int sgn = sign(y2 - y1);
			int x, y;
			for (y = y1; y != y2 + sgn; y += sgn)
			{
				x = x1 + (x2 - x1) * (y - y1) / (y2 - y1);
				if (x >= 0 && y >= 0 && x < winSize.width && y < winSize.height)
					pixels[x + y * winSize.width] = 0xFFC0C0C0;
			}
		}
		else
		{
			int sgn = sign(x2 - x1);
			int x, y;
			for (x = x1; x != x2 + sgn; x += sgn)
			{
				y = y1 + (y2 - y1) * (x - x1) / (x2 - x1);
				if (x >= 0 && y >= 0 && x < winSize.width && y < winSize.height)
					pixels[x + y * winSize.width] = 0xFFC0C0C0;
			}
		}
	}

	private int abs(int x)
	{
		return x < 0 ? -x : x;
	}

	private int sign(int x)
	{
		return (x < 0) ? -1 : (x == 0) ? 0 : 1;
	}

	private void renderEquips()
	{
		int x, y, gridsize = gridHolder.getGridSize();
		for (x = 0; x != gridsize; x++)
			for (y = 0; y != gridsize; y++)
			{
				if (!gridHolder.isVisible(x, y) )
					continue;
				tryEdge(x, y, x + 1, y, x, y + 1, x + 1, y + 1);
				tryEdge(x, y, x + 1, y, x, y, x, y + 1);
				tryEdge(x, y, x + 1, y, x + 1, y, x + 1, y + 1);
				tryEdge(x, y, x, y + 1, x + 1, y, x + 1, y + 1);
				tryEdge(x, y, x, y + 1, x, y + 1, x + 1, y + 1);
				tryEdge(x + 1, y, x + 1, y + 1, x, y + 1, x + 1, y + 1);
			}
	}

	private void tryEdge(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
	{
		int i;
		double emult = 5;
		double mult = 1 / (40 * emult * .1);
		
		int imin = gridHolder.calcImin(x1, y1, x2, y2, x3, y3, x4, y4, mult);
		
		int imax = gridHolder.calcImin(x1, y1, x2, y2, x3, y3, x4, y4, mult);
		
		for (i = imin; i <= imax; i++)
		{
			double pval = i * mult;
			if (!(gridHolder.spanning(x1, y1, x2, y2, pval) && gridHolder.spanning(x3, y3, x4, y4, pval)))
				continue;
			FloatPair pa = gridHolder.interpPoint(x1, y1, x2, y2, pval);
			FloatPair pb = gridHolder.interpPoint(x3, y3, x4, y4, pval);			
			map3d(pa.x, pa.y, pval, xpoints, ypoints, 0);
			map3d(pb.x, pb.y, pval, xpoints, ypoints, 1);
			drawLine(xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
		}
	}

	private void drawLineBackground(Graphics g)
	{
		int x, y, gridsize = gridHolder.getGridSize();
		gridHolder.setAllVisible();

		if (isFlat)
			return;
		for (y = gridsize - 1; y >= 0; y--)
		{
			for (x = 0; x < gridsize; x += 5)
			{
				double ny = y * (2.0 / gridsize) - 1;
				double nx1 = (x + 1) * (2.0 / gridsize) - 1;
				double ny1 = (y + 1) * (2.0 / gridsize) - 1;
				map3d(nx1, ny, gridHolder.getHeight(x, y), xpoints, ypoints, 1);
				map3d(nx1, ny1, gridHolder.getHeight(x, y + 1), xpoints, ypoints, 2);
				ypoints[1] = bound_y(ypoints[1]);
				ypoints[2] = bound_y(ypoints[2]);
				g.drawLine(xpoints[1], ypoints[1], xpoints[2], ypoints[2]);
			}
		}
		for (y = 0; y < gridsize; y += 5)
		{
			for (x = gridsize - 1; x >= 0; x--)
			{
				double nx = x * (2.0 / gridsize) - 1;
				double nx1 = (x + 1) * (2.0 / gridsize) - 1;
				double ny1 = (y + 1) * (2.0 / gridsize) - 1;
				map3d(nx, ny1, gridHolder.getHeight(x, y), xpoints, ypoints, 3);
				map3d(nx1, ny1, gridHolder.getHeight(x + 1, y), xpoints, ypoints, 2);
				ypoints[3] = bound_y(ypoints[3]);
				ypoints[2] = bound_y(ypoints[2]);
				g.drawLine(xpoints[3], ypoints[3], xpoints[2], ypoints[2]);
			}
		}
	}

	private int bound_y(int y)
	{
		if (y < -100)
			y = -100;
		if (y > winSize.height + 100)
			y = winSize.height + 100;
		return y;
	}

	public void paint(Graphics g)
	{
		cv.repaint();
	}

	private void map3d(double x, double y, double z, int xpoints[], int ypoints[],
			int pt)
	{
		viewHolder.map3d(x, y, z, xpoints, ypoints, pt, viewMain, isFlat, curfunc.getLevelHeight());
	}
	
	private void map3d(double x, double y, double z, int xpoints[], int ypoints[],
			int pt, Rectangle view)
	{
		viewHolder.map3d(x, y, z, xpoints, ypoints, pt, view, isFlat, curfunc.getLevelHeight());
	}

	private void sayCalculating(Graphics realg)
	{
		realg.setColor(cv.getBackground());
		FontMetrics fm = realg.getFontMetrics();
		String s = "Calculating...";
		realg.fillRect(0, winSize.height - 30, 20 + fm.stringWidth(s), 30);
		realg.setColor(Color.white);
		realg.drawString(s, 10, winSize.height - 10);
	}

	public void updateVecDemo(Graphics realg)
	{
		Graphics g = dbimage.getGraphics();
		if (winSize == null || winSize.width == 0)
			return;
		if (xpoints == null)
			return;
		checkFlatState();
		barFieldStrength = fieldStrength = java.lang.Math.exp((strengthBar
				.getValue() - 50) / 10.);
		if (functionChanged || backgroundChanged)
		{
			if (functionChanged)
			{
				sayCalculating(realg);
				generateFunction();
			}
			if (!slowDragView || !draggingView)
			{
				long tm1 = System.currentTimeMillis();
				sayCalculating(realg);
				drawBackground();
				long tm2 = System.currentTimeMillis();
				slowDragView = (tm2 - tm1 > 40);
			}
		}
		if ((draggingView && slowDragView) || functionChanged)
		{
			g.setColor(isFlat ? fieldColors[0] : cv.getBackground());
			g.fillRect(0, 0, winSize.width, winSize.height);
			g.setColor(cv.getForeground());
			drawLineBackground(g);
		}
		else
			g.drawImage(backimage, 0, 0, this);
		boolean allquiet = true;
		curfunc.setupFrame();
		fieldStrength = barFieldStrength;
		partMult = fieldStrength * reverse * timeStep;
		int disp = dispChooser.getSelectedIndex();
		timeStep = 1;
		if (!stoppedCheck.getState())
		{
			if (lastTime > 0)
				timeStep = (System.currentTimeMillis() - lastTime) * .03;
			if (timeStep > 3)
				timeStep = 3;
			lastTime = System.currentTimeMillis();
			if (disp != DISP_VECTORS && disp != DISP_NONE)
			{
				moveParticles(getParticleCount(), curfunc.redistribute(), dispChooser.getSelectedIndex(), timeStep);
				allquiet = false;
			}
			currentStep += reverse;
			if (currentStep < 0)
				currentStep += 800;
		}
		else
			lastTime = 0;
		if (disp == DISP_VECTORS)
			drawVectors(g);
		else if (disp != DISP_NONE)
			drawParticles(g, dispChooser.getSelectedIndex(), getParticleCount());
		g.setColor(Color.gray);
		if (!isFlat)
			drawAxes(g);
		curfunc.finishFrame();
		int mode = modeChooser.getSelectedIndex();
		if (mode == View.MODE_LINE_INT)
			lineIntegral(g, true);
		else if (mode == View.MODE_SURF_INT)
			lineIntegral(g, false);
		if (parseError)
			centerString(g, "Can't parse expression", winSize.height - 20);
		realg.drawImage(dbimage, 0, 0, this);
		long t = System.currentTimeMillis();
		frames++;
		if (firsttime == 0)
			firsttime = t;
		else if (t - firsttime > 1000)
		{
			firsttime = t;
			frames = 0;
		}
		if (!stoppedCheck.getState() && !allquiet)
			cv.repaint(pause);
	}

	private void drawAxes(Graphics g)
	{
		g.setColor(Color.white);
		map3d(0, 0, 0, xpoints, ypoints, 0, viewAxes);
		map3d(1, 0, 0, xpoints, ypoints, 1, viewAxes);
		drawArrow(g, "x", xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
		map3d(0, 1, 0, xpoints, ypoints, 1, viewAxes);
		drawArrow(g, "y", xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
		map3d(0, 0, 1, xpoints, ypoints, 1, viewAxes);
		drawArrow(g, "z", xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
	}

	private void drawVectors(Graphics g)
	{
		int x, y;
		DrawData dd = new DrawData();
		dd.mult = barFieldStrength * 40;
		dd.g = g;
		dd.field = new double[3];
		dd.vv = new double[3];
		vectorSpacing = vecDensityBar.getValue();
		double vec[] = new double[3];
		for (x = 0; x != vectorSpacing; x++)
		{
			vec[0] = x * (2.0 / (vectorSpacing - 1)) - 1;
			for (y = 0; y != vectorSpacing; y++)
			{
				vec[1] = y * (2.0 / (vectorSpacing - 1)) - 1;
				drawVector(dd, vec);
			}
		}
	}

	private void lineIntegral(Graphics g, boolean line)
	{
		if (integralX == -1)
			return;
		if (dragStartX == integralX || dragStartY == integralY)
			return;
		int x1 = VecMath.min(dragStartX, integralX);
		int y1 = VecMath.min(dragStartY, integralY);
		int x2 = VecMath.max(dragStartX, integralX);
		int y2 = VecMath.max(dragStartY, integralY);
		int step = 15;
		int x;
		double pos[] = rk_k2;
		if (!line)
		{
			g.setColor(Color.white);
			g.drawRect(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
		}
		double y1p = 1 - 2. * y1 / winSize.height;
		double y2p = 1 - 2. * y2 / winSize.height;
		for (x = x1; x <= x2; x += step)
		{
			int step1 = x2 - x;
			if (step1 > step)
				step1 = step;
			pos[0] = 2. * x / winSize.width - 1;
			pos[1] = y1p;
			lineIntegralStep(g, x, y1, pos, step1, 0, line);
			pos[1] = y2p;
			lineIntegralStep(g, x + step1, y2, pos, -step1, 0, line);
		}
		int y;
		double x1p = 2. * x1 / winSize.width - 1;
		double x2p = 2. * x2 / winSize.width - 1;
		for (y = y2; y >= y1; y -= step)
		{
			int step1 = y - y1;
			if (step1 > step)
				step1 = step;
			pos[0] = x1p;
			pos[1] = 1 - 2. * y / winSize.height;
			lineIntegralStep(g, x1, y, pos, 0, step1, line);
			pos[0] = x2p;
			lineIntegralStep(g, x2, y - step1, pos, 0, -step1, line);
		}
	boundCheck = false;
		pos[1] = y1p;
		double iv1 = numIntegrate(pos, 0, x1p, x2p, line);
		pos[1] = y2p;
		double iv2 = numIntegrate(pos, 0, x1p, x2p, line);
		pos[0] = x1p;
		double iv3 = numIntegrate(pos, 1, y1p, y2p, line);
		pos[0] = x2p;
		double iv4 = numIntegrate(pos, 1, y1p, y2p, line);
		double ivtot = -iv1 + iv2 + iv3 - iv4;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(3);
		if (ivtot < 1e-7 && ivtot > -1e-7)
			ivtot = 0;
		ivtot *= reverse;
		String s = ((!line) ? "Flux = " : "Circulation = ");
		s += nf.format(ivtot * 1e5);
		g.setColor(cv.getBackground());
		FontMetrics fm = g.getFontMetrics();
		g.fillRect(0, winSize.height - 30, 20 + fm.stringWidth(s), 30);
		g.setColor(Color.white);
		g.drawString(s, 10, winSize.height - 10);
	}

	private double numIntegrate(double pos[], int n1, double x1, double x2, boolean line)
	{
		int steps = 8;
		double lastres = 0;
		double res = 0;
		int n2 = (line) ? n1 : 1 - n1;
		while (true)
		{
			int i;
			double h = (x2 - x1) / steps;
			res = 0;
			for (i = 0; i <= steps; i++)
			{
				pos[n1] = x1 + i * h;
				double field[] = rk_k1;
				curfunc.getField(field, pos);
				int ss = (i == 0 || i == steps) ? 1 : ((i & 1) == 1) ? 4 : 2;
				res += field[n2] * h * ss;
			}
			res /= 3;
			if (java.lang.Math.abs(lastres - res) < 1e-7)
				break;
			lastres = res;
			steps *= 2;
			if (steps == 65536)
				break;
		}
		if (!line && n1 == 0)
			res = -res;
		return res;
	}

	private void lineIntegralStep(Graphics g, int x, int y, double pos[], int dx,
			int dy, boolean line)
	{
		double field[] = rk_k1;
		curfunc.getField(field, pos);
		double f = (line) ? field[0] * dx + field[1] * dy : field[0] * dy
				- field[1] * dx;
		f *= reverse;
		double dn = java.lang.Math.abs(f * 100);
		if (dn > 1)
			dn = 1;
		int col1 = (int) (dn * 128 + 127);
		int col2 = (int) (127 - dn * 127);
		if (!line)
		{
			x += dx / 2;
			y -= dy / 2;
		}
		if (f == 0)
		{
			g.setColor(new Color(col2, col2, col2));
			g.drawLine(x, y, x + dx, y - dy);
		}
		else if (f > 0)
		{
			g.setColor(new Color(col1, col2, col2));
			if (line)
				drawArrow(g, null, x, y, x + dx, y - dy);
			else
				drawArrow(g, null, x, y, x + dy, y + dx);
		}
		else
		{
			g.setColor(new Color(col2, col1, col2));
			if (line)
				drawArrow(g, null, x + dx, y - dy, x, y);
			else
				drawArrow(g, null, x, y, x - dy, y - dx);
		}
	}

	private void genLines()
	{
		int i;
		int lineGridSize = 8;
		if (lineGridSize < 3)
			lineGridSize = 3;
		if (lineGridSize > 8)
			lineGridSize = 8;
		lineGridSize *= 2;
		int ct = 30 * lineGridSize * lineGridSize;
		double brightmult = 80 * barFieldStrength;
		fieldStrength = 10;
		boolean lineGrid[][] = new boolean[lineGridSize][lineGridSize];
		double lineGridMult = lineGridSize / 2.;
		double origp[] = new double[3];
		double field[] = new double[3];
		Particle p = new Particle();
		p.lifetime = -1;
		p.stepsize = 10;
		int dir = -1;
		int segs = 0;
		double lastdist = 0;
		for (i = 0; i != ct; i++)
		{
			if (p.lifetime < 0)
			{
				p.lifetime = 1;
				p.stepsize = 10;
				segs = 0;
				lastdist = 0;
				if (dir == 1)
				{
					int j;
					for (j = 0; j != 3; j++)
						p.pos[j] = origp[j];
					dir = -1;
					continue;
				}
				dir = 1;
				int px = 0, py = 0;
				while (true)
				{
					if (!lineGrid[px][py])
						break;
					if (++px < lineGridSize)
						continue;
					px = 0;
					if (++py < lineGridSize)
						continue;
					break;
				}
				if (py == lineGridSize)
					break;
				lineGrid[px][py] = true;
				double offs = .5 / lineGridMult;
				origp[0] = p.pos[0] = px / lineGridMult - 1 + offs;
				origp[1] = p.pos[1] = py / lineGridMult - 1 + offs;
			}
			double p1x = p.pos[0];
			double p1y = p.pos[1];
			double p1z = gridHolder.getDoubleHeight(p1x, p1y);
			int gridsize = gridHolder.getGridSize();
			GridElement ge = gridHolder.getGridElement((int) ((p1x + 1) * gridsize / 2), (int) ((p1y + 1)
					* gridsize / 2));
			if (!ge.visible)
			{
				p.lifetime = -1;
				continue;
			}
			double x[] = p.pos;
			lineSegment(p, dir);
			if (p.lifetime < 0)
				continue;
			int gx = (int) ((x[0] + 1) * lineGridMult);
			int gy = (int) ((x[1] + 1) * lineGridMult);
			if (!lineGrid[gx][gy])
				segs--;
			lineGrid[gx][gy] = true;
			ge = gridHolder.getGridElement((int) ((p.pos[0] + 1) * gridsize / 2), (int) ((p.pos[1] + 1)
					* gridsize / 2));
			if (!ge.visible)
			{
				p.lifetime = -1;
				continue;
			}
			double dn = brightmult * p.phi;
			if (dn > 2)
				dn = 2;
			map3d(p1x, p1y, p1z, xpoints, ypoints, 0);
			map3d(p.pos[0], p.pos[1], gridHolder.getDoubleHeight(p.pos[0], p.pos[1]), xpoints,
					ypoints, 1);
			drawLine(xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
			double d2 = dist2(origp, x);
			if (d2 > lastdist)
				lastdist = d2;
			else
				segs++;
			if (segs > 10 || d2 < .001)
				p.lifetime = -1;
		}
	}

	private void drawVector(DrawData dd, double vec[])
	{
		double field[] = dd.field;
		curfunc.getField(field, vec);
		double dn = java.lang.Math.sqrt(field[0] * field[0] + field[1]
				* field[1]);
		double dnr = dn * reverse;
		if (dn > 0)
		{
			field[0] /= dnr;
			field[1] /= dnr;
		}
		dn *= dd.mult;
		if (dn > 2)
			dn = 2;
		int col = (int) (dn * 255);
		double sw2 = 1. / (vectorSpacing - 1);
		map3d(vec[0], vec[1], 0, xpoints, ypoints, 0);
		map3d(vec[0] + sw2 * field[0], vec[1] + sw2 * field[1], 0, xpoints,
				ypoints, 1);
		dd.g.setColor(fieldColors[col]);
		drawArrow(dd.g, null, xpoints[0], ypoints[0], xpoints[1], ypoints[1], 2);
	}

	private void drawArrow(Graphics g, String text, int x1, int y1, int x2, int y2)
	{
		drawArrow(g, text, x1, y1, x2, y2, 5);
	}

	private void drawArrow(Graphics g, String text, int x1, int y1, int x2, int y2,
			int as)
	{
		g.drawLine(x1, y1, x2, y2);
		double l = java.lang.Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1)
				* (y2 - y1));
		if (l > as / 2)
		{
			double hatx = (x2 - x1) / l;
			double haty = (y2 - y1) / l;
			g.drawLine(x2, y2, (int) (haty * as - hatx * as + x2), (int) (-hatx
					* as - haty * as + y2));
			g.drawLine(x2, y2, (int) (-haty * as - hatx * as + x2), (int) (hatx
					* as - haty * as + y2));
			if (text != null)
				g.drawString(text, (int) (x2 + hatx * 10),
						(int) (y2 + haty * 10));
		}
	}
	


	private double curlcalc(double x, double y, double ax, double ay)
	{
		rk_yn[0] = x;
		rk_yn[1] = y;
		curfunc.getField(rk_k1, rk_yn);
		return partMult * (rk_k1[0] * ax + rk_k1[1] * ay);
	}



	private void edit(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		EditViewResult editViewResult = viewHolder.editView(x, y, isFlat, modeChooser.getSelectedIndex(),
				dragStartX, dragStartY, dragStartView);
		if ( editViewResult.isNeedsRepaint() )
		{
			cv.repaint(pause);
		}
		if ( editViewResult.isBackgroundChanged() )
		{
			draggingView = backgroundChanged = true;
		}
		if ( editViewResult.isResetIntegralXAndY() )
		{
			integralX = x;
			integralY = y;
		}
	}



	public void componentHidden(ComponentEvent e)
	{
	}

	public void componentMoved(ComponentEvent e)
	{
	}

	public void componentShown(ComponentEvent e)
	{
		cv.repaint(pause);
	}

	public void componentResized(ComponentEvent e)
	{
		handleResize();
		cv.repaint(pause);
	}

	public void actionPerformed(ActionEvent e)
	{
		particlesHolder.clearVectors();
		if (e.getSource() == resetButton)
			resetParticles();
		if (e.getSource() == kickButton)
			particlesHolder.kickParticles(getParticleCount());
		if (e.getSource() == infoButton)
		{
			String s = curfunc.getClass().getName();
			try
			{
				s = s.substring(s.lastIndexOf('.') + 1);
				applet.getAppletContext().showDocument(
						new URL(applet.getCodeBase(), "functions.html" + '#'
								+ s), "functionHelp");
			}
			catch (Exception ex)
			{
			}
		}
		curfunc.actionPerformed();
		cv.repaint(pause);
	}

	public boolean handleEvent(Event ev)
	{
		if (ev.id == Event.WINDOW_DESTROY)
		{
			applet.destroyFrame();
			return true;
		}
		return super.handleEvent(ev);
	}

	public void scrollbarValueChanged(DecentScrollbar ds)
	{
		particlesHolder.clearVectors();
		System.out.print(ds.getValue() + "\n");
		if (ds == partCountBar)
			particlesHolder.resetDensityGroups(getParticleCount());
		if (ds == auxBars[0].getScrollbar() || ds == auxBars[1].getScrollbar() || ds == auxBars[2].getScrollbar())
		{
			functionChanged = true;
			draggingView = true;
		}
		cv.repaint(pause);
	}

	public void scrollbarFinished(DecentScrollbar ds)
	{
		draggingView = false;
		cv.repaint(pause);
	}

	public void mouseDragged(MouseEvent e)
	{
		edit(e);
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0)
			return;
		dragStartX = e.getX();
		dragStartY = e.getY();
		try
		{
			dragStartView = (View)viewHolder.clone();
		}
		catch (CloneNotSupportedException e1)
		{
			throw new RuntimeException(e1);
		}
		edit(e);
	}

	public void mouseReleased(MouseEvent e)
	{
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0)
			return;
		draggingView = false;
		cv.repaint(pause);
	}

	private void dispChooserChanged()
	{
		int disp = dispChooser.getSelectedIndex();
		if (disp == DISP_PART_FORCE)
			kickButton.enable();
		else
			kickButton.disable();
		potentialLabel.hide();
		potentialBar.hide();
		vecDensityLabel.hide();
		vecDensityBar.hide();
		partCountLabel.hide();
		partCountBar.hide();
		strengthLabel.show();
		strengthBar.show();
		if (disp == DISP_VECTORS)
		{
			vecDensityLabel.show();
			vecDensityBar.show();
		}
		else
		{
			partCountLabel.show();
			partCountBar.show();
		}
		validate();
		resetParticles();
	}

	public void itemStateChanged(ItemEvent e)
	{
		particlesHolder.clearVectors();
		cv.repaint(pause);
		reverse = (reverseCheck.getState()) ? -1 : 1;
		if (e.getItemSelectable() == dispChooser)
		{
			dispChooserChanged();
			resetParticles();
		}
		if (e.getItemSelectable() == functionChooser)
			functionChanged();
		if (e.getItemSelectable() == reverseCheck)
			functionChanged = true;
		if (e.getItemSelectable() == floorColorChooser
				|| e.getItemSelectable() == floorLineChooser)
			backgroundChanged = true;
	}

	void checkFlatState()
	{
		boolean oldFlat = isFlat;
		int disp = dispChooser.getSelectedIndex();
		isFlat = flatCheck.getState() || curfunc.nonGradient()
				|| disp == DISP_VECTORS || disp == DISP_CURLERS;
		int mode = modeChooser.getSelectedIndex();
		if (mode == View.MODE_LINE_INT || mode == View.MODE_SURF_INT)
			isFlat = true;
		if (isFlat != oldFlat)
			backgroundChanged = true;
	}

	void functionChanged()
	{
		reverse = 1;
		reverseCheck.setState(false);
		parseError = false;
		curfunc = (VecFunction) functionList.elementAt(functionChooser
				.getSelectedIndex());
		int i;
		for (i = 0; i != 3; i++)
		{
			auxBars[i].label.hide();
			auxBars[i].bar.hide();
		}
		for (i = 0; i != 2; i++)
			textFields[i].hide();
		if (textFieldLabel != null)
			textFieldLabel.hide();
		strengthBar.setValue(80);
		curfunc.setup();
		
		ConfigurableValues[] configurableValues = curfunc.getConfigurableValues();
		if ( configurableValues != null )
		{
			for ( int n=0; n<configurableValues.length; n++ )
			{
				auxBars[n].label.setText(configurableValues[n].getName());
				auxBars[n].label.show();
				auxBars[n].bar.setValue(configurableValues[n].getInitialValue());
				auxBars[n].bar.show();
			}
		}
		
		validate();
		resetParticles();
		dispChooserChanged();
		functionChanged = true;
		integralX = -1;
	}

	void setupDispChooser(boolean potential)
	{
		dispChooser.removeAll();
		dispChooser.add("Display: Particles (Vel.)");
		dispChooser.add("Display: Particles (Force)");
		dispChooser.add("Display: Field Vectors");
		dispChooser.add("Display: None");
	}

	void cross(double res[], double v1[], double v2[])
	{
		res[0] = v1[1] * v2[2] - v1[2] * v2[1];
		res[1] = v1[2] * v2[0] - v1[0] * v2[2];
		res[2] = v1[0] * v2[1] - v1[1] * v2[0];
	}

	double dot(double v1[], double v2[])
	{
		return v1[0] * v2[0] + v1[1] * v2[1];
	}

	void rk(int order, double x, double Y[], double stepsize)
	{
		int i;
		if (order == 2)
		{
			double fmult = stepsize * partMult;
			for (i = 0; i != order; i++)
				rk_yn[i] = Y[i];
			curfunc.getField(rk_k1, rk_yn);
			for (i = 0; i != order; i++)
				rk_yn[i] = (Y[i] + 0.5 * fmult * rk_k1[i]);
			curfunc.getField(rk_k2, rk_yn);
			for (i = 0; i != order; i++)
				rk_yn[i] = (Y[i] + 0.5 * fmult * rk_k2[i]);
			curfunc.getField(rk_k3, rk_yn);
			for (i = 0; i != order; i++)
				rk_yn[i] = (Y[i] + fmult * rk_k3[i]);
			curfunc.getField(rk_k4, rk_yn);
			for (i = 0; i != order; i++)
				Y[i] = Y[i] + fmult
						* (rk_k1[i] + 2 * (rk_k2[i] + rk_k3[i]) + rk_k4[i]) / 6;
			Y[2] = rk_k4[2];
		}
		else
		{
			double fmult = stepsize * partMult;
			for (i = 0; i != order; i++)
				rk_yn[i] = Y[i];
			getForceField(rk_k1, rk_yn, stepsize, fmult);
			for (i = 0; i != order; i++)
				rk_yn[i] = (Y[i] + 0.5 * rk_k1[i]);
			getForceField(rk_k2, rk_yn, stepsize, fmult);
			for (i = 0; i != order; i++)
				rk_yn[i] = (Y[i] + 0.5 * rk_k2[i]);
			getForceField(rk_k3, rk_yn, stepsize, fmult);
			for (i = 0; i != order; i++)
				rk_yn[i] = (Y[i] + rk_k3[i]);
			getForceField(rk_k4, rk_yn, stepsize, fmult);
			for (i = 0; i != order; i++)
				Y[i] = Y[i] + (rk_k1[i] + 2 * (rk_k2[i] + rk_k3[i]) + rk_k4[i])
						/ 6;
			Y[4] = rk_k4[4];
		}
	}

	void getForceField(double result[], double y[], double stepsize,
			double fmult)
	{
		curfunc.getField(result, y);
		result[4] = result[2];
		int i;
		for (i = 0; i != 2; i++)
			result[i + 2] = fmult * result[i] * .1;
		for (i = 0; i != 2; i++)
			result[i] = stepsize * timeStep * rk_yn[i + 2];
	}

	private double dist2(double a[], double b[])
	{
		double c0 = a[0] - b[0];
		double c1 = a[1] - b[1];
		return c0 * c0 + c1 * c1;
	}

	private void lineSegment(Particle p, int dir)
	{
		double maxh = 20;
		double E = .001, localError;
		int order = 2;
		double Y[] = rk_Y;
		double Yhalf[] = rk_Yhalf;
		oldY = rk_oldY;
		int i;
		for (i = 0; i != 2; i++)
			oldY[i] = Y[i] = Yhalf[i] = p.pos[i];
		double h = p.stepsize;
		ls_fieldavg[0] = ls_fieldavg[1] = ls_fieldavg[2] = 0;
		int steps = 0;
		double minh = .1;
		double segSize2max = 1. / gridHolder.getGridSize();
		double segSize2min = segSize2max / 4;
		double lastd = 0;
		int avgct = 0;
		while (true)
		{
			boundCheck = false;
			steps++;
			if (steps > 100)
			{
				System.out.print("maxsteps\n");
				p.lifetime = -1;
				return;
			}
			rk(order, 0, Y, dir * h);
			rk(order, 0, Yhalf, dir * h * 0.5);
			rk(order, 0, Yhalf, dir * h * 0.5);
			if (boundCheck)
			{
				for (i = 0; i != order; i++)
					Y[i] = Yhalf[i] = oldY[i];
				h /= 2;
				if (h < minh)
				{
					p.lifetime = -1;
					return;
				}
				continue;
			}
			if (Y[0] < -1 || Y[0] >= .999 || Y[1] < -1 || Y[1] >= .999)
			{
				for (i = 0; i != order; i++)
					Y[i] = Yhalf[i] = oldY[i];
				h /= 2;
				if (h < minh)
				{
					p.lifetime = -1;
					return;
				}
				continue;
			}
			localError = java.lang.Math.abs(Y[0] - Yhalf[0])
					+ java.lang.Math.abs(Y[1] - Yhalf[1]);
			if (localError > E && h > minh)
			{
				h *= 0.75;
				if (h < minh)
					h = minh;
				for (i = 0; i != order; i++)
					Y[i] = Yhalf[i] = oldY[i];
				continue;
			}
			else if (localError < (E * 0.5))
			{
				h *= 1.25;
				if (h > maxh)
					h = maxh;
			}
			double d = dist2(p.pos, Y);
			if (!(d - lastd > 1e-10))
			{
				p.lifetime = -1;
				return;
			}
			if (d > segSize2max)
			{
				h /= 2;
				if (h < minh)
				{
					p.lifetime = -1;
					return;
				}
				for (i = 0; i != order; i++)
					Y[i] = Yhalf[i] = oldY[i];
				continue;
			}
			ls_fieldavg[0] += rk_k1[0];
			ls_fieldavg[1] += rk_k1[1];
			avgct++;
			if (d > segSize2min)
				break;
			lastd = d;
			for (i = 0; i != order; i++)
				oldY[i] = Yhalf[i] = Y[i];
		}
		p.stepsize = h;
		for (i = 0; i != 3; i++)
			p.pos[i] = Y[i];
		p.phi = java.lang.Math.sqrt(ls_fieldavg[0] * ls_fieldavg[0]
				+ ls_fieldavg[1] * ls_fieldavg[1])
				/ avgct;
	}
	
	public final int getValue1()
	{
		return auxBars[0].getScrollbar().getValue();
	}
	
	public final int getValue2()
	{
		return auxBars[1].getScrollbar().getValue();
	}
	
	public final int getValue3()
	{
		return auxBars[2].getScrollbar().getValue();
	}

	public int getReverse()
	{
		return reverse;
	}

	public boolean getBoundCheck()
	{
		return boundCheck;
	}

	public void setBoundCheck(boolean boundCheck)
	{
		this.boundCheck = boundCheck;
	}

	public double[] getRkK1()
	{
		return rk_k1;
	}

	public double[] getRkK2()
	{
		return rk_k2;
	}

	public double[] getRkK3()
	{
		return rk_k3;
	}

	public double getLineWidth()
	{
		return lineWidth;
	}

	private int getParticleCount()
	{
		return partCountBar.getValue();
	}
	
	private void resetParticles()
	{
		particlesHolder.resetParticles(getParticleCount());
		integralX = -1;
	}
	
	private void drawParticles(Graphics g, int disp, int pcount)
	{
		g.setColor(Color.white);
		if (disp == VecDemoFrame.DISP_VECTORS)
		{
			throw new RuntimeException("I think this part can be deleted");
		}
		int i, gridsize = gridHolder.getGridSize();
		wooft += .3;
		if (disp == VecDemoFrame.DISP_CURLERS)
			pcount = (pcount + 4) / 5;
		for (i = 0; i < pcount; i++)
		{
			Particle p = particlesHolder.getParticle(i);
			double pos[] = p.pos;
			GridElement ge = gridHolder.getGridElement((int) ((pos[0] + 1) * gridsize / 2), (int) ((pos[1] + 1)
					* gridsize / 2));
			map3d(pos[0], pos[1], gridHolder.getDoubleHeight(pos[0], pos[1]), xpoints, ypoints,
					0);
			if (xpoints[0] < 0 || xpoints[0] >= winSize.width || ypoints[0] < 0
					|| ypoints[0] >= winSize.height)
				continue;
			if (disp == VecDemoFrame.DISP_CURLERS)
			{
				g.setColor(p.color);
				final double len = .02;
				double ax = java.lang.Math.cos(p.theta) * len;
				double ay = java.lang.Math.sin(p.theta) * len;
				double offx = ax;
				double offy = ay;
				double a1 = curlcalc(p.pos[0] + offx, p.pos[1] + offy, -ay, ax);
				double a2 = curlcalc(p.pos[0] - offy, p.pos[1] + offx, -ax, -ay);
				double a3 = curlcalc(p.pos[0] - offx, p.pos[1] - offy, ay, -ax);
				double a4 = curlcalc(p.pos[0] + offy, p.pos[1] - offx, ax, ay);
				p.theta += (a1 + a2 + a3 + a4) / (4 * len * len);
				map3d(p.pos[0] - offx, p.pos[1] - offy, 0, xpoints, ypoints, 0);
				map3d(p.pos[0] + offx, p.pos[1] + offy, 0, xpoints, ypoints, 1);
				map3d(p.pos[0] - offy, p.pos[1] + offx, 0, xpoints, ypoints, 2);
				map3d(p.pos[0] + offy, p.pos[1] - offx, 0, xpoints, ypoints, 3);
				g.drawLine(xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
				g.drawLine(xpoints[2], ypoints[2], xpoints[3], ypoints[3]);
				g.fillOval(xpoints[0] - 1, ypoints[0] - 1, 3, 3);
			}
			else if (ge.visible && ge.valid)
				g.fillRect(xpoints[0], ypoints[0] - 1, 2, 2);
		}
	}
	
	private void moveParticles(int pcount, boolean redistribute, int disp, double timeStep)
	{
		int bestd = 0;
		int i;
		for (i = 0; i != pcount; i++)
		{
			Particle pt = particlesHolder.getParticle(i);
			particlesHolder.removeFromDensityGroup(pt);
			moveParticle(pt, disp);
			double x[] = pt.pos;
			if (!(x[0] >= -1 && x[0] < 1 && x[1] >= -1 && x[1] < 1)
					|| (pt.lifetime -= timeStep) < 0)
				particlesHolder.positionParticle(pt, redistribute);
			int d = particlesHolder.addToDensityGroup(pt);
			if (d > bestd)
				bestd = d;
		}
		boolean withforce = (disp == VecDemoFrame.DISP_PART_FORCE);
		if ( redistribute && ! withforce )
			particlesHolder.redistributeIfNeeded(bestd, pcount);

	}

	private void moveParticle(Particle p, int disp)
	{
		double maxh = 1;
		double E = .001, localError;
		boolean useForce = (disp == VecDemoFrame.DISP_PART_FORCE);
		int order = useForce ? 4 : 2;
		double Y[] = rk_Y;
		double Yhalf[] = rk_Yhalf;
		oldY = rk_oldY;
		int i;
		for (i = 0; i != 2; i++)
			oldY[i] = Y[i] = Yhalf[i] = p.pos[i];
		if (useForce)
			for (i = 0; i != 2; i++)
				Y[i + 2] = Yhalf[i + 2] = p.vel[i];
		double t = 0;
		if (!curfunc.useRungeKutta())
		{
			boundCheck = false;
			curfunc.getField(Yhalf, Y);
			if (boundCheck && (!useForce || curfunc.checkBoundsWithForce()))
			{
				p.pos[0] = -100;
				return;
			}
			double fmult = partMult;
			if (useForce)
			{
				fmult *= .1;
				for (i = 0; i != 2; i++)
				{
					p.vel[i] += fmult * Yhalf[i];
					p.pos[i] += p.vel[i] * timeStep;
				}
			}
			else
			{
				for (i = 0; i != 2; i++)
					p.pos[i] += fmult * Yhalf[i];
			}
			p.pos[2] = Yhalf[2];
			for (i = 0; i != 2; i++)
				Y[i] = p.pos[i];
			if (curfunc.checkBounds(Y, oldY))
				p.pos[0] = -100;
			return;
		}
		boolean adapt = curfunc.useAdaptiveRungeKutta();
		double h = (adapt) ? p.stepsize : 1;
		int steps = 0;
		double minh = .0001;
		while (t >= 0 && t < 1)
		{
			if (t + h > 1)
				h = 1 - t;
			boundCheck = false;
			rk(order, 0, Y, h);
			if (!adapt)
				break;
			rk(order, 0, Yhalf, h * 0.5);
			rk(order, 0, Yhalf, h * 0.5);
			if (boundCheck && (!useForce || curfunc.checkBoundsWithForce()))
			{
				p.pos[0] = -100;
				return;
			}
			localError = java.lang.Math.abs(Y[0] - Yhalf[0])
					+ java.lang.Math.abs(Y[1] - Yhalf[1]);
			if (localError > E && h > minh)
			{
				h *= 0.75;
				if (h < minh)
					h = minh;
				for (i = 0; i != order; i++)
					Y[i] = Yhalf[i] = oldY[i];
				continue;
			}
			else if (localError < (E * 0.5))
			{
				h *= 1.25;
				if (h > maxh)
					h = maxh;
			}
			for (i = 0; i != order; i++)
				oldY[i] = Yhalf[i] = Y[i];
			t += h;
			steps++;
		}
		if (boundCheck && (!useForce || curfunc.checkBoundsWithForce()))
		{
			p.pos[0] = -100;
			return;
		}
		p.stepsize = h;
		for (i = 0; i != 3; i++)
			p.pos[i] = Y[i];
		if (useForce)
		{
			for (i = 0; i != 2; i++)
				p.vel[i] = Y[i + 2];
			p.pos[2] = Y[4];
		}
	}

	public GridElement[][] getGrid()
	{
		return gridHolder.getGrid();
	}

	public int getGridSize()
	{
		return gridHolder.getGridSize();
	}
}

