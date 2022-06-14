package gui.gost;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import entity.Gost;
import entity.Usluge;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class GostNovaRezervacijaFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	Gost gost;

	ManageAll manageAll = ManageAll.getInstance();
	
	SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	JButton btnKreiraj = new JButton("Sačuvaj");
	JButton btnPretraga = new JButton("Pretraži");
	JButton btnIzracunaj = new JButton("Izračunaj");
	JLabel cena = new JLabel();
	DefaultListModel<String> opcije_usluga = new DefaultListModel<String>();
	JList<String> usluge = new JList<>(opcije_usluga);
	JComboBox<String> tipSobe = new JComboBox<>();
	JFormattedTextField datumOd = new JFormattedTextField(datum_formatter);
	JFormattedTextField datumDo = new JFormattedTextField(datum_formatter);
	
	private String datumRegex = "\\d{4}-\\d{2}-\\d{2}";
	
	public GostNovaRezervacijaFrame (Gost gost) {
		this.gost = gost;
		usluge.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		this.setTitle("Rezervacije");
		this.setPreferredSize(new Dimension(800, 500));
		this.setResizable(false);
		prikaziNovigost();
		this.pack();
		this.setLocationRelativeTo(null);
		allButtons();
	}
	
	private void prikaziNovigost() {
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[]20[]", "[]40[][]20[]10[][]20[]20[]20[]"));
		Border margin = new EmptyBorder(10, 120, 10, 10);
		panel.setBorder(margin);
		
		Dimension d = new Dimension(100, 10);
		datumOd.setPreferredSize(d);
		datumDo.setPreferredSize(d);
		
		JLabel naslov = new JLabel("Dodavanje nove rezervacije");
		naslov.setFont(naslov.getFont().deriveFont(18f));
		panel.add(naslov, "center, span 2");
		
		panel.add(new JLabel("Datum od"));
		panel.add(datumOd, "right, wrap, grow");
		panel.add(new JLabel("Datum do"));
		panel.add(datumDo, "right, wrap, grow");
		
		panel.add(btnPretraga, "center, span 2");
		
		panel.add(new JLabel("Tip sobe"));
		panel.add(tipSobe, "right, wrap, grow");
		panel.add(new JLabel("Usluge (pritisnite cmd za označavanje više usluga)"));
		panel.add(usluge, "right, wrap, grow");
		
		panel.add(btnIzracunaj, "center, span 2");
		panel.add(new JLabel("Cena"));
		panel.add(cena, "right, wrap, grow");
		
		panel.add(btnKreiraj, "center, span 2");
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
		
	private void allButtons() {
		btnIzracunaj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] uslugeText = usluge.getSelectedIndices();
				String tipSobeText = (String) tipSobe.getSelectedItem();
				String datumOdText = datumOd.getText();
				String datumDoText = datumDo.getText();
				
				int[] lista_usluga = new int[uslugeText.length];
				List<Usluge> sve_usluge = manageAll.getUslugeManager().getAll();
				for (int i = 0; i < uslugeText.length; i++) {
					lista_usluga[i] = sve_usluge.get(uslugeText[i]).getId();
				}
				try {
					cena.setText(String.valueOf(manageAll.getRezervacijeManager().cena(manageAll.getTipSobeManager().get_id(tipSobeText), lista_usluga, datum_formatter.parse(datumOdText), datum_formatter.parse(datumDoText), false)));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		btnPretraga.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String datumOdText = datumOd.getText();
				String datumDoText = datumDo.getText();
				boolean greska = false;
				try {
					if (datum_formatter.parse(datumOdText).after(datum_formatter.parse(datumDoText)) || datum_formatter.parse(datumOdText).equals(datum_formatter.parse(datumDoText)) || datum_formatter.parse(datumOdText).before(new java.util.Date())) {
						JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
						greska = true;
					}
				} catch (HeadlessException | ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if (!greska) {
					String[] opcije_tip;
					try {
						opcije_tip = manageAll.getRezervacijeManager().getSlobodneSobe(datum_formatter.parse(datumOdText), datum_formatter.parse(datumDoText)).toArray(new String[0]);
						tipSobe.removeAllItems();
						if (opcije_tip.length == 0) {
							JOptionPane.showMessageDialog(null, "Ne postoje slobodne sobe za izabrani datum.", "Informacija", JOptionPane.INFORMATION_MESSAGE);
						}
						for (String s: opcije_tip) {
							tipSobe.addItem(s);
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String[] opcije_usluge;
					try {
						opcije_usluge = manageAll.getRezervacijeManager().getUsluge(datum_formatter.parse(datumOdText), datum_formatter.parse(datumDoText)).toArray(new String[0]);
						opcije_usluga.removeAllElements();
						for (String s: opcije_usluge) {
							opcije_usluga.addElement(s);
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		});
		
		btnKreiraj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean proslo = true;
				//upisi u objekat i sacuvaj u bazu
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
						if (datum_formatter.parse(datumOdText).after(datum_formatter.parse(datumDoText)) || datum_formatter.parse(datumOdText).before(new java.util.Date())) {
							JOptionPane.showMessageDialog(null, "Loš unos datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
						}
						else {
							try {
								proslo = manageAll.getRezervacijeManager().add(manageAll.getTipSobeManager().get_id(tipSobeText), lista_usluga, gost.getId(), datum_formatter.parse(datumOdText), datum_formatter.parse(datumDoText), "NA_CEKANJU");
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (proslo) {
								JOptionPane.showMessageDialog(null, "Uspešno", "Informacija", JOptionPane.INFORMATION_MESSAGE);
								zatvoriProzor();
							}
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
}