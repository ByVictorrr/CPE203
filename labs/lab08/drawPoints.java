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
			.filter(p-> p.getZ() > 2.0) //filter z > 2.0
			.map(p-> p.scalePoint()) //scale all points by 1/2
			.map(p-> p.translated(new Point("-150.0","-37.0"))) //translates all points by {-150,-37}
			.collect(Collectors.toList());

    //put in for loop to draw filtered 
   
		for(int i =0; i < points.size(); i++){
		ellipse((int)(points.get(i).getX()),(int)(points.get(i).getY()), 1,1);
		}
	}


	public void populateList(List <Point> p)
	{
		p = new ArrayList<Point>();

		String[] lines = loadStrings("drawMe.txt");
		println("there are " + lines.length);
  		for (int i=0; i < lines.lengthi; i++){
      	if (lines[i].length() > 0 ) { 
        		String[] words= lines[i].split(",");

		points.get(i) = new Point(words[0],words[1],words[2]);
       //points.get(i).getX() = Double.parseDouble(words[0]);
		//points.get(i).getY() = Double.parseDouble(words[1]);
        //points.get(i).getZ() = Double.parseDouble(words[2]);
		}
	}

}

	public static void main(String args[]) {
      PApplet.main("drawPoints");
   }
}
