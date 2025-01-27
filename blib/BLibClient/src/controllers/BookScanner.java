package controllers;

import java.util.Random;

/**
 * The BookScanner class simulates the behavior of a book scanning device.
 * This class follows the Singleton design pattern to ensure a single instance.
 */
public class BookScanner {
	private BookScanner(){

	}

    /**
     * The single instance of the BookScanner.
     */
	private static BookScanner instance = new BookScanner();
	
	 /**
     * Provides access to the singleton of the BookScanner.
     * 
     * @return the single instance of BookScanner
     */
	public static BookScanner getInstance(){
		if(instance == null){
			instance = new BookScanner();
		}
		return instance;
	}

	 /**
     * Simulates the scanning of a book. as the scanner supposed to be  an outer software were only simulating it.
     * 
     * @return a simulated scan result as a string
     * @throws InterruptedException if the thread is interrupted while simulating the scan
     */
	public String Scan() throws InterruptedException {
		Thread.sleep(1000);
		
		Random rn = new Random();
		int BookID = rn.nextInt(9) + 1;
		
		return String.valueOf(BookID);
	}
}