import java.util.HashMap;

public class Constants
{
   public static final int
      LEXEME_FIRST      =	 0, // should match the first entry in lexemes
      TK_LPAREN         =	 0,
      TK_RPAREN      	=	 1,
      TK_ASSIGN		=	 2,
      TK_SET		=	 3,
      TK_PLUS	        =	 4,
      TK_MINUS   	=	 5,
      TK_TIMES   	=	 6,
      TK_DIVIDE   	=	 7,
      TK_NEWLINE	=	 8,
      TK_EOF		=	 9,	// last in lexemes
         // the following correspond to sets of tokens
      TK_ID		=	10,
      TK_NUM            =	11,
      TK_NONE		=	12,

      LEXEME_LAST	=	TK_EOF; // should match the last
                                        // entry in lexemes array
                                        // which is EOF

      // the entries in the array match the lexemes for the token
      // representations above
   static final String [] lexemes =
   {
      "(", ")", "=", "set", "+", "-", "*", "/", "\n", "*EOF*"
   };

   private static final HashMap<String,Integer> map = new HashMap<>();

      // static initializer to populate the hash map based on the
      // lexemes array
   static
   {
      for (int i = LEXEME_FIRST; i <= LEXEME_LAST; i++)
      {
         map.put(lexemes[i], new Integer(i));
      }
   };

   public static int lookup(String str)
   {
      return map.getOrDefault(str, TK_NONE);
   }

   public static String getLexeme(int tk)
   {
      if (0 <= tk && tk <= lexemes.length)
      {
         return lexemes[tk];
      }

      return "<invalid>";
   }
}
