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
import gui.models.RezervacijeModel;
import manage.ManageAll;

public class RecepcionerCheckIn extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JButton btnDodela;
	JTable tabela;

	RecepcionerCheckIn () {
		this.setTitle("Check in");
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
				
		btnDodela = new JButton("Dodeli sobu");
		btnDodela.setPreferredSize(d);
		
		panel.add(btnDodela);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziTabelu() {
		tabela = new JTable();
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacijePotvrdjene()));
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
		btnDodela.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati rezervaciju iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					Rezervacije izabran = manageAll.getRezervacijeManager().find(id);
					RecepcionerDodelaSobe prikaziRezervacije = new RecepcionerDodelaSobe(tabela, izabran);
					prikaziRezervacije.setVisible(true);
					osveziTabelu();
				}
			}
		});
	}
	
	private void osveziTabelu() {
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacijePotvrdjene()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}