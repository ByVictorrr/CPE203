public class Rectangle implements Shape{
	
	private double width, height;
	public Point topLeftCorner;
	public Color color;

    Rectangle(double w, double h, Point tLC, Color c){ 
	width = w;
	height = h;
	topLeftCorner = tLC;
	color = c;
	//- A constructor with parameters to initialize all its instance variables. Do not implement a default constructor.
	}
	
	double getWidth(){return width}
	void setWidth(double w){width =w;}    
	double getHeight(){return height;} // - Returns the height of the Rectangle.
	void setHeight(double h){height = h;} //- Sets the height of the Rectangle
	Point getTopLeft(return topLeftCorner;)// - Returns the Point representing the upper left corner of the Rectangle
	Color getColor(){return color;} //- Returns the java.awt.Color of the Shape.
	void setColor(Color c){color = c;} // Sets the java.awt.Color of the Shape.
	double getArea(){return width * height;} // - Returns the area of the Shape
    	double getPerimeter(){ return (2.0*width + 2.0 * height)} // - Returns the perimeter of the Shape
    	void translate(Point p){topLeftCorner = p;}// - Translates the entire shape by the (x,y) coordinates of a given java.awt.Point

}

