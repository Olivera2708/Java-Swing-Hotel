package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import entity.Usluge;
import gui.models.RezervacijeModel;
import gui.models.TipSobaModel;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorDodajRezervaciju extends JFrame{
	private static final long serialVersionUID = 1L;
	JTable tabela;

	ManageAll manageAll = ManageAll.getInstance();
	
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	JButton btnKreiraj = new JButton("Sačuvaj");
	JTextField brojSobe = new JTextField(40);
	String[] opcije_status = {"NA_CEKANJU", "POTVRDJENA", "ODBIJENA", "OTKAZANA"};
	JComboBox<String> status = new JComboBox<>(opcije_status);
	String[] opcije_usluge = manageAll.getUslugeManager().getNames();
	JList<String> usluge = new JList<>(opcije_usluge);
	String[] opcije_tip = manageAll.getTipSobeManager().getNames();
	JComboBox<String> tipSobe = new JComboBox<>(opcije_tip);
	String[] opcije_korisnik = manageAll.getGostManager().getNames();
	JComboBox<String> korisnik = new JComboBox<>(opcije_korisnik);
	JFormattedTextField datumOd = new JFormattedTextField(datum_formatter);
	JFormattedTextField datumDo = new JFormattedTextField(datum_formatter);
	
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";
	
	public AdministratorDodajRezervaciju (JTable tabela) {
		this.tabela = tabela;
		usluge.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		this.setTitle("Rezervacije");
		this.setPreferredSize(new Dimension(700, 500));
		this.setResizable(false);
		prikaziNovigost();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNovigost() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]20[]", "[]40[][][][][][]40[]"));
		Border margin = new EmptyBorder(30, 10, 10, 10);
		panel.setBorder(margin);
		
		JLabel naslov = new JLabel("Dodavanje nove rezervacije");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		panel.add(new JLabel("Tip sobe"));
		panel.add(tipSobe, "right, wrap, grow");
		panel.add(new JLabel("Usluge (pritisnite cmd za označavanje više usluga)"));
		panel.add(usluge, "right, wrap, grow");
		panel.add(new JLabel("Gost"));
		panel.add(korisnik, "right, wrap, grow");
		panel.add(new JLabel("Datum od"));
		panel.add(datumOd, "right, wrap, grow");
		panel.add(new JLabel("Datum do"));
		panel.add(datumDo, "right, wrap, grow");
		panel.add(new JLabel("Status"));
		panel.add(status, "right, wrap, grow");
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
			btnKreiraj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//upisi u objekat i sacuvaj u bazu
					String statusText = (String) status.getSelectedItem();
					String korisnikText = (String) korisnik.getSelectedItem();
					int[] uslugeText = usluge.getSelectedIndices();
					String tipSobeText = (String) tipSobe.getSelectedItem();
					String datumOdText = datumOd.getText();
					String datumDoText = datumDo.getText();
					
					int[] lista_usluga = new int[uslugeText.length];
					List<Usluge> sve_usluge = manageAll.getUslugeManager().getAll();
					for (int i = 0; i < uslugeText.length; i++) {
						lista_usluga[i] = sve_usluge.get(uslugeText[i]).getId();
					}
					
					
					if (!datumOdText.matches(datumRegex) || !datumDoText.matches(datumRegex)){
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
					} else
						try {
							if (datum_formatter.parse(datumOdText).after(datum_formatter.parse(datumDoText))) {
								JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
							}
							else {
								try {
									manageAll.getRezervacijeManager().add(manageAll.getTipSobeManager().get_id(tipSobeText), lista_usluga, manageAll.getGostManager().get_id(korisnikText), datum_formatter.parse(datumOdText), datum_formatter.parse(datumDoText), statusText);
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
								zatvoriProzor();
								osveziTabelu();
							}
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}	
			});
	}
	
	private void zatvoriProzor() {
		this.setVisible(false);
		this.dispose();
	}
	
	private void osveziTabelu() {
		tabela.setModel(new RezervacijeModel(manageAll.getRezervacijeManager().getAll()));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	}
}