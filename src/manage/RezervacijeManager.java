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
import entity.Usluge;

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
	
	//dodaj novu rezervaciju
	public void edit(int id, int tipSobe, int[] usluge, int gost, Date odDatum, Date doDatum, String status) {
		Rezervacije s = this.find(id);
		s.setTipSobe(tipSobeManager.find(tipSobe));
		List<Usluge> lista_usluga = new ArrayList<Usluge>();
		for (int sa: usluge) {
			lista_usluga.add(uslugeManager.find(sa));
		}
		s.setUsluge(lista_usluga);
		s.setGost(gostManager.find(gost));
		s.setOdDatum(odDatum);
		s.setDoDatum(doDatum);
		//Izracunaj cenu na osnovu datuma
		s.setStatus(EnumStatusRezervacije.valueOf(status));
		this.saveData();
	}
	
	//izmeni rezervaciju
	public void add(int tipSobe, int[] usluge, int gost, Date odDatum, Date doDatum, String status) {
		Random random = new Random();
		int id = random.nextInt(8998) + 1001;
		//Izracunaj cenu 
		int cena = 0;
		List<Usluge> lista_usluga = new ArrayList<Usluge>();
		for (int s: usluge) {
			System.out.print(s);
			lista_usluga.add(uslugeManager.find(s));
		}
		this.rezervacijeLista.add(new Rezervacije(id, tipSobeManager.find(tipSobe), lista_usluga, gostManager.find(gost), odDatum, doDatum, cena, EnumStatusRezervacije.valueOf(status)));
		this.saveData();
	}
	
	//obrisi rezervaciju
	public void remove(int id) {
		Rezervacije s = find(id);
		this.rezervacijeLista.remove(s);
		this.saveData();
	}
	
}