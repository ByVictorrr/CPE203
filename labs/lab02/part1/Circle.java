public class Circle{

	private Point Center;
	private double radius;

	Circle(Point c1, double r){
		Center=c1;
		radius = r;
	}


	public double getRadius(){
		return radius;
	}
	public Point getCenter(){
		return Center;
	}

}
