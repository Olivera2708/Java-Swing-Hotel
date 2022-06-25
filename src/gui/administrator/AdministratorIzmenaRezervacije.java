package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import entity.Rezervacije;
import entity.Usluge;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorIzmenaRezervacije extends JFrame{
	private static final long serialVersionUID = 1L;
	Rezervacije rezervacija;

	ManageAll manageAll = ManageAll.getInstance();
	
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	JButton btnKreiraj = new JButton("Sačuvaj");
	JTextField brojSobe = new JTextField(40);
	String[] opcije_status = {"NA_CEKANJU", "POTVRDJENA", "ODBIJENA", "OTKAZANA"};
	JComboBox<String> status = new JComboBox<>(opcije_status);
	String[] opcije_usluge = manageAll.getUslugeManager().getNames();
	JList<String> usluge = new JList<>(opcije_usluge);
	String[] opcije_tip = manageAll.getTipSobeManager().getNames();
	JComboBox<String> tipSobe = new JComboBox<>(opcije_tip);
	String[] opcije_korisnik = manageAll.getGostManager().getNames();
	JComboBox<String> korisnik = new JComboBox<>(opcije_korisnik);
	JFormattedTextField datumOd = new JFormattedTextField(datum_formatter);
	JFormattedTextField datumDo = new JFormattedTextField(datum_formatter);
	
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";
	
	public AdministratorIzmenaRezervacije (Rezervacije rezervacija) {
		this.rezervacija = rezervacija;
		usluge.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		this.setTitle("Rezervacije");
		this.setPreferredSize(new Dimension(700, 500));
		this.setResizable(false);
		prikaziNovigost();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNovigost() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]20[]", "[]40[][][][][][]40[]"));
		Border margin = new EmptyBorder(30, 10, 10, 10);
		panel.setBorder(margin);
		
		JLabel naslov = new JLabel("Izmena podataka o rezervaciji");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		int[] oznaceni = new int[rezervacija.getUsluge().size()];
		
		for (int i = 0; i < rezervacija.getUsluge().size(); i++) {
			oznaceni[i] = Arrays.asList(opcije_usluge).indexOf(rezervacija.getUsluge().get(i).getTip());
		}
		
		JScrollPane skrol = new JScrollPane(usluge);
		skrol.setPreferredSize(new Dimension(80, 60));
		
		usluge.setSelectedIndices(oznaceni);
		
		status.setSelectedItem(String.valueOf(rezervacija.getStatus()));
		tipSobe.setSelectedItem(rezervacija.getTipSobe().getTip());
		korisnik.setSelectedItem(rezervacija.getGost().getKorisnickoIme());
		datumOd.setText(datum_formatter.format(rezervacija.getOdDatum()));
		datumDo.setText(datum_formatter.format(rezervacija.getDoDatum()));
		
		panel.add(new JLabel("Tip sobe"));
		panel.add(tipSobe, "right, wrap, grow");
		panel.add(new JLabel("Usluge (pritisnite cmd za označavanje više usluga)"));
		panel.add(skrol, "right, wrap, grow");
		panel.add(new JLabel("Gost"));
		panel.add(korisnik, "right, wrap, grow");
		panel.add(new JLabel("Datum od"));
		panel.add(datumOd, "right, wrap, grow");
		panel.add(new JLabel("Datum do"));
		panel.add(datumDo, "right, wrap, grow");
		panel.add(new JLabel("Status"));
		panel.add(status, "right, wrap, grow");
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
			btnKreiraj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean proslo = true;
					//upisi u objekat i sacuvaj u bazu
					String statusText = (String) status.getSelectedItem();
					String korisnikText = (String) korisnik.getSelectedItem();
					int[] uslugeText = usluge.getSelectedIndices();
					String tipSobeText = (String) tipSobe.getSelectedItem();
					String datumOdText = datumOd.getText();
					String datumDoText = datumDo.getText();
					
					int[] lista_usluga = new int[uslugeText.length];
					List<Usluge> sve_usluge = manageAll.getUslugeManager().getAll();
					for (int i = 0; i < uslugeText.length; i++) {
						lista_usluga[i] = sve_usluge.get(uslugeText[i]).getId();
					}
					
					if (!datumOdText.matches(datumRegex) || !datumDoText.matches(datumRegex)){
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
					} else
						try {
							if (datum_formatter.parse(datumOdText).after(datum_formatter.parse(datumDoText))) {
								JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
							}
							else {
								try {
									proslo = manageAll.getRezervacijeManager().edit(rezervacija.getId(), manageAll.getTipSobeManager().get_id(tipSobeText), lista_usluga, manageAll.getGostManager().get_id(korisnikText), datum_formatter.parse(datumOdText), datum_formatter.parse(datumDoText), statusText, null);		
									manageAll.getRezervacijeManager().dodajDatumKraja(rezervacija);
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								if (proslo) {
									JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
									zatvoriProzor();
								}
							}
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}	
			});
	}
	
	private void zatvoriProzor() {
		this.setVisible(false);
		this.dispose();
	}
}