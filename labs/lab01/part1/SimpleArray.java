class SimpleArray
{
   public static int [] squareAll(int values[])
   {
      /* TO DO: This size is not right.  Fix it to work with any
         input array.  The length of an array is accessible through
         an array's length field (e.g., values.length).
      */
      int [] newValues = new int[values.length];  // This allocates an array of integers.

      /* TO DO: The output array, newValues, should hold as
         its elements the square of the corresponding element
         in the input array.

         Write a loop to compute the square of each element from the
         input array and to place the result into the output array.
      */
	for(int x=0; x< values.length; x++)
	{
	newValues[x] = values[x] * values[x];
	}

      return newValues;
   }
}
