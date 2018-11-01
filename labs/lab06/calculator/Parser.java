class Parser
{
   private final Scanner scanner;
   private Token currentToken;

   public Parser(final Scanner scanner)
   {
      this.scanner = scanner;
      this.currentToken = Token.createToken(Constants.TK_NONE);
   }

   public Operation parse()
      throws InvalidOperationException
   {
      nextToken();
      return parseOperation();
   }

   private Operation parseOperation()
      throws InvalidOperationException
   {
      Operation op = null;
      switch (currentTokenCode())
      {
         case Constants.TK_SET:
            op = parseSet();
            expectNewline();
            break;
         case Constants.TK_NEWLINE:
         case Constants.TK_EOF:
            break;
         default:
            op = parseExpression();
            expectNewline();
            break;
      }

      return op;
   }

   private Assignment parseSet()
      throws InvalidOperationException
   {
      match(Constants.TK_SET);
      String id = matchIdentifier();
      match(Constants.TK_ASSIGN);
      Expression e = parseExpression();
      return new Assignment(id, e);
   }

   private Expression parseExpression()
      throws InvalidOperationException
   {
      Expression e = parseTerm();
      while (isAddOp(currentTokenCode()))
      {
         int tkcode = parseBinaryOp();
         e = newBinaryOp(tkcode, e, parseTerm());
      }

      return e;
   }

   private Expression parseTerm()
      throws InvalidOperationException
   {
      Expression e = parseUnary();
      while (isMultOp(currentTokenCode()))
      {
         int tkcode = parseBinaryOp();
         e = newBinaryOp(tkcode, e, parseUnary());
      }

      return e;
   }

   private Expression parseUnary()
      throws InvalidOperationException
   {
      Expression e;
      switch (currentTokenCode())
      {
         case Constants.TK_MINUS:
            e = parseUnaryNegation();
            break;
         default:
            e = parseFactor();
            break;
      }
      return e;
   }

   private Expression parseUnaryNegation()
      throws InvalidOperationException
   {
      match(Constants.TK_MINUS);
      return new NegationExpression(parseUnary());
   }

   private Expression parseFactor()
      throws InvalidOperationException
   {
      Expression e = null;
      switch (currentTokenCode())
      {
         case Constants.TK_LPAREN:
            match(Constants.TK_LPAREN);
            e = parseExpression();
            match(Constants.TK_RPAREN);
            break;
         case Constants.TK_ID:
            e = new IdentifierExpression(matchIdentifier());
            break;
         case Constants.TK_NUM:
            e = new DoubleConstantExpression(
               Double.parseDouble(currentLexeme()));
            nextToken();
            break;
         default:
            expected("identifier or value");
      }
      return e;
   }

   private static boolean isAddOp(final int tkcode)
   {
      return tkcode == Constants.TK_PLUS || tkcode == Constants.TK_MINUS;
   }

   private static boolean isMultOp(final int tkcode)
   {
      return tkcode == Constants.TK_TIMES || tkcode == Constants.TK_DIVIDE;
   }

   private int parseBinaryOp()
      throws InvalidOperationException
   {
      int tkcode = currentTokenCode();
      nextToken();
      return tkcode;
   }

   private static Expression newBinaryOp(final int tkcode,
      final Expression lft, final Expression rht)
   {
      Expression e = null;
      switch (tkcode)
      {
         case Constants.TK_PLUS:
            e = new AddExpression(lft, rht);
            break;
         case Constants.TK_MINUS:
            e = new SubtractExpression(lft, rht);
            break;
         case Constants.TK_TIMES:
            e = new MultiplyExpression(lft, rht);
            break;
         case Constants.TK_DIVIDE:
            e = new DivideExpression(lft, rht);
            break;
      }
      return e;
   }

   private String currentLexeme()
   {
      return currentToken.toString();
   }

   private int currentTokenCode()
   {
      return currentToken.getCode();
   }

   private void nextToken()
      throws InvalidOperationException
   {
      try
      {
         currentToken = scanner.nextToken();
      }
      catch (InvalidNumberException e)
      {
         currentToken = Token.createToken(Constants.TK_NONE);
         clearLine();
         throw new InvalidOperationException("Invalid number.");
      }
      catch (InvalidCharacterException e)
      {
         currentToken = Token.createToken(Constants.TK_NONE);
         clearLine();
         throw new InvalidOperationException("Invalid input.");
      }
   }

   public boolean atEOF()
   {
      return currentTokenCode() == Constants.TK_EOF;
   }

   private String matchIdentifier()
      throws InvalidOperationException
   {
      if (currentTokenCode() != Constants.TK_ID)
      {
         expected("identifier");
      }
      String id = currentLexeme();
      nextToken();
      return id;
   }

   private void match(final int tkcode)
      throws InvalidOperationException
   {
      if (currentTokenCode() != tkcode)
      {
         expected("'" + Constants.getLexeme(tkcode) + "'");
      }
      nextToken();
   }

   /************************
      error handling
   ************************/
   private void expected(final String msg)
      throws InvalidOperationException
   {
      String tk = currentLexeme();
      clearLine();
      throw new InvalidOperationException(
         "expected " + msg + ", got '" + tk + "'");
   }

   private void expectNewline()
      throws InvalidOperationException
   {
      if (currentTokenCode() != Constants.TK_NEWLINE)
      {
         clearLine();
         throw new InvalidOperationException(
            "unexpected symbols after expression");
      }
   }

   private void clearLine()
   {
      int tkcode = currentTokenCode();
      while (tkcode != Constants.TK_NEWLINE && tkcode != Constants.TK_EOF)
      {
         try
         {
            nextToken();
         }
         catch (InvalidOperationException e)
         {}
         tkcode = currentTokenCode();
      }
   }
}
