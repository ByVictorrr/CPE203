import java.util.*;

public class Polygon {

   private ArrayList<Point> vertices;
	
   public Polygon(List<Point> points) {

      vertices = new ArrayList<Point>();

      for (int i=0; i<points.size(); i++) {

         vertices.add(points.get(i));
      }
   }
	public List<Point> getPoints()
	{
	return vertices;
	}
} 
