public class Buy{
//	private int sessionID;
	private String productName;
	private int priceCost;
	private int quanity;

	public Buy( String productName, String priceCost, String quanity){
			
			this.productName = productName;
			
			this.priceCost = Integer.parseInt(priceCost);
			
			this.quanity = Integer.parseInt(quanity);
	}



}