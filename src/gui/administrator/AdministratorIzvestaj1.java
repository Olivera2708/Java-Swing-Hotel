package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import entity.Zaposleni;
import gui.models.SobaricaRadModel;
import manage.ManageAll;

public class AdministratorIzvestaj1 extends JFrame{
	private static final long serialVersionUID = 1L;
	
	int prihodi;
	int rashodi;
	int brojPotvrdjenih;
	HashMap<Zaposleni, Integer> sobaricaSoba;
	
	JTable tabela;

	ManageAll manageAll = ManageAll.getInstance();
	
	public AdministratorIzvestaj1 (int prihodi, int rashodi, int brojPotvrdjenih, HashMap<Zaposleni, Integer> sobaricaSoba) {
		this.prihodi = prihodi;
		this.rashodi = rashodi;
		this.brojPotvrdjenih = brojPotvrdjenih;
		this.sobaricaSoba = sobaricaSoba;
		
		this.setTitle("Izveštaj 1");
		this.setPreferredSize(new Dimension(500, 300));
		this.setResizable(false);
		prikaziIzvestaj();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void prikaziIzvestaj() {
		tabela = new JTable();
		tabela.setModel(new SobaricaRadModel(sobaricaSoba));
		tabela.setShowGrid(true);
		tabela.setGridColor(Color.GRAY);
		tabela.setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setBackground(Color.GRAY);
		tabela.getTableHeader().setForeground(Color.WHITE);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tabela.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		tabela.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		tabela.setDefaultEditor(Object.class, null);
		
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabela.getTableHeader().setReorderingAllowed(false);
		tabela.setFocusable(false);
		tabela.setRowSelectionAllowed(false);
		
		JScrollPane srcPan_1 = new JScrollPane(tabela);
		srcPan_1.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Prikaz rada sobarica",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		
		JPanel panel = new JPanel(new GridLayout(2,1));
		
		JPanel panel_info = new JPanel(new GridLayout(3,1));
		JLabel prihodiText = new JLabel("Prihodi su " + String.valueOf(prihodi));
		prihodiText.setHorizontalAlignment(JLabel.CENTER);
		panel_info.add(prihodiText);
		
		JLabel rashodiText = new JLabel("Rashodi su " + String.valueOf(rashodi));
		rashodiText.setHorizontalAlignment(JLabel.CENTER);
		panel_info.add(rashodiText);
		
		JLabel potvrdjeneText = new JLabel("Broj potvrdjenih rezervacija je " + String.valueOf(brojPotvrdjenih));
		potvrdjeneText.setHorizontalAlignment(JLabel.CENTER);
		panel_info.add(potvrdjeneText);
		
		panel.add(srcPan_1);
		panel.add(panel_info);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
}