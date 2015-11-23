import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SummausSaie extends Thread {
	/*
	 * Tietokentät
	 */
	private Summalista luvut;
	private int portti;
	private Socket lukuportti;
	private ObjectInputStream lukuVirta;
	private int luku;
	private boolean aktiivinen;
	
	public SummausSaie(Summalista lista, int portti) {
		this.luvut = lista;
		this.portti = portti;
		 aktiivinen = true;
	}

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

	private int kuunteleLuku() throws SocketException {
			try {
				luku=(int)lukuVirta.readInt();
			} catch (IOException e) {
//				System.out.println("Säie sulkeutuu...");
				throw new SocketException();
			}
		return luku;
	}

}
