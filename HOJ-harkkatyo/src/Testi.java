public class Testi {

	public static void main(String[] args) {
		(new Thread(){
			public void run(){
				try {
					WorkDistributor.main(new String[]{"verbose"});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		SummausPalvelin x=new SummausPalvelin();
		x.start();
	}

}