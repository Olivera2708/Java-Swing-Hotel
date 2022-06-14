package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import enums.EnumStatusRezervacije;
import entity.Rezervacije;
import entity.Sobe;
import entity.TipSobe;
import entity.Usluge;

public class RezervacijeManager {
	private TipSobeManager tipSobeManager;
	private UslugeManager uslugeManager;
	private GostManager gostManager;
	private CenovnikUslugaManager cenovnikUslugaManager;
	private CenovnikSobaManager cenovnikSobaManager;
	private SobeManager sobeManager;
	private List<Rezervacije> rezervacijeLista;
	
	public RezervacijeManager(TipSobeManager tipSobeManager, UslugeManager uslugeManager, GostManager gostManager, CenovnikUslugaManager cenovnikUslugaManager, CenovnikSobaManager cenovnikSobaManager, SobeManager sobeManager) {
		//super(); ne znam jel treba?
		this.rezervacijeLista = new ArrayList<Rezervacije>();
		this.tipSobeManager = tipSobeManager;
		this.gostManager = gostManager;
		this.uslugeManager = uslugeManager;
		this.cenovnikSobaManager = cenovnikSobaManager;
		this.cenovnikUslugaManager = cenovnikUslugaManager;
		this.sobeManager = sobeManager;
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/rezervacije.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				List<Usluge> lista_usluga = new ArrayList<Usluge>();
				String[] usluge = vrednosti[2].split(",");
				if (usluge.length != 0) {
					for (String s: usluge) {
						lista_usluga.add(uslugeManager.find(Integer.parseInt(s)));
					}
				}
				Rezervacije rezervacije = new Rezervacije(Integer.parseInt(vrednosti[0]), tipSobeManager.find(Integer.parseInt(vrednosti[1])), lista_usluga, gostManager.find_name(vrednosti[3]), datum.parse(vrednosti[4]), datum.parse(vrednosti[5]), Integer.parseInt(vrednosti[6]), EnumStatusRezervacije.valueOf(vrednosti[7]));
				this.rezervacijeLista.add(rezervacije);
			}
			br.close();
		}
		catch (IOException | NumberFormatException | ParseException e1){
			return false;
		}
		return true;
	}
	
	//lista svih soba
	public List<Rezervacije> getAll(){
		return rezervacijeLista;
	}
	
	//cuvanje podataka iz objekta nazad u csv
	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("data/rezervacije.csv", false));
			for (Rezervacije s : rezervacijeLista) {
				pw.println(s.toFileString());
			}
			pw.close();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}
	
	//pronadji rezervaciju po id
	public Rezervacije find(int id) {
		for (int i = 0; i < rezervacijeLista.size(); i++) {
			Rezervacije s = rezervacijeLista.get(i);
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	
	//izracunaj cenu
	public int cena(int tipSobe, int[] usluge, Date odDatum, Date doDatum, boolean prikaz) {
		int cena = 0;
		//idemo kroz svaki dan i racunamo cenu za tipsobe i usluge za taj dan
		int dana = (int) ChronoUnit.DAYS.between(odDatum.toInstant(), doDatum.toInstant());
		for (int i = 0; i <= dana; i++) {
			
			Date provera = new Date(odDatum.getTime() + i * (1000 * 60 * 60 * 24));
			
			//cena tipsobe
			int nova_cena = cenovnikSobaManager.get_cena(tipSobe, provera);
			if (nova_cena == -1) {
				if (prikaz) {
					JOptionPane.showMessageDialog(null, "Ne postoji cenovnik za traženi period.", "Greška", JOptionPane.ERROR_MESSAGE);
				}
				return -1;
			}
			else {
				cena += nova_cena;
			}
			
			//cena za usluge
			for (int sa: usluge) {
				int nova_cena_u = cenovnikUslugaManager.get_cena(sa, provera);
				if (nova_cena_u == -1) {
					if (prikaz) {
						JOptionPane.showMessageDialog(null, "Ne postoji cenovnik za traženi period.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
					return -1;
				}
				else {
					cena += nova_cena_u;
				}	
			}
		}
		return cena;
	}
	
	public int cenaUsluge(int usluge, Date odDatum, Date doDatum) {
		int cena = 0;
		//idemo kroz svaki dan i racunamo cenu za tipsobe i usluge za taj dan
		int dana = (int) ChronoUnit.DAYS.between(odDatum.toInstant(), doDatum.toInstant());
		for (int i = 0; i <= dana; i++) {
			
			Date provera = new Date(odDatum.getTime() + i * (1000 * 60 * 60 * 24));
			
			int nova_cena_u = cenovnikUslugaManager.get_cena(usluge, provera);
			if (nova_cena_u == -1) {
				return -1;
			}
			else {
				cena += nova_cena_u;
			}	
		}
		return cena;
	}
	
	public int brojSlobodnihSoba(int id_tipaSobe, Date datumOd, Date datumDo) {
		//predbroj koliko ima izabranog tipa soba
		int ukupno = 0;
		for (Sobe s: sobeManager.getAll()) {
			if (s.getTipSobe().getId() == id_tipaSobe) {
				ukupno++;
			}
		}
		
		int zauzete = 0;
		for (Rezervacije s: rezervacijeLista) {
			if (s.getTipSobe().getId() == id_tipaSobe) {
				if ((s.getOdDatum().before(datumDo) && s.getDoDatum().after(datumOd))
						|| (s.getDoDatum().before(datumDo) && s.getDoDatum().after(datumOd))
						|| s.getDoDatum().equals(datumOd) || s.getOdDatum().equals(datumDo)) {
					zauzete++;
				}
			}
		}
		return ukupno - zauzete;
	}
	
	//vraca za izabran datum sta se sve moze rezervisati
	public List<String> getSlobodneSobe(Date datumOd, Date datumDo){
		List<String> sve = new ArrayList<String>();
		//za svaki tip sobe
		for (TipSobe s: tipSobeManager.getAll()) {
			if (brojSlobodnihSoba(s.getId(), datumOd, datumDo) == 0) {
				continue;
			}
			//provara jel moze da se izracuna cena za tip sobe
			int[] usluge = new int[]{};
			if (cena(s.getId(), usluge, datumOd, datumDo, false) == -1){
				continue;
			}
			sve.add(s.getTip());
		}
		return sve;
	}
	
	//vraca listu usluga koje imaju cenovnik definisan
		public List<String> getUsluge(Date datumOd, Date datumDo){
			List<String> sve = new ArrayList<String>();
			//za svaki tip sobe
			for (Usluge s: uslugeManager.getAll()) {
				//provara jel moze da se izracuna cena za tip sobe
				if (cenaUsluge(s.getId(), datumOd, datumDo) == -1){
					continue;
				}
				sve.add(s.getTip());
			}
			return sve;
		}
	
	//dodaj novu rezervaciju
	public boolean edit(int id, int tipSobe, int[] usluge, int gost, Date odDatum, Date doDatum, String status) {
		Rezervacije s = this.find(id);
		//Izracunaj cenu na osnovu datuma
		int cena = cena(tipSobe, usluge, odDatum, doDatum, true);
		if (cena == -1) {
			return false;
		}
		s.setCena(cena);
		
		s.setTipSobe(tipSobeManager.find(tipSobe));
		List<Usluge> lista_usluga = new ArrayList<Usluge>();
		for (int sa: usluge) {
			lista_usluga.add(uslugeManager.find(sa));
		}
		s.setUsluge(lista_usluga);
		s.setGost(gostManager.find(gost));
		s.setOdDatum(odDatum);
		s.setDoDatum(doDatum);
		s.setStatus(EnumStatusRezervacije.valueOf(status));
		this.saveData();
		return true;
	}
	
	//izmeni rezervaciju
	public boolean add(int tipSobe, int[] usluge, int gost, Date odDatum, Date doDatum, String status) {
		Random random = new Random();
		int id = random.nextInt(8998) + 1001;
		List<Usluge> lista_usluga = new ArrayList<Usluge>();
		for (int s: usluge) {
			lista_usluga.add(uslugeManager.find(s));
		}
		int cena = cena(tipSobe, usluge, odDatum, doDatum, true);
		if (cena == -1) {
			return false;
		}
		this.rezervacijeLista.add(new Rezervacije(id, tipSobeManager.find(tipSobe), lista_usluga, gostManager.find(gost), odDatum, doDatum, cena, EnumStatusRezervacije.valueOf(status)));
		this.saveData();
		return true;
	}
	
	//obrisi rezervaciju
	public void remove(int id) {
		Rezervacije s = find(id);
		this.rezervacijeLista.remove(s);
		this.saveData();
	}
	
}