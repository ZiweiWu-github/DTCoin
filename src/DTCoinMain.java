import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class DTCoinMain {
	private static String fileName = "coin.dat";
	private static CoinInfo coinInfo;
	
	public static void main(String[] args) throws InterruptedException{
		try {
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream stream = new ObjectInputStream(file);
			coinInfo = (CoinInfo) stream.readObject();
			stream.close();
		} catch (Exception e) {
			coinInfo = new CoinInfo();
		}
		DTCoinModel m = new DTCoinModel(coinInfo);
		DTCoinFrame frame = new DTCoinFrame(m);
		frame.refresh();
		Thread t= new Thread(m);
		
		t.start();
	}
	
	public static void close() {
		try {
			FileOutputStream file = new FileOutputStream(fileName);
			ObjectOutputStream stream = new ObjectOutputStream(file);
			stream.writeObject(coinInfo);
			stream.close();
		} catch (Exception e) {
		}
	}
}
