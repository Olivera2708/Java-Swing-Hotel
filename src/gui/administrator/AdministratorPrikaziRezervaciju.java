package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import entity.Rezervacije;
import entity.Usluge;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorPrikaziRezervaciju extends JFrame{
	private static final long serialVersionUID = 1L;
	Rezervacije r;

	ManageAll manageAll = ManageAll.getInstance();
	
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public AdministratorPrikaziRezervaciju (Rezervacije r) {
		this.r = r;
		
		this.setTitle("Rezervacije");
		this.setPreferredSize(new Dimension(600, 400));
		this.setResizable(false);
		prikaziNovigost();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void prikaziNovigost() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]20[]", "[]40[][][][][][][]"));
		Border margin = new EmptyBorder(20, 150, 10, 10);
		panel.setBorder(margin);
		
		JLabel naslov = new JLabel("Prikaz rezervacije");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		String us = "";
		if (!r.getUsluge().isEmpty()) {
			for(Usluge u: r.getUsluge()) {
				us += u.getTip() + ",";
			}
			us = us.substring(0, us.length()-1);
		}
		
		int cena = r.getCena();
		
		if (String.valueOf(r.getStatus()).equals("ODBIJENA")) {
			cena = 0;
		}
		
		panel.add(new JLabel("Id"));
		panel.add(new JLabel(String.valueOf(r.getId())), "right wrap, grow");
		panel.add(new JLabel("Tip sobe"));
		panel.add(new JLabel(r.getTipSobe().getTip()), "right, wrap, grow");
		panel.add(new JLabel("Usluge"));
		panel.add(new JLabel(us), "right, wrap, grow");
		panel.add(new JLabel("Gost"));
		panel.add(new JLabel(r.getGost().getKorisnickoIme()), "right, wrap, grow");
		panel.add(new JLabel("Datum od"));
		panel.add(new JLabel(datum_formatter.format(r.getOdDatum())), "right, wrap, grow");
		panel.add(new JLabel("Datum do"));
		panel.add(new JLabel(datum_formatter.format(r.getDoDatum())), "right, wrap, grow");
		panel.add(new JLabel("Cena"));
		panel.add(new JLabel(String.valueOf(cena)), "right, wrap, grow");
		panel.add(new JLabel("Status"));
		panel.add(new JLabel(String.valueOf(r.getStatus())), "right, wrap, grow");
		
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
}