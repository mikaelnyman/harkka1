
public class Summalista 
{
	//Tietokent‰t
	private int summa = 0;
	private int alkiot = 0;
	
	//Konstruktori, joka luo uuden Summalista-olion.
	public Summalista()
	{
		this.summa = 0;
		this.alkiot = 0;
	}
	
	//Metodi lis‰‰ parametrina annetun int tyypin luvun
	// listassa olevaan summaan ja kasvattaa lis‰ttyjen
	// lukujen lukum‰‰r‰‰ yhdell‰.
	public void setUusiluku(int x)
	{
		this.summa = summa+x;
		this.alkiot = alkiot+1;
	}
	
	//Metodi palauttaa listassa olevan summan ja sinne
	// lis‰ttyjen lukujen lukum‰‰r‰n ArrayList-oliossa.
	//Palautettavan listan ensimm‰inen alkio on alkioiden
	//summa ja toinen alkio on alkioiden lukum‰‰r‰.
	public int[] getSisalto()
	{
		int[] Arvot = {this.summa,this.alkiot};
		return Arvot;
	}

}


