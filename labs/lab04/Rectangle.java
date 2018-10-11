import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

import java.awt.Color;
import java.awt.Point;


public class Rectangle implements Shape{
	
	private double width, height;
	private Point topLeftCorner;
	private Color color;

    public Rectangle(double w, double h, Point tLC, Color c){ 
	width = w;
	height = h;
	topLeftCorner = tLC;
	color = c;
	//- A constructor with parameters to initialize all its instance variables. Do not implement a default constructor.
	}
	
	public double getWidth(){return width;}
	public void setWidth(double w){width =w;}    
	public double getHeight(){return height;} // - Returns the height of the Rectangle.
	public void setHeight(double h){height = h;} //- Sets the height of the Rectangle
	public Point getUpperLeft(){return topLeftCorner;}// - Returns the Point representing the upper left corner of the Rectangle
	public Color getColor(){return color;} //- Returns the java.awt.Color of the Shape.
	public void setColor(Color c){color = c;} // Sets the java.awt.Color of the Shape.
	public double getArea(){return width * height;} // - Returns the area of the Shape
    public double getPerimeter(){ return (2.0*width + 2.0 * height);} // - Returns the perimeter of the Shape
    public void translate(Point p){topLeftCorner = p;}// - Translates the entire shape by the (x,y) coordinates of a given java.awt.Point

}

