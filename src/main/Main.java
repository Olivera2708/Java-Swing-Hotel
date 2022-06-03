package main;

import gui.MainFrame;
import manage.ManageAll;

public class Main {
	public static void main(String[] args) {
		System.out.println("Dobrodosli...");
		
		//ucitavamo sve podatke
		ManageAll manageAll = new ManageAll();
		manageAll.loadData();
		
		//pozivamo glavni prozor
		MainFrame main = new MainFrame();
		
	}
}