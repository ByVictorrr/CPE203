public class Calculator
{
   private static final String STANDARD_HEADER =
      "Welcome to the simple calculator.\n " +
      "To exit, hit <ctrl-d> in Unix or <ctrl-z> in Windows.";
   private static final String STANDARD_PROMPT = "-> ";

   private final String header;
   private final String prompt;
   private final Parser parser;
   private final Bindings bindings;

   public Calculator(final String header, final String prompt,
      final Parser parser, final Bindings bindings)
   {
      this.header = header;
      this.prompt = prompt;
      this.parser = parser;
      this.bindings = bindings;
   }

   public void calculate()
   {
      printHeader();
      while (!this.parser.atEOF())
      {
         printPrompt();
         try
         {
            Operation op = this.parser.parse();
            if (op != null)
            {
               System.out.println(op + " => " + op.evaluate(this.bindings));
            }
         }
         catch (InvalidOperationException exp)
         {
            System.err.println(exp.getMessage());
         }
         catch (UnboundIdentifierException exp)
         {
            System.err.println(exp.getMessage());
         }
      }
   }

   private void printHeader()
   {
      System.out.println(this.header);
   }

   private void printPrompt()
   {
      System.out.print(this.prompt);
   }

   public static void main(String [] args)
   {
      new Calculator(
         STANDARD_HEADER,
         STANDARD_PROMPT,
         new Parser(new Scanner()),
         new VariableBindings()).calculate();
      System.out.println();
   }
}
