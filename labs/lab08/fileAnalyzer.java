import java.io.File;


import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class fileAnalyzer {

        private List <Point> points;

        private List<Point> getPoints(){return points;}

      private static void processLine(String line, List <Point> points )
      {
          String[] words = line.split("\\h");
   //if no words
         if (words.length == 0)
         {
            return;
         }
             points.add(words[0],words[1],words[2]);
      }

   private static void processFile(Scanner input, List<Point> points)
   {
      while (input.hasNextLine())
      {
         //each try that suceeds puts a string
         processLine(input.nextLine(), points);
      }
   }


      //called from main - mostly just pass through important data structures
   private static void populateDataStructures( String filename,List <Point> points)
      throws FileNotFoundException
   {
      try (Scanner input = new Scanner(new File(filename))) {

         processFile(input, points);
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

                List<Point> points = new ArrayList<Point>();


       String filename = getFilename(args);



      try
      {
         populateDataStructures(filename, points);


         printStatistics(sessionsFromCustomer,viewsFromSession, buysFromSession);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }
}
