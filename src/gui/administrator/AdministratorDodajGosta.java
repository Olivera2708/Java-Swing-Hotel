package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import gui.models.GostModel;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorDodajGosta extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JTable tabela;
	
	JButton btnKreiraj = new JButton("Kreiraj");
	String[] opcije_pol = {"Muško", "Žensko"};
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	JTextField ime = new JTextField(40);
	JTextField prezime = new JTextField(40);
	JComboBox<String> pol = new JComboBox<>(opcije_pol);
	JTextField pasos = new JTextField(40);
	JTextField email = new JTextField(40);
	JTextField telefon = new JTextField(40);
	JFormattedTextField datum = new JFormattedTextField(datum_formatter);
	JTextField adresa = new JTextField(40);
	
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";
	private String telefonRegex = "^(\\d{8,10})$";
	private String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
	
	public AdministratorDodajGosta (JTable tabela) {
		this.tabela = tabela;
		
		this.setTitle("Gost");
		this.setPreferredSize(new Dimension(600, 500));
		this.setResizable(false);
		prikaziNoviZaposleni();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNoviZaposleni() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]", "[]20[][][][][][][][]20[]"));
		Border margin = new EmptyBorder(15, 50, 10, 50);
		panel.setBorder(margin);
		
		pol.setSelectedIndex(0);
		datum.setColumns(40);
		
		JLabel naslov = new JLabel("Novi gost");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		panel.add(new JLabel("Ime"));
		panel.add(ime, "right, wrap, grow");
		panel.add(new JLabel("Prezime"));
		panel.add(prezime, "right, wrap, grow");
		panel.add(new JLabel("Pol"));
		panel.add(pol, "right, wrap, grow");
		panel.add(new JLabel("Broj pasoša"));
		panel.add(pasos, "right, wrap, grow");
		panel.add(new JLabel("Adresa"));
		panel.add(adresa, "right, wrap, grow");
		panel.add(new JLabel("Email"));
		panel.add(email, "right, wrap, grow");
		panel.add(new JLabel("Telefon"));
		panel.add(telefon, "right, wrap, grow");
		panel.add(new JLabel("Datum rođenja"));
		panel.add(datum, "right, wrap, grow");
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
					String pasosText = pasos.getText().trim();
					String adresaText = adresa.getText().trim();
					String emailText = email.getText().trim();
					String telefonText = telefon.getText().trim();
					String datumText = datum.getText().trim();
					
					if (imeText.equals("") || prezimeText.equals("") || pasosText.equals("") ||
							adresaText.equals("") || emailText.equals("") || telefonText.equals("") || datumText.equals("")) {
						JOptionPane.showMessageDialog(null, "Potrebno je uneti sve podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (manageAll.getGostManager().vecPostojiKorisnicko(emailText)) {
						JOptionPane.showMessageDialog(null, "Već postoji nalog sa ovom email adresom.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!emailText.matches(emailRegex)) {
						JOptionPane.showMessageDialog(null, "Loš unos email adrese.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!datumText.matches(datumRegex)){
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (pasosText.length() < 7){
						JOptionPane.showMessageDialog(null, "Broj pasoša mora da ima bar 7 karaktera.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!telefonText.matches(telefonRegex)) {
						JOptionPane.showMessageDialog(null, "Loš unos broja telefona.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else {
						try {
							manageAll.getGostManager().add(imeText, prezimeText, polText, datum_formatter.parse(datumText), telefonText, adresaText, emailText, pasosText);
							JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
							osveziTabelu();
							zatvoriProzor();
						} catch (ParseException e1) {
							System.out.print("Greska");
						}
					}
				}	
			});
	}
	
	private void zatvoriProzor() {
		this.setVisible(false);
		this.dispose();
	}
	
	private void osveziTabelu() {
		tabela.setModel(new GostModel(manageAll.getGostManager().getAll()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}