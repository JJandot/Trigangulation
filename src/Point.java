/** La classe Point. */
public class Point
{
	/** La valeur de x. */
	public double x;

	/** La valeur de y. */
	public double y;

	public boolean right;
	public int flag;


	/** Constructeur avec initialisation de x et y. */
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/** Constructeur sans initialisation. */
	public Point(){}

	@Override
	public String toString() {
		return "Point{ flag=" + flag + '}';
	}
}