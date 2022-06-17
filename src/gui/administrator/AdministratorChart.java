package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;

import chart.MainChart;
import chart.PieChart30Dana;
import entity.Zaposleni;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorChart extends JFrame {
	private static final long serialVersionUID = 1L;
	ManageAll manageAll = ManageAll.getInstance();
	
	public AdministratorChart () {
		this.setTitle("Grafik");
		this.setPreferredSize(new Dimension(800, 700));
		this.setResizable(false);
		ChartGUI();
		this.pack();
		this.setLocationRelativeTo(null);
		Dugmici();
	}
	
	private void ChartGUI() {
		MainChart<PieChart> sobarica = new PieChart30Dana(manageAll.getSobeManager(), manageAll.getRezervacijeManager(), true);
		PieChart chart = sobarica.getChart();
		JPanel chartPanel = new XChartPanel(chart);
		this.getContentPane().add(chartPanel, BorderLayout.CENTER);
	}
	
	private void Dugmici() {
//		this.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				areYouSure();
//			}	
//		});
	}
	
//	private void areYouSure() {
//		String[] option = new String[2];
//		option[0] = "Da";
//		option[1] = "Ne";
//		int vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da izađete iz aplikacije?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
//		if (vrednost == JOptionPane.YES_OPTION) {
//			System.exit(0);
//		}	
//	}	
}