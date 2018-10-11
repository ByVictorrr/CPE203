import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

import java.awt.Color;
import java.awt.Point;


public class ConvexPolygon implements Shape{
	private Point [] v;
	private Color color;
	
	
	public ConvexPolygon(Point [] a, Color c){
		
		color = c;
		
		for (int i=0; i<a.length; i++)
		{
		v[i] = a[i];				
		}
	
	}
	 //- A constructor with parameters to initialize all its instance variables. Do not implement a default constructor.
    public Point getVertex(int index){return v[index];} 
	//- Takes an index and returns the specified vertex of the ConvexPolygon.
	
	public Color getColor(){return color;} //- Returns the java.awt.Color of the Shape.
     public void setColor(Color c){color = c;} // Sets the java.awt.Color of the Shape.
     public double getArea(){
	//formula: http://www.mathwords.com/a/area_convex_polygon.htm
		double area=0.0;
		double left=0.0;
		double right =0.0;
		for(int i=0; i< v.length; i++){
			//for last term of lef
			if(i==v.length-1)
			{
			left = left + v[v.length-1].getX()*v[0].getY();
			right = right + v[v.length-1].getY()*v[0].getX();

			}
			else{
	
			left = v[i].getX()*v[i+1].getY()+ left; 
			right = v[i].getY()*v[i+1].getX()+ right;

			}
		}
		return 0.5*(left-right);
	
	} // - Returns the area of the Shape
        public double getPerimeter(){

        	double per=0.0;

			for(int i=0; i< v.length; i++){
				if ( v.length -1 == i){
					per = per + Math.sqrt( Math.pow( v[v.length-1].getX() - v[0].getX() ,2) + Math.pow(v[v.length-1].getY() - v[0].getY(),2) );
				}

				per = per + Math.sqrt( Math.pow( v[i].getX() - v[i+1].getX() ,2) + Math.pow(v[i].getY() - v[i+1].getY(),2) );                         
			}
			return per;
	 	
	 	} // - Returns the perimeter of the Shape
       public void translate(Point p){//center = p;
       }// - Translates the entire shape by the (x,y) coordinates of a given java.awt.Point



}
