import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.*;
import org.junit.Test;
import org.junit.Before;

public class TestCases
{
   private static final Song[] songs = new Song[] {
         new Song("Decemberists", "The Mariner's Revenge Song", 2005),
         new Song("Rogue Wave", "Love's Lost Guarantee", 2005),
         new Song("Avett Brothers", "Talk on Indolence", 2006),
         new Song("Gerry Rafferty", "Baker Street", 1998),
         new Song("City and Colour", "Sleeping Sickness", 2007),
         new Song("Foo Fighters", "Baker Street", 1997),
         new Song("Queen", "Bohemian Rhapsody", 1975),
         new Song("Gerry Rafferty", "Baker Street", 1978)
      };

   @Test
   public void testArtistComparator()
   {


   System.out.println((int)'A');
	System.out.println((int)'D');
   
//as A.....Z -> 65 66 ..... N

   Comparator <Song> compArtist = new ArtistComparator();

   							// true,true,true 
   boolean [] testCompare = { compArtist.compare(songs[0],songs[1]) < 0 , compArtist.compare(songs[5],songs[4]) > 0 , compArtist.compare(songs[3],songs[7]) == 0};

   
 	assertEquals(true,testCompare[0]);

 	assertEquals(true,testCompare[1]);

 	assertEquals(true,testCompare[2]);
	
	}

   @Test
   public void testLambdaTitleComparator()
   {
   	//return N<0 when s1 has string title of natural order higher than s2

   	// lambda = int compare (Song s1, Song s2 ){return s1.getTitle() - s2.gettitle();} 
   	Comparator<Song> lambda = (Song s1, Song s2) -> {return (int)(s1.getTitle().charAt(0)) - (int)(s2.getTitle().charAt(0)) ;} ; 
 	

   	//true, true, true
 	boolean [] testCompare = { lambda.compare(songs[3],songs[0]) < 0 , lambda.compare(songs[5],songs[6]) == 0  , lambda.compare(songs[0],songs[7]) > 0};


   	assertEquals(true, testCompare[0]);
   	assertEquals(true, testCompare[1]);
	assertEquals(true, testCompare[2]);

   }
//use key extraction if we are trying to compare more than two vars
   @Test
   public void testYearExtractorComparator()
   {
   	//write a few test cases test a compartor <song> ordering by year in descending order (in other words most recent songs woulb be first)

   	Comparator<Song> compKeyExtractor = Comparator.comparing(Song::getYear); //tells compare method to sort by natural order of year

   	compKeyExtractor  = compKeyExtractor.reversed();

   	int [] compareTest = {
   						compKeyExtractor.compare(songs[2],songs[3]), //song[2].getYear() = 2006, //song[3].getYear() = 1998

   						compKeyExtractor.compare(songs[7],songs[6]), //song[7].getYear() = 1978, //song[6].getYear() = 1975

   	};

  	boolean [] testCompare = { compareTest[0] < 0 , compareTest[1] < 0};


  	assertEquals(true, testCompare[0]);
  	assertEquals(true, testCompare[1]);

   	//should be neg
   	//System.out.println(compareTest[0]);


	//assertEquals(true, [0]);

   }

   @Test
   public void testComposedComparator()
   {

	Comparator<Song> c1 = Comparator.comparing(Song::geTitle);
	Comparator<Song> c2 = Comparator.comparing(Song::Year);

	ComposedComparator comp = new ComposedComparator(c1,c2);
	
	comp.compare(songs[0],songs[1]);

	

	int [] compareTest = { 
 

};






   }

   @Test
   public void testThenComparing()
   {
   }

 /*  @Test
   public void runSort()
   {
      List<Song> songList = new ArrayList<>(Arrays.asList(songs));
      List<Song> expectedList = Arrays.asList(
         new Song("Avett Brothers", "Talk on Indolence", 2006),
         new Song("City and Colour", "Sleeping Sickness", 2007),
         new Song("Decemberists", "The Mariner's Revenge Song", 2005),
         new Song("Foo Fighters", "Baker Street", 1997),
         new Song("Gerry Rafferty", "Baker Street", 1978),
         new Song("Gerry Rafferty", "Baker Street", 1998),
         new Song("Queen", "Bohemian Rhapsody", 1975),
         new Song("Rogue Wave", "Love's Lost Guarantee", 2005)
         );

      songList.sort(
         // pass comparator here
      );

      assertEquals(songList, expectedList);
   }
   */
}
