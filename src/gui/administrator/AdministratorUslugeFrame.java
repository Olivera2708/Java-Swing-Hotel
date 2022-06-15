package gui.administrator;

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
import entity.Usluge;
import gui.models.UslugeModel;
import manage.ManageAll;

public class AdministratorUslugeFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JButton btnAdd;
	JButton btnDelete;
	JButton btnEdit;
	JTable tabela;

	AdministratorUslugeFrame () {
		this.setTitle("Dodatne usluge");
		this.setPreferredSize(new Dimension(500, 350));
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
				
		btnAdd = new JButton("Dodaj");
		btnAdd.setPreferredSize(d);
		btnEdit = new JButton("Izmeni");
		btnEdit.setPreferredSize(d);
		btnDelete = new JButton("Obriši");
		btnDelete.setPreferredSize(d);
		
		panel.add(btnAdd);
		panel.add(btnEdit);
		panel.add(btnDelete);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziTabelu() {
		tabela = new JTable();
		tabela.setModel(new UslugeModel(manageAll.getUslugeManager().getAll()));
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
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorDodajUslugu dodajUslugu = new AdministratorDodajUslugu(tabela);
				dodajUslugu.setVisible(true);
			}
			
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int gost = tabela.getSelectedRow();
				if (gost == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati uslugu iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(gost, 0);
					Usluge izabran = manageAll.getUslugeManager().find(id);
					AdministratorIzmenaUsluga izmenaUsluga = new AdministratorIzmenaUsluga(izabran);
					izmenaUsluga.setVisible(true);
					osveziTabelu();
				}
			}
		});
		
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int gost = tabela.getSelectedRow();
				if (gost == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati uslugu iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(gost, 0);
					areYouSure(id);
				}
			}
		});
	}
	
	private void areYouSure(int id) {
		//Ne moze da se obrise ako se koristi
		boolean moze = true;
		for (Rezervacije r: manageAll.getRezervacijeManager().getAll()) {
			for (Usluge u: r.getUsluge()) {
				if (u.getId() == id) {
					JOptionPane.showMessageDialog(null, "Ova usluga je u upotrebi.", "Greška", JOptionPane.WARNING_MESSAGE);
					moze = false;
					break;
				}
			}
			if (!moze)
				break;
		}
		if (moze) {
			String[] option = new String[2];
			option[0] = "Da";
			option[1] = "Ne";
			int vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da obrišete ovu uslugu?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
			if (vrednost == JOptionPane.YES_OPTION) {
				manageAll.getUslugeManager().remove(id);
				osveziTabelu();
			}	
		}
	}
	
	private void osveziTabelu() {
		tabela.setModel(new UslugeModel(manageAll.getUslugeManager().getAll()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}