package controllers;

public class BookScanner {
	private BookScanner(){

	}

	private static BookScanner instance = new BookScanner();
	public static BookScanner getInstance(){
		if(instance == null){
			instance = new BookScanner();
		}
		return instance;
	}

	public String Scan() throws InterruptedException {
		Thread.sleep(1000);
		return "1";
	}
}