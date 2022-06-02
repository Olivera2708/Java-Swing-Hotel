package main;

import manage.ManageAll;
import window.MainFrame;

public class Main {
	public static void main(String[] args) {
		System.out.println("Podesavanje aplikacije ...");
		
		//ucitavamo sve podatke
		ManageAll manageAll = new ManageAll();
		manageAll.loadData();
		
		MainFrame main = new MainFrame();
		
	}
}