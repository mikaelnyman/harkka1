public class SummausPalvelin {
	/*
	 * Koko palvelimen toiminnallinen runko
	 */
	public static void main(String...strings ){
		//Portti, johon muodostetaan UDP. Annettu tehtävänannossa.
		final int yPortti=3126;
		//Keksitty porttinumero, johon palvelin Y muodostaa TCP-yhteyden
		final int zPortti=2000;
		//Y:ltä saatava Summausäikeiden lukumäärä
		Summalista[] summaTaulukko=null;
		// Lista, jossa summauspalvelijat ovat
		SummausSaie[] ss=null;
		int t=0;

		int i=0;
		leima1 : while (i<5){
			i++;
			muodostaUDP(yPortti, zPortti);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long aika=System.currentTimeMillis();

			while(true){
				if(kuunteleTCPMuodostus()) break; // Jatkaa eteenpäin 
				if(System.currentTimeMillis()-aika>5000L){ //Odotetaan max 5 sec
					continue leima1;	// Uusi yritys
				}
			}
			t=kuunteleLuku();
			luoSumausPalvelijat(t, summaTaulukko, ss);
			lahetaPortit();
			while(true){
				int a=kuunteleLuku();
				if(a==1){		// Kokonaissumma
					int b=0;
					for(Summalista s : summaTaulukko){
						b+=s.getSisalto()[0];
					}
					// TODO Lähetä b Y:lle
				}
				else if(a==2){  // Säie, jolla suurin summa
					int c=0;
					int b=summaTaulukko[0].getSisalto()[0];
					for(int j=1; j<t; j++){
						if(summaTaulukko[j].getSisalto()[0]>b){
							b=summaTaulukko[j].getSisalto()[0];
							c=j;
						}
					}
					// TODO Lähetä i Y:lle (tai i:nnen säikeen nimi?)
				}
				else if(a==3){  // Kokonaismäärä
					int b=0;
					for(Summalista s : summaTaulukko){
						b+=s.getSisalto()[1];
					}
					// TODO Lähetä b Y:lle
				}
				else if(a==0){
					lopetaKokoPaska();
				}
			}
			// TODO Laske minuutteja ja jos minuutti kulunut, kutsu metodia lopetaKokoPaska()
		}
		//Y ei vastannut, lopetus hallitusti
		System.exit(-1);
	}
	
	/*
	 * Sulkee koko ohjelman, myös SummausSäikeet
	 */
	private static void lopetaKokoPaska() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Kuuntelee ja palauttaa kokonaisluvun TCP-yhteyden kautta
	 */
	private static int kuunteleLuku() {
		// TODO Auto-generated method stub
		return 0;
	}


	/*
	 * Luo t kappaletta Summauspalvelijoita ja jokaista palvelijaa kohdin summalistan
	 * 
	 */
	private static void luoSumausPalvelijat(int t, Summalista[] sl, SummausSaie[] ss) {
		// Jokaisella palvelijalla on summalista, joka on samalla listan sl alkio
		sl=new Summalista[t];
		for(int i=0; i<t; i++){
			sl[i]=new Summalista();
			ss[i]=new SummausSaie(sl[i], 2001+i);
			ss[i].start(); //Portit ovat 2001...2000+t
		}		
	}
	
	/*
	 * Lähettää palvelijoiden porttinumerot Y:lle
	 */
	private static void lahetaPortit() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * 
	 * @return true, jos yhteys onnistui, false, jos ei
	 */
	private static boolean kuunteleTCPMuodostus() {
		boolean b=false;
		// TODO runko
		return b;
	}

	/*
	 * Muodostaa UDP-yhteyden palvelimeen ja antaa sille portin, johon palvelimen pitää ottaa TCP-yhteys
	 * 
	 */
	private static void muodostaUDP(int yPortti, int zPortti){

	}
}