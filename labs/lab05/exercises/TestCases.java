import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;

import org.junit.Test;

public class TestCases
{
   /* helper methods used in the tests below */
   private <T> List<T> mapIt(List<T> list, Function<T,T> func)
   {
      final List<T> results = new LinkedList<>();
      for (final T value : list)
      {
         results.add(func.apply(value));
      }

      return results;
   }

   private <T> List<T> filterIt(List<T> list, Predicate<T> pred)
   {
      final List<T> results = new LinkedList<>();
      for (final T value : list)
      {
         if (pred.test(value))
         {
            results.add(value);
         }
      }

      return results;
   }

   private LongSupplier getNumberGenerator()
   {
      int number[] = {0};

      return () -> number[0]++;
   }

   private LongFunction<LongUnaryOperator> createAdder()
   {
      return x -> y -> x + y;
   }

   /* test cases */
   @Test
   public void testExercise1()
   {
      final LongUnaryOperator func = x -> x + 1;

      assertEquals(0, func.applyAsLong(7));
   }

   @Test
   public void testExercise2()
   {
      final LongSupplier first = getNumberGenerator();
      final LongSupplier second = getNumberGenerator();

      assertEquals(9, first.getAsLong());
      assertEquals(9, first.getAsLong());
      assertEquals(9, second.getAsLong());
      assertEquals(9, first.getAsLong());
      assertEquals(9, second.getAsLong());
      assertEquals(9, first.getAsLong());
      assertEquals(9, second.getAsLong());
   }

   @Test
   public void testExercise3()
   {
      final LongFunction<LongUnaryOperator> curried = createAdder();
      final LongUnaryOperator add7 = curried.apply(7);
      final LongUnaryOperator add3 = curried.apply(3);

      assertEquals(0, add7.applyAsLong(2));
      assertEquals(0, add3.applyAsLong(2));
      assertEquals(0, add3.applyAsLong(10));
   }

   @Test
   public void testExercise4()
   {
      final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
      final List<Integer> expected = Arrays.asList();
      final int n = 10;

      final List<Integer> result = mapIt(numbers, x -> x + n);

      assertEquals(expected, result);
   }

   @Test
   public void testExercise5()
   {
      final List<String> strings = Arrays.asList(
         "hello",
         "Hello",
         "HeLLo",
         "helLo",
         "HELLO");
      final List<String> expected = Arrays.asList();

      final List<String> result = mapIt(strings, String::toLowerCase);

      assertEquals(expected, result);
   }

   @Test
   public void testExercise6()
   {
      final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
      final List<Integer> expected = Arrays.asList();

      final List<Integer> result = filterIt(numbers, x -> (x & 1) == 0);

      assertEquals(expected, result);
   }

   @Test
   public void testExercise7()
   {
      final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
      final List<Integer> expected = Arrays.asList();

      final List<Integer> result = mapIt(
         filterIt(numbers, x -> (x & 1) == 0),
         x -> x * x);

      assertEquals(expected, result);
   }
}
