import processing.core.*;
import java.util.*;

public class drawPoints extends PApplet {
	private List <Point> points;
	public void settings() {
    size(500, 500);
	}
  
	public void setup() {
    	background(180);
    	noLoop();
  	}

  	public void draw() {

		//assigning values to points
		populateList(points);

		points.stream()
			.filter() //filter z > 2.0
			.mapToDouble() //scale all points by 1/2
			.mapToDouble() //translates all points by {-150,-37}
			.collect(collection.toList());



		//put in for loop to draw filtered 
   //ellipse((int)(points.get(i)),(int)(points.get(i).y), 1,1);
  	}


	public void populateList(List <Point> p)
	{
		p = new ArrayList<Point>();

		String[] lines = loadStrings("drawMe.txt");
		println("there are " + lines.length);
  		for (int i=0; i < lines.length; i++){
      	if (lines[i].length() > 0 ) {
        		String[] words= lines[i].split(",");

        		points.get(i).x = Double.parseDouble(words[0]);
        		points.get(i).y = Double.parseDouble(words[1]);
        		points.get(i).z = Double.parseDouble(words[2]);
		}
	}

}

  	public static void main(String args[]) {
      PApplet.main("drawPoints");
   }
}
