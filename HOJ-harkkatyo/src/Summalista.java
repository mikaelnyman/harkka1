
public class Summalista 
{
	//Tietokent‰t
	private int summa = 0;
	private int alkiot = 0;
	private boolean onAktiivinen = true;
	
	/*
	 * Konstruktori, joka luo uuden Summalista-olion.
	 */
	public Summalista()
	{
		this.summa = 0;
		this.alkiot = 0;
	}
	
	/*
	 * Metodi lis‰‰ parametrina annetun int tyypin luvun
	 * listassa olevaan summaan ja kasvattaa lis‰ttyjen
	 * lukujen lukum‰‰r‰‰ yhdell‰.
	 */
	public void setUusiluku(int x)
	{
		this.summa = summa+x;
		this.alkiot = alkiot+1;
	}
	
	/*
	 * Metodi palauttaa listassa olevan summan ja sinne lis‰ttyjen lukujen
	 * lukum‰‰r‰n int[]-taulukossa. Palautettavan listan ensimm‰inen alkio on
	 * alkioiden summa ja toinen alkio on alkioiden lukum‰‰r‰.
	 */
	public int[] getSisalto()
	{
		int[] Arvot = {this.summa,this.alkiot};
		return Arvot;
	}
	
    /*
     * Metodi muuttaa onAktiivinen booleanin arvon arvoksi false.
     */
	public void setAktiivisuus()
	{
		onAktiivinen = false;
	}
	
	/*
	 * Metodi palauttaa onAktiivinen booleanin arvon.
	 */
	public boolean getAktiivisuus()
	{
		return onAktiivinen;
	}
}


