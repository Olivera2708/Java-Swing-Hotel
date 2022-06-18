package gui.administrator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import manage.ManageAll;
import net.miginfocom.swing.MigLayout;

public class AdministratorIzaberiChart extends JFrame {
	private static final long serialVersionUID = 1L;
	
	ManageAll manageAll = ManageAll.getInstance();
	
	JButton btnSobarice;
	JButton btnRezrvacije;
	JButton btnPrihodi;
	
	
	
	public AdministratorIzaberiChart () {
		this.setTitle("Izbor grafika");
		this.setPreferredSize(new Dimension(340, 100));
		this.setResizable(false);
		AdministratorGUI();
		this.pack();
		this.setLocationRelativeTo(null);
		Dugmici();
	}
	
	private void AdministratorGUI() {
		getContentPane().setLayout(new MigLayout("", "[]", "[]20[]20[]"));
		
		Dimension d = new Dimension(100, 20);
		
		btnSobarice = new JButton("Sobarice");
		btnSobarice.setPreferredSize(d);
		getContentPane().add(btnSobarice, "cell 0 0");
		
		btnRezrvacije = new JButton("Rezervacije");
		btnRezrvacije.setPreferredSize(d);
		getContentPane().add(btnRezrvacije, "cell 1 0");
		
		btnPrihodi = new JButton("Prihodi");
		btnPrihodi.setPreferredSize(d);
		getContentPane().add(btnPrihodi, "cell 2 0");
	}
	
	private void Dugmici() {
		
		btnSobarice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorChart grafik = new AdministratorChart(1);
				grafik.setVisible(true);
			}
		});
		
		btnRezrvacije.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorChart grafik = new AdministratorChart(2);
				grafik.setVisible(true);
			}
		});
		
		btnPrihodi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdministratorChart grafik = new AdministratorChart(3);
				grafik.setVisible(true);
			}
		});
	}
}