package gui.recepcioner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import gui.models.DolasciOdlasciModel;
import manage.ManageAll;

public class RecepcionerStanje extends JFrame{
	private static final long serialVersionUID = 1L;
	ManageAll manageAll = ManageAll.getInstance();
	JButton btnSpremljena;
	JTable tabela;
	
	JLabel zauzetost = new JLabel(); 
	private JTable table;

	RecepcionerStanje () {
		this.setTitle("Sobe");
		this.setPreferredSize(new Dimension(800, 600));
		this.setResizable(false);
		prikaziDugmice();
		prikaziTabelu();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void prikaziDugmice() {
		JToolBar toolBar = new JToolBar();
		JPanel panel = new JPanel();

		panel.add(new JLabel("Trenutna zauzetost soba je "));
		int zauzete = manageAll.getSobeManager().brojZauzetihSoba();
		int ukupno = manageAll.getSobeManager().getAll().size();
		zauzetost.setText(String.valueOf(zauzete) + "/" + String.valueOf(ukupno));
		panel.add(zauzetost);
		
		toolBar.add(panel);
		
		getContentPane().add(toolBar, BorderLayout.SOUTH);
	}
	
	private void prikaziTabelu() {
		//tabela za dolaske
		tabela = new JTable();
		tabela.setModel(new DolasciOdlasciModel(manageAll.getRezervacijeManager().getDolasci()));
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
		
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabela.getTableHeader().setReorderingAllowed(false);
		tabela.setFocusable(false);
		tabela.setRowSelectionAllowed(false);
		
		JScrollPane srcPan_1 = new JScrollPane(tabela);
		
		//tabela za odlaske
		table = new JTable();
		table.setModel(new DolasciOdlasciModel(manageAll.getRezervacijeManager().getOdlasci()));
		table.setShowGrid(true);
		table.setGridColor(Color.GRAY);
		table.setFont(table.getFont().deriveFont(14f));
		table.getTableHeader().setFont(table.getFont().deriveFont(14f));
		table.getTableHeader().setBackground(Color.GRAY);
		table.getTableHeader().setForeground(Color.WHITE);
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.setDefaultEditor(Object.class, null);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.setFocusable(false);
		table.setRowSelectionAllowed(false);
		
		JScrollPane srcPan = new JScrollPane(table);
		srcPan_1.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Današnji dolasci",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		
		JPanel panel = new JPanel(new GridLayout(2,1));
		
		panel.add(srcPan_1);
		srcPan.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Današnji odlasci",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		panel.add(srcPan);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
}