import java.util.HashMap;
import java.util.Map;

class VariableBindings
   implements Bindings
{ 
   private final Map<String, Double> bindings = new HashMap<>();

   public void addBinding(final String id, final double value)
   {
      bindings.put(id, value);
   }

   public double lookupBinding(final String id)
      throws UnboundIdentifierException
   {
      Double value = bindings.get(id);

      if (value == null)
      {
         throw new UnboundIdentifierException("unbound identifier: " + id);
      }
      else
      {
         return value;
      }
   }
}
