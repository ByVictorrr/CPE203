public class ComposedComparator implements Comparator<Song>{
	private Comparator<Song> c1;
	private  Comparator<Song> c2;
	
	public ComposedComparator(Comparator<Song> c1, Comparator<Song> c2)
	{
	this.c1 = c1;
	this.c2 = c2;
	}
	public int compare(Comparator<Song> c1, Comparator<Song> c2)
	{
		//if comparision is equal go through c2 and check
		c1.compare()	
	}
/*
 * The compare method must be defined to use c1 to compare the Song objects and then, if they are equivalent by the c1 ordering, use c2.

Write a test using this comparator; be sure to select a pair of songs that demonstrate the sequencing behavior of this comparator.

For example, when you compare the fourth and eighth songs in the song list, they are both by the same artist, but with different years. When compared think about what the result would be based on the years of these songs. 
 *
 * */


}
