public class Util{

	public static double perimeter(Circle C)
	{
		return (2.0 * Math.PI * C.getRadius());
	}
	public static double perimeter(Polygon P)
	{	
		double polyPerimeter = 0.0;
		double Delta_x, Delta_y = 0.0;

	
		for (int i=0; i <= P.getPoints().size()-1; i++){
		//delta x is
		if ( i == P.getPoints().size()-1){
	        
		Delta_x = Math.abs(P.getPoints().get(P.getPoints().size()-1 ).getX()- P.getPoints().get(0).getX());

                Delta_y = Math.abs(P.getPoints().get(P.getPoints().size()-1 ).getY()- P.getPoints().get(0).getY());

                polyPerimeter = polyPerimeter + Math.sqrt(Math.pow(Delta_x, 2)+ Math.pow(Delta_y, 2));
 		
		}

		else{
		Delta_x = Math.abs(P.getPoints().get(i+1).getX()- P.getPoints().get(i).getX());	

		Delta_y = Math.abs( P.getPoints().get(i+1).getY()- P.getPoints().get(i).getY());
		
		polyPerimeter = Math.sqrt(Math.pow(Delta_x, 2)+ Math.pow(Delta_y, 2))+ polyPerimeter;
		}
		
		
	}
		return polyPerimeter;
	}
		

		public static double perimeter(Rectangle R)
		{

		   
                double width = Math.abs((R.getBottomRight().getX()- R.getTopLeft().getX()));


	         double height = Math.abs((R.getBottomRight().getY()-R.getTopLeft().getY()));


		return (2.0 * width + 2.0 * height);

		}

}	
