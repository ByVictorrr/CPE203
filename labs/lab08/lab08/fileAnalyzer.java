import java.io.File;
import java.io.*;
import java.nio.DoubleBuffer;
import java.util.*;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;


public class fileAnalyzer {

		FileWriter writerFile;

        private static List <Point> points;

        public List<Point> getPoints(){return points;}

		public static  void processLine(String line, List <Point> points )
      {
        
		String[] words = line.split(",\\s"); //split on commas
   //if no words
         if (words.length == 0)
         {
            return;
         }

        points.add(new Point(words[0],words[1],words[2]));
      }

   public static void processFile(Scanner input, List<Point> points)
   {
      while (input.hasNextLine())
      {
         processLine(input.nextLine(), points);
      }
   }


      //called from main - mostly just pass through important data structures
   public static void populateDataStructures( String filename,List <Point> points)
      throws FileNotFoundException
   {
      try (Scanner input = new Scanner(new File(filename))) {

         processFile(input, points);
      }
   }



   public static  String getFilename(String[] args)
   {
      if (args.length < 1)
      {
         System.err.println("Log file not specified.");
         System.exit(1);
      }

      return args[0];
   }


   public void openWriterFile()
   {
	try{
       writerFile = new FileWriter(( new File("drawMe.txt")));
	}
	catch(Exception e){

	System.out.println("something went wrong");
	
	}
   }

public void writeToFile()
{

    try {
        writerFile = new FileWriter((new File("drawMe.txt")));

        points = points.stream()
                .filter(p -> p.getZ() > 2.0)
                .map(p -> p.scalePoint(0.5))
                .map(p -> p.translate(new Point(150.0, -37, 0)))
                .collect(Collectors.toList());

        for (int i = 0; i < points.size(); i++) {
            //just going to write to a file
            writerFile.write(String.format("%s, ",Double.toString(points.get(i).getX())));
            writerFile.write(String.format("%s, ",Double.toString(points.get(i).getY())));
            writerFile.write(String.format("%s%n", Double.toString(points.get(i).getZ())));
        }

        writerFile.close();
    }
    catch(IOException ex){

        System.out.println("something went wrong");

    }


}


   public static void main(String[] args)
   {

       String filename = getFilename(args);


      try
      {
         populateDataStructures(filename, points);
          //after maybe write to the file

      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }
}
