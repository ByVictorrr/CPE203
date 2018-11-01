abstract class BinaryExpression implements Expression
{
   private final Expression lft;
   private final Expression rht;
   private final String operator;

   public BinaryExpression(final Expression lft, final Expression rht, String operator)
   {
      this.operator = operator;
      this.lft = lft;
      this.rht = rht;
   }
	
  
   public String toString(){

	return "(" + lft +  operator.toString() +  ")";
	
}
    abstract protected double _applyOperator(double lft, double rht);

       

  public double evaluate(final Bindings bindings){
      
      double lftExp = lft.evaluate(bindings);
      double rhtExp = rht.evaluate(bindings);

      return _applyOperator(lftExp,rhtExp);
}


}
