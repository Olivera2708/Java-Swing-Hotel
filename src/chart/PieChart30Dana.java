package chart;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;

import entity.Zaposleni;
import manage.RezervacijeManager;
import manage.SobeManager;

public class PieChart30Dana implements MainChart<PieChart>{
	SobeManager sobeManager;
	RezervacijeManager rezervacijeManager;
	boolean sobarice;
	
	public PieChart30Dana(SobeManager sobeManager, RezervacijeManager rezervacijeManager, boolean sobarice) {
		this.sobarice = sobarice;
		this.rezervacijeManager = rezervacijeManager;
		this.sobeManager = sobeManager;
	}
	
	public PieChart getChart() {
		String title = "";
		if (sobarice) {
			title = "Opterećenje sobarica u prethodnih 30 dana";
		}
		else {
			title = "Status reervacija u prethodnih 30 dana";
		}
		
		PieChart chart = new PieChartBuilder().width(800).height(600).title(title).build();
	    
	    Date danas = new Date();
	    Date pocetni = new Date(danas.getTime() - 30 * (1000 * 60 * 60 * 24));
	    if (sobarice) {
	    	HashMap<Zaposleni, Integer> maps = sobeManager.getBrojSobaPoSobarici(pocetni, danas);
		    
		    for (Zaposleni z: maps.keySet()) {
		    	chart.addSeries(z.getIme() + " " + z.getPrezime(), maps.get(z));
		    }
	    }
	    else {
	    	HashMap<String, Integer> maps = rezervacijeManager.prikazPoTipu(pocetni, danas);
	    	for (String s: maps.keySet()) {
		    	chart.addSeries(s, maps.get(s));
		    }
	    }
	    
	    return chart;
	}
	
}