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

import entity.Zaposleni;
import gui.models.ZaposleniModel;
import manage.ManageAll;

public class AdministratorZaposleniFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JButton btnAdd;
	JButton btnDelete;
	JButton btnEdit;
	JButton btnShow;
	JTable tabela;
	
	Zaposleni admin;

	AdministratorZaposleniFrame (Zaposleni admin) {
		this.admin = admin;
		
		this.setTitle("Zaposleni");
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
		btnShow = new JButton("Prikaži");
		btnShow.setPreferredSize(d);
		
		panel.add(btnShow);
		panel.add(btnAdd);
		panel.add(btnEdit);
		panel.add(btnDelete);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziTabelu() {
		tabela = new JTable();
		tabela.setModel(new ZaposleniModel(manageAll.getZaposleniManager().getAll()));
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
				AdministratorDodajZaposlenog dodajZaposlenog = new AdministratorDodajZaposlenog(tabela);
				dodajZaposlenog.setVisible(true);
			}
			
		});
		
		btnShow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati zaposlenog iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					Zaposleni izabran = manageAll.getZaposleniManager().find(id);
					AdministratorPrikaziZaposlenog prikaziZaposlenog = new AdministratorPrikaziZaposlenog(izabran);
					prikaziZaposlenog.setVisible(true);
				}
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati zaposlenog iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					Zaposleni izabran = manageAll.getZaposleniManager().find(id);
					AdministratorIzmenaZaposlenog izmenaZaposlenog = new AdministratorIzmenaZaposlenog(izabran);
					izmenaZaposlenog.setVisible(true);
					osveziTabelu();
				}
			}
		});
		
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati zaposlenog iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else if (manageAll.getZaposleniManager().brojSpremacica() == 1) {
						JOptionPane.showMessageDialog(null, "Mora postojati bar jedna sobarica.", "Greška", JOptionPane.WARNING_MESSAGE);
					}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					if (admin.getId() == id) {
						JOptionPane.showMessageDialog(null, "Ne možete obrisati sebe.", "Greška", JOptionPane.WARNING_MESSAGE);
					}
					else if (manageAll.getSobeManager().getPosao(manageAll.getZaposleniManager().find(id)).isEmpty()) {
						areYouSure(id);
					}
					else {
						JOptionPane.showMessageDialog(null, "Sobarica ima posao koji mora da obavi.", "Greška", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
	}
	
	private void areYouSure(int id) {
		String[] option = new String[2];
		option[0] = "Da";
		option[1] = "Ne";
		int vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da obrišete zaposlenog?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
		if (vrednost == JOptionPane.YES_OPTION) {
			manageAll.getZaposleniManager().remove(id);
			osveziTabelu();
		}	
	}
	
	private void osveziTabelu() {
		tabela.setModel(new ZaposleniModel(manageAll.getZaposleniManager().getAll()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}