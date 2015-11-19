public class Testi {

	public static void main(String[] args) {
		(new Thread(){
			public void run(){
				try {
					WorkDistributor.main(new String[]{"verbose"});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		SummausPalvelin x=new SummausPalvelin();
		x.start();
	}

}