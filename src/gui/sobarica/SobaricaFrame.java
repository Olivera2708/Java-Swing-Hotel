package gui.sobarica;

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

public class SobaricaFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Zaposleni spremacica;
	
	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnOdjava;
	JButton btnSobe;
	
	public SobaricaFrame (Zaposleni zaposleni) {
		this.spremacica = zaposleni;
		
		this.setTitle("Sobarica " + spremacica.getIme() + " " + spremacica.getPrezime());
		this.setPreferredSize(new Dimension(1000, 700));
		this.setResizable(false);
		GostGUI();
		this.pack();
		this.setLocationRelativeTo(null);
		Dugmici();
	}
	
	private void GostGUI() {
		getContentPane().setLayout(new MigLayout("", "[425][150][425]", "80[150]20[][]50[]30[]30[]30"));
		
		Dimension d = new Dimension(200,36);
		
		ImageIcon imageIcon = new ImageIcon(new ImageIcon("images/userIcon.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		JLabel slika = new JLabel(imageIcon, JLabel.CENTER);
		getContentPane().add(slika, "cell 1 0");
		
		JLabel lblNewLabel = new JLabel(spremacica.getIme() + " " + spremacica.getPrezime(), JLabel.CENTER);
		lblNewLabel.setFont(lblNewLabel.getFont().deriveFont(20f));
		getContentPane().add(lblNewLabel, "cell 1 1,alignx center");
		
		JLabel lblNewLabel_1 = new JLabel("Sobarica", JLabel.CENTER);
		lblNewLabel_1.setFont(lblNewLabel_1.getFont().deriveFont(16f));
		getContentPane().add(lblNewLabel_1, "cell 1 2,alignx center");
		
		btnSobe = new JButton("Sobe za spremanje");
		btnSobe.setPreferredSize(d);
		getContentPane().add(btnSobe, "cell 1 3,alignx center");
		
		btnOdjava = new JButton("Odjava");
		btnOdjava.setPreferredSize(d);
		getContentPane().add(btnOdjava, "cell 1 4,alignx center");
	}
	
	private void Dugmici() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				areYouSure();
			}	
		});
		
		btnSobe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SobaricaSobe novaRezervacija = new SobaricaSobe(spremacica);
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