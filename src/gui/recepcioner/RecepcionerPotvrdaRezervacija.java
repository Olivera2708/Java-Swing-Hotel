package gui.recepcioner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import entity.Rezervacije;
import gui.administrator.AdministratorPrikaziRezervaciju;
import gui.models.PotvrdaRezervacijeModel;
import manage.ManageAll;

public class RecepcionerPotvrdaRezervacija extends JFrame{
	private static final long serialVersionUID = 1L;

	private ManageAll manageAll = ManageAll.getInstance();
	private JButton btnPotvrdi;
	private JButton btnOtkazi;
	private JButton btnShow;
	private JTable tabela;
	
	private JTextField tfSearch = new JTextField(20);
	private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();

	RecepcionerPotvrdaRezervacija () {
		this.setTitle("Potvrda rezervacija");
		this.setPreferredSize(new Dimension(800, 600));
		this.setResizable(false);
		prikaziDugmice();
		prikaziTabelu();
		prikaziPretragu();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziDugmice() {
		JToolBar toolBar = new JToolBar();
		JPanel panel = new JPanel();
		Dimension d = new Dimension(150, 20);
				
		btnPotvrdi = new JButton("Potvrdi");
		btnPotvrdi.setPreferredSize(d);
		btnOtkazi = new JButton("Odbij");
		btnOtkazi.setPreferredSize(d);
		btnShow = new JButton("Prikaži");
		btnShow.setPreferredSize(d);
		
		panel.add(btnShow);
		panel.add(btnPotvrdi);
		panel.add(btnOtkazi);
		toolBar.add(panel);
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void prikaziTabelu() {
		tabela = new JTable();
		tabela.setModel(new PotvrdaRezervacijeModel(manageAll.getRezervacijeManager().getRezervacijeNaCekanju()));
		tabela.setShowGrid(true);
		tabela.setGridColor(Color.GRAY);
		tabela.setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setFont(tabela.getFont().deriveFont(14f));
		tabela.getTableHeader().setBackground(Color.GRAY);
		tabela.getTableHeader().setForeground(Color.WHITE);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tabela.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		tabela.setDefaultEditor(Object.class, null);
		JPanel panel = new JPanel(new GridLayout(1,1));
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabela.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.getTableHeader().setReorderingAllowed(false);
		JScrollPane srcPan = new JScrollPane(tabela);
		panel.add(srcPan);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
	
	public void prikaziPretragu() {
		// podesavanje manuelnog sortera tabele, potrebno i za pretragu
		tableSorter.setModel((AbstractTableModel) tabela.getModel());
		tabela.setRowSorter(tableSorter);
		tabela.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// preuzimanje indeksa kolone potrebnog za sortiranje
				int index = tabela.getTableHeader().columnAtPoint(arg0.getPoint());
				// call abstract sort method
				sort(index);
			}
			
		});
		// podesavanje pretrage 
		JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.CENTER));		
		pSearch.setBackground(Color.GRAY);
		pSearch.add(new JLabel("Search:"));
		pSearch.add(tfSearch);
		
		add(pSearch, BorderLayout.SOUTH);
		
		tfSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//System.out.println("~ "+tfSearch.getText());
				if (tfSearch.getText().trim().length() == 0) {
				     tableSorter.setRowFilter(null);
				  } else {
					  tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + tfSearch.getText().trim()));
				  }
			}
		});
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
		
		btnPotvrdi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati rezervaciju iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					areYouSure(id, true);
				}
			}
		});
		
		
		btnOtkazi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int zaposleni = tabela.getSelectedRow();
				if (zaposleni == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati rezervaciju iz tabele.", "Greška", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int id = (int) tabela.getValueAt(zaposleni, 0);
					areYouSure(id, false);
				}
			}
		});
	}
		
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {{put(0, 1);put(1, 1);put(2, 1);put(3, 1);put(4, 1);put(5,1);}};
	
	protected void sort(int index) {
		// index of table column
		manageAll.getRezervacijeManager().getRezervacijeNaCekanju().sort(new Comparator<Rezervacije>() {
			int retVal = 0;
			@Override
			public int compare(Rezervacije o1, Rezervacije o2) {
				switch (index) {
				case 0:
					retVal = ((Integer)o1.getId()).compareTo((Integer)o2.getId());
					break;
				case 1:
					retVal = o1.getTipSobe().getTip().compareTo(o2.getTipSobe().getTip());
					break;
				case 2:
					retVal = o1.getOdDatum().compareTo(o2.getOdDatum());
					break;
				case 3:
					retVal = o1.getDoDatum().compareTo(o2.getDoDatum());
					break;
				case 4:
					int cenaO1 = o1.getCena();
					if (String.valueOf(o1.getStatus()).equals("ODBIJENA")) {
						cenaO1 = 0;
					}
					int cenaO2 = o2.getCena();
					if (String.valueOf(o2.getStatus()).equals("ODBIJENA")) {
						cenaO2 = 0;
					}
					retVal = ((Integer)cenaO1).compareTo((Integer)cenaO2);
					break;
				case 5:
					retVal = ((Integer)o1.getUsluge().size()).compareTo((Integer)o2.getUsluge().size());
					break;
				default:
					break;
				}
				return retVal*sortOrder.get(index);
			}
		});
		
		sortOrder.put(index, sortOrder.get(index)*-1);
		osveziTabelu(true);	
	}
	
	private void areYouSure(int id, boolean potvrda) {
		String[] option = new String[2];
		option[0] = "Da";
		option[1] = "Ne";
		int vrednost;
		Rezervacije ir = manageAll.getRezervacijeManager().find(id);
		
		int[] lista_usluga = new int[ir.getUsluge().size()];
		for (int i =0; i < ir.getUsluge().size(); i++) {
			lista_usluga[i] = ir.getUsluge().get(i).getId();
		}
		//provara jel ima slobodnih soba
		if (manageAll.getRezervacijeManager().brojSlobodnihSoba(ir.getTipSobe().getId(), ir.getOdDatum(), ir.getDoDatum(), ir.getSadrzaj()) == 0) {
			JOptionPane.showMessageDialog(null, "Ne postoji slobodna soba za ovaj period.", "Greška", JOptionPane.WARNING_MESSAGE);
		}
		else {
			if (potvrda) {
				vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da potvrdite rezervaciju?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
			}
			else {
				vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da odbijete rezervaciju?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
			}
			if (vrednost == JOptionPane.YES_OPTION) {
				if (potvrda) {
					manageAll.getRezervacijeManager().edit(id, ir.getTipSobe().getId(), lista_usluga, ir.getGost().getId(), ir.getOdDatum(), ir.getDoDatum(), "POTVRDJENA", null);
				}
				else {
					manageAll.getRezervacijeManager().edit(id, ir.getTipSobe().getId(), lista_usluga, ir.getGost().getId(), ir.getOdDatum(), ir.getDoDatum(), "ODBIJENA", null);
				}
				manageAll.getRezervacijeManager().dodajDatumKraja(ir);
				osveziTabelu(false);
			}	
		}
	}
	
	private void osveziTabelu(boolean search) {
		if (search) {
			tabela.setModel(this.tabela.getModel());
		}
		else {
			tabela.setModel(new PotvrdaRezervacijeModel(manageAll.getRezervacijeManager().getRezervacijeNaCekanju()));
			tableSorter.setModel((AbstractTableModel) tabela.getModel());
		}
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tabela.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	}
}