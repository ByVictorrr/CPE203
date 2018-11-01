class Token
{
   private final String lexeme;
   private final int code;

   public static Token createToken(final int code)
   {
      return new Token(Constants.getLexeme(code), code);
   }

   public static Token createIdToken(final String lexeme)
   {
      return new Token(lexeme, Constants.TK_ID);
   }

   public static Token createNumToken(final String lexeme)
   {
      return new Token(lexeme, Constants.TK_NUM);
   }

   private Token(final String lexeme, final int code)
   {
      this.lexeme = lexeme;
      this.code = code;
   }

   public String toString()
   {
      return lexeme;
   }

   public int getCode()
   {
      return code;
   }
}
