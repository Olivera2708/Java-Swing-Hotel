package gui.administrator;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import entity.Zaposleni;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Zaposleni administrator;
	
	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnOdjava;
	JButton btnSobe;
	JButton btnCenovnik;
	JButton btnTipoviSoba;
	JButton btnUsluge;
	JButton btnGosti;
	JButton btnRezervacije;
	JButton btnIzvestaji;
	JButton btnZaposleni;
	
	
	
	public AdministratorFrame (Zaposleni administrator) {
		this.administrator = administrator;
		
		this.setTitle("Administator " + administrator.getIme() + " " + administrator.getPrezime());
		this.setPreferredSize(new Dimension(1000, 700));
		this.setResizable(false);
		AdministratorGUI();
		this.pack();
		this.setLocationRelativeTo(null);
		Dugmici();
	}
	
	private void AdministratorGUI() {
		getContentPane().setLayout(new MigLayout("", "200[200][45][150][45][200]200", "50[150]20[][]50[]20[]20[]20[]20[][][][]"));
		
		Dimension d = new Dimension(200,36);
		
		ImageIcon imageIcon = new ImageIcon(new ImageIcon("images/userIcon.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		JLabel slika = new JLabel(imageIcon, JLabel.CENTER);
		getContentPane().add(slika, "cell 2 0");
		
		JLabel lblNewLabel = new JLabel(administrator.getIme() + " " + administrator.getPrezime(), JLabel.CENTER);
		lblNewLabel.setFont(lblNewLabel.getFont().deriveFont(20f));
		getContentPane().add(lblNewLabel, "cell 2 1,alignx center");
		
		JLabel lblNewLabel_1 = new JLabel("Administrator", JLabel.CENTER);
		lblNewLabel_1.setFont(lblNewLabel_1.getFont().deriveFont(16f));
		getContentPane().add(lblNewLabel_1, "cell 2 2,alignx center");
		
		btnZaposleni = new JButton("Zaposleni");
		btnZaposleni.setPreferredSize(d);
		getContentPane().add(btnZaposleni, "cell 0 3");
		
		btnRezervacije = new JButton("Rezervacije");
		btnRezervacije.setPreferredSize(d);
		getContentPane().add(btnRezervacije, "cell 4 3");
		
		btnGosti = new JButton("Gosti");
		btnGosti.setPreferredSize(d);
		getContentPane().add(btnGosti, "cell 0 4");
		
		btnUsluge = new JButton("Dodatne usluge");
		btnUsluge.setPreferredSize(d);
		getContentPane().add(btnUsluge, "cell 4 4");
		
		btnTipoviSoba = new JButton("Tipovi soba");
		btnTipoviSoba.setPreferredSize(d);
		getContentPane().add(btnTipoviSoba, "cell 0 5");
		
		btnCenovnik = new JButton("Cenovnik");
		btnCenovnik.setPreferredSize(d);
		getContentPane().add(btnCenovnik, "cell 4 5");
		
		btnSobe = new JButton("Sobe");
		btnSobe.setPreferredSize(d);
		getContentPane().add(btnSobe, "cell 0 6");
		
		btnIzvestaji = new JButton("Izveštaji");
		btnIzvestaji.setPreferredSize(d);
		getContentPane().add(btnIzvestaji, "cell 0 7");
		
		btnOdjava = new JButton("Odjava");
		btnOdjava.setPreferredSize(d);
		getContentPane().add(btnOdjava, "cell 4 7");
	}
	
	private void Dugmici() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				areYouSure();
			}	
		});
		
		btnZaposleni.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorZaposleniFrame zaposleni = new AdministratorZaposleniFrame(administrator);
				zaposleni.setVisible(true);
			}
		});
		
		btnGosti.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorGostiFrame gosti = new AdministratorGostiFrame();
				gosti.setVisible(true);
			}
		});
		
		btnTipoviSoba.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorTipSobeFrame tipSobe = new AdministratorTipSobeFrame();
				tipSobe.setVisible(true);
			}
		});
		
		btnIzvestaji.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorIzvestaji izvestaji = new AdministratorIzvestaji();
				izvestaji.setVisible(true);
			}
		});
		
		btnUsluge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorUslugeFrame usluga = new AdministratorUslugeFrame();
				usluga.setVisible(true);
			}
		});
		
		btnSobe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorSobeFrame soba = new AdministratorSobeFrame();
				soba.setVisible(true);
			}
		});
		
		btnRezervacije.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorRezervacijeFrame rezervacije = new AdministratorRezervacijeFrame();
				rezervacije.setVisible(true);
			}
		});
		
		btnCenovnik.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorCenovnikFrame cenovnik = new AdministratorCenovnikFrame(false);
				cenovnik.setVisible(true);
			}
		});
		
		btnOdjava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				gui.MainFrame login = new gui.MainFrame();
				login.setVisible(true);
			}
		});
	}
	
	private void areYouSure() {
		String[] option = new String[2];
		option[0] = "Da";
		option[1] = "Ne";
		int vrednost = JOptionPane.showOptionDialog(null, "Da li ste sigurni da želite da izađete iz aplikacije?", "Izlazak", 0, JOptionPane.INFORMATION_MESSAGE, null, option, null);
		if (vrednost == JOptionPane.YES_OPTION) {
			System.exit(0);
		}	
	}	
}