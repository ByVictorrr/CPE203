public class CircleTest 
{
   public static void main(String[] args)
   { 
      try
      {
		
         Circle c1 = new Circle(0);
		 System.out.println(c1);
         Circle c2 = new Circle(-1);
		 System.out.println(c2);	
         Circle c3 = new Circle(6);
		 System.out.println(c3);
       }
      catch (ZeroRadiusException e) {
         System.out.println(e.getMessage() + ": 0");
       }
	  catch(NegativeRadiusException e){
         System.out.println(e.getMessage() + ": " + e.radius());
	  } 
      finally {
         System.out.println("In finally.");
      } 
      System.out.println("Done.");
   }
}

