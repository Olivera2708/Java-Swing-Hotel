package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.Zaposleni;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorPrikaziZaposlenog extends JFrame{
	private static final long serialVersionUID = 1L;
	
	Zaposleni z;

	ManageAll manageAll = ManageAll.getInstance();
	
	public AdministratorPrikaziZaposlenog (Zaposleni zaposleni) {
		this.z = zaposleni;
		
		this.setTitle("Prikaz zaposlenog");
		this.setPreferredSize(new Dimension(500, 400));
		this.setResizable(false);
		prikaziNoviZaposleni();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void prikaziNoviZaposleni() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[100]50[100]", "[]40[][][][][][][][][][][][]20[]"));
		JPanel content = new JPanel(new GridBagLayout());
		
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String datum_string = datum_formatter.format(z.getDatumRodjenja());
		
		JLabel naslov = new JLabel("Zaposleni", JLabel.CENTER);
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "cell 0 0 2 1,growx");
		
		panel.add(new JLabel("Ime"), "cell 0 1");
		panel.add(new JLabel(z.getIme()), "cell 1 1");
		panel.add(new JLabel("Prezime"), "cell 0 2");
		panel.add(new JLabel(z.getPrezime()), "cell 1 2");
		panel.add(new JLabel("Pol"), "cell 0 3");
		panel.add(new JLabel(z.getPol()), "cell 1 3");
		panel.add(new JLabel("Adresa"), "cell 0 4");
		panel.add(new JLabel(z.getAdresa()), "cell 1 4");
		panel.add(new JLabel("Korisničko ime"), "cell 0 5");
		panel.add(new JLabel(z.getKorisnickoIme()), "cell 1 5");
		panel.add(new JLabel("Lozinka"), "cell 0 6");
		panel.add(new JLabel(z.getLozinka()), "cell 1 6");
		panel.add(new JLabel("Telefon"), "cell 0 7");
		panel.add(new JLabel(z.getTelefon()), "cell 1 7");
		panel.add(new JLabel("Datum rođenja"), "cell 0 8");
		panel.add(new JLabel(datum_string), "cell 1 8");
		panel.add(new JLabel("Pozicija"), "cell 0 9");
		panel.add(new JLabel(z.getPozicija()), "cell 1 9");
		panel.add(new JLabel("Stručna sprema"), "cell 0 10");
		panel.add(new JLabel(Integer.toString(z.getStrucnaSprema()) + ". stepen"), "cell 1 10");
		panel.add(new JLabel("Staž"), "cell 0 11");
		panel.add(new JLabel(Integer.toString(z.getStaz()) + " godine"), "cell 1 11");
		panel.add(new JLabel("Plata"), "cell 0 12");
		panel.add(new JLabel(Integer.toString((int)z.getPlata()) + " dinara"), "cell 1 12");
		
		content.add(panel);
		this.getContentPane().add(content, BorderLayout.CENTER);
	}
}