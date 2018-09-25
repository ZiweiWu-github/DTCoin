
public class DTCoinModel extends Thread{

	private CoinInfo coinInfo;
	private long startTime;
	private long endTime;
	private long nanoSecRemainingUntilSecond; 
	//remainingTime is used to measure amount of nanoseconds before 1 second has passed
	private final long nanoSecsPerSec = 1000000000;
	private int numSecs = 0;
	private final int secsPer15Min = 15* 60;
	private boolean count = false;
	
	public DTCoinModel(CoinInfo coinInfo) {
		this.coinInfo = coinInfo;
		this.nanoSecRemainingUntilSecond = this.nanoSecsPerSec;
	}
	
	public CoinInfo getCoinInfo() {
		return this.coinInfo;
	}
	
	public void Stop() {
		this.count = false;
	}
	
	public int getSecondsRemaining() {
		return this.secsPer15Min - this.numSecs;
	}
	
	public void Start() {
		count = true;
		startTime = System.nanoTime();
		this.start();
	}

	@Override
	public void run() {
		while(true) {
			if(count) {
				endTime = System.nanoTime();
				nanoSecRemainingUntilSecond -= (endTime - startTime);
				if(nanoSecRemainingUntilSecond <= 0) {
					numSecs++;
					System.out.println(numSecs);
					if(numSecs >= secsPer15Min) {
						coinInfo.addCoin();
						numSecs = 0;
					}
					nanoSecRemainingUntilSecond += nanoSecsPerSec;
				}
				startTime = endTime;
			}
		}
	}
	
}
