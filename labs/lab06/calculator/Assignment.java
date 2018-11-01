class Assignment
   implements Operation
{
   private final String id;
   private final Expression e;

   public Assignment(final String id, final Expression e)
   {
      this.id = id;
      this.e = e;
   }

   public double evaluate(final Bindings bindings)
   {
      double value = e.evaluate(bindings);
      bindings.addBinding(id, value);
      return value;
   }

   public String toString()
   {
      return "set " + id + " = " + e;
   }
}
