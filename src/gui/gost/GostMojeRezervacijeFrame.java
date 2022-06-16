package gui.gost;

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

import entity.Gost;
import entity.Rezervacije;
import gui.administrator.AdministratorPrikaziRezervaciju;
import gui.models.RezervacijeModel;
import manage.ManageAll;

public class GostMojeRezervacijeFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	Gost gost;

	ManageAll manageAll = ManageAll.getInstance();
	JButton btnDelete;
	JButton btnShow;
	JTable tabela;

	GostMojeRezervacijeFrame (Gost gost) {
		this.gost = gost;
		
		this.setTitle("Moje rezervacije");
		this.setPreferredSize(new Dimension(800, 600));
		this.setResizable(false);
		prikaziDugmice();
		prikaziTabelu();
		prikaziUkupanTrosak();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziDugmice() {
		JToolBar toolBar = new JToolBar();
		JPanel panel = new JPanel();
		Dimension d = new Dimension(150, 20);
				
		btnDelete = new JButton("Otkaži");
		btnDelete.setPreferredSize(d);
		btnShow = new JButton("Prikaži");
		btnShow.setPreferredSize(d);
		
		panel.add(btnShow);
		panel.add(btnDelete);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziTabelu() {
		tabela = new JTable();
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacije(gost)));
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
	
	private void prikaziUkupanTrosak() {
		int trosak = 0;
		for (Rezervacije r: manageAll.getRezervacijeManager().getRezervacije(gost)) {
			if (String.valueOf(r.getStatus()).equals("ODBIJENA")) {
				continue;
			}
			trosak += r.getCena();
		}
		JToolBar toolBar = new JToolBar();
		JPanel panel = new JPanel();
		panel.add(new JLabel("Ukupni trošak iznosi"));
		panel.add(new JLabel(String.valueOf(trosak) + " dinara"));
		toolBar.add(panel);
		add(toolBar, BorderLayout.SOUTH);
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
		
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati rezervaciju iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					areYouSure(id);
				}
			}
		});
	}
	
	private void areYouSure(int id) {
		String[] option = new String[2];
		option[0] = "Da";
		option[1] = "Ne";
		int vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da otkažete rezervaciju?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
		if (vrednost == JOptionPane.YES_OPTION) {
			//update rezeraciju na OTKAZANA
			Rezervacije ir = manageAll.getRezervacijeManager().find(id);
			
			int[] lista_usluga = new int[ir.getUsluge().size()];
			for (int i =0; i < ir.getUsluge().size(); i++) {
				lista_usluga[i] = ir.getUsluge().get(i).getId();
			}
			
			manageAll.getRezervacijeManager().edit(id, ir.getTipSobe().getId(), lista_usluga, gost.getId(), ir.getOdDatum(), ir.getDoDatum(), "OTKAZANA", null);
			manageAll.getRezervacijeManager().dodajDatumKraja(ir);
			osveziTabelu();
		}	
	}
	
	private void osveziTabelu() {
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacije(gost)));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}