import java.io.EOFException;
import java.io.FileNotFoundException;

public class ExceptWhat
{
   private static void exceptional(boolean isBad)
   {
      if (isBad)
      {
         throw new EOFException();
      }
   }

   private static void outstanding()
   {
      throw new FileNotFoundException();
   }

   private static void fantastic()
   {
      throw new NullPointerException();
   }

   private static void intermediate3(boolean isBad)
   {
      // This method should not attempt to recover from any exceptions.
      try
      {
         exceptional(isBad);
         outstanding();
         fantastic();
      }
      catch (EOFException | FileNotFoundException e)
      {
         System.out.println(
            String.format("intermediate 3 handled %s -- NOT SUPPOSED TO HAPPEN",
            e));
      }
   }

   private static void intermediate2(boolean isBad)
   {
      /* This method should handle EOFExceptions and print
       * "intermediate 2 - eof handled" when so doing.  But it is not
       * capable of recovering from any other exceptions.
       */
      intermediate3(isBad);
   }

   private static void intermediate1()
   {
      /* This method should handle FileNotFoundExceptions and print
       * "intermediate 1 - handling missing file" when so doing.
       * But this method is not capable of recovering from any
       * other exceptions.
       */
      intermediate2(true);
   }

   private static void start1()
   {
      // Does not attempt to recover from any exceptions.
      intermediate1();
   }

   private static void start2()
   {
      // Does not attempt to recover from any exceptions.
      intermediate2(false);
   }

   public static void main(String[] args)
   {
      /* Though there are exceptions to most rules, let's establish
       * for now that checked exceptions should not escape main.
       * As such, handle any checked exceptions raised below by
       * printing the "main handling " and the type of exception.
       */

      start1();
      start2();
   }
}
