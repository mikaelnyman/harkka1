
public class SummausSaie extends Thread {
	private Summalista luvut;
	private int portti;
	
	public SummausSaie(Summalista lista, int portti) {
		this.luvut=lista;
		this.portti=portti;
	}

	public void start(){
		while(true){
			int a=kuunteleLuku();
			if(a==0){
				//lopeta säie
			}
			luvut.setUusiluku(a);
		}
	}

	private int kuunteleLuku() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
