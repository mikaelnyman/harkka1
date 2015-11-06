
public class Summalista 
{
	//Tietokent‰t
	private String nimi = "";
	private int summa = 0;
	private int alkiot = 0;
	
	//Konstruktori, joka luo uuden Summalista-olion.
	//Parametrina annetaan Summalistan nimi.
	public Summalista(String y)
	{
		this.nimi = y;
		this.summa = 0;
		this.alkiot = 0;
	}
	
	//Metodi lis‰‰ parametrina annetun int tyypin luvun
	// listassa olevaan summaan ja kasvattaa lis‰ttyjen
	// lukujen lukum‰‰r‰‰ yhdell‰.
	public void SetUusiluku (int x)
	{
		this.summa = summa+x;
		this.alkiot = alkiot+1;
	}
	
	//Metodi palauttaa listassa olevan summan ja sinne
	// lis‰ttyjen lukujen lukum‰‰r‰n list-oliossa.
	//Palautettavan listan ensimm‰inen alkio on alkioiden
	//summa ja toinen alkio on alkioiden lukum‰‰r‰.s
	Public List<Int> GetSisalto()
	{
		java.util.List<Int> Arvot = new List<Int>;
		Arvot.add(this.summa);
		Arvot.add(this.alkiot);
		return Arvot;
	}

}
