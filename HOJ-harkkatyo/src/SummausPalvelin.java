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

public class SummausPalvelin extends Thread {
	/*
	 * Tietokent‰t
	 */
	// Portti, johon muodostetaan UDP. Annettu teht‰v‰nannossa.
	private final int YPORTTI;
	// Keksitty porttinumero, johon palvelin Y muodostaa TCP-yhteyden
	private final int ZPORTTI;
	// Y:lt‰ saatava Summaus‰ikeiden lukum‰‰r‰
	private Summalista[] summaTaulukko;
	// Lista, jossa summauspalvelijat ovat
	private SummausSaie[] ss;
	// Yhteys Y:hyn TCP:t‰ muodostettaessa
	private Socket asiakasSoketti;
	// X:lt‰ Y:lle
	private ObjectOutputStream oVirta;
	// Y:lt‰ X:lle
	private ObjectInputStream iVirta;
	// Y:lt‰ saatava s‰ikeiden lukum‰‰r‰
	private int t;
	
	/*
	 * Konstruktori
	 */
	public SummausPalvelin(){
		YPORTTI=3126;
		ZPORTTI=2000;
	}
	
	/*
	 * Koko palvelimen toiminnallinen runko
	 */
	@Override
	public void run(){
		int i=0;
		while (i<5){ //yritet‰‰n viisi kertaa
			i++;
			muodostaUDP();
			try {							// Odotetaan 1 sec
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				kuunteleTCPMuodostus();
			} catch (SocketException e) {
				continue; 					// aikaraja meni, yritet‰‰n uudelleen, jos viel‰ kertoja j‰ljell‰
			}
			long time1=System.currentTimeMillis();
			while(t<2 || t>10){
				long time2=System.currentTimeMillis();
				int aikaraja=5000-(int)(time2-time1);
				if(aikaraja>0){
					try{
						t=kuunteleLuku(aikaraja); 		// Y:n pit‰isi l‰hett‰‰ palvelijoiden lukum‰‰r‰
					}
					catch(SocketTimeoutException ste){	// ei tullut oikeaa lukua oikeassa ajassa
						lahetaLuku(-1); 	// Y:lle -1
						lopetaKokoPaska(); 	// Hallittu lopetus
					}
				}
				else{						// ei tullut oikeaa lukua oikeassa ajassa
					lahetaLuku(-1); 		// Y:lle -1
					lopetaKokoPaska(); 		// Hallittu lopetus
				}
			}
//			System.out.println("Palvelijoiden lukum‰‰r‰ "+t);
			luoSummausPalvelijat();
			for (int p=2001; p<2001+t; p++){ 	// Porttien l‰hetys Y:lle
				lahetaLuku(p);
			}
			while(true){
				try{
					int a=kuunteleLuku(60000); 	// Y:n kysely, aikaraja 1 min
					if(a==1){					// Kokonaissumma
						int b=0; 				// v‰lisuumma, johon lis‰t‰‰n jokaisen s‰ikeen summa
						synchronized (summaTaulukko) {
							for(Summalista s : summaTaulukko){
								b+=s.getSisalto()[0];
							}
						}
						lahetaLuku(b);
					}
					else if(a==2){  			// S‰ie, jolla suurin summa
	//					System.out.println("Kysyt‰‰n s‰iett‰, jolla suurin summa.");
	//					tulosta();
						int c=0;
						synchronized (summaTaulukko) {
							int b=summaTaulukko[0].getSisalto()[0];
							for(int j=1; j<t; j++){
								if(summaTaulukko[j].getSisalto()[0]>b){
									b=summaTaulukko[j].getSisalto()[0];
									c=j;
								}
							}
						}
	//					System.out.println("S‰ie "+(c+1));
						lahetaLuku(c+1); 		// Y:lle s‰ikeen indeksi+1
					}
					else if(a==3){  // Kokonaism‰‰r‰
						int b=0;
						synchronized (asiakasSoketti) {
							for(Summalista s : summaTaulukko){
								b+=s.getSisalto()[1];
							}
						}
						lahetaLuku(b);
					}
					else if(a==0){		// Y k‰skee lopettaa
						lopetaKokoPaska();
					}
					else{
						lahetaLuku(-1); // Jos luku on joku muu, Y:lle -1
					}
				}
				catch(SocketTimeoutException ste){  // Aikaraja meni, lopetetaan
					lopetaKokoPaska();
				}
			}
		}
		//Y ei vastannut, lopetus hallitusti
		System.exit(-1);
	}
	
	/*
	 * Muodostaa UDP-yhteyden palvelimeen ja
	 * antaa sille portin, johon palvelimen pit‰‰ ottaa TCP-yhteys
	 */
	private void muodostaUDP(){
		try {
			DatagramSocket udp=new DatagramSocket();
			byte[] tavu=(""+ZPORTTI).getBytes();
//			for (byte b : tavu){
//				System.out.println("Tavu on "+b);
//			}
			InetAddress omaIP=InetAddress.getLocalHost();
			DatagramPacket paketti=new DatagramPacket(tavu, tavu.length, omaIP, YPORTTI);
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

	/*
	 * Luo t kappaletta Summauspalvelijoita ja jokaista palvelijaa kohdin summalistan
	 * Kaikkien summalista on yhteisen summalistataulukon alkio
	 * Portit ovat 2001...2001+t
	 */
	private void luoSummausPalvelijat() {
		// Jokaisella palvelijalla on summalista, joka on samalla listan summaTaulukko alkio
		summaTaulukko=new Summalista[t];
		ss=new SummausSaie[t];
		for(int i=0; i<t; i++){
			summaTaulukko[i]=new Summalista();
			ss[i]=new SummausSaie(summaTaulukko[i], 2001+i);
			ss[i].start();
		}		
	}

	/*
	 * Muodostaa Socket-olion ja , jos tulee parametrina annettuun porttiin
	 * TCP-yhteydenotto
	 * @throws SocketEception jos viiden sekunnin aikana ei tule pyyntˆ‰
	 */
	private void kuunteleTCPMuodostus() throws SocketException {
		try {
			ServerSocket soketti=new ServerSocket(ZPORTTI);
			soketti.setSoTimeout(5000);
			asiakasSoketti=soketti.accept();
			soketti.close();
			oVirta=new ObjectOutputStream(asiakasSoketti.getOutputStream());
			iVirta=new ObjectInputStream(asiakasSoketti.getInputStream());
		} 
		catch (SocketException s){
			throw new SocketException("Aikaraja meni jo");
		}
		catch (IOException e1) {
			// TODO mit‰ tapahtuu
			e1.printStackTrace();
		}
	}

	/*
	 * Kuuntelee ja palauttaa TCP-yhteyden kautta saadun kokonaisluvun.
	 * Jos aikarajan kuluessa ei tule kokonaislukua, heitet‰‰n poikkeus
	 * 
	 * @param aikaraja: aika millisekunteina, joka odotetaan kunnes lopetetaan
	 * @return TCP-yhteyden kautta saatu kokonaisluku
	 * @throw SocketTimeoutException jos aikaraja ylittyi
	 */
	private int kuunteleLuku(int aikaraja) throws SocketTimeoutException {
		int luku=-1;		// T‰t‰ ei koskaan palauteta
		try {
			asiakasSoketti.setSoTimeout(aikaraja);
			luku=(int)iVirta.readInt();
		}
		catch (SocketTimeoutException e1){ //Aika loppu
			throw new SocketTimeoutException("Aika loppu");
		}
		catch (IOException e) { //Odottematon virhe, mit‰ tehd‰?
			// TODO ratkaisu
			e.printStackTrace();
			lopetaKokoPaska();
		}
		return luku;
	}


	/*
	 * L‰hett‰‰ kokonaisluvun Y:lle.
	 * K‰ytt‰‰ TCP-yhteytt‰
	 * @param luku l‰hetett‰v‰ kokonaisluku
	 */
	private void lahetaLuku(int luku) {
		try {
			oVirta.writeInt(luku);
			oVirta.flush();
		} catch (IOException e) {
			// TODO Mik‰ on ongelma? Mik‰ ratkaisu?
			e.printStackTrace();
		}
	}

	/*
	 * Sulkee koko ohjelman.
	 * Myˆs SummausS‰ikeet, jos niit‰ on viel‰ elossa
	 */
	private void lopetaKokoPaska() {
		if (!(ss==null)){
			for (Summalista s : summaTaulukko){
				s.setAktiivisuus();
			}
		}
		try{
			oVirta.close();
			iVirta.close();
			asiakasSoketti.close();
		} catch (IOException e){
			//TODO mit‰ tehd‰
		}
		System.exit(0);		
	}
	
	/*
	 * Tulostaa tilantaan
	 */
//	public void tulosta(){
//		for (int i=0;i<t;i++){
//			System.out.println("Summa: "+summaTaulukko[i].getSisalto()[0]);
//			System.out.println("Lukum‰‰r‰: "+summaTaulukko[i].getSisalto()[1]);
//		}
//	}
}