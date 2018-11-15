public class Point
{
   private  double x;
   private  double y;
   private  double z;


   public Point(String x, String y, String z) {
      this.x =  Double.parseDouble(x);
      this.y =  Double.parseDouble(y);
	  this.z =  Double.parseDouble(z);  
}


  public Point(double x, double y, double z) {
      this.x = x;
      this.y = y;
	  this.z = z;  
}


public Point(){	
	x=0.0;
	y=0.0;
	z=0.0;
}

public double getX(){return x;}
public double getY(){return y;}
public double getZ(){return z;}

//scale each point by scaleFactor
public Point scalePoint(double scaleFactor){	
	
	return new Point(scaleFactor*this.getX(), scaleFactor*this.getY(), scaleFactor*this.getZ());
}
//translates point by a given point
public Point translate(Point translater)
{
	this.x = this.x + translater.getX();
	this.y = this.y + translater.getY();
	return this;
}



   @Override
   public String toString() {
      return "(" + x + "," + y +"," + z + ")";
   }
}
