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
import entity.Sobe;
import gui.models.SobeModel;
import manage.ManageAll;

public class AdministratorSobeFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JButton btnAdd;
	JButton btnDelete;
	JButton btnEdit;
	JTable tabela;

	AdministratorSobeFrame () {
		this.setTitle("Sobe");
		this.setPreferredSize(new Dimension(900, 600));
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
		tabela.setModel(new SobeModel(manageAll.getSobeManager().getAll()));
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
				AdministratorDodajSobu dodajSobu = new AdministratorDodajSobu(tabela);
				dodajSobu.setVisible(true);
			}
			
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati sobu iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					Sobe izabran = manageAll.getSobeManager().find(id);
					AdministratorIzmenaSobe izmenaSobe = new AdministratorIzmenaSobe(izabran);
					izmenaSobe.setVisible(true);
					osveziTabelu();
				}
			}
		});
		
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati sobu iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					areYouSure(id);
				}
			}
		});
	}
	
	private void areYouSure(int id) {
		boolean moze = true;
		for (Rezervacije r: manageAll.getRezervacijeManager().getAll()) {
			int brojSobe = 0;
			try {
				brojSobe = r.getSoba().getBrojSobe();
			}
			catch (NullPointerException e) {
				
			}
			if (brojSobe == id) {
				JOptionPane.showMessageDialog(null, "Ova soba je u rezervisanim.", "Greška", JOptionPane.WARNING_MESSAGE);
				moze = false;
				break;
			}
		}
		if (moze) {
			String[] option = new String[2];
			option[0] = "Da";
			option[1] = "Ne";
			int vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da obrišete sobu?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
			if (vrednost == JOptionPane.YES_OPTION) {
				manageAll.getSobeManager().remove(id);
				osveziTabelu();
			}	
		}
	}
	
	private void osveziTabelu() {
		tabela.setModel(new SobeModel(manageAll.getSobeManager().getAll()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}