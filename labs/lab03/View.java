public class View{
	//private int sessionID;
	private String productID;
	private int priceCost;


	public View( String productID, String priceCost){
			//this.sessionID = Integer.parseInt(sessionID);
			this.productID = productID;
			this.priceCost = Integer.parseInt(priceCost);
	}

	public void setProduct(String productID){this.productID = productID;}
	public String getProduct(){return productID;}

	public void setPriceCost(int print){this.priceCost = priceCost;}
	public int getPriceCost(){return this.priceCost;}




}