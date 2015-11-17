public class SummausSaie extends Thread {
	/*
	 * Tietokentät
	 */
	//TODO soketit, ObjektInput(Output)Stream
	private Summalista luvut;
	private int portti;
	
	public SummausSaie(Summalista lista, int portti) {
		this.luvut=lista;
		this.portti=portti;
	}

	public void run(){
		while(true){
			int a=kuunteleLuku();
			if(a==0){
				return;//lopeta säie
			}
			luvut.setUusiluku(a);
		}
	}

	private int kuunteleLuku() {
		// TODO voi kopioida summauspalvelimesta
		return 0;
	}

}
