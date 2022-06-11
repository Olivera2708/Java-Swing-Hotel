package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.Gost;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorPrikaziGosta extends JFrame{
	private static final long serialVersionUID = 1L;
	
	Gost g;

	ManageAll manageAll = ManageAll.getInstance();
	
	public AdministratorPrikaziGosta (Gost gost) {
		this.g = gost;
		
		this.setTitle("Prikaz gosta");
		this.setPreferredSize(new Dimension(480, 320));
		this.setResizable(false);
		prikaziNoviZaposleni();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void prikaziNoviZaposleni() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[100]50[100]", "[]40[][][][][][][][][][][][]20[]"));
		JPanel content = new JPanel(new GridBagLayout());
		
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String datum_string = datum_formatter.format(g.getDatumRodjenja());
		
		JLabel naslov = new JLabel("Gost", JLabel.CENTER);
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "cell 0 0 2 1,growx");
		
		panel.add(new JLabel("Ime"), "cell 0 1");
		panel.add(new JLabel(g.getIme()), "cell 1 1");
		panel.add(new JLabel("Prezime"), "cell 0 2");
		panel.add(new JLabel(g.getPrezime()), "cell 1 2");
		panel.add(new JLabel("Pol"), "cell 0 3");
		panel.add(new JLabel(g.getPol()), "cell 1 3");
		panel.add(new JLabel("Adresa"), "cell 0 4");
		panel.add(new JLabel(g.getAdresa()), "cell 1 4");
		panel.add(new JLabel("Email"), "cell 0 5");
		panel.add(new JLabel(g.getKorisnickoIme()), "cell 1 5");
		panel.add(new JLabel("Broj pasoša"), "cell 0 6");
		panel.add(new JLabel(g.getLozinka()), "cell 1 6");
		panel.add(new JLabel("Telefon"), "cell 0 7");
		panel.add(new JLabel(g.getTelefon()), "cell 1 7");
		panel.add(new JLabel("Datum rođenja"), "cell 0 8");
		panel.add(new JLabel(datum_string), "cell 1 8");
		
		content.add(panel);
		this.getContentPane().add(content, BorderLayout.CENTER);
	}
}