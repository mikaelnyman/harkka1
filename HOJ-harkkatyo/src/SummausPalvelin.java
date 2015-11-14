import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SummausPalvelin {
	/*
	 * Koko palvelimen toiminnallinen runko
	 */
	public static void main(String...strings ){
		//Portti, johon muodostetaan UDP. Annettu teht‰v‰nannossa.
		final int yPortti=3126;
		//Keksitty porttinumero, johon palvelin Y muodostaa TCP-yhteyden
		final int zPortti=2000;
		//Y:lt‰ saatava Summaus‰ikeiden lukum‰‰r‰
		Summalista[] summaTaulukko=null;
		// Lista, jossa summauspalvelijat ovat
		SummausSaie[] ss=null;
		Socket asiakasSoketti=null;
		ObjectOutputStream oVirta=null;
		ObjectInputStream iVirta=null;
		int t=0;

		int i=0;
		leima1 : while (i<5){ //yritet‰‰n viisi kertaa
			i++;
			muodostaUDP(yPortti, zPortti);
			try {							// Odotetaan 1 sec
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				asiakasSoketti=kuunteleTCPMuodostus(zPortti, oVirta, iVirta);
			} catch (SocketException e) {
				continue leima1; // aikaraja meni, yritet‰‰n uudelleen, jos viel‰ kertoja j‰ljell‰
			}
			t=kuunteleLuku(asiakasSoketti, oVirta, iVirta, 5000, 2, 10);
			if (t==-1){ //ei tullut oikeaa lukua oikeassa ajassa
				//TODO Y:lle -1 ja hallittu lopetus t‰lle sovellukselle
			}
			luoSummausPalvelijat(t, summaTaulukko, ss);
			lahetaPortit();
			while(true){
				int a=kuunteleLuku(asiakasSoketti, oVirta, iVirta, 10000, 0, 3);
				if(a==1){		// Kokonaissumma
					int b=0;
					for(Summalista s : summaTaulukko){
						b+=s.getSisalto()[0];
					}
					// TODO L‰het‰ b Y:lle
				}
				else if(a==2){  // S‰ie, jolla suurin summa
					int c=0;
					int b=summaTaulukko[0].getSisalto()[0];
					for(int j=1; j<t; j++){
						if(summaTaulukko[j].getSisalto()[0]>b){
							b=summaTaulukko[j].getSisalto()[0];
							c=j;
						}
					}
					// TODO L‰het‰ i Y:lle (tai i:nnen s‰ikeen nimi?)
				}
				else if(a==3){  // Kokonaism‰‰r‰
					int b=0;
					for(Summalista s : summaTaulukko){
						b+=s.getSisalto()[1];
					}
					// TODO L‰het‰ b Y:lle
				}
				else if(a==0 || a== -1){
					lopetaKokoPaska();
				}
			}
		}
		//Y ei vastannut, lopetus hallitusti
		System.exit(-1);
	}
	
	/*
	 * Sulkee koko ohjelman, myˆs SummausS‰ikeet
	 */
	private static void lopetaKokoPaska() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Kuuntelee ja palauttaa TCP-yhteyden kautta saadun kokonaisluvun.
	 * Jos aikarajan kuluessa ei tule kokonaislukua annetulta v‰lilt‰ p‰‰tepisteet mukaan lukien,
	 * palautetaan -1.
	 */
	private static int kuunteleLuku(Socket soketti, ObjectOutputStream oVirta, ObjectInputStream iVirta, int aikaraja, int minimi, int maksimi) {
		int luku=-1;
		try {
			soketti.setSoTimeout(aikaraja);
			while (!(minimi<=luku && maksimi>=luku)) {
				luku=(int)iVirta.readInt();
			}
		} catch (SocketTimeoutException e1){ //Aika loppu
			return -1;
		}
		catch (IOException e) { //Odottematon virhe, mit‰ tehd‰?
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO oliovirrat ja niiden k‰sittely
		return luku;
	}


	/*
	 * Luo t kappaletta Summauspalvelijoita ja jokaista palvelijaa kohdin summalistan
	 * Kaikkien summalista on yhteisen summalistataulukon alkio
	 * Portit ovat 2001...2001+t
	 */
	private static void luoSummausPalvelijat(int t, Summalista[] sl, SummausSaie[] ss) {
		// Jokaisella palvelijalla on summalista, joka on samalla listan sl alkio
		sl=new Summalista[t];
		for(int i=0; i<t; i++){
			sl[i]=new Summalista();
			ss[i]=new SummausSaie(sl[i], 2001+i);
			ss[i].start(); //Portit ovat 2001...2000+t
		}		
	}
	
	/*
	 * L‰hett‰‰ palvelijoiden porttinumerot 2001...2001+t Y:lle
	 */
	private static void lahetaPortit() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Muodostaa Socket-olion ja palauttaa sen, jos tulee parametrina annettuun porttiin
	 * TCP-yhteydenotto
	 * @throws SocketEception jos viiden sekunnin aikana ei tule pyyntˆ‰
	 * @return Kuunneltu asiakassoketti
	 */
	private static Socket kuunteleTCPMuodostus(int portti, ObjectOutputStream oVirta, ObjectInputStream iVirta) throws SocketException {
		try {
			ServerSocket soketti=new ServerSocket(portti);
			soketti.setSoTimeout(5000);
			Socket asiakasSoketti=soketti.accept();
			soketti.close();
			oVirta=new ObjectOutputStream(asiakasSoketti.getOutputStream());
			iVirta=new ObjectInputStream(asiakasSoketti.getInputStream());
			return asiakasSoketti;
		} 
		catch (SocketException s){
			throw new SocketException("Aikaraja meni jo");
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	/*
	 * Muodostaa UDP-yhteyden palvelimeen ja
	 * antaa sille portin, johon palvelimen pit‰‰ ottaa TCP-yhteys
	 */
	private static void muodostaUDP(int yPortti, int zPortti){
		try {
			DatagramSocket udp=new DatagramSocket(zPortti);
			byte[] tavu=new byte[]{(byte) zPortti};
			InetAddress omaIP=InetAddress.getLocalHost();
			DatagramPacket paketti=new DatagramPacket(tavu, tavu.length, omaIP, yPortti);
			udp.send(paketti);
			udp.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}