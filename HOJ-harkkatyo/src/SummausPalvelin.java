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
		Summalista[] sl=null;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long aika=System.currentTimeMillis();

			while(true){
				if(kuunteleTCP()) break; // Jatka eteenp‰in 
				if(System.currentTimeMillis()-aika>5000L){ //Odotetaan max 5 sec
					continue leima1;	// Uusi yritys
				}
			}
			//TODO TCP-oliolta saadaan muuttujalle t arvo
			luoSumausPalvelijat(t, sl, ss);
			lahetaPortit();
			while(true){
				int a=kuuntele();
				if(a==1){		// Kokonaissumma
					int b=0;
					for(Summalista s : sl){
						b+=s.getSisalto()[0];
					}
					// TODO L‰het‰ b Y:lle
				}
				else if(a==2){  // S‰ie, jolla suurin summa
					int c=0;
					int b=sl[0].getSisalto()[0];
					for(int j=1; j<t; j++){
						if(sl[j].getSisalto()[0]>b){
							b=sl[j].getSisalto()[0];
							c=j;
						}
					}
					// TODO L‰het‰ i Y:lle (tai i:nnen s‰ikeen nimi?)
				}
				else if(a==3){  // Kokonaism‰‰r‰
					int b=0;
					for(Summalista s : sl){
						b+=s.getSisalto()[1];
					}
					// TODO L‰het‰ b Y:lle
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
	
	
	private static void lopetaKokoPaska() {
		// TODO Auto-generated method stub
		
	}


	private static int kuuntele() {
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
	 * L‰hett‰‰ palvelijoiden porttinumerot Y:lle
	 */
	private static void lahetaPortit() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * 
	 * @return true, jos yhteys onnistui, false, jos ei
	 */
	private static boolean kuunteleTCP() {
		boolean b=false;
		// TODO runko
		return b;
	}

	/*
	 * Muodostaa UDP-yhteyden palvelimeen ja antaa sille portin, johon palvelimen pit‰‰ ottaa TCP-yhteys
	 * 
	 */
	private static void muodostaUDP(int yPortti, int zPortti){

	}
}