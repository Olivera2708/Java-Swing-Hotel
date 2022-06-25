package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import chart.LineChart;
import chart.PieChart30Dana;
import manage.ManageAll;

public class AdministratorChart extends JFrame {
	private static final long serialVersionUID = 1L;
	ManageAll manageAll = ManageAll.getInstance();
	int broj;
	
	public AdministratorChart (int broj) {
		this.broj = broj;
		this.setTitle("Grafik");
		this.setPreferredSize(new Dimension(800, 700));
		this.setResizable(false);
		ChartGUI();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void ChartGUI() {
		if (broj == 1) {
			PieChart30Dana sobarica = new PieChart30Dana(manageAll.getSobeManager(), manageAll.getRezervacijeManager(), true);
			PieChart chart = sobarica.getChart();
			JPanel chartPanel = new XChartPanel<PieChart>(chart);
			this.getContentPane().add(chartPanel, BorderLayout.CENTER);
		}
		else if (broj == 2) {
			PieChart30Dana sobarica = new PieChart30Dana(manageAll.getSobeManager(), manageAll.getRezervacijeManager(), false);
			PieChart chart = sobarica.getChart();
			JPanel chartPanel = new XChartPanel<PieChart>(chart);
			this.getContentPane().add(chartPanel, BorderLayout.CENTER);
		}
		else {
			LineChart sobarica = new LineChart(manageAll.getRezervacijeManager());
			XYChart chart = sobarica.getChart();
			JPanel chartPanel = new XChartPanel<XYChart>(chart);
			this.getContentPane().add(chartPanel, BorderLayout.CENTER);
		}
	}
}