import java.io.Serializable;

public class CoinInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int amountOfCoins = 0;
	private long nanoSecRemainingUntilSecond = DTCoinModel.nanoSecsPerSec;
	private int numSecs = 0;
	
	public int getAmountOfCoins() {
		return this.amountOfCoins;
	}
	
	public void addCoin() {
		this.amountOfCoins++;
	}

	public long getNanoSecRemainingUntilSecond() {
		return nanoSecRemainingUntilSecond;
	}

	public void setNanoSecRemainingUntilSecond(long nanoSecRemainingUntilSecond) {
		this.nanoSecRemainingUntilSecond = nanoSecRemainingUntilSecond;
	}

	public int getNumSecs() {
		return numSecs;
	}

	public void setNumSecs(int numSecs) {
		this.numSecs = numSecs;
	}
}
