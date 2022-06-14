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

import entity.Zaposleni;

public class ZaposleniManager {
	private List<Zaposleni> zaposleniLista;
	
	public ZaposleniManager() {
		//super(); ne znam jel treba?
		this.zaposleniLista = new ArrayList<Zaposleni>();
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/zaposleni.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				Zaposleni zaposleni = new Zaposleni(Integer.parseInt(vrednosti[0]), vrednosti[1], vrednosti[2], vrednosti[3], datum.parse(vrednosti[4]), vrednosti[5], vrednosti[6], vrednosti[7], vrednosti[8], Integer.parseInt(vrednosti[9]), Integer.parseInt(vrednosti[10]), vrednosti[11]);
				this.zaposleniLista.add(zaposleni);
			}
			br.close();
		}
		catch (IOException | NumberFormatException | ParseException e1){
			return false;
		}
		return true;
	}
	
	public boolean vecPostojiKorisnicko(String korisnickoIme) {
		for (Zaposleni z: this.zaposleniLista) {
			if (z.getKorisnickoIme().equals(korisnickoIme)) {
				return true;
			}
		}
		return false;
	}
	
	//lista svih zaposlenih
	public List<Zaposleni> getAll(){
		return this.zaposleniLista;
	}
	
	//za ucitavanje u tabelu
	public String[][] ucitajPodatke(){
		String[][] podaci = new String[this.zaposleniLista.size()][];
		for (int i=0; i < this.zaposleniLista.size(); i++) {
			Zaposleni z = this.zaposleniLista.get(i);
			String[] dodaj = {Integer.toString(z.getId()), z.getIme(), z.getPrezime(), z.getKorisnickoIme(), z.getPozicija()};
			podaci[i] = dodaj;
		}
		return podaci;
	}
	
	//cuvanje podataka iz objekta nazad u csv
	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("data/zaposleni.csv", false));
			for (Zaposleni z : this.zaposleniLista) {
				pw.println(z.toFileString());
			}
			pw.close();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}
	
	//pronadji zaposlenog po id
	public Zaposleni find(int id) {
		for (int i = 0; i < this.zaposleniLista.size(); i++) {
			Zaposleni z = this.zaposleniLista.get(i);
			if (z.getId() == id) {
				return z;
			}
		}
		return null;
	}
	
	//pronadji zaposlenog po id
		private int find_index(int id) {
			for (int i = 0; i < this.zaposleniLista.size(); i++) {
				Zaposleni z = this.zaposleniLista.get(i);
				if (z.getId() == id) {
					return i;
				}
			}
			return -1;
		}
	
	//dodaj novog zaposlenog
	public void add(String ime, String prezime, String pol, Date datum, String telefon, String adresa, String korisnickoIme, String lozinka, int strucnaSprema, int staz, String pozicija) {
		//random id
		Random random = new Random();
		int id = random.nextInt(8998) + 1001;
		while (find(id) != null) {
			id = random.nextInt(8998) + 1001;
		}
		this.zaposleniLista.add(new Zaposleni(id, ime, prezime, pol, datum, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz, pozicija));
		this.saveData();
	}
	
	//izmeni zaposlenog
	public void edit(int id, String ime, String prezime, String pol, Date datum, String telefon, String adresa, String korisnickoIme, String lozinka, int strucnaSprema, int staz, String pozicija) {
		Zaposleni z = this.find(id);
		z.setIme(ime);
		z.setPrezime(prezime);
		z.setAdresa(adresa);
		z.setDatumRodjenja(datum);
		z.setPol(pol);
		z.setTelefon(telefon);
		z.setKorisnickoIme(korisnickoIme);
		z.setLozinka(lozinka);
		z.setStucnaSprema(strucnaSprema);
		z.setStaz(staz);
		z.setPozicija(pozicija);
		saveData();
	}
	
	//obrisi zaposlenog
	public void remove(int id) {
		int z = find_index(id);
		this.zaposleniLista.remove(z);
		this.saveData();
	}
	
	
}