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
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import entity.TipSobe;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorIzmenaTipaSobe extends JFrame{
	private static final long serialVersionUID = 1L;
	TipSobe tipSobe;

	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnKreiraj = new JButton("Sačuvaj");
	JTextField tipSobeField = new JTextField(40);
	
	public AdministratorIzmenaTipaSobe (TipSobe tipSobe) {
		this.tipSobe = tipSobe;
		
		this.setTitle("Tip sobe");
		this.setPreferredSize(new Dimension(300, 200));
		this.setResizable(false);
		prikaziNovigost();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNovigost() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]", "[]20[]"));
		Border margin = new EmptyBorder(10, 10, 10, 10);
		panel.setBorder(margin);
		
		JLabel naslov = new JLabel("Izmena podataka o vrsti soba");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		tipSobeField.setText(tipSobe.getTip());
		
		panel.add(new JLabel("Opis"));
		panel.add(tipSobeField, "right, wrap, grow");
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
			btnKreiraj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//upisi u objekat i sacuvaj u bazu
					String tipSobeText = tipSobeField.getText().trim();
					
					if (tipSobeText.equals("")) {
						JOptionPane.showMessageDialog(null, "Potrebno je uneti sve podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					else {
						manageAll.getTipSobeManager().edit(tipSobe.getId(), tipSobeText);
						JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
						zatvoriProzor();
					}
				}	
			});
	}
	
	private void zatvoriProzor() {
		this.setVisible(false);
		this.dispose();
	}
}