import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.awt.Color;
import java.awt.Point;


public class WorkSpace{

	public List<Shape> obj;
	
	
    public WorkSpace(){
    	obj = new ArrayList<Shape>();
    } 
	//- A default constructor to initialize its instance variable to an empty List. The constructor should not take any parameters. (If you do nothing in this constructor, you may omit it from your code altogether.)
    public void add(Shape shape){

    	obj.add(obj.size(),shape);
    }
	// - Adds an object which implements the Shape interface to the end of the List in the WorkSpace.
    public Shape get(int index){

    	return obj.get(index);
    }
	// - Returns the specified Shape from the WorkSpace.
   public int size(){return obj.size();}
	// - Returns the number of Shapes contained by the WorkSpace.
    public List<Circle> getCircles(){

    		List<Circle> circles = new ArrayList<Circle>();

    	for (int i =0; i< obj.size(); i++)
    		{
    			if( obj.get(i).getClass() == Circle.class){
    				

				circles.add((Circle)obj.get(i)); 	
    			//obj.remove(i);
    			
    			}

    		}
    		//
    		return circles;

	//(ArrayList<Circle>)(Object)obj;
    } 
	//- Returns a List of all the Circle objects contained by the WorkSpace.
    public List<Rectangle> getRectangles(){

    		List<Rectangle> rectangle = new ArrayList<Rectangle>();

    	for (int i =0; i< obj.size(); i++)
    		{
    			if(obj.get(i).getClass() == Rectangle.class){
    			
    			//obj.remove(i);
    			rectangle.add((Rectangle)obj.get(i));
    		}
    		}
    		return rectangle;
    }
	// - Returns a List of all the Rectangle objects contained by the WorkSpace.
    public List<Triangle> getTriangles(){ 

    		List<Triangle> triangle = new ArrayList<Triangle>();

    	for (int i =0; i< obj.size(); i++)
    		{
    			if( obj.get(i).getClass() == Triangle.class){


				triangle.add((Triangle)obj.get(i));
    		}
    		}
    		return triangle;
	} //- Returns a List of all the Triangle objects contained by the WorkSpace.
    public List<ConvexPolygon> getConvexPolygons() {

    		List<ConvexPolygon> CP = new ArrayList<ConvexPolygon>();

    	for (int i =0; i< obj.size(); i++)
    		{
    			if( obj.get(i).getClass() == ConvexPolygon.class){

    			CP.add((ConvexPolygon)obj.get(i));
    		}
    		}
 
		return CP;
   }

	//- Returns a List of all the ConvexPolygon objects contained by the WorkSpace.
    public List<Shape> getShapesByColor(Color color){
    		

    	for (int i =0; i< obj.size(); i++)
    		{
    			if(  !(obj.get(i).getColor().equals(color) ) ){

 					obj.remove(i);
    		}

    		}
    		return obj;
    }
// - Returns a List of all the Shape objects contained by the WorkSpace that match the specified Color.
    public double getAreaOfAllShapes(){

    	double Area = 0.0;

    	for(Shape shapess : obj){

    		Area = Area + shapess.getArea();

    	}
    	return Area;
	}
	// - Returns the sum of the areas of all the Shapes contained by the WorkSpace.
    public double getPerimeterOfAllShapes(){

//- Returns the sum of the perimeters of all the Shapes contained by the WorkSpace.

    	double perimeter = 0.0;

    	for(Shape shapess : obj){

    		perimeter = perimeter + shapess.getPerimeter();

    	}
    	return perimeter;



}
}
