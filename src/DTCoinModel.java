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
	private Object lockObject = new Object();

	public DTCoinModel(CoinInfo coinInfo) {
		this.coinInfo = coinInfo;
		this.nanoSecRemainingUntilSecond = this.coinInfo.getNanoSecRemainingUntilSecond();
		this.numSecs = this.coinInfo.getNumSecs();
	}

	/*
	 * Below methods are getters
	 */
	public CoinInfo getCoinInfo() {
		return this.coinInfo;
	}

	/**
	 * Returns a string representing the amount of time left before the Lord graces
	 * you with an imaginary coin of which is worth absolutely nothing.
	 * @return Time left as a String
	 */
	public String getRemainingTime() {
		int secsRemaining = this.secsPer15Min - this.numSecs;
		int minutes = secsRemaining/60;
		int seconds = secsRemaining%60;
		String s = "";
		if(minutes == 1) {
			s += minutes + " Minute ";
		}else {
			s += minutes + " Minutes ";
		}
		if(seconds == 1) {
			s += seconds + " Second";
		}
		else {
			s += seconds + " Seconds";
		}
		return s;
	}
	
	public DTCoinStatus getDTCoinStatus() {
		return this.status;
	}
	
	/*
	 * Below methods are to add/inform change listeners
	 */
	
	public void addChangeListener(ChangeListener c) {
		this.listeners.add(c);
	}
	
	public void informListeners() {
		for(ChangeListener c: this.listeners) {
			c.stateChanged(new ChangeEvent(this));
		}
	}
	

	/*
	 * Below methods are to stop/start/pause/resume the counting
	 */

	//Pauses the counting
	public void Pause() {
		count = false;
		this.status = DTCoinStatus.PAUSED;
		this.informListeners();
	}
	
	/**
	 * For use with the frame closing -- stops counting but
	 * without informing the change listeners.
	 */
	public void PauseWithoutListeners() {
		count = false;
	}
	
	/*
	 * Stops the run() method by setting isRunning to false
	 * Then saves info into coinInfo
	 * The notify is to be able to exit when the run() is waiting
	 */
	public void Stop() {
		synchronized (lockObject) {
			this.isRunning = false;
			this.coinInfo.setNanoSecRemainingUntilSecond(this.nanoSecRemainingUntilSecond);
			this.coinInfo.setNumSecs(this.numSecs);
			DTCoinMain.close();
			this.lockObject.notifyAll();
		}
		
	}
	
	
	/*
	 * Resumes the counting and notifies all who are waiting on this instance
	 */
	public void Resume() {
		synchronized (lockObject) {
			this.startTime = System.nanoTime();
			this.count = true;
			this.status = DTCoinStatus.COUNTING;
			this.informListeners();
			this.lockObject.notifyAll();
		}
	}
	
	/**
	 * For use with the frame closing. Resumes the counting without having to
	 * inform the change listeners.
	 */
	public void ResumeWithoutListeners() {
		synchronized (lockObject) {
			this.startTime = System.nanoTime();
			this.count = true;
			this.status = DTCoinStatus.COUNTING;
			this.lockObject.notifyAll();
		}
	}

	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (this.isRunning) {
			synchronized (this.lockObject) {
				if(!this.isRunning) {
					break;
				}
				if (!count) {
					try {
						this.lockObject.wait();
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
