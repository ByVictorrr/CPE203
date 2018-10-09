import java.util.Arrays;
import java.util.*;

public class Bigger{
	//by perimeter
	 public static double whichIsBigger(Circle c1, Rectangle R1, Polygon p1)
        {	
		 double pCircle = c1.perimeter();
                double pRectangle = R1.perimeter();
                double pPolygon = p1.perimeter();


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
