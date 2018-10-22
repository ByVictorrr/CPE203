public class Buy{
//	private int sessionID;
	private String productID;
	private int priceCost;
	private int quanity;

	public Buy( String productID, String priceCost, String quanity){
			
			this.productID = productID;
			
			this.priceCost = Integer.parseInt(priceCost);
			
			this.quanity = Integer.parseInt(quanity);
	}


	public void setProduct(String productID){this.productID = productID;}
	public String getProduct(){return productID;}

	public void setPriceCost(int print){this.priceCost = priceCost;}
	public int getPriceCost(){return this.priceCost;}

	public void setQuanity(int quanity){this.quanity = quanity;}
	public int getQuanity(){return this.quanity;}



}