public class Testi {

	public static void main(String[] args) {
		try {
			WorkDistributor.main(new String[]{"verbose"});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SummausPalvelin.main(null);
	}

}