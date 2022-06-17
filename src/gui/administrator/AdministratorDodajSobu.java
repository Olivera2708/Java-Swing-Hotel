package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import gui.models.SobeModel;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorDodajSobu extends JFrame{
	private static final long serialVersionUID = 1L;
	ManageAll manageAll = ManageAll.getInstance();
	
	JTable tabela;
	
	JButton btnKreiraj = new JButton("Sačuvaj");
	JTextField brojSobe = new JTextField(40);
	String[] opcije_status = {"SLOBODNA", "ZAUZETO", "SPREMANJE"};
	JComboBox<String> status = new JComboBox<>(opcije_status);
	String[] opcije_tip = manageAll.getTipSobeManager().getNames();
	JComboBox<String> tipSobe = new JComboBox<>(opcije_tip);
	String[] dodatne_stvari = {"Klima", "TV", "Balkon", "Pušačka", "Nepušačka"};
	JList<String> sadrzaji = new JList<String>(dodatne_stvari);
	
	public AdministratorDodajSobu (JTable tabela) {
		this.tabela= tabela;
		sadrzaji.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
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
		
		JLabel naslov = new JLabel("Dodaj sobu");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		panel.add(new JLabel("Broj sobe"));
		panel.add(brojSobe, "right, wrap, grow");
		panel.add(new JLabel("Status"));
		panel.add(status, "right, wrap, grow");
		panel.add(new JLabel("Tip sobe"));
		panel.add(tipSobe, "right, wrap, grow");
		panel.add(new JLabel("Sadržaji"));
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
						else if (manageAll.getSobeManager().find(Integer.parseInt(brojSobeText)) != null){
							JOptionPane.showMessageDialog(null, "Ovaj broj sobe se vec koristi.", "Greška", JOptionPane.ERROR_MESSAGE);
						}
						else {
							manageAll.getSobeManager().add(Integer.parseInt(brojSobeText), manageAll.getTipSobeManager().get_id(tipSobeText), statusText, lista_sadrzaja);
							JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
							zatvoriProzor();
							osveziTabelu();
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
	
	private void osveziTabelu() {
		tabela.setModel(new SobeModel(manageAll.getSobeManager().getAll()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}