package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import entity.Gost;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorIzmenaGosta extends JFrame{
	private static final long serialVersionUID = 1L;
	Gost gost;

	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnKreiraj = new JButton("Potvrdi");
	String[] opcije_pol = {"Muško", "Žensko"};
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	JTextField ime = new JTextField(40);
	JTextField prezime = new JTextField(40);
	JComboBox<String> pol = new JComboBox<>(opcije_pol);
	JTextField korisnicko = new JTextField(40);
	JTextField lozinka = new JTextField(40);
	JTextField telefon = new JTextField(40);
	JFormattedTextField datum = new JFormattedTextField(datum_formatter);
	JTextField adresa = new JTextField(40);
	
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";
	private String telefonRegex = "^(\\d{8,10})$";
	private String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
	
	public AdministratorIzmenaGosta (Gost gost) {
		this.gost = gost;
		
		this.setTitle("Gost");
		this.setPreferredSize(new Dimension(600, 500));
		this.setResizable(false);
		prikaziNovigost();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNovigost() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]", "[]20[][][][][][][][][][][]20[]"));
		Border margin = new EmptyBorder(15, 50, 10, 50);
		panel.setBorder(margin);
		
		datum.setColumns(40);
		
		JLabel naslov = new JLabel("Izmena podataka gosta");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		pol.setSelectedItem(gost.getPol());
		ime.setText(gost.getIme());
		prezime.setText(gost.getPrezime());
		adresa.setText(gost.getAdresa());
		korisnicko.setText(gost.getKorisnickoIme());
		lozinka.setText(gost.getLozinka());
		telefon.setText(gost.getTelefon());
		datum.setText(datum_formatter.format(gost.getDatumRodjenja()));
		
		panel.add(new JLabel("Ime"));
		panel.add(ime, "right, wrap, grow");
		panel.add(new JLabel("Prezime"));
		panel.add(prezime, "right, wrap, grow");
		panel.add(new JLabel("Pol"));
		panel.add(pol, "right, wrap, grow");
		panel.add(new JLabel("Adresa"));
		panel.add(adresa, "right, wrap, grow");
		panel.add(new JLabel("Email"));
		panel.add(korisnicko, "right, wrap, grow");
		panel.add(new JLabel("Broj pasoša"));
		panel.add(lozinka, "right, wrap, grow");
		panel.add(new JLabel("Telefon"));
		panel.add(telefon, "right, wrap, grow");
		panel.add(new JLabel("Datum rođenja"));
		panel.add(datum, "right, wrap, grow");
		panel.add(new JLabel("Pozicija"));
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
			btnKreiraj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//upisi u objekat i sacuvaj u bazu
					String imeText = ime.getText().trim();
					String prezimeText = prezime.getText().trim();
					String polText = (String) pol.getSelectedItem();
					String adresaText = adresa.getText().trim();
					String korisnickoText = korisnicko.getText().trim();
					String lozinkaText = lozinka.getText().trim();
					String telefonText = telefon.getText().trim();
					String datumText = datum.getText().trim();
					
					if (imeText.equals("") || prezimeText.equals("") || lozinkaText.equals("") ||
							adresaText.equals("") || korisnickoText.equals("") || telefonText.equals("") || datumText.equals("")) {
						JOptionPane.showMessageDialog(null, "Potrebno je uneti sve podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (manageAll.getGostManager().vecPostojiKorisnicko(korisnickoText) && !korisnickoText.equals(gost.getKorisnickoIme())) {
						JOptionPane.showMessageDialog(null, "Već postoji nalog sa ovom email adresom.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!korisnickoText.matches(emailRegex)) {
						JOptionPane.showMessageDialog(null, "Loš unos email adrese.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!datumText.matches(datumRegex)){
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					try {
						if (datum_formatter.parse(datumText).after(new java.util.Date())) {
							JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
						}
						else if (korisnickoText.length() < 7){
							JOptionPane.showMessageDialog(null, "Broj pasoša mora da ima bar 7 karaktera.", "Greška", JOptionPane.ERROR_MESSAGE);
						}
						else if (!telefonText.matches(telefonRegex)) {
							JOptionPane.showMessageDialog(null, "Loš unos broja telefona.", "Greška", JOptionPane.ERROR_MESSAGE);
						}
						else {
							try {
								manageAll.getGostManager().edit(gost.getId(), imeText, prezimeText, polText, datum_formatter.parse(datumText), telefonText, adresaText, korisnickoText, lozinkaText);
								manageAll.getRezervacijeManager().promeniGosta(gost.getKorisnickoIme(), korisnickoText);
								JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
								zatvoriProzor();
							} catch (ParseException e1) {
								System.out.print("Greska");
							}
						}
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e1) {
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