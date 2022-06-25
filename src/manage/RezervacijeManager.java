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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import enums.EnumMeseci;
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
				String[] sadrzaji = null;
				try {
					sadrzaji = vrednosti[10].split(",");
				}
				catch (ArrayIndexOutOfBoundsException e) {
				}
				Rezervacije rezervacije = new Rezervacije(Integer.parseInt(vrednosti[0]), tipSobeManager.find(Integer.parseInt(vrednosti[1])), lista_usluga, gostManager.find_name(vrednosti[3]), datum.parse(vrednosti[4]), datum.parse(vrednosti[5]), Integer.parseInt(vrednosti[6]), EnumStatusRezervacije.valueOf(vrednosti[7]), soba, sadrzaji);
				
				this.ucitajDatumKraja(datum.parse(vrednosti[9]), rezervacije);
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
	
	//cuvanje podataka iz objekta nazad u csv
	//za potrebe testiranja komentarisati save
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
	
	public void update() {
		for (Rezervacije ir: rezervacijeLista) {
			if (String.valueOf(ir.getStatus()).equals("NA_CEKANJU") && (ir.getOdDatum().before(new java.util.Date()) || ir.getOdDatum().equals(new java.util.Date()))) {
				this.editStatus(ir.getId(), "ODBIJENA");
				this.dodajDatumKraja(ir);
			}
		}
		saveData();
	}
	
	public boolean editStatus(int id, String status) {
		Rezervacije s = this.find(id);
		s.setStatus(EnumStatusRezervacije.valueOf(status));
		this.saveData();
		return true;
	}
	
	public boolean dodelaSobe(int id, int broj) {
		Rezervacije s = this.find(id);
		s.setSoba(sobeManager.find(broj));
		this.saveData();
		return true;
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
			if (datum.format(r.getOdDatum()).equals(datum.format(new Date())) && String.valueOf(r.getStatus()).equals("POTVRDJENA")){
				rezervacije.add(r);
			}
		}
		return rezervacije;
	}
	
	public List<Rezervacije> getOdlasci(){
		List<Rezervacije> rezervacije = new ArrayList<>();
		for (Rezervacije r: rezervacijeLista) {
			SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
			if (datum.format(r.getDoDatum()).equals(datum.format(new Date())) && String.valueOf(r.getStatus()).equals("POTVRDJENA")){
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
		
		for (Rezervacije r: rezervacijeLista) {
			if (!String.valueOf(r.getStatus()).equals("ODBIJENA") && !String.valueOf(r.getStatus()).equals("NA_CEKANJU")) {
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				if ((r.getKonacanDatum().after(datumOd) && r.getKonacanDatum().before(datumDo)) || 
						datum.format(r.getKonacanDatum()).equals(datum.format(datumOd)) || datum.format(r.getKonacanDatum()).equals(datum.format(datumDo))) {
					prihodi += r.getCena();
				}
			}
		}
		return prihodi;
	}
	
	public HashMap<String, Integer> prikazPoTipu(Date datumOd, Date datumDo){
		HashMap<String, Integer> mapa = new HashMap<String, Integer>();
		
		mapa.put("Potvrdjena", this.getBrojPotvrdjenih(datumOd, datumDo));
		mapa.put("Otkazana", this.getBrojOtkazanih(datumOd, datumDo));
		mapa.put("Odbijena", this.getBrojOdbijenih(datumOd, datumDo));
		mapa.put("Na čekanju", this.getBrojNaCekanju(datumOd, datumDo));
		
		return mapa;
	}
	
	public int getBrojNaCekanju(Date datumOd, Date datumDo){
		int brojac = 0;
		for (Rezervacije r: rezervacijeLista) {
			if (String.valueOf(r.getStatus()).equals("NA_CEKANJU")) {
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				if (datum.format(r.getKonacanDatum()).equals(datum.format(datumDo)) || datum.format(r.getKonacanDatum()).equals(datum.format(datumOd)) || (datumOd.before(r.getKonacanDatum()) && datumDo.after(r.getKonacanDatum()))) {
					brojac ++;
				}
			}
		}
		return brojac;
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
	
	//za izvestaje
	public HashMap<Sobe, Integer[]> prikazSoba(Date datumOd, Date datumDo){
		HashMap<Sobe, Integer[]> mapa = new HashMap<Sobe, Integer[]>();
		for (Sobe s: sobeManager.getAll()) {
			Integer[] lista = {0, 0};
			mapa.put(s, lista);
		}
			
		for (Rezervacije r: rezervacijeLista) {
			//proverim samo da li je soba bila rezervisana u tom periodu, od do
			if (String.valueOf(r.getStatus()).equals("POTVRDJENA") && r.getSoba() != null) {
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				int dana = (int) Math.abs(ChronoUnit.DAYS.between(r.getDoDatum().toInstant(), r.getOdDatum().toInstant()));
				if ((datumOd.before(r.getOdDatum()) || datum.format(datumOd).equals(datum.format(r.getOdDatum()))) && (datumDo.after(r.getDoDatum()) || datum.format(datumDo).equals(datum.format(r.getDoDatum())))) {
					Integer[] lista = mapa.get(r.getSoba());
					lista[0] += (int) ChronoUnit.DAYS.between(r.getOdDatum().toInstant(), r.getDoDatum().toInstant());
					lista[1] += r.getCena();
					mapa.put(r.getSoba(), lista);
				}
				else if ((datumOd.before(r.getDoDatum()) || datum.format(datumOd).equals(datum.format(r.getDoDatum()))) && (datumDo.after(r.getDoDatum()) || datum.format(datumDo).equals(datum.format(r.getDoDatum())))) {
					int broj_nocenja = (int) ChronoUnit.DAYS.between(datumOd.toInstant(), r.getDoDatum().toInstant());
					Integer[] lista = mapa.get(r.getSoba());
					System.out.println(r.getId());
					lista[0] += broj_nocenja;
					lista[1] += (int) r.getCena()/dana*broj_nocenja;
					mapa.put(r.getSoba(), lista);
				}
				else if ((datumDo.after(r.getOdDatum()) || datum.format(datumDo).equals(datum.format(r.getOdDatum()))) && (datumOd.before(r.getOdDatum()) || datum.format(datumOd).equals(datum.format(r.getOdDatum())))) {
					int broj_nocenja = (int) ChronoUnit.DAYS.between(datumOd.toInstant(), r.getOdDatum().toInstant());
					Integer[] lista = mapa.get(r.getSoba());
					lista[0] += broj_nocenja;
					lista[1] += (int) r.getCena()/dana*broj_nocenja;
					mapa.put(r.getSoba(), lista);
				}
			}
		}
		
		return mapa;
	}
	
	public int getRashodi(Date odDatum, Date doDatum) {
		int rashodi = 0;
		int meseci = 0;
		meseci = (int) ChronoUnit.DAYS.between(odDatum.toInstant(), doDatum.toInstant());
		meseci /= 30;
		
		for (Zaposleni z: zaposleniManager.getAll()) {
			rashodi += z.getPlata();
		}
		return rashodi * meseci;
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
	
	//za chart
	public HashMap<TipSobe, LinkedHashMap<EnumMeseci, Integer>> getPrihodiPoTipu(){
		//[TipSobe, Mesec, Prihod]
		HashMap<TipSobe, LinkedHashMap<EnumMeseci, Integer>> map = new HashMap<TipSobe, LinkedHashMap<EnumMeseci, Integer>>();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		Date datumOd = cal.getTime();
		int mesec = cal.get(Calendar.MONTH) + 2;
		
		for (TipSobe ts: tipSobeManager.getAll()) {
			LinkedHashMap<EnumMeseci, Integer> unutra = new LinkedHashMap<EnumMeseci, Integer>();
			for (int i = mesec; i < mesec+12; i++) {
				if (i < 13) {
					unutra.put(EnumMeseci.Int(i), 0);
				}
				else {
					unutra.put(EnumMeseci.Int(i-12), 0);
				}
			}
			map.put(ts, unutra);
		}
		
		for (Rezervacije r: rezervacijeLista) {
			if (r.getKonacanDatum().after(datumOd) && !r.getStatus().equals(EnumStatusRezervacije.NA_CEKANJU) && !r.getStatus().equals(EnumStatusRezervacije.ODBIJENA)) {
				Calendar cal1 = Calendar.getInstance();
				cal1.setTime(r.getKonacanDatum());
				mesec = cal1.get(Calendar.MONTH) + 1;
				
				LinkedHashMap<EnumMeseci, Integer> unutra1 = map.get(r.getTipSobe());
				unutra1.put(EnumMeseci.Int(mesec), unutra1.get(EnumMeseci.Int(mesec)) + r.getCena());	
				map.put(r.getTipSobe(), unutra1);
			}
		}
		
		return map;
	}
	
	//za chart
	public LinkedHashMap<EnumMeseci, Integer> getUkupno() {
		LinkedHashMap<EnumMeseci, Integer> map = new LinkedHashMap<EnumMeseci, Integer>();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		int mesec = cal.get(Calendar.MONTH) + 2;
		
		for (int i = mesec; i < mesec+12; i++) {
			if (i < 13) {
				map.put(EnumMeseci.Int(i), 0);
			}
			else {
				map.put(EnumMeseci.Int(i-12), 0);
			}
		}
		for (int i = mesec; i < mesec+12; i++) {
			if (i < 13) {
				map.put(EnumMeseci.Int(i), 0);
			}
			else {
				map.put(EnumMeseci.Int(i-12), 0);
			}
		}
		
		HashMap<TipSobe, LinkedHashMap<EnumMeseci, Integer>> odvojeno = this.getPrihodiPoTipu();
		
		for (TipSobe ts: tipSobeManager.getAll()) {
			LinkedHashMap<EnumMeseci, Integer> unutra1 = odvojeno.get(ts);
			for (EnumMeseci m: unutra1.keySet()) {
				map.put(m, map.get(m) + unutra1.get(m));
			}
		}
		return map;
		
	}

	public int brojSlobodnihSoba(int id_tipaSobe, Date datumOd, Date datumDo, String[] sadrzaji) {
		//predbroj koliko ima izabranog tipa soba
		int ukupnoSaSadrzajem = 0;
		int ukupno = 0;
		List<Integer> sobeSaSadrzajem = new ArrayList<Integer>(); 
		for (Sobe s: sobeManager.getAll()) {
			if (s.getTipSobe().getId() == id_tipaSobe) {
				ukupno ++;
				boolean postoji = true;
				if (sadrzaji != null) {
					if (s.getSadrzaj() != null) {
						for (String st: sadrzaji) {
							boolean nasli = false;
							for (String ss: s.getSadrzaj()) {
								if (ss.equals(st)) {
									nasli = true;
								}
							}
							if (!nasli) {
								postoji = false;
								break;
							}
						}
					}
					else {
						continue;
					}
					if (postoji) {
						sobeSaSadrzajem.add(s.getBrojSobe());
						ukupnoSaSadrzajem ++;
					}
				}
				else {
					ukupnoSaSadrzajem++;
				}
			}
		}
		int zauzete = 0;
		for (Rezervacije s: rezervacijeLista) {
			if (s.getTipSobe().getId() == id_tipaSobe &&  String.valueOf(s.getStatus()).equals("POTVRDJENA")) {
				if ((s.getOdDatum().before(datumDo) && s.getDoDatum().after(datumOd))
						|| (s.getDoDatum().before(datumDo) && s.getDoDatum().after(datumOd))
						|| s.getDoDatum().equals(datumOd) || s.getOdDatum().equals(datumDo)) {
					zauzete++;
				}
			}
			
		}
		if (ukupno - zauzete != 0 && ukupnoSaSadrzajem > 0) {
			return 1;
		}
		return 0;
	}
	
	//vraca za izabran datum sta se sve moze rezervisati
	public List<String> getSlobodneSobe(Date datumOd, Date datumDo, String[] sadrzaji){
		List<String> sve = new ArrayList<String>();
		//za svaki tip sobe
		for (TipSobe s: tipSobeManager.getAll()) {
			if (brojSlobodnihSoba(s.getId(), datumOd, datumDo, sadrzaji) == 0) {
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
	public boolean add(int tipSobe, int[] usluge, int gost, Date odDatum, Date doDatum, String status, String[] sadrzaji) {
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
		Rezervacije rezervacija = new Rezervacije(id, tipSobeManager.find(tipSobe), lista_usluga, gostManager.find(gost), odDatum, doDatum, cena, EnumStatusRezervacije.valueOf(status), null, sadrzaji);
		this.rezervacijeLista.add(rezervacija);
		this.dodajDatumKraja(rezervacija);
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