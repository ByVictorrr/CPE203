import java.util.*;

public class Polygon {

   private ArrayList<Point> vertices;
	
  Polygon(List<Point> points) {

      vertices = new ArrayList<Point>();

      for (int i=0; i<points.size(); i++) {

         vertices.add(points.get(i));
      }
   }
	public List<Point> getPoints()
	{
	return vertices;
	}


	 public double perimeter()
        {  
		 double polyPerimeter = 0.0;               
		double Delta_x, Delta_y = 0.0;
                
		for (int i=0; i <= this.getPoints().size()-1; i++){
                         if ( i == this.getPoints().size()-1){
       
	                	Delta_x = Math.abs(this.getPoints().get(this.getPoints().size()-1 ).getX()- this.getPoints().get(0).getX());
	                	Delta_y = Math.abs(this.getPoints().get(this.getPoints().size()-1 ).getY()- this.getPoints().get(0).getY());
                		polyPerimeter = polyPerimeter + Math.sqrt(Math.pow(Delta_x, 2)+ Math.pow(Delta_y, 2));
                	}
			else{
                 	
				Delta_x = Math.abs(this.getPoints().get(i+1).getX()- this.getPoints().get(i).getX());
	                	Delta_y = Math.abs( this.getPoints().get(i+1).getY()- this.getPoints().get(i).getY());
                        	polyPerimeter = Math.sqrt(Math.pow(Delta_x, 2)+ Math.pow(Delta_y, 2))+ polyPerimeter;
			}

            	}               
		 return polyPerimeter;       
	 }


} 
