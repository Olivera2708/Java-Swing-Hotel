package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import gui.models.TipSobaModel;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorDodajTipSobe extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JTable tabela;
	
	JButton btnKreiraj = new JButton("Kreiraj");
	JTextField tipSobe = new JTextField(100);

	AdministratorDodajTipSobe (JTable tabela) {
		this.tabela = tabela;
		
		this.setTitle("Tip sobe");
		this.setPreferredSize(new Dimension(300, 200));
		this.setResizable(false);
		prikaziNoviZaposleni();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNoviZaposleni() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]", "[]20[]"));
		Border margin = new EmptyBorder(10, 10, 10, 10);
		panel.setBorder(margin);
		
		JLabel naslov = new JLabel("Novi tip sobe");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		panel.add(new JLabel("Opis"));
		panel.add(tipSobe, "right, wrap, grow");
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
			
			btnKreiraj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//upisi u objekat i sacuvaj u bazu
					String opisText = tipSobe.getText().trim();
					
					if (opisText.equals("")) {
						JOptionPane.showMessageDialog(null, "Potrebno je uneti sve podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else {
						manageAll.getTipSobeManager().add(opisText);
						JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
						osveziTabelu();
						zatvoriProzor();
					}
				}	
			});
	}
	
	private void zatvoriProzor() {
		this.setVisible(false);
		this.dispose();
	}
	
	private void osveziTabelu() {
		tabela.setModel(new TipSobaModel(manageAll.getTipSobeManager().getAll()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}