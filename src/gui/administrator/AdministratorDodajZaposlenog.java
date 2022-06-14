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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import gui.models.ZaposleniModel;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorDodajZaposlenog extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JTable tabela;
	
	JButton btnKreiraj = new JButton("Kreiraj");
	String[] opcije_pol = {"Muško", "Žensko"};
	String[] opcije_zaposleni = {"Sobarica", "Recepcioner", "Administrator"};
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	JTextField ime = new JTextField(40);
	JTextField prezime = new JTextField(40);
	JComboBox<String> pol = new JComboBox<>(opcije_pol);
	JComboBox<String> pozicija = new JComboBox<>(opcije_zaposleni);
	JTextField korisnicko = new JTextField(40);
	JTextField lozinka = new JTextField(40);
	JTextField telefon = new JTextField(40);
	JFormattedTextField datum = new JFormattedTextField(datum_formatter);
	JTextField adresa = new JTextField(40);
	JTextField strucna = new JTextField(1);
	JTextField staz = new JTextField(2);
	
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";
	private String telefonRegex = "^(\\d{8,10})$";
	private String strucnaRegex = "[1-8]";
	private String stazRegex = "[0-9]|[1-3][0-9]|4[0-5]";
	
	public AdministratorDodajZaposlenog (JTable tabela) {
		this.tabela = tabela;
		
		this.setTitle("Zaposleni");
		this.setPreferredSize(new Dimension(600, 500));
		this.setResizable(false);
		prikaziNoviZaposleni();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNoviZaposleni() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]", "[]20[][][][][][][][][][][]20[]"));
		Border margin = new EmptyBorder(15, 50, 10, 50);
		panel.setBorder(margin);
		
		pol.setSelectedIndex(0);
		datum.setColumns(40);
		
		JLabel naslov = new JLabel("Novi zaposleni");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		panel.add(new JLabel("Ime"));
		panel.add(ime, "right, wrap, grow");
		panel.add(new JLabel("Prezime"));
		panel.add(prezime, "right, wrap, grow");
		panel.add(new JLabel("Pol"));
		panel.add(pol, "right, wrap, grow");
		panel.add(new JLabel("Adresa"));
		panel.add(adresa, "right, wrap, grow");
		panel.add(new JLabel("Korisnicko ime"));
		panel.add(korisnicko, "right, wrap, grow");
		panel.add(new JLabel("Lozinka"));
		panel.add(lozinka, "right, wrap, grow");
		panel.add(new JLabel("Telefon"));
		panel.add(telefon, "right, wrap, grow");
		panel.add(new JLabel("Datum rođenja"));
		panel.add(datum, "right, wrap, grow");
		panel.add(new JLabel("Pozicija"));
		panel.add(pozicija, "right, wrap, grow");
		panel.add(new JLabel("Strucna sprema"));
		panel.add(strucna, "right, wrap, grow");
		panel.add(new JLabel("Staž"));
		panel.add(staz, "right, wrap, grow");
		
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
					String pozicijaText = (String) pozicija.getSelectedItem();
					String strucnaText = strucna.getText().trim();
					String stazText = staz.getText().trim();
					
					
					if (imeText.equals("") || prezimeText.equals("") || korisnickoText.equals("") ||
							adresaText.equals("") || lozinkaText.equals("") || telefonText.equals("") || datumText.equals("") ||
							strucnaText.equals("") || stazText.equals("")) {
						JOptionPane.showMessageDialog(null, "Potrebno je uneti sve podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!strucnaText.matches(strucnaRegex)) {
						JOptionPane.showMessageDialog(null, "Strucna sprema mora biti broj izmedju 1 i 8.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!datumText.matches(datumRegex)){
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!telefonText.matches(telefonRegex)) {
						JOptionPane.showMessageDialog(null, "Loš unos broja telefona.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else if (!stazText.matches(stazRegex)) {
						JOptionPane.showMessageDialog(null, "Staž mora biti broj izmedju 0 i 45.", "Greška", JOptionPane.ERROR_MESSAGE);
					} else
						try {
							if (datum_formatter.parse(datumText).after(new java.util.Date())) {
								JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
							}
							else if (manageAll.getZaposleniManager().vecPostojiKorisnicko(korisnickoText)) {
								JOptionPane.showMessageDialog(null, "Izabrano korisničko ime je zauzeto.", "Greška", JOptionPane.ERROR_MESSAGE);
							}
							else if (korisnickoText.length() < 5) {
								JOptionPane.showMessageDialog(null, "Korisničko ime mora imati bar 5 karaktera.", "Greška", JOptionPane.ERROR_MESSAGE);
							}
							else if (lozinkaText.length() < 7) {
								JOptionPane.showMessageDialog(null, "Lozinka ime mora imati bar 7 karaktera.", "Greška", JOptionPane.ERROR_MESSAGE);
							}
							else {
								try {
									manageAll.getZaposleniManager().add(imeText, prezimeText, polText, datum_formatter.parse(datumText), telefonText, adresaText, korisnickoText, lozinkaText, Integer.parseInt(strucnaText), Integer.parseInt(stazText), pozicijaText);
									JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
									osveziTabelu();
									zatvoriProzor();
								} catch (ParseException e1) {
									System.out.print("Greska");
								}
							}
						} catch (HeadlessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NumberFormatException e1) {
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
	
	private void osveziTabelu() {
		tabela.setModel(new ZaposleniModel(manageAll.getZaposleniManager().getAll()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}