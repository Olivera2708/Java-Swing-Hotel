package window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public MainFrame() {
		Login();
		
		MainWindow();
	}
	
	private void Login() {
		JDialog dialog = new JDialog();
		dialog.setTitle("Prijava");
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		LoginGUI(dialog);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	private void LoginGUI(JDialog dialog) {
		MigLayout layout = new MigLayout("wrap", "[][]", "[]10[]15[]");
		dialog.setLayout(layout);
		
		JTextField KorisnickoIme = new JTextField(20);
		JPasswordField Lozinka = new JPasswordField(20);
		JButton Ok = new JButton("OK");
		JButton Cancel = new JButton("Cancel");
		dialog.getRootPane().setDefaultButton(Ok); //da radi enter
		
		dialog.add(new JLabel("Korisničko ime"));
		dialog.add(KorisnickoIme);
		dialog.add(new JLabel("Šifra"));
		dialog.add(Lozinka);
		dialog.add(new JLabel());
		dialog.add(Ok, "split 2");
		dialog.add(Cancel);
		
		Ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String korisnickoIme = KorisnickoIme.getText().trim();
				String lozinka = new String(Lozinka.getPassword()).trim();
				//ucitamo recepcioneri.csv
				List<List<String>> recepcioneri = new ArrayList<>();
				try (BufferedReader br = new BufferedReader(new FileReader("data/recepcioneri.csv"))) {
				    String linija;
				    while ((linija = br.readLine()) != null) {
				        String[] vrednosti = linija.split(",");
				        recepcioneri.add(Arrays.asList(vrednosti));
				        if ((korisnickoIme.equals(vrednosti[5])) && (lozinka.equals(vrednosti[6]))) {
				        	
				        	dialog.setVisible(false);
							dialog.dispose();
							
							MainFrame.this.setVisible(true);
				        }
				    }
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		Cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
	}
	
	private void MainWindow() {
		//ispisi + tip korisnika
		this.setTitle("Pregled");
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		
		//provera koji korisnik radi pozivanja dobrog ekrana
		RecepcionerGUI();
	}
	
	private void RecepcionerGUI() {
		
	}
	
}