package gui.recepcioner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import entity.Rezervacije;
import entity.Usluge;
import enums.EnumStatusSobe;
import gui.models.RezervacijeModel;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class RecepcionerDodelaSobe extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JTable tabela;
	Rezervacije rezervacija;
	String[] opcije_usluge;
	
	JButton btnKreiraj = new JButton("Dodeli");
	JComboBox<Integer> sobe = new JComboBox<>();
	DefaultListModel<String> opcije_usluga = new DefaultListModel<String>();
	JList<String> usluge = new JList<>(opcije_usluga);

	RecepcionerDodelaSobe (JTable tabela, Rezervacije r) {
		this.tabela = tabela;
		this.rezervacija = r;
		
		Integer[] opcije_tip = manageAll.getSobeManager().getSlobodneSobe(rezervacija.getTipSobe().getId(), rezervacija.getSadrzaj()).toArray(new Integer[0]); // koje su slobodne
		if (opcije_tip.length == 0) {
			JOptionPane.showMessageDialog(null, "Nema soba koje zadovoljavaju sve kriterijume, nude se sobe slične sobe.", "Upozorenje", JOptionPane.INFORMATION_MESSAGE);
			opcije_tip = manageAll.getSobeManager().getSlobodneSobeSlicne(rezervacija.getTipSobe().getId()).toArray(new Integer[0]);
		}
		sobe = new JComboBox<>(opcije_tip);
		
		usluge.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		opcije_usluge = manageAll.getRezervacijeManager().getUsluge(r.getOdDatum(), r.getDoDatum()).toArray(new String[0]);
		opcije_usluga.removeAllElements();
		for (String s: opcije_usluge) {
			opcije_usluga.addElement(s);
		}
		
		this.setTitle("Dodela sobe");
		this.setPreferredSize(new Dimension(600, 300));
		this.setResizable(false);
		prikaziNoviZaposleni();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNoviZaposleni() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]", "[]20[]20[]40[]"));
		Border margin = new EmptyBorder(10, 65, 10, 65);
		panel.setBorder(margin);
		
		int[] oznaceni = new int[rezervacija.getUsluge().size()];
		for (int i = 0; i < rezervacija.getUsluge().size(); i++) {
			oznaceni[i] = Arrays.asList(opcije_usluge).indexOf(rezervacija.getUsluge().get(i).getTip());
		}
		usluge.setSelectedIndices(oznaceni);
		
		JLabel naslov = new JLabel("Novi tip sobe");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		JScrollPane skrol = new JScrollPane(usluge);
		skrol.setPreferredSize(new Dimension(80, 60));
		
		panel.add(new JLabel("Broj sobe"));
		panel.add(sobe, "right, wrap, grow");
		panel.add(new JLabel("Usluge (pritisnite cmd za označavanje više usluga)"));
		panel.add(skrol, "right, wrap, grow");
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
			
			btnKreiraj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//upisi u objekat i sacuvaj u bazu
					int sobeBroj = (int) sobe.getSelectedItem();
					
					int[] uslugeText = usluge.getSelectedIndices();
					int[] lista_usluga = new int[uslugeText.length];
					List<Usluge> sve_usluge = manageAll.getUslugeManager().getAll();
					for (int i = 0; i < uslugeText.length; i++) {
						lista_usluga[i] = sve_usluge.get(uslugeText[i]).getId();
					}
					
					manageAll.getRezervacijeManager().dodelaSobe(rezervacija.getId(), sobeBroj);
					manageAll.getSobeManager().find(sobeBroj).setStatus(EnumStatusSobe.ZAUZETO);
					manageAll.getSobeManager().saveData();
					
					JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
					osveziTabelu();
					zatvoriProzor();
				}	
			});
	}
	
	private void zatvoriProzor() {
		this.setVisible(false);
		this.dispose();
	}
	
	private void osveziTabelu() {
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacijePotvrdjene()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}