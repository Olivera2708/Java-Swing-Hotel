package gui.recepcioner;

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

public class RecepcionerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Zaposleni recepcioner;
	
	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnOdjava;
	JButton btnPrikazRezervacija;
	JButton btnSobe;
	JButton btnStanje;
	JButton btnCheckIn;
	JButton btnCheckOut;
	JButton btnDodajGosta;
	
	
	public RecepcionerFrame (Zaposleni recepcioner) {
		this.recepcioner = recepcioner;
		
		this.setTitle("Recepcioner " + recepcioner.getIme() + " " + recepcioner.getPrezime());
		this.setPreferredSize(new Dimension(1000, 700));
		this.setResizable(false);
		RecepcionerGUI();
		this.pack();
		this.setLocationRelativeTo(null);
		Dugmici();
	}
	
	private void RecepcionerGUI() {
		getContentPane().setLayout(new MigLayout("", "[425][150][425]", "80[150]20[][]50[]10[]10[]10[]10[]10"));
		
		Dimension d = new Dimension(200,36);
		
		ImageIcon imageIcon = new ImageIcon(new ImageIcon("images/userIcon.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		JLabel slika = new JLabel(imageIcon, JLabel.CENTER);
		getContentPane().add(slika, "cell 1 0");
		
		JLabel lblNewLabel = new JLabel(recepcioner.getIme() + " " + recepcioner.getPrezime(), JLabel.CENTER);
		lblNewLabel.setFont(lblNewLabel.getFont().deriveFont(20f));
		getContentPane().add(lblNewLabel, "cell 1 1,alignx center");
		
		JLabel lblNewLabel_1 = new JLabel("Recepcioner", JLabel.CENTER);
		lblNewLabel_1.setFont(lblNewLabel_1.getFont().deriveFont(16f));
		getContentPane().add(lblNewLabel_1, "cell 1 2,alignx center");
		
		btnDodajGosta = new JButton("Novi gost");
		btnDodajGosta.setPreferredSize(d);
		getContentPane().add(btnDodajGosta, "cell 0 3,alignx right");
		
		btnCheckIn = new JButton("Check in");
		btnCheckIn.setPreferredSize(d);
		getContentPane().add(btnCheckIn, "cell 2 3,alignx left");
		
		btnSobe = new JButton("Status soba");
		btnSobe.setPreferredSize(d);
		getContentPane().add(btnSobe, "cell 0 4,alignx right");
		
		btnCheckOut = new JButton("Check out");
		btnCheckOut.setPreferredSize(d);
		getContentPane().add(btnCheckOut, "cell 2 4,alignx left");
		
		btnPrikazRezervacija = new JButton("Potvrda rezervacija");
		btnPrikazRezervacija.setPreferredSize(d);
		getContentPane().add(btnPrikazRezervacija, "cell 0 5,alignx right");
		
		btnStanje = new JButton("Dnevno stanje");
		btnStanje.setPreferredSize(d);
		getContentPane().add(btnStanje, "cell 0 6,alignx right");
		
		btnOdjava = new JButton("Odjava");
		btnOdjava.setPreferredSize(d);
		getContentPane().add(btnOdjava, "cell 2 5,alignx left");
	}
	
	private void Dugmici() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				areYouSure();
			}	
		});
		
		btnPrikazRezervacija.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RecepcionerPotvrdaRezervacija novaRezervacija = new RecepcionerPotvrdaRezervacija();
				novaRezervacija.setVisible(true);
			}
		});
		
		btnSobe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RecepcionerUvidSobe uvid = new RecepcionerUvidSobe();
				uvid.setVisible(true);
			}
			
		});
		
		btnDodajGosta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RecepcionerDodajGosta novaRezervacija = new RecepcionerDodajGosta();
				novaRezervacija.setVisible(true);
			}
		});
		
		btnStanje.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RecepcionerStanje novaRezervacija = new RecepcionerStanje();
				novaRezervacija.setVisible(true);
			}
		});
		
		btnCheckIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RecepcionerCheckIn novaRezervacija = new RecepcionerCheckIn();
				novaRezervacija.setVisible(true);
			}
		});
		
		btnCheckOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RecepcionerCheckOut novaRezervacija = new RecepcionerCheckOut();
				novaRezervacija.setVisible(true);
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