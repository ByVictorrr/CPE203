
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestCases
{
   private static final double DELTA = 0.00001;

   @Test
   public void testSimpleIf1()
   {
      assertEquals(1.7, SimpleIf.max(1.2, 1.7), DELTA);
   }

   @Test
   public void testSimpleIf2()
   {
      assertEquals(9.0, SimpleIf.max(9.0, 3.2), DELTA);
   }

   @Test
   public void testSimpleIf3()
   {
     // fail("Missing SimpleIf3");
      /* TO DO: Write one more valid test case. */
	assertEquals(0.21, SimpleIf.max(0.21, .0001), DELTA);

   }

   @Test
   public void testSimpleLoop1()
   {
      assertEquals(7, SimpleLoop.sum(3, 4));
   }

   @Test
   public void testSimpleLoop2()
   {
	//SimpleLoop.sum(x,y) = x+(x+1)+.....+(y)
      assertEquals(7, SimpleLoop.sum(-2, 4));
   }

   @Test
   public void testSimpleLoop3()
   {
     // fail("Missing SimpleLoop3");
      /* TO DO: Write one more valid test case to make sure that
         this function is not just returning 7. */
	assertEquals(3, SimpleLoop.sum(1,2));
   }

   @Test
   public void testSimpleArray1()
   {
      /* What are those parameters?  They are newly allocated arrays
         with initial values. */
      assertArrayEquals(
         new int[] {1, 4, 9},
         SimpleArray.squareAll(new int[] {1, 2, 3}));
   }

   @Test
   public void testSimpleArray2()
   {
      assertArrayEquals(
         new int[] {49, 25},
         SimpleArray.squareAll(new int[] {7, 5}));
   }

   @Test
   public void testSimpleArray3()
   {
      //fail("Missing SimpleArray3");
      /* TO DO: Add a new test case. */

  assertArrayEquals(
         new int[] {256, 64},
         SimpleArray.squareAll(new int[] {16,8}));

   }

   @Test
   public void testBetterLoop1()
   {
      assertTrue(BetterLoop.contains(new int[] {7, 5}, 5));
   }

   @Test
   public void testBetterLoop2()
   {
      assertTrue(BetterLoop.contains(new int[] {7, 5, 2, 4}, 4));
   }

   @Test
   public void testBetterLoop3()
   {
      //fail("Missing BetterLoop3");
      /* TO DO: Write a valid test case where the expected result is false. */

	 assertTrue(BetterLoop.contains(new int[] {8, 12, 3, 9}, 9));

   }
}
