public class SummausSaie extends Thread {
	/*
	 * Tietokent�t
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
				return;//lopeta s�ie
			}
			luvut.setUusiluku(a);
		}
	}

	private int kuunteleLuku() {
		// TODO voi kopioida summauspalvelimesta
		return 0;
	}

}
