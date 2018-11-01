import java.io.IOException;
import java.io.Reader;

class CharReader
{
   private final Reader input;

   private boolean gotEOF = false;
   private boolean putback = false;
   private int currentChar = '\0';

   public static final int EOF = -1;

   public CharReader(final Reader reader)
   {
      this.input = reader;
   }

   public int read()
   {
      if (putback)
      {
         putback = false;
      }
      else
      {
         currentChar = protectedRead();
      }

      if (currentChar == EOF)
      {
         gotEOF = true;
      }

      return currentChar;
   }

   public int lookahead()
   {
      if (putback)
      {
         return currentChar;
      }
      else
      {
         int c = read();
         putback = true;
         return c;
      }
   }

   public boolean gotEOF()
   {
      return gotEOF;
   }

   private int protectedRead()
   {
      try
      {
         return input.read();
      }
      catch (IOException e)
      {
         System.err.println("Unexpected I/O error.");
         System.exit(1);
      }
      return -1;
   }
}

