/**
 * A started JUnit test class to test the Lab 09
 * various CircleException classes.
 *
 * @author Julie Workman
 * @version 5/3/2016 (Written to JUnit-4.12)
 * @version 11/11/2017 (Made version to give to students.)
 */

import static org.junit.Assert.*;
import org.junit.*;
import java.lang.reflect.*;
import org.junit.runners.MethodSorters;
import org.junit.rules.*;
import org.junit.runner.Description;
import java.util.concurrent.TimeUnit;

public class Lab09Tests
{   
   /* Some rules to indicate the start of a test and how long the test took. */
   @Rule
   public TestRule watcher = new TestWatcher() {
      protected void starting(Description description) {
         System.out.print("Starting: " + description.getMethodName() + "...");
      }
   };
   
   @Rule
   public Stopwatch sw = new Stopwatch() {
      protected void finished(long nanos, Description description) {
         System.out.println(" (" + runtime(TimeUnit.MILLISECONDS) + " ms)");
      }
      protected void succeeded(long nanos, Description description) {
         System.out.print("Passed");
      }
      protected void failed(long nanos, Throwable e, Description description) {
         System.out.print("Failed");
      }
   };
   
   // ---------------------------- Architecture Tests --------------------------------
   
   /* CircleException architecture test 
      - no instance variables, one constructor, no other methods */
   @Test (timeout = 5000)
   public void circleExceptionArchitectureTest()
   {
      Class c = CircleException.class;
      Field[] fields = c.getDeclaredFields();
      Method[] methods = c.getDeclaredMethods();
      
      assertEquals("CircleException should have no declared fields.", 
                   0, fields.length); 
      assertEquals("CircleException should have no declared methods.", 
                   0, methods.length);              
      assertEquals("Parent of CircleException should be RuntimeException.", 
                   "java.lang.RuntimeException", c.getSuperclass().getName());   
   }
   
   /* ZeroRadiusException architecture test 
      - no instance variables, one constructor, no other methods */
   @Test (timeout = 5000)
   public void zeroRadiusExceptionArchitectureTest()
   {
      Class c = ZeroRadiusException.class;
      Field[] fields = c.getDeclaredFields();
      Method[] methods = c.getDeclaredMethods();
      
      assertEquals("CircleException should have no declared fields.", 
                   0, fields.length); 
      assertEquals("CircleException should have no declared methods.", 
                   0, methods.length);              
      assertEquals("Parent of ZeroRadiusException should be CircleException.",
                   "CircleException", c.getSuperclass().getName());   
   }
   
   /* NegativeRadiusException architecture test 
      - one instance variable, one constructor, one public method */
   @Test (timeout = 5000)
   public void negativeRadiusExceptionArchitectureTest()
   {
      Class c = NegativeRadiusException.class;
      Field[] fields = c.getDeclaredFields();
      Method[] methods = c.getDeclaredMethods();
      int cnt;
      
      assertEquals("NegativeRadiusException should have one declared field.", 
                   1, fields.length); 
      assertEquals("NegativeRadiusException's instance variable should be private.", 
                   Modifier.PRIVATE, fields[0].getModifiers()); 
      assertEquals("NegativeRadiusException should have one declared method.", 
                   1, methods.length);  
      assertEquals("NegativeRadiusException's method should be public.", 
                   Modifier.PUBLIC, methods[0].getModifiers());             
      assertEquals("Parent of NegativeRadiusException should be CircleException.", 
                   "CircleException", c.getSuperclass().getName());   
   }
  
   // ---------------------------- Fuctionality Tests --------------------------------
      
   @Test
   public void throwsNothing() {
      Circle c = new Circle(3);
   }
   
   @Test(expected = ZeroRadiusException.class)
   public void throwsZeroRadiusException() 
   {
      Circle c = new Circle(0);
   }
}
   