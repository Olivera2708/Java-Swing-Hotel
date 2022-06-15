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

public class RecepcionerCheckOut extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JButton btnCheckOut;
	JTable tabela;

	RecepcionerCheckOut() {
		this.setTitle("Check out");
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
				
		btnCheckOut = new JButton("Check out");
		btnCheckOut.setPreferredSize(d);
		
		panel.add(btnCheckOut);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziTabelu() {
		tabela = new JTable();
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacijePotvrdjeneCheckOut()));
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
		btnCheckOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati rezervaciju iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					Rezervacije izabran = manageAll.getRezervacijeManager().find(id);
					//status sobe u spremanje
					manageAll.getSobeManager().dodeliSobuSpremacici(izabran.getSoba().getBrojSobe());
					osveziTabelu();
				}
			}
		});
	}
	
	private void osveziTabelu() {
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getRezervacijePotvrdjeneCheckOut()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}