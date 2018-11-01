import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class Scanner
{
   private final CharReader in = new CharReader(
      new BufferedReader(new InputStreamReader(System.in)));

   public Token nextToken()
      throws InvalidNumberException, InvalidCharacterException
   {
      clearWhitespace();
      if (in.gotEOF())
      {
         return Token.createToken(Constants.TK_EOF);
      }

      int c = in.lookahead();

      if (Character.isLetter((char)c))
      {
         return buildIdentifier();
      }
      else if (Character.isDigit((char)c) ||
         ((char)c) == '.')
      {
         return buildNumber();
      }
      else
      {
         return buildSymbol();
      }
   }

   private void clearWhitespace()
   {
      while (!in.gotEOF() && Character.isWhitespace((char)in.lookahead())
         && (char)in.lookahead() != '\n')
      {
         in.read();
      }
   }

   private Token buildIdentifier()
   {
      StringBuilder buf = new StringBuilder();

      while (Character.isLetterOrDigit((char)in.lookahead()))
      {
         buf.append((char)in.read());
      }

      return checkForKeyword(buf.toString());
   }

   private Token buildNumber()
      throws InvalidNumberException
   {
      StringBuilder buf = new StringBuilder();

      while (Character.isDigit((char)in.lookahead()))
      {
         buf.append((char)in.read());
      }

      if ((char)in.lookahead() == '.')
      {
         buf.append((char)in.read());
         while (Character.isDigit((char)in.lookahead()))
         {
            buf.append((char)in.read());
         }
      }

      String num = buf.toString();

      if (num.equals("."))
      {
         throw new InvalidNumberException();
      }

      return Token.createNumToken(num);
   }

   private Token buildSymbol()
      throws InvalidCharacterException
   {
      switch (in.lookahead())
      {
         case '(':
         case ')':
         case '+':
         case '-':
         case '*':
         case '/':
         case '=':
         case '\n':
            return Token.createToken(
               Constants.lookup(String.valueOf((char)in.read())));
         // unrecognized character
         default:
         {
            in.read(); // skip character
            throw new InvalidCharacterException();
         }
      }
   }

   private Token checkForKeyword(final String id)
   {
      int tk = Constants.lookup(id);
      if (tk != Constants.TK_NONE)
      {
         return Token.createToken(tk);
      }
      else
      {
         return Token.createIdToken(id);
      }
   }
}
