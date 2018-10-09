import java.util.Arrays;
import java.util.*;

public class Bigger{
	//by perimeter
	 public static double whichIsBigger(Circle c1, Rectangle R1, Polygon p1)
        {	
		List<Point> points_polygon = Arrays.asList(new Point(0.0, 0.0), new Point(3.0,2.0), new Point(1.0,4.0), new Point(-1.0,4.0 ));
//this.circle = c1;
//
//
		  double pCircle = Util.perimeter(c1);
                double pRectangle = Util.perimeter(R1);
                double pPolygon = Util.perimeter(p1);


		if ( pCircle > pRectangle && pCircle > pPolygon)
		{
		//return -1 circle if perimeter bigger than other
		return -1.0;
		}
		//return 0
		if ( pRectangle > pCircle && pRectangle > pPolygon)
		{
		return 1.0;
		}
		else{
		
		return 0.0;	
        	}
	}




}
