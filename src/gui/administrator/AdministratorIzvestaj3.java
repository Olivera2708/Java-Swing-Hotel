package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import entity.Sobe;
import gui.models.SobeIzvestajModel;
import manage.ManageAll;

public class AdministratorIzvestaj3 extends JFrame{
	private static final long serialVersionUID = 1L;
	
	HashMap<Sobe, Integer[]> sobe;
	
	JTable tabela;

	ManageAll manageAll = ManageAll.getInstance();
	
	public AdministratorIzvestaj3 (HashMap<Sobe, Integer[]> sobe) {
		this.sobe = sobe;
		
		this.setTitle("Izveštaj 3");
		this.setPreferredSize(new Dimension(700, 400));
		this.setResizable(false);
		prikaziIzvestaj();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void prikaziIzvestaj() {
		tabela = new JTable();
		tabela.setModel(new SobeIzvestajModel(sobe));
		tabela.setShowGrid(true);
		tabela.setGridColor(Color.GRAY);
		tabela.setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setBackground(Color.GRAY);
		tabela.getTableHeader().setForeground(Color.WHITE);
		tabela.setDefaultEditor(Object.class, null);
		
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabela.getTableHeader().setReorderingAllowed(false);
		tabela.setFocusable(false);
		tabela.setRowSelectionAllowed(false);
		
		JScrollPane srcPan_1 = new JScrollPane(tabela);
		srcPan_1.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Sobe",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		
		JPanel panel = new JPanel(new GridLayout(1,1));
		
		panel.add(srcPan_1);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
}