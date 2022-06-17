package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import entity.Sobe;
import entity.Zaposleni;
import manage.ManageAll;

public class AdministratorIzvestaji extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	JButton btnIzvestaj1 = new JButton("Izveštaj 1");
	JButton btnIzvestaj2 = new JButton("Izveštaj 2");
	JButton btnIzvestaj3 = new JButton("Izveštaj 3");
	
	JFormattedTextField datumOd = new JFormattedTextField(datum_formatter);
	JFormattedTextField datumDo = new JFormattedTextField(datum_formatter);
	
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";

	AdministratorIzvestaji () {
		this.setTitle("Izveštaji");
		this.setPreferredSize(new Dimension(300, 130));
		this.setResizable(false);
		prikaziToolBar();
		prikaziUnosDatuma();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziToolBar() {
		JToolBar toolBar = new JToolBar();
		JPanel panel = new JPanel();
		Dimension d = new Dimension(70, 20);
				
		btnIzvestaj1.setPreferredSize(d);
		btnIzvestaj2.setPreferredSize(d);
		btnIzvestaj3.setPreferredSize(d);
		
		panel.add(btnIzvestaj1);
		panel.add(btnIzvestaj2);
		panel.add(btnIzvestaj3);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziUnosDatuma() {
		JPanel panel = new JPanel();
		Dimension d = new Dimension(150, 20);
		
		datumOd.setPreferredSize(d);
		datumDo.setPreferredSize(d);
		
		panel.add(new JLabel("Od datuma"));
		panel.add(datumOd);
		panel.add(new JLabel("Do datuma"));
		panel.add(datumDo);
		
		add(panel);
	}
		
	private void allButtons() {
		btnIzvestaj1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String datumOdText = datumOd.getText();
				String datumDoText = datumDo.getText();
				
				if (!datumOdText.matches(datumRegex) || !datumDoText.matches(datumRegex)) {
					JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else {
					Date odDatum = null;
					Date doDatum = null;
					
					try {
						odDatum = datum_formatter.parse(datumOdText);
						doDatum = datum_formatter.parse(datumDoText);
					} catch (ParseException e1) {}
					
					if (odDatum.after(doDatum) || doDatum.after(new Date())) {
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else {
						//prihodi
						int prihodi = manageAll.getRezervacijeManager().getPrihodi(odDatum, doDatum);
						//rashodi
						int rashodi = manageAll.getRezervacijeManager().getRashodi(odDatum, doDatum);
						//svaka sobarica broj spremljenih soba
						HashMap<Zaposleni, Integer> sobaricaSoba = manageAll.getSobeManager().getBrojSobaPoSobarici(odDatum, doDatum);
						//broj potvrdjenih rezervacija
						int brojPotvrdjenih = manageAll.getRezervacijeManager().getBrojPotvrdjenih(odDatum, doDatum);
						
						AdministratorIzvestaj1 izv = new AdministratorIzvestaj1(prihodi, rashodi, brojPotvrdjenih, sobaricaSoba);
						izv.setVisible(true);
					}
				}
			}
			
		});
		
		btnIzvestaj2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String datumOdText = datumOd.getText();
				String datumDoText = datumDo.getText();
				
				if (!datumOdText.matches(datumRegex) || !datumDoText.matches(datumRegex)) {
					JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else {
					Date odDatum = null;
					Date doDatum = null;
					
					try {
						odDatum = datum_formatter.parse(datumOdText);
						doDatum = datum_formatter.parse(datumDoText);
					} catch (ParseException e1) {}
					
					if (odDatum.after(doDatum) || doDatum.after(new Date())) {
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else {
						//broj odbijenih zahteva
						int odbijenih = manageAll.getRezervacijeManager().getBrojOdbijenih(odDatum, doDatum);
						//broj otkazanih zahteva
						int otkazanih = manageAll.getRezervacijeManager().getBrojOtkazanih(odDatum, doDatum);
						//broj obradjenih zahteva
						int obradjenih = odbijenih + otkazanih + manageAll.getRezervacijeManager().getBrojPotvrdjenih(odDatum, doDatum);
						
						AdministratorIzvestaj2 izv = new AdministratorIzvestaj2(odbijenih, otkazanih, obradjenih);
						izv.setVisible(true);
					}
				}
			}
			
		});
		
		btnIzvestaj3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String datumOdText = datumOd.getText();
				String datumDoText = datumDo.getText();
				
				if (!datumOdText.matches(datumRegex) || !datumDoText.matches(datumRegex)) {
					JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else {
					Date odDatum = null;
					Date doDatum = null;
					
					try {
						odDatum = datum_formatter.parse(datumOdText);
						doDatum = datum_formatter.parse(datumDoText);
					} catch (ParseException e1) {}
					
					if (odDatum.after(doDatum) || doDatum.after(new Date())) {
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else {
						HashMap <Sobe, Integer[]> sobe = manageAll.getRezervacijeManager().prikazSoba(odDatum, doDatum);
						
						AdministratorIzvestaj3 izv = new AdministratorIzvestaj3(sobe);
						izv.setVisible(true);
					}
				}
			}
			
		});
	}
}