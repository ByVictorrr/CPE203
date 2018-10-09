import java.awt.*;

public interface Shape{
    
    Color getColor(); //- Returns the java.awt.Color of the Shape.
    void setColor(Color c); // Sets the java.awt.Color of the Shape.
    double getArea(); // - Returns the area of the Shape
    double getPerimeter(); // - Returns the perimeter of the Shape
    void translate(Point p);// - Translates the entire shape by the (x,y) coordinates of a given java.awt.Point

}
	
