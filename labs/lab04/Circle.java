public class Circle implements Shape{
	private Point center;
	private Color color;
	private double radius;
	
	Circle(double r, Point p, Color c){
	center = p;
	radius = r
	color = c;
	}
	double getRadius(){ return radius;}
	void setRadius(double r){radius = r;}
	Point getCenter(){return center;}
	Color getColor(){return color;} //- Returns the java.awt.Color of the Shape.
	void setColor(Color c){color = c;} // Sets the java.awt.Color of the Shape.
	double getArea(){return (Math.PI * radius* radius)} // - Returns the area of the Shape
    	double getPerimeter(){ return (2.0 * Math.PI * radius)} // - Returns the perimeter of the Shape
    	void translate(Point p){center = p;}// - Translates the entire shape by the (x,y) coordinates of a given java.awt.Point
}
