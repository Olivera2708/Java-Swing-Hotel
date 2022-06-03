package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import entity.Gost;
import entity.Zaposleni;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class RecepcionerFrame extends JFrame {
	private Zaposleni recepcioner;
	
	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnKreiraj = new JButton("Kreiraj");
	String[] opcije_pol = {"Muško", "Žensko"};
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	JTextField ime = new JTextField(40);
	JTextField prezime = new JTextField(40);
	JComboBox<String> pol = new JComboBox<>(opcije_pol);
	JTextField jmbg = new JTextField(40);
	JTextField email = new JTextField(40);
	JTextField telefon = new JTextField(40);
	JFormattedTextField datum = new JFormattedTextField(datum_formatter);
	JTextField adresa = new JTextField(40);
	
	private String jmbgRegex = "^(\\d{13})$";
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";
	private String telefonRegex = "^(\\d{8,10})$";
	private String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

	private static final long serialVersionUID = 1L;
	
	public RecepcionerFrame (Zaposleni recepcioner) {
		this.recepcioner = recepcioner;
		
		this.setTitle("Recepcioner " + recepcioner.getIme() + " " + recepcioner.getPrezime());
		this.setPreferredSize(new Dimension(1000, 700));
		RecepcionerGUI();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void RecepcionerGUI() {
		JMenuBar meni = new JMenuBar();
		JMenu noviGost = new JMenu("Novi gost");
		JMenu rezervacije = new JMenu("Rezervacije");
		JMenu sobe = new JMenu("Sobe");
		JMenu prijavaGostiju = new JMenu("Check-in/out");
		JMenu odjava = new JMenu("Odjavi se");
		
		meni.add(noviGost);
		meni.add(rezervacije);
		meni.add(sobe);
		meni.add(prijavaGostiju);
		meni.add(odjava);
		
		this.setJMenuBar(meni);
		
		prikaziNoviGost();
	}
	
	private void prikaziNoviGost() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]", "[]20[][][][][][][][]20[]"));
		Border margin = new EmptyBorder(40, 300, 10, 300);
		panel.setBorder(margin);
		
		pol.setSelectedIndex(0);
		datum.setColumns(40);
		
		JLabel naslov = new JLabel("Novi korisnik");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		panel.add(new JLabel("Ime"));
		panel.add(ime, "right, wrap, grow");
		panel.add(new JLabel("Prezime"));
		panel.add(prezime, "right, wrap, grow");
		panel.add(new JLabel("Pol"));
		panel.add(pol, "right, wrap, grow");
		panel.add(new JLabel("JMBG"));
		panel.add(jmbg, "right, wrap, grow");
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
				String jmbgText = jmbg.getText().trim();
				String adresaText = adresa.getText().trim();
				String emailText = email.getText().trim();
				String telefonText = telefon.getText().trim();
				String datumText = datum.getText().trim();
				
				if (imeText.equals("") || prezimeText.equals("") || jmbgText.equals("") ||
						adresaText.equals("") || emailText.equals("") || telefonText.equals("") || datumText.equals("")) {
					JOptionPane.showMessageDialog(null, "Potrebno je uneti sve podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else if (!emailText.matches(emailRegex)) {
					JOptionPane.showMessageDialog(null, "Loš unos email adrese.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else if (!datumText.matches(datumRegex)){
					JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else if (!telefonText.matches(telefonRegex)) {
					JOptionPane.showMessageDialog(null, "Loš unos broja telefona.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else if (!jmbgText.matches(jmbgRegex)) {
					JOptionPane.showMessageDialog(null, "JMBG mora imati 13 brojeva.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else {
					try {
						manageAll.getGostManager().add(imeText, prezimeText, polText, datum_formatter.parse(datumText), telefonText, adresaText, emailText, jmbgText);
						ime.setText("");
						prezime.setText("");
						jmbg.setText("");
						adresa.setText("");
						email.setText("");
						telefon.setText("");
						datum.setText("");
						JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
					} catch (ParseException e1) {
						System.out.print("Greska");
					}
				}
			}	
		});
	}
}
