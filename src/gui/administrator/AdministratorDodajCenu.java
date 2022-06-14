package gui.administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import entity.CenaSobe;
import entity.CenaUsluge;
import gui.models.CenovnikSobeModel;
import gui.models.CenovnikUslugeModel;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorDodajCenu extends JFrame{
	private static final long serialVersionUID = 1L;

	ManageAll manageAll = ManageAll.getInstance();
	JTable tabela;
	
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	JButton btnKreiraj = new JButton("Kreiraj");
	String[] opcije_usluge = new String[manageAll.getUslugeManager().getNames().length + manageAll.getTipSobeManager().getNames().length];
	JComboBox<String> usluge = new JComboBox<>(opcije_usluge);
	JFormattedTextField datumOd = new JFormattedTextField(datum_formatter);
	JFormattedTextField datumDo = new JFormattedTextField(datum_formatter);
	JTextField cena = new JTextField(40);
	
	boolean issoba;
	
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";
	private String cenaRegex = "\\d{3,4}";

	AdministratorDodajCenu (JTable tabela, boolean issoba) {
		this.issoba = issoba;
		this.tabela = tabela;
		if (issoba) {
			opcije_usluge = manageAll.getTipSobeManager().getNames();
		}
		else {
			opcije_usluge = manageAll.getUslugeManager().getNames();
		}
		usluge = new JComboBox<>(opcije_usluge);
		
		this.setTitle("Cenovnik");
		this.setPreferredSize(new Dimension(500, 400));
		this.setResizable(false);
		prikaziNoviZaposleni();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNoviZaposleni() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]", "[]20[]"));
		Border margin = new EmptyBorder(10, 10, 10, 10);
		panel.setBorder(margin);
		
		JLabel naslov = new JLabel("Nova cena");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		panel.add(new JLabel("Usluge"));
		panel.add(usluge, "right, wrap, grow");
		panel.add(new JLabel("Datum od"));
		panel.add(datumOd, "right, wrap, grow");
		panel.add(new JLabel("Datum do"));
		panel.add(datumDo, "right, wrap, grow");
		panel.add(new JLabel("Cena"));
		panel.add(cena, "right, wrap, grow");
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
			
			btnKreiraj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//upisi u objekat i sacuvaj u bazu
					String datumOdText = datumOd.getText().trim();
					String datumDoText = datumDo.getText().trim();
					String cenaText = cena.getText().trim();
					String uslugaText = (String) usluge.getSelectedItem();
					
					//provara da se datumi ne preklapaju za iste entitete
					boolean preklapanje = false;
					if (issoba) {
						for (CenaSobe s: manageAll.getCenovnikSobaManager().getAll()) {
							if (s.getTipSobe().getTip().equals(uslugaText)){
								try {
									if ((s.getOdDatum().before(datum_formatter.parse(datumDoText)) && s.getDoDatum().after(datum_formatter.parse(datumOdText)))
											|| (s.getDoDatum().before(datum_formatter.parse(datumDoText)) && s.getDoDatum().after(datum_formatter.parse(datumOdText)))
											|| s.getDoDatum().equals(datum_formatter.parse(datumOdText)) || s.getOdDatum().equals(datum_formatter.parse(datumDoText))) {
										JOptionPane.showMessageDialog(null, "Datumi se preklapaju!", "Greška", JOptionPane.ERROR_MESSAGE);
										preklapanje = true;
									}
								} catch (HeadlessException | ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
					else {
						for (CenaUsluge s: manageAll.getCenovnikUslugaManager().getAll()) {
							if (s.getUsluge().getTip().equals(uslugaText)){
								try {
									if ((s.getOdDatum().before(datum_formatter.parse(datumDoText)) && s.getDoDatum().after(datum_formatter.parse(datumOdText)))
											|| (s.getDoDatum().before(datum_formatter.parse(datumDoText)) && s.getDoDatum().after(datum_formatter.parse(datumOdText)))
											|| s.getDoDatum().equals(datum_formatter.parse(datumOdText)) || s.getOdDatum().equals(datum_formatter.parse(datumDoText))) {
										JOptionPane.showMessageDialog(null, "Datumi se preklapaju!", "Greška", JOptionPane.ERROR_MESSAGE);
										preklapanje = true;
									}
								} catch (HeadlessException | ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
					if (!preklapanje) {
						if (!datumOdText.matches(datumRegex) || !datumDoText.matches(datumRegex)){
							JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
						} else
							try {
								if (datum_formatter.parse(datumOdText).after(datum_formatter.parse(datumDoText))) {
									JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
								}
								else if (!cenaText.matches(cenaRegex)) {
									JOptionPane.showMessageDialog(null, "Loš unos cene.", "Greška", JOptionPane.ERROR_MESSAGE);
								}
								else {
									//u zavisnosti od tipa usluge
									if (!issoba) {
										try {
											manageAll.getCenovnikUslugaManager().add(manageAll.getUslugeManager().get_id(uslugaText), Integer.parseInt(cenaText), datum_formatter.parse(datumOdText), datum_formatter.parse(datumDoText));
											manageAll.getCenovnikSobaManager().appendData();
										} catch (NumberFormatException | ParseException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									else {
										try {
											manageAll.getCenovnikSobaManager().add(manageAll.getTipSobeManager().get_id(uslugaText), Integer.parseInt(cenaText), datum_formatter.parse(datumOdText), datum_formatter.parse(datumDoText));
											manageAll.getCenovnikUslugaManager().appendData();
										} catch (NumberFormatException | ParseException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
									osveziTabelu();
									zatvoriProzor();
								}
							} catch (HeadlessException | ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					}
				}
			});
	}
	
	private void zatvoriProzor() {
		this.setVisible(false);
		this.dispose();
	}
	
	private void osveziTabelu() {
		if (issoba) {
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