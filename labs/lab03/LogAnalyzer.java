import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LogAnalyzer
{
      //constants to be used when pulling data out of input
      //leave these here and refer to them to pull out values
   private static final String START_TAG = "START"; //idenitfier 

   private static final int START_NUM_FIELDS = 3;
   private static final int START_SESSION_ID = 1; //index where the start sesesion in an array of strings
   private static final int START_CUSTOMER_ID = 2;


   private static final String BUY_TAG = "BUY";
   private static final int BUY_NUM_FIELDS = 5;
   private static final int BUY_SESSION_ID = 1;
   private static final int BUY_PRODUCT_ID = 2;
   //each price is an integer number of cents
   private static final int BUY_PRICE = 3;
   private static final int BUY_QUANTITY = 4;

   private static final String VIEW_TAG = "VIEW"; 
   private static final int VIEW_NUM_FIELDS = 4; //indices in the array of string 
   private static final int VIEW_SESSION_ID = 1;
   private static final int VIEW_PRODUCT_ID = 2;
   private static final int VIEW_PRICE = 3;
   

   private static final String END_TAG = "END";
   private static final int END_NUM_FIELDS = 2;
   private static final int END_SESSION_ID = 1;

      //a good example of what you will need to do next
      //creates a map of sessions to customer ids


   // pass each map through each 

   private static void processStartEntry(final String[] words, final Map<String, List<String>> sessionsFromCustomer)
   {
      if (words.length != START_NUM_FIELDS)
      {
         return;
      }

         //check if there already is a list entry in the map
         //for this customer, if not create one
      List<String> sessions = sessionsFromCustomer.get(words[START_CUSTOMER_ID]);
    
      if (sessions == null)
      {
         //making the entry 
         sessions = new LinkedList<>();

         sessionsFromCustomer.put(words[START_CUSTOMER_ID], sessions);
      }

         //now that we know there is a list, add the current session
      sessions.add(words[START_SESSION_ID]);
   }

      //similar to processStartEntry, should store relevant view
      //data in a map - model on processStartEntry, but store
      //your data to represent a view in the map (not a list of strings)
   



//sessionFrom custommer = < customer id , {session1,session2,....,sessionn}>
//viewFromsession = < sesioni, {View1,View2,...,Viewn}


//session id is the key, the value is a list of views<string,int for price>
   private static void processViewEntry(final String[] words, final Map <String, List<View>> viewFromSession)
   {
      if (words.length != VIEW_NUM_FIELDS)
      {
         return;
      }

         //check if there already is a list entry in the map
         //for this customer, if not create one
      List<View> views = viewFromSession.get(words[VIEW_SESSION_ID]);
    
      if (viewFromSession == null)
      {
         //making the entry 
         viewFromSession = new LinkedList<>();

         viewFromSession.put(words[VIEW_SESSION_ID], views);
      }

         //now that we know there is a list, add the current session
      views.add(words[VIEW_SESSION_ID]);

   }

      //similar to processStartEntry, should store relevant purchases
      //data in a map - model on processStartEntry, but store
      //your data to represent a purchase in the map (not a list of strings)

   //product id, price and quanity

   //buy object will have getters and setters
//session id is the key, the value is a list of views<string,int for price>
   private static void processBuyEntry(final String[] words, final Map <String, List<Buy>> buyFromSession)
   {
      if (words.length != BUY_NUM_FIELDS)
      {
         return;
      }

      if( words [0 ] == BUY_TAG )
      {

         Buy b_i = new Buy(words[BUY_PRODUCT_ID], words[BUY_PRICE], words[BUY_QUANTITY]);
      }
         //check if there already is a list entry in the map
         //for this customer, if not create one
      List<Buy> buys = buyFromSession.get(b_i[BUY_SESSION_ID]);
    
      if (buyFromSession == null)
      {
         //making the entry 
         buyFromSession = new LinkedList<>();

         buyFromSession.put(b_i[BUY_SESSION_ID], buys);
      }

         //now that we know there is a list, add the current session
      buys.add(words[BUY_SESSION_ID])

   }

//
   private static void processEndEntry(final String[] words)
   {
      if (words.length != END_NUM_FIELDS)
      {
         return;
      }
   }

     

      //write this after you have figured out how to store your data
      //make sure that you understand the problem
   private static void printSessionPriceDifference(
      /* add parameters as needed */
      )
   {
      System.out.println("Price Difference for Purchased Product by Session");

      /* add printing */
   }

      //write this after you have figured out how to store your data
      //make sure that you understand the problem
   private static void printCustomerItemViewsForPurchase(
      /* add parameters as needed */
      )
   {
      System.out.println("Number of Views for Purchased Product by Customer");

      /* add printing */
   }

      //write this after you have figured out how to store your data
      //make sure that you understand the problem
   private static void printStatistics(
      /* add parameters as needed */)
   {
      printSessionPriceDifference( /*add arguments as needed */);
      printCustomerItemViewsForPurchase( /*add arguments as needed */);

      /* This is commented out as it will not work until you read
         in your data to appropriate data structures, but is included
         to help guide your work - it is an example of printing the
         data once propogated 
         printOutExample(sessionsFromCustomer, viewsFromSession, buysFromSession);
      */
		
   }





   /* provided as an example of a method that might traverse your
      collections of data once they are written 
      commented out as the classes do not exist yet - write them! */









//last step


   private static void printOutExample(
      final Map<String, List<String>> sessionsFromCustomer,
      final Map<String, List<View>> viewsFromSession,
      final Map<String, List<Buy>> buysFromSession) 
   {
      //for each customer, get their sessions
      //for each session compute views
      for(Map.Entry<String, List<String>> entry: sessionsFromCustomer.entrySet()) 
      {
         System.out.println(entry.getKey());
         List<String> sessions = entry.getValue();

         for(String sessionID : sessions)
         {
            System.out.println("\tin " + sessionID);
            List<View> theViews = viewsFromSession.get(sessionID);
            for (View thisView: theViews)
            {
               System.out.println("\t\tviewed " + thisView.getProduct());
            }
         }
      }
   }




         //this is called by processFile below - its main purpose is
         //to process the data using the methods you write above
      private static void processLine(
         final String line,
         final Map<String, List<String>> sessionsFromCustomer,
         final Map<String, List<View>> viewsFromSession,
         final Map<String, List<Buy>> buyFromSession
         /* add parameters as needed */
         )
      {
         final String[] words = line.split("\\h");
   //if no words 
         if (words.length == 0)
         {
            return;
         }
         //word[0] going to be start view or buy

         switch (words[0])
         {
            case START_TAG:
               processStartEntry(words, sessionsFromCustomer);
               break;
            case VIEW_TAG:
               processViewEntry(words, viewFromSession);
               break;
            case BUY_TAG:
               processBuyEntry(words, buyFromSession);
               break;
            case END_TAG:
               //processEndEntry(words, /* add arguments as needed */ );
               break;
         }
      }

      //called in populateDataStructures
   private static void processFile(
      final Scanner input,
      final Map<String, List<String>> sessionsFromCustomer,
      final Map<String, List<View>> viewsFromSession,
      final Map<String, List<Buy>> buyFromSession
   
      )
   {
      while (input.hasNextLine())
      {
         //each try that suceeds puts a string 
         processLine(input.nextLine(), sessionsFromCustomer, viewsFromSession, buyFromSession);
      }
   }

      //called from main - mostly just pass through important data structures	
   private static void populateDataStructures(final String filename,
                                             final Map<String, List<String>> sessionsFromCustomer,
                                             final Map<String, List<View>> viewsFromSession,
                                             final Map<String, List<Buy>> buyFromSession
   )
      throws FileNotFoundException
   {
      try (Scanner input = new Scanner(new File(filename)))
      {

         processFile(input, sessionsFromCustomer, viewsFromSession, buyFromSession);
      }
   }





   private static String getFilename(String[] args)
   {
      if (args.length < 1)
      {
         System.err.println("Log file not specified.");
         System.exit(1);
      }

      return args[0];
   }








   public static void main(String[] args)
   {
      /* Map from a customer id to a list of session ids associated with
       * that customer.
       */

      
//master map
      final Map<String, List<String>> sessionsFromCustomer = new HashMap<>();

//slave map
      final Map<String, List<View>> viewsFromSession = new HashMap<>();
      final Map<String, List<Buy>> buysFromSession = new HashMap<>();

      /* create additional data structures to hold relevant information */
      //<custommerID, 
      /* they will most likely be maps to important data in the logs */

      final String filename = getFilename(args);
      

      try
      {
         populateDataStructures(filename, sessionsFromCustomer, viewsFromSession, buyFromSession);
         


         printStatistics(
            /* add parameters as needed */
            );
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }
}
