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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import enums.EnumStatusRezervacije;
import enums.EnumStatusSobe;
import entity.Gost;
import entity.Rezervacije;
import entity.Sobe;
import entity.TipSobe;
import entity.Usluge;
import entity.Zaposleni;

public class RezervacijeManager {
	private TipSobeManager tipSobeManager;
	private UslugeManager uslugeManager;
	private GostManager gostManager;
	private CenovnikUslugaManager cenovnikUslugaManager;
	private CenovnikSobaManager cenovnikSobaManager;
	private SobeManager sobeManager;
	private ZaposleniManager zaposleniManager;
	private List<Rezervacije> rezervacijeLista;
	
	public RezervacijeManager(TipSobeManager tipSobeManager, UslugeManager uslugeManager, GostManager gostManager, CenovnikUslugaManager cenovnikUslugaManager, CenovnikSobaManager cenovnikSobaManager, SobeManager sobeManager, ZaposleniManager zaposleniManager) {
		this.rezervacijeLista = new ArrayList<Rezervacije>();
		this.tipSobeManager = tipSobeManager;
		this.gostManager = gostManager;
		this.uslugeManager = uslugeManager;
		this.cenovnikSobaManager = cenovnikSobaManager;
		this.cenovnikUslugaManager = cenovnikUslugaManager;
		this.sobeManager = sobeManager;
		this.zaposleniManager = zaposleniManager;
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/rezervacije.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				
				List<Usluge> lista_usluga = new ArrayList<Usluge>();
				String[] usluge;
				if (!vrednosti[2].equals("")) {
					usluge = vrednosti[2].split(",");
					for (String s: usluge) {
						lista_usluga.add(uslugeManager.find(Integer.parseInt(s)));
					}
				}
				Sobe soba = null;
				if (vrednosti.length > 8){
					if (!vrednosti[8].equals("")) {
						int brojSobe = Integer.parseInt(vrednosti[8]);
						soba = sobeManager.find(brojSobe);
					}
				}
				Rezervacije rezervacije = new Rezervacije(Integer.parseInt(vrednosti[0]), tipSobeManager.find(Integer.parseInt(vrednosti[1])), lista_usluga, gostManager.find_name(vrednosti[3]), datum.parse(vrednosti[4]), datum.parse(vrednosti[5]), Integer.parseInt(vrednosti[6]), EnumStatusRezervacije.valueOf(vrednosti[7]), soba);
				
				if (!String.valueOf(rezervacije.getStatus()).equals("NA_CEKANJU")) {
					this.ucitajDatumKraja(datum.parse(vrednosti[9]), rezervacije);
				}
				this.rezervacijeLista.add(rezervacije);
			}
			br.close();
		}
		catch (IOException | NumberFormatException | ParseException e1){
			return false;
		}
		update();
		return true;
	}
	
	public void update() {
		for (Rezervacije ir: rezervacijeLista) {
			if (String.valueOf(ir.getStatus()).equals("NA_CEKANJU") && (ir.getOdDatum().before(new java.util.Date()) || ir.getOdDatum().equals(new java.util.Date()))) {
				int[] lista_usluga = new int[ir.getUsluge().size()];
				for (int i =0; i < ir.getUsluge().size(); i++) {
					lista_usluga[i] = ir.getUsluge().get(i).getId();
				}
				
				Sobe soba = null;
				try {
					soba = ir.getSoba();
				}
				catch (ArrayIndexOutOfBoundsException e) {
				}
				this.edit(ir.getId(), ir.getTipSobe().getId(), lista_usluga, ir.getGost().getId(), ir.getOdDatum(), ir.getDoDatum(), "ODBIJENA", soba);
				this.dodajDatumKraja(ir);
			}
		}
		saveData();
	}
	
	public void promeniSobe(int stari, int novi) {
		for (Rezervacije r: rezervacijeLista) {
			if (r.getSoba() != null) {
				if (r.getSoba().getBrojSobe() == stari) {
					r.getSoba().setBrojSobe(novi);
				}
			}
		}
		saveData();
	}
	
	public void promeniGosta(String staro, String novo) {
		for (Rezervacije r: rezervacijeLista) {
			if (r.getGost().getKorisnickoIme().equals(staro)) {
				r.getGost().setKorisnickoIme(novo);
			}
		}
		saveData();
	}
	
	public void ucitajDatumKraja(Date datum, Rezervacije r) {
		r.setKonacanDatum(datum);
		saveData();
	}
	
	public void dodajDatumKraja(Rezervacije r) {
		r.setKonacanDatum(new Date());
		saveData();
	}
	
	//lista svih soba
	public List<Rezervacije> getAll(){
		return rezervacijeLista;
	}
	
	//lista rezervacija korisnika
	public List<Rezervacije> getRezervacije(Gost gost){
		List<Rezervacije> rezervacije = new ArrayList<>();
		for (Rezervacije r: rezervacijeLista) {
			if (r.getGost() == gost) {
			rezervacije.add(r);
			}
		}
		return rezervacije;
	}
	
	public List<Rezervacije> getRezervacijeNaCekanju(){
		List<Rezervacije> rezervacije = new ArrayList<>();
		for (Rezervacije r: rezervacijeLista) {
			if (String.valueOf(r.getStatus()).equals("NA_CEKANJU")) {
			rezervacije.add(r);
			}
		}
		return rezervacije;
	}
	
	//check in
	public List<Rezervacije> getRezervacijePotvrdjene(){
		List<Rezervacije> rezervacije = new ArrayList<>();
		for (Rezervacije r: rezervacijeLista) {
			SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
			if (String.valueOf(r.getStatus()).equals("POTVRDJENA") && r.getSoba() == null && datum.format(r.getOdDatum()).equals(datum.format(new Date()))) {
				rezervacije.add(r);
			}
		}
		return rezervacije;
	}
	
	public List<Rezervacije> getDolasci(){
		List<Rezervacije> rezervacije = new ArrayList<>();
		for (Rezervacije r: rezervacijeLista) {
			SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
			if (datum.format(r.getOdDatum()).equals(datum.format(new Date()))){
				rezervacije.add(r);
			}
		}
		return rezervacije;
	}
	
	public List<Rezervacije> getOdlasci(){
		List<Rezervacije> rezervacije = new ArrayList<>();
		for (Rezervacije r: rezervacijeLista) {
			SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
			if (datum.format(r.getDoDatum()).equals(datum.format(new Date()))){
				rezervacije.add(r);
			}
		}
		return rezervacije;
	}
	
	public List<Rezervacije> getRezervacijePotvrdjeneCheckOut(){
		List<Rezervacije> rezervacije = new ArrayList<>();
		for (Rezervacije r: rezervacijeLista) {
			SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
			if (String.valueOf(r.getStatus()).equals("POTVRDJENA") && r.getSoba() != null && datum.format(r.getDoDatum()).equals(datum.format(new Date()))) {
				if (r.getSoba().getStatus().equals(EnumStatusSobe.ZAUZETO))
					rezervacije.add(r);
			}
		}
		return rezervacije;
	}
	
	public int getPrihodi(Date datumOd, Date datumDo) {
		int prihodi = 0;
		
		int dani = (int) ChronoUnit.DAYS.between(datumOd.toInstant(), datumDo.toInstant());
		for (int i = 0; i <= dani; i++) {
			Date danas = new Date(datumOd.getTime() + i * (1000 * 60 * 60 * 24));
			for (Rezervacije r: rezervacijeLista) {
				if (!String.valueOf(r.getStatus()).equals("OTKAZANA") && !String.valueOf(r.getStatus()).equals("NA_CEKANJU")) {
					SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
					if (datum.format(r.getKonacanDatum()).equals(datum.format(danas))) {
						prihodi += r.getCena();
					}
				}
			}
		}
		return prihodi;
	}
	
	public int getBrojPotvrdjenih(Date datumOd, Date datumDo){
		int brojac = 0;
		for (Rezervacije r: rezervacijeLista) {
			if (String.valueOf(r.getStatus()).equals("POTVRDJENA")) {
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				if (datum.format(r.getKonacanDatum()).equals(datum.format(datumDo)) || datum.format(r.getKonacanDatum()).equals(datum.format(datumOd)) || (datumOd.before(r.getKonacanDatum()) && datumDo.after(r.getKonacanDatum()))) {
					brojac ++;
				}
			}
		}
		return brojac;
	}
	
	public int getBrojOdbijenih(Date datumOd, Date datumDo){
		int brojac = 0;
		for (Rezervacije r: rezervacijeLista) {
			if (String.valueOf(r.getStatus()).equals("ODBIJENA")) {
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				if (datum.format(r.getKonacanDatum()).equals(datum.format(datumDo)) || datum.format(r.getKonacanDatum()).equals(datum.format(datumOd)) || (datumOd.before(r.getKonacanDatum()) && datumDo.after(r.getKonacanDatum()))) {
					brojac ++;
				}
			}
		}
		return brojac;
	}
	
	public int getBrojOtkazanih(Date datumOd, Date datumDo){
		int brojac = 0;
		for (Rezervacije r: rezervacijeLista) {
			if (String.valueOf(r.getStatus()).equals("OTKAZANA")) {
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				if (datum.format(r.getKonacanDatum()).equals(datum.format(datumDo)) || datum.format(r.getKonacanDatum()).equals(datum.format(datumOd)) || (datumOd.before(r.getKonacanDatum()) && datumDo.after(r.getKonacanDatum()))) {
					brojac ++;
				}
			}
		}
		return brojac;
	}
	
	public int getRashodi(Date odDatum, Date doDatum) {
		int rashodi = 0;
		int meseci = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(doDatum);
		meseci += cal.get(Calendar.MONTH);
		cal.setTime(odDatum);
		meseci -= cal.get(Calendar.MONTH);
		
		for (Zaposleni z: zaposleniManager.getAll()) {
			rashodi += z.getPlata();
		}
		return rashodi * meseci;
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
			if (s.getTipSobe().getId() == id_tipaSobe && (String.valueOf(s.getStatus()).equals("NA_CEKANJU") || String.valueOf(s.getStatus()).equals("POTVRDJENA"))) {
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
	public boolean edit(int id, int tipSobe, int[] usluge, int gost, Date odDatum, Date doDatum, String status, Sobe soba) {
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
		s.setSoba(soba);
		this.saveData();
		return true;
	}
	
	//izmeni rezervaciju
	public boolean add(int tipSobe, int[] usluge, int gost, Date odDatum, Date doDatum, String status) {
		Random random = new Random();
		int id = random.nextInt(8998) + 1001;
		while (find(id) != null) {
			id = random.nextInt(8998) + 1001;
		}
		List<Usluge> lista_usluga = new ArrayList<Usluge>();
		for (int s: usluge) {
			lista_usluga.add(uslugeManager.find(s));
		}
		int cena = cena(tipSobe, usluge, odDatum, doDatum, true);
		if (cena == -1) {
			return false;
		}
		this.rezervacijeLista.add(new Rezervacije(id, tipSobeManager.find(tipSobe), lista_usluga, gostManager.find(gost), odDatum, doDatum, cena, EnumStatusRezervacije.valueOf(status), null));
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