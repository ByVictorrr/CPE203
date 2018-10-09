public class Point{

	private double x;
	private double y;

	Point(double x1, double y1){
		x=x1;
		y=y1;
	}


	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	public double getRadius(){

		return (Math.sqrt(y*y+x*x));
	}	

	public double getAngle(){
	
		//returns in radians [-pi, pi]
			
			return (Math.atan2(y,x));

	}

	public Point rotate90(){
		double  xprime = this.getRadius()*Math.cos(this.getAngle()+Math.PI/2);	
		double yprime =  this.getRadius()*Math.sin(this.getAngle()+Math.PI/2);
	
		return new Point(xprime,yprime);
	}	

}
