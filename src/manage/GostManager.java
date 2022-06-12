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

import entity.Gost;

public class GostManager {
	private List<Gost> gostLista;
	
	public GostManager() {
		//super(); ne znam jel treba?
		this.gostLista = new ArrayList<Gost>();
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/gosti.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
				Gost gost = new Gost(Integer.parseInt(vrednosti[0]), vrednosti[1], vrednosti[2], vrednosti[3], datum.parse(vrednosti[4]), vrednosti[5], vrednosti[6], vrednosti[7], vrednosti[8]);
				this.gostLista.add(gost);
			}
			br.close();
		}
		catch (IOException | NumberFormatException | ParseException e1){
			return false;
		}
		return true;
	}
	
	public boolean vecPostojiKorisnicko(String korisnickoIme) {
		for (Gost g: gostLista) {
			if (g.getKorisnickoIme().equals(korisnickoIme)) {
				return true;
			}
		}
		return false;
	}
	
	
	//lista svih gostiju
	public List<Gost> getAll(){
		return gostLista;
	}
	
	//vraca listu naziva
		public String[] getNames() {
			String[] string = new String[this.gostLista.size()];
			for (int i = 0; i < this.gostLista.size(); i++) {
				string[i] = this.gostLista.get(i).getKorisnickoIme();
			}
			return string;
		}
	
	//cuvanje podataka iz objekta nazad u csv
	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("data/gosti.csv", false));
			for (Gost z : gostLista) {
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
	public Gost find(int id) {
		for (int i = 0; i < gostLista.size(); i++) {
			Gost z = gostLista.get(i);
			if (z.getId() == id) {
				return z;
			}
		}
		return null;
	}
	
	public int get_id(String koriscniko) {
		for (int i = 0; i < gostLista.size(); i++) {
			Gost z = gostLista.get(i);
			if (z.getKorisnickoIme().equals(koriscniko)) {
				return z.getId();
			}
		}
		return -1;
	}
	
	//pronadji zaposlenog po id
		public Gost find_name(String name) {
			for (int i = 0; i < gostLista.size(); i++) {
				Gost z = gostLista.get(i);
				if (z.getKorisnickoIme().equals(name)) {
					return z;
				}
			}
			return null;
		}
	
	//dodaj novog zaposlenog
	public void add(String ime, String prezime, String pol, Date datum, String telefon, String adresa, String korisnickoIme, String lozinka) {
		//random id
		Random random = new Random();
		int id = random.nextInt(8998) + 1001;
		this.gostLista.add(new Gost(id, ime, prezime, pol, datum, telefon, adresa, korisnickoIme, lozinka));
		this.saveData();
	}
	
	//izmeni zaposlenog
	public void edit(int id, String ime, String prezime, String pol, Date datum, String telefon, String adresa, String korisnickoIme, String lozinka) {
		Gost z = this.find(id);
		z.setIme(ime);
		z.setPrezime(prezime);
		z.setAdresa(adresa);
		z.setDatumRodjenja(datum);
		z.setPol(pol);
		z.setTelefon(telefon);
		z.setKorisnickoIme(korisnickoIme);
		z.setLozinka(lozinka);
	}
	
	//obrisi zaposlenog
	public void remove(int id) {
		Gost z = find(id);
		this.gostLista.remove(z);
		this.saveData();
	}
	
	
}