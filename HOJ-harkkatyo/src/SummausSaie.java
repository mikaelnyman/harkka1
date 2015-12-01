import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SummausSaie extends Thread {
	/**
	 * Tietokentät
	 */
	private Summalista luvut;
	private int portti;
	private Socket lukuportti;
	private ObjectInputStream lukuVirta;
	private int luku;
	private boolean aktiivinen;
	
	/**
	 * Konstruktori
	 * @param lista
	 * @param portti
	 */
	public SummausSaie(Summalista lista, int portti) {
		this.luvut = lista;
		this.portti = portti;
		 aktiivinen = true;
	}

	/**
	 * Kuuntelle lukuja Y:ltä ja tallentaa ne listaan.
	 */
	@Override
	public void run(){
		try {
			ServerSocket soketti=new ServerSocket(portti);
			lukuportti=soketti.accept();
			soketti.close();
			lukuVirta=new ObjectInputStream(lukuportti.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int a=0;
		while(true){
			aktiivinen = luvut.getAktiivisuus();
			try{
				a=kuunteleLuku();
			}
			catch (SocketException e){
				aktiivinen=false;
			}
			if((a==0)||(!aktiivinen)){
				try {
					lukuVirta.close();
					lukuportti.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;	//lopeta säie
			}
			luvut.setUusiluku(a);
		}
	}
	
	/**
	 * Kuuntelee ja palauttaa luvun.
	 * @return luku, joka saadaan Y:ltä
	 * @throws SocketException, jos Y:n puolella tapahtuu virhe, eikä lukua tule
	 */
	private int kuunteleLuku() throws SocketException {
			try {
				luku=(int)lukuVirta.readInt();
			} catch (IOException e) {
				throw new SocketException();
			}
		return luku;
	}

}
