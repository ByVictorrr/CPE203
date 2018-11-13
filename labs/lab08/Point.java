public class Point
{
   public  double x;
   public  double y;
   public  double z;

   public Point(String x, String y, String z)
   {
      this.x = Double.parseDouble(x);
      this.y =  Double.parseDouble(y);
	  this.z =  Double.parseDouble(z); 

 
}

   @Override
   public String toString() {
      return "(" + x + "," + y +"," + z + ")";
   }
}
