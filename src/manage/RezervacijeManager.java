package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import enums.EnumStatusRezervacije;
import entity.Rezervacije;

public class RezervacijeManager {
	private TipSobeManager tipSobeManager;
	private UslugeManager uslugeManager;
	private GostManager gostManager;
	private List<Rezervacije> rezervacijeLista;
	
	public RezervacijeManager(TipSobeManager tipSobeManager, UslugeManager uslugeManager, GostManager gostManager) {
		//super(); ne znam jel treba?
		this.rezervacijeLista = new ArrayList<Rezervacije>();
		this.tipSobeManager = tipSobeManager;
		this.gostManager = gostManager;
		this.uslugeManager = uslugeManager;
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/rezervacije.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				Rezervacije rezervacije = new Rezervacije(Integer.parseInt(vrednosti[0]), tipSobeManager.find(Integer.parseInt(vrednosti[1])), uslugeManager.find(Integer.parseInt(vrednosti[2])), gostManager.find(Integer.parseInt(vrednosti[3])), datum.parse(vrednosti[4]), datum.parse(vrednosti[5]), Integer.parseInt(vrednosti[6]), EnumStatusRezervacije.valueOf(vrednosti[7]));
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
			pw = new PrintWriter(new FileWriter("data/zaposleni.csv", false));
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
	
	//dodaj novu rezervaciju
	public void edit(int id, int tipSobe, int usluge, int gost, Date odDatum, Date doDatum, int cena, String status) {
		Rezervacije s = this.find(id);
		s.setTipSobe(tipSobeManager.find(tipSobe));
		s.setUsluge(uslugeManager.find(usluge));
		s.setGost(gostManager.find(gost));
		s.setOdDatum(odDatum);
		s.setOdDatum(doDatum);
		s.setCena(cena);
		s.setStatus(EnumStatusRezervacije.valueOf(status));
	}
	
	//izmeni rezervaciju
	public void add(int tipSobe, int usluge, int gost, Date odDatum, Date doDatum, int cena, String status) {
		Random random = new Random();
		int id = random.nextInt(8998) + 1001;
		this.rezervacijeLista.add(new Rezervacije(id, tipSobeManager.find(tipSobe), uslugeManager.find(usluge), gostManager.find(gost), odDatum, doDatum, cena, EnumStatusRezervacije.valueOf(status)));
		this.saveData();
	}
	
	//obrisi rezervaciju
	public void remove(int id) {
		Rezervacije s = find(id);
		this.rezervacijeLista.remove(s);
		this.saveData();
	}
	
}