public class Lab00
{
   public static void main(String[] args)
   {

	//declaring and initalizing some vars
	int x = 5;
	String y = "hello";
	double z = 9.8;

	//printing the variables

	System.out.println(" x: " + x + " y: " + y + " z: " + z  );

	//make a list of number

	int [] nums = {3, 6, -1, 2 };

	//printing all array values
	for ( int i = 0; i< nums.length; i++){

		System.out.println(nums[i]);

	}

	//call a function

	int numFound = char_count(y,'l');

	System.out.println("Found:" +  numFound);


	//a counting for loop

	for (int j=1; j< 11; ++j)
	{
		System.out.print(j + " ");
	}  
 
  
 	}

	//new function
	//trying to find if the character is in the input string
	public static int char_count(String s, char c)
	{
 	int count = 0;

	for (int x= 0; x < s.length(); x++)
	{
	if (c == s.charAt(x))
	{ //if our char is found in string
		++count;
	}
	}

	return count;
	}



}
