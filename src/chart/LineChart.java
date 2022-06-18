package chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import entity.TipSobe;
import enums.EnumMeseci;
import manage.RezervacijeManager;

public class LineChart implements MainChart<XYChart>{
	RezervacijeManager rezervacijeManager;
	
	public LineChart(RezervacijeManager rezervacijeManager) {
		this.rezervacijeManager = rezervacijeManager;
	}
	
	public XYChart getChart() {
		XYChart chart = new XYChartBuilder().width(800).height(600).title("Prihodi po tipu sobe").build();
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
	    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
	    chart.getStyler().setDatePattern("yyyy-MMM");
	    
	    HashMap<TipSobe, LinkedHashMap<EnumMeseci, Integer>> maps = rezervacijeManager.getPrihodiPoTipu();
	    LinkedHashMap<EnumMeseci, Integer> ukupno = rezervacijeManager.getUkupno();
	    
	    Calendar cal123 = Calendar.getInstance();
		Date datumi = cal123.getTime();
		int danas_mesec = cal123.get(Calendar.MONTH) + 1;
		int danas_godina = cal123.get(Calendar.YEAR);
	    
	    for (TipSobe z: maps.keySet()) {
	    	LinkedHashMap<EnumMeseci, Integer> unutra = maps.get(z);
	    	List<Date> meseci = new ArrayList<Date>();
	    	List<Integer> zarada = new ArrayList<Integer>();
	    	for (EnumMeseci m: unutra.keySet()) {
	    		Calendar c = Calendar.getInstance();
	    		if (m.getValue() > danas_mesec) {
	    			c.set(Calendar.YEAR, danas_godina - 1);
	    		}
	    	    c.set(Calendar.MONTH, m.getValue());
	    	    c.set(Calendar.DAY_OF_MONTH, 1);
	    	    c.set(Calendar.HOUR_OF_DAY, 0);
	    	    c.set(Calendar.MINUTE, 0);
	    	    c.set(Calendar.SECOND, 0);
	    	    c.set(Calendar.MILLISECOND, 0);
	    	    Date datum = c.getTime();
	    		meseci.add(datum);
	    		zarada.add(unutra.get(m));
	    	}
	    	chart.addSeries(z.getTip(), meseci, zarada);
	    }
	    
	    List<Date> meseci = new ArrayList<Date>();
    	List<Integer> zarada = new ArrayList<Integer>();
	    for (EnumMeseci m: ukupno.keySet()) {
	    	Calendar c = Calendar.getInstance();
    		if (m.getValue() > danas_mesec) {
    			c.set(Calendar.YEAR, danas_godina - 1);
    		}
    	    c.set(Calendar.MONTH, m.getValue());
    	    c.set(Calendar.DAY_OF_MONTH, 0);
    	    c.set(Calendar.HOUR_OF_DAY, 0);
    	    c.set(Calendar.MINUTE, 0);
    	    c.set(Calendar.SECOND, 0);
    	    c.set(Calendar.MILLISECOND, 0);
    	    Date datum = c.getTime();
    		meseci.add(datum);
    		zarada.add(ukupno.get(m));
	    }
	    chart.addSeries("Ukupno", meseci, zarada);
	    
	    return chart;
	}
	
}