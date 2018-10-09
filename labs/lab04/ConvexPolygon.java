public class ConvexPolygon implements shape{
	private Point [] v;
	private Color color;
	
	
	ConvexPolygon(Point [] a, Color c){
		color = c;
		
		for (int i=0; i<a.length; i++)
		{
		v[i] = a[i];				
		}
	
	}
	 //- A constructor with parameters to initialize all its instance variables. Do not implement a default constructor.
    	Point getVertex(int index){return a[index];} 
	//- Takes an index and returns the specified vertex of the ConvexPolygon.
	
	Color getColor(){return color;} //- Returns the java.awt.Color of the Shape.
        void setColor(Color c){color = c;} // Sets the java.awt.Color of the Shape.
        double getArea(){
	//formula: http://www.mathwords.com/a/area_convex_polygon.htm
		double area,left,right;
		for(int i=0; i< v.lenth; i++){
		area = 
		}
		return ();
	
	} // - Returns the area of the Shape
        double getPerimeter(){ return (2.0 * Math.PI * radius)} // - Returns the perimeter of the Shape
        void translate(Point p){center = p;}// - Translates the entire shape by the (x,y) coordinates of a given java.awt.Point




}
