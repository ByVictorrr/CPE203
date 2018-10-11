import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

import java.awt.Color;
import java.awt.Point;

public class Circle implements Shape{
	private Point center;
	private Color color;
	private double radius;
	
	public Circle(double r, Point p, Color c){
	center = p;
	radius = r;
	color = c;
	}
	public double getRadius(){ return radius;}
	public void setRadius(double r){radius = r;}
	public Point getCenter(){return center;}
	public Color getColor(){return color;} //- Returns the java.awt.Color of the Shape.
	public void setColor(Color c){color = c;} // Sets the java.awt.Color of the Shape.
	public double getArea(){return (Math.PI * radius* radius);} // - Returns the area of the Shape
    public double getPerimeter(){ return (2.0 * Math.PI * radius);} // - Returns the perimeter of the Shape
    public void translate(Point p){center = p;}// - Translates the entire shape by the (x,y) coordinates of a given java.awt.Point
}
