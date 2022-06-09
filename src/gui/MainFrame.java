package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.Gost;
import entity.Zaposleni;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnPrijava = new JButton("Prijava");
	JButton btnCancel = new JButton("Otkaži");
	
	JTextField korisnickoIme = new JTextField(20);
	JPasswordField lozinka = new JPasswordField(20);
	
	public MainFrame() {
		Login();
	}
	
	private void Login() {
		this.setTitle("Prijava");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		LoginGUI();
		this.pack();
		this.setVisible(true);
		allButtons();
	}
	
	private void LoginGUI() {
		MigLayout layout = new MigLayout("wrap", "[][]", "[]10[]15[]");
		this.setLayout(layout);
		
		this.getRootPane().setDefaultButton(btnPrijava); //da radi enter
		
		this.add(new JLabel("Korisničko ime"));
		this.add(korisnickoIme);
		this.add(new JLabel("Šifra"));
		this.add(lozinka);
		this.add(new JLabel());
		this.add(btnPrijava, "split 2");
		this.add(btnCancel);
	}
	
	private void allButtons() {
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				areYouSure();
			}	
		});
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				areYouSure();
			}	
		});
		
		btnPrijava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ime = korisnickoIme.getText();
				String loz = new String(lozinka.getPassword());
				
				if (ime.equals("") || loz.equals("")) {
					JOptionPane.showMessageDialog(null, "Unesite korisničko ime i lozinku.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				else {
					checkIfUser(ime, loz);
				}
				
			}
		});
	}
	
	private void checkIfUser(String ime, String loz) {
		boolean found = false;
		for (Gost g: manageAll.getGostManager().getAll()) {
			if (ime.equals(g.getKorisnickoIme()) && loz.equals(g.getLozinka())) {
				found = true;
				this.setVisible(false);
				this.dispose();
			}
		}
		for (Zaposleni z: manageAll.getZaposleniManager().getAll()) {
			if (ime.equals(z.getKorisnickoIme()) && loz.equals(z.getLozinka())) {
				found = true;
				this.setVisible(false);
				this.dispose();
				
				String pozicija = z.getPozicija();
				switch(pozicija) {
				case "Recepcioner":
					RecepcionerFrame recepcionerFrame = new RecepcionerFrame(z);
					recepcionerFrame.setVisible(true);
					break;
				case "Administrator":
					AdministratorFrame administratorFrame = new AdministratorFrame(z);
					administratorFrame.setVisible(true);
					break;
				}	
			}
		}
		if (!found){
			JOptionPane.showMessageDialog(null, "Ne postoji korisnik sa ovim koricničkim imenom i lozinkom", "Greška", JOptionPane.ERROR_MESSAGE);
		}
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