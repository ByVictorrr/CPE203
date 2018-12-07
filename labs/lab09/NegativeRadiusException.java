public class NegativeRadiusException extends CircleException{
	private double radius;
	public NegativeRadiusException(double radius){
		super("negative radius");
		this.radius = radius;
	}
	public double radius(){return radius;}
    

}
