public class Point
{
   private  double x;
   private  double y;
   private  double z;

   public Point(String x, String y, String z) {
      this.x = Double.parseDouble(x);
      this.y =  Double.parseDouble(y);
	  this.z =  Double.parseDouble(z);  
}

public Point(){x=0.0;y=0.0;z=0.0;};
public double getX(){return x;}
public double getY(){return y;}
public double getZ(){return z;}

//scale each point by scaleFactor
public Point scalePoint(double scaleFactor){	
	Point scaledP;

	scaledP.x = 0.5*this.getX();
	scaledP.y = 0.5*this.getY();
	scaledP.z = 0.5*this.getZ();

	return scaledP;
}
   @Override
   public String toString() {
      return "(" + x + "," + y +"," + z + ")";
   }
}
