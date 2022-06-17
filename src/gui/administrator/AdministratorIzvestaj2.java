package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import manage.ManageAll;

public class AdministratorIzvestaj2 extends JFrame{
	private static final long serialVersionUID = 1L;
	
	int odbijeni;
	int otkazani;
	int obradjeni;
	
	JTable tabela;

	ManageAll manageAll = ManageAll.getInstance();
	
	public AdministratorIzvestaj2 (int odbijeni, int otkazani, int obradjeni) {
		this.odbijeni = odbijeni;
		this.otkazani = otkazani;
		this.obradjeni = obradjeni;
		
		this.setTitle("Izveštaj 2");
		this.setPreferredSize(new Dimension(380, 120));
		this.setResizable(false);
		prikaziIzvestaj();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void prikaziIzvestaj() {
		JPanel panel = new JPanel(new GridLayout(3,1));
		JLabel prihodiText = new JLabel("Broj odbjenih rezervacija je " + String.valueOf(odbijeni));
		prihodiText.setHorizontalAlignment(JLabel.CENTER);
		panel.add(prihodiText);
		
		JLabel rashodiText = new JLabel("Broj otkazanih rezervacija je " + String.valueOf(otkazani));
		rashodiText.setHorizontalAlignment(JLabel.CENTER);
		panel.add(rashodiText);
		
		JLabel potvrdjeneText = new JLabel("Broj obradjenih rezervacija je " + String.valueOf(obradjeni));
		potvrdjeneText.setHorizontalAlignment(JLabel.CENTER);
		panel.add(potvrdjeneText);
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
}