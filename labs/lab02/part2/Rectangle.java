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

	public double perimeter(){
		
		double width = Math.abs((this.getBottomRight().getX()- this.getTopLeft().getX()));
                double height = Math.abs((this.getBottomRight().getY()-this.getTopLeft().getY()));
                return (2.0 * width + 2.0 * height);               
	}

}	
