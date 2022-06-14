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

import entity.CenaSobe;
import entity.CenaUsluge;
import gui.models.CenovnikSobeModel;
import gui.models.CenovnikUslugeModel;
import manage.ManageAll;

public class AdministratorCenovnikFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JButton btnAdd;
	JButton btnDelete;
	JButton btnEdit;
	JButton vrsta;
	JTable tabela;
	
	boolean issobe;

	AdministratorCenovnikFrame (boolean issobe) {
		this.issobe = issobe;
		this.setTitle("Cenovnik");
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
				
		btnAdd = new JButton("Dodaj");
		btnAdd.setPreferredSize(d);
		btnEdit = new JButton("Izmeni");
		btnEdit.setPreferredSize(d);
		btnDelete = new JButton("Obriši");
		btnDelete.setPreferredSize(d);
		vrsta = new JButton("Drugi cenovnik");
		vrsta.setPreferredSize(d);
		
		panel.add(btnAdd);
		panel.add(btnEdit);
		panel.add(btnDelete);
		panel.add(vrsta);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziTabelu() {
		tabela = new JTable();
		if (issobe) {
			tabela.setModel(new CenovnikSobeModel(manageAll.getCenovnikSobaManager().getAll()));
		}
		else {
			tabela.setModel(new CenovnikUslugeModel(manageAll.getCenovnikUslugaManager().getAll()));
		}
		tabela.setShowGrid(true);
		tabela.setGridColor(Color.GRAY);
		tabela.setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setBackground(Color.GRAY);
		tabela.getTableHeader().setForeground(Color.WHITE);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
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
				AdministratorDodajCenu dodajCenu = new AdministratorDodajCenu(tabela, issobe);
				dodajCenu.setVisible(true);
			}
			
		});
		
		vrsta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				issobe = !issobe;
				osveziTabelu();
			}
			
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int gost = tabela.getSelectedRow();
				if (gost == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati cenu iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if (issobe) {
						CenaSobe izabran = manageAll.getCenovnikSobaManager().getAll().get(gost);
						AdministratorIzmeniCenu izmenaCenu = new AdministratorIzmeniCenu(tabela, issobe, null, izabran);
						izmenaCenu.setVisible(true);
						osveziTabelu();
					}
					else {
						CenaUsluge izabran = manageAll.getCenovnikUslugaManager().getAll().get(gost);
						AdministratorIzmeniCenu izmenaCenu = new AdministratorIzmeniCenu(tabela, issobe, izabran, null);
						izmenaCenu.setVisible(true);
						osveziTabelu();
					}
				}
			}
		});
		
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int gost = tabela.getSelectedRow();
				if (gost == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati cenu iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					areYouSure(gost);
				}
			}
		});
	}
	
	private void areYouSure(int id) {
		String[] option = new String[2];
		option[0] = "Da";
		option[1] = "Ne";
		int vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da obrišete cenu?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
		if (vrednost == JOptionPane.YES_OPTION) {
			if (issobe) {
				manageAll.getCenovnikSobaManager().remove(id);
				manageAll.getCenovnikUslugaManager().appendData();
			}
			else{
				manageAll.getCenovnikUslugaManager().remove(id);
				manageAll.getCenovnikSobaManager().appendData();
			}
			osveziTabelu();
		}	
	}
	
	private void osveziTabelu() {
		if (issobe) {
			tabela.setModel(new CenovnikSobeModel(manageAll.getCenovnikSobaManager().getAll()));
		}
		else {
			tabela.setModel(new CenovnikUslugeModel(manageAll.getCenovnikUslugaManager().getAll()));
		}
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	}
}