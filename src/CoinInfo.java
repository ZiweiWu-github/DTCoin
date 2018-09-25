import java.io.Serializable;

public class CoinInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int amountOfCoins = 0;
	
	public int getAmountOfCoins() {
		return this.amountOfCoins;
	}
	
	public void addCoin() {
		this.amountOfCoins++;
	}
}
