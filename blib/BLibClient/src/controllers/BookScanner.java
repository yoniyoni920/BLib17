package controllers;

import gui.LendBookScreen;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

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