import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SummausSaie extends Thread {
	/*
	 * Tietokentät
	 */
	//TODO soketit, ObjektInput(Output)Stream
	private Summalista luvut;
	private int portti;
	private Socket lukuportti = null;
	private ObjectInputStream lukuVirta = null;
	private int luku;
	
	
	public SummausSaie(Summalista lista, int portti) {
		this.luvut=lista;
		this.portti=portti;
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
		while(true){
			int a=kuunteleLuku();
			if(a==0){
				try {
					lukuVirta.close();
					lukuportti.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;//lopeta säie
			}
			luvut.setUusiluku(a);
		}
	}

	private int kuunteleLuku() {
			try {
				luku=(int)lukuVirta.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return luku;
	}

}
