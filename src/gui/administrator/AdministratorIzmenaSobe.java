package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import entity.Sobe;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorIzmenaSobe extends JFrame{
	private static final long serialVersionUID = 1L;
	Sobe sobe;

	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnKreiraj = new JButton("Sačuvaj");
	JTextField brojSobe = new JTextField(40);
	String[] opcije_status = {"SLOBODNA", "ZAUZETO", "SPREMANJE"};
	JComboBox<String> status = new JComboBox<>(opcije_status);
	String[] opcije_tip = manageAll.getTipSobeManager().getNames();
	JComboBox<String> tipSobe = new JComboBox<>(opcije_tip);
	String[] dodatne_stvari = {"Klima", "TV", "Balkon", "Pušačka", "Nepušačka"};
	JList<String> sadrzaji = new JList<String>(dodatne_stvari);
	
	public AdministratorIzmenaSobe (Sobe tipSobe) {
		this.sobe = tipSobe;
		
		this.setTitle("Sobe");
		this.setPreferredSize(new Dimension(500, 400));
		this.setResizable(false);
		prikaziNovigost();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNovigost() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]10[][][][]", "[]20[]"));
		Border margin = new EmptyBorder(10, 10, 10, 10);
		panel.setBorder(margin);
		
		JLabel naslov = new JLabel("Izmena podataka o sobi");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		brojSobe.setText(String.valueOf(sobe.getBrojSobe()));
		status.setSelectedItem(String.valueOf(sobe.getStatus()));
		tipSobe.setSelectedItem(sobe.getTipSobe().getTip());
		if (sobe.getSadrzaj() != null) {
			int[] oznaceni = new int[sobe.getSadrzaj().length];
			for (int i = 0; i < sobe.getSadrzaj().length; i++) {
				oznaceni[i] = Arrays.asList(dodatne_stvari).indexOf(sobe.getSadrzaj()[i]);
			}
			sadrzaji.setSelectedIndices(oznaceni);
		}
		
		panel.add(new JLabel("Broj sobe"));
		panel.add(brojSobe, "right, wrap, grow");
		panel.add(new JLabel("Status"));
		panel.add(status, "right, wrap, grow");
		panel.add(new JLabel("Tip sobe"));
		panel.add(tipSobe, "right, wrap, grow");
		panel.add(new JLabel("Sadžaji"));
		panel.add(sadrzaji, "right, wrap, grow");
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
			btnKreiraj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//upisi u objekat i sacuvaj u bazu
					String brojSobeText = brojSobe.getText().trim();
					String statusText = (String) status.getSelectedItem();
					String tipSobeText = (String) tipSobe.getSelectedItem();
					int[] sadrzajiText = sadrzaji.getSelectedIndices();
					
					String[] lista_sadrzaja = null;
					if (sadrzajiText.length != 0) {
						lista_sadrzaja = new String[sadrzajiText.length];
						for (int i = 0; i < sadrzajiText.length; i++) {
							lista_sadrzaja[i] = dodatne_stvari[sadrzajiText[i]];
						}
					}
		
					try {
						if (Integer.parseInt(brojSobeText) <= 0) {
							JOptionPane.showMessageDialog(null, "Potrebno je uneti sve podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
						}
						else if (manageAll.getSobeManager().find(Integer.parseInt(brojSobeText)) != null && Integer.parseInt(brojSobeText) != sobe.getBrojSobe()){
							JOptionPane.showMessageDialog(null, "Ovaj broj sobe se vec koristi.", "Greška", JOptionPane.ERROR_MESSAGE);
						}
						else {
							int spremacica = 0;
							try {
								spremacica = sobe.getSpremacica().getId();
							}
							catch (NullPointerException ex){	
							}
							manageAll.getSobeManager().edit(sobe.getBrojSobe(), Integer.parseInt(brojSobeText), manageAll.getTipSobeManager().get_id(tipSobeText), statusText, spremacica, lista_sadrzaja);
							if (sobe.getBrojSobe() == Integer.parseInt(brojSobeText)) {
								manageAll.getRezervacijeManager().promeniSobe(sobe.getBrojSobe(), Integer.parseInt(brojSobeText));
							}
							if (statusText.equals("SPREMANJE")) {
								manageAll.getSobeManager().dodeliSobuSpremacici(sobe.getBrojSobe());
							}
							JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
							zatvoriProzor();
						}
					}
					catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "Broj sobe mora biti broj.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
				}	
			});
	}
	
	private void zatvoriProzor() {
		this.setVisible(false);
		this.dispose();
	}
}