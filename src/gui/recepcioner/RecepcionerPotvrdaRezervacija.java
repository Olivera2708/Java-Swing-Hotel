package gui.recepcioner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import entity.Rezervacije;
import gui.administrator.AdministratorPrikaziRezervaciju;
import gui.models.RezervacijeModel;
import manage.ManageAll;

public class RecepcionerPotvrdaRezervacija extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JButton btnPotvrdi;
	JButton btnOtkazi;
	JButton btnShow;
	JTable tabela;

	RecepcionerPotvrdaRezervacija () {
		this.setTitle("Potvrda rezervacija");
		this.setPreferredSize(new Dimension(800, 600));
		this.setResizable(false);
		prikaziDugmice();
		prikaziTabelu();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziDugmice() {
		JToolBar toolBar = new JToolBar();
		JPanel panel = new JPanel();
		Dimension d = new Dimension(150, 20);
				
		btnPotvrdi = new JButton("Potvrdi");
		btnPotvrdi.setPreferredSize(d);
		btnOtkazi = new JButton("Odbij");
		btnOtkazi.setPreferredSize(d);
		btnShow = new JButton("Prikaži");
		btnShow.setPreferredSize(d);
		
		panel.add(btnShow);
		panel.add(btnPotvrdi);
		panel.add(btnOtkazi);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziTabelu() {
		tabela = new JTable();
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacijeNaCekanju()));
		tabela.setShowGrid(true);
		tabela.setGridColor(Color.GRAY);
		tabela.setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setBackground(Color.GRAY);
		tabela.getTableHeader().setForeground(Color.WHITE);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tabela.setDefaultEditor(Object.class, null);
		JPanel panel = new JPanel(new GridLayout(1,1));
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabela.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.getTableHeader().setReorderingAllowed(false);
		JScrollPane srcPan = new JScrollPane(tabela);
		panel.add(srcPan);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
	
	private void allButtons() {
		btnShow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati rezervaciju iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					Rezervacije izabran = manageAll.getRezervacijeManager().find(id);
					AdministratorPrikaziRezervaciju prikaziRezervacije = new AdministratorPrikaziRezervaciju(izabran);
					prikaziRezervacije.setVisible(true);
				}
			}
		});
		
		btnPotvrdi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati rezervaciju iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					areYouSure(id, true);
				}
			}
		});
		
		
		btnOtkazi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati rezervaciju iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					areYouSure(id, false);
				}
			}
		});
	}
	
	private void areYouSure(int id, boolean potvrda) {
		String[] option = new String[2];
		option[0] = "Da";
		option[1] = "Ne";
		int vrednost;
		Rezervacije ir = manageAll.getRezervacijeManager().find(id);
		
		int[] lista_usluga = new int[ir.getUsluge().size()];
		for (int i =0; i < ir.getUsluge().size(); i++) {
			lista_usluga[i] = ir.getUsluge().get(i).getId();
		}
		//provara jel ima slobodnih soba
		if (manageAll.getRezervacijeManager().brojSlobodnihSoba(ir.getTipSobe().getId(), ir.getOdDatum(), ir.getDoDatum()) == 0) {
			JOptionPane.showMessageDialog(null, "Ne postoji slobodna soba za ovaj period.", "Greška", JOptionPane.WARNING_MESSAGE);
		}
		else {
			if (potvrda) {
				vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da potvrdite rezervaciju?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
			}
			else {
				vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da odbijete rezervaciju?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
			}
			if (vrednost == JOptionPane.YES_OPTION) {
				if (potvrda) {
					manageAll.getRezervacijeManager().edit(id, ir.getTipSobe().getId(), lista_usluga, ir.getGost().getId(), ir.getOdDatum(), ir.getDoDatum(), "POTVRDJENA", null);
				}
				else {
					manageAll.getRezervacijeManager().edit(id, ir.getTipSobe().getId(), lista_usluga, ir.getGost().getId(), ir.getOdDatum(), ir.getDoDatum(), "ODBIJENA", null);
				}
				manageAll.getRezervacijeManager().dodajDatumKraja(ir);
				osveziTabelu();
			}	
		}
	}
	
	private void osveziTabelu() {
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacijeNaCekanju()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}