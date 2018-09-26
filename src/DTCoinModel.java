import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class DTCoinModel implements Runnable {

	private CoinInfo coinInfo;
	private long startTime;
	private long endTime;
	private long nanoSecRemainingUntilSecond;
	public final static long nanoSecsPerSec = 1000000000;
	private int numSecs;
	private final int secsPer15Min = 15 * 60;
	private boolean count = false;
	private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	private DTCoinStatus status = DTCoinStatus.PAUSED;
	private boolean isRunning = true;

	public DTCoinModel(CoinInfo coinInfo) {
		this.coinInfo = coinInfo;
	}

	/*
	 * Below methods are getters
	 */
	public CoinInfo getCoinInfo() {
		return this.coinInfo;
	}

	public int getSecondsRemaining() {
		return this.secsPer15Min - this.numSecs;
	}
	
	public DTCoinStatus getDTCoinStatus() {
		return this.status;
	}
	
	/*
	 * Below methods are to add/remove/inform change listeners
	 */
	
	public void addChangeListener(ChangeListener c) {
		this.listeners.add(c);
	}
	
	public void removeChangeListener(ChangeListener c) {
		this.listeners.remove(c);
	}
	
	public void informListeners() {
		for(ChangeListener c: this.listeners) {
			c.stateChanged(new ChangeEvent(this));
		}
	}
	

	/*
	 * Below methods are to stop/start/pause/resume the counting
	 */

	
	public void Pause() {
		count = false;
		this.status = DTCoinStatus.PAUSED;
		this.informListeners();
	}	
	
	public void Stop() {
		this.isRunning = false;
		this.status = DTCoinStatus.PAUSED;
		this.coinInfo.setNanoSecRemainingUntilSecond(this.nanoSecRemainingUntilSecond);
		this.coinInfo.setNumSecs(this.numSecs);
	}
	
	public void Start() {
		this.isRunning = true;
		this.count = false;
		this.status = DTCoinStatus.PAUSED;
		this.nanoSecRemainingUntilSecond = this.coinInfo.getNanoSecRemainingUntilSecond();
		this.numSecs = this.coinInfo.getNumSecs();
		this.informListeners();
	}
	
	public synchronized void Resume() {
		this.startTime = System.nanoTime();
		this.count = true;
		this.status = DTCoinStatus.COUNTING;
		this.notifyAll();
		this.informListeners();
	}

	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (this.isRunning) {
			synchronized (this) {
				if(!this.isRunning) {
					break;
				}
				if (!count) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						break;
					}
					if(!this.isRunning) {
						break;
					}
				}
			}
			endTime = System.nanoTime();
			nanoSecRemainingUntilSecond -= (endTime - startTime);
			if (nanoSecRemainingUntilSecond <= 0) {
				numSecs++;
				if (numSecs >= secsPer15Min) {
					coinInfo.addCoin();
					numSecs = 0;
				}
				nanoSecRemainingUntilSecond += nanoSecsPerSec;
				this.informListeners();
			}
			startTime = endTime;
		}
	}
}

enum DTCoinStatus{
	COUNTING, PAUSED;
}
