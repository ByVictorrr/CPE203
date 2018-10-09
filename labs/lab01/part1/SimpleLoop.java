class SimpleLoop
{
   public static int sum(int low, int high)
   {
      /* TO DO:  Return the sum of the integers between
         low and high (inclusive).  Yes, this can be
         done without a loop, but the point is to
         practice the syntax for a loop.
      */
	//should be abs(high-low)
	int sumHtoL = high;
	int counter = low;

	for(int x=0; x < high-low; x++){
			
		sumHtoL = sumHtoL + counter;
		counter = counter + 1;
	}


      return sumHtoL;
   }
}
