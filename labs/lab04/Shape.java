import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

import java.awt.Color;
import java.awt.Point;


public interface Shape{
    
    public Color getColor(); //- Returns the java.awt.Color of the Shape.
    public void setColor(Color c); // Sets the java.awt.Color of the Shape.
    public double getArea(); // - Returns the area of the Shape
    public double getPerimeter(); // - Returns the perimeter of the Shape
    public void translate(Point p);// - Translates the entire shape by the (x,y) coordinates of a given java.awt.Point

}
	
