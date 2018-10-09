public class Rectangle{

	private Point topLeft, bottomRight;

	Rectangle(Point point1, Point point2){
		topLeft = point1;
		bottomRight = point2;
	}

	public Point getTopLeft()
	{
	return topLeft;
	}
	public Point getBottomRight()
	{
	return bottomRight;
	}

}	
