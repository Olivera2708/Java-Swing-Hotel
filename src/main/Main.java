package main;

import chart.PieChart30Dana;
import gui.MainFrame;
import manage.ManageAll;

public class Main {
	public static void main(String[] args) {
		System.out.println("Dobrodosli...");
		
		//ucitavamo sve podatke
		ManageAll manageAll = ManageAll.getInstance();
		manageAll.loadData();
		
		//pozivamo glavni prozor
		MainFrame main = new MainFrame();
		
	}
}