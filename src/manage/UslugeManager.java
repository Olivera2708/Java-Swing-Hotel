package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.Usluge;

public class UslugeManager {
	private List<Usluge> uslugeLista;
	
	public UslugeManager() {
		//super(); ne znam jel treba?
		this.uslugeLista = new ArrayList<Usluge>();
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/usluge.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				Usluge usluge = new Usluge(Integer.parseInt(vrednosti[0]), vrednosti[1]);
				this.uslugeLista.add(usluge);
			}
			br.close();
		}
		catch (IOException | NumberFormatException e1){
			return false;
		}
		return true;
	}
	
	//lista svih soba
	public List<Usluge> getAll(){
		return uslugeLista;
	}
	
	//vraca listu naziva
		public String[] getNames() {
			String[] string = new String[this.uslugeLista.size()];
			for (int i = 0; i < this.uslugeLista.size(); i++) {
				string[i] = this.uslugeLista.get(i).getTip();
			}
			return string;
		}
	
	//cuvanje podataka iz objekta nazad u csv
	public boolean saveData() {
//		PrintWriter pw = null;
//		try {
//			pw = new PrintWriter(new FileWriter("data/tipsoba.csv", false));
//			for (Usluge s : uslugeLista) {
//				pw.println(s.toFileString());
//			}
//			pw.close();
//		}
//		catch (IOException e) {
//			return false;
//		}
		return true;
	}
	
	//pronadji sobu po broju sobe
	public Usluge find(int id) {
		for (int i = 0; i < uslugeLista.size(); i++) {
			Usluge s = uslugeLista.get(i);
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	
	public int get_id(String opis) {
		for (int i = 0; i < uslugeLista.size(); i++) {
			Usluge s = uslugeLista.get(i);
			if (s.getTip().equals(opis)) {
				return s.getId();
			}
		}
		return -1;
	}
	
	//dodaj novu uslugu
	public void edit(int id, String usluga) {
		Usluge s = this.find(id);
		s.setId(id);
		s.setTip(usluga);
		saveData();
	}
	
	//izmeni uslugu
	public void add(String usluga) {
		Random random = new Random();
		int id = random.nextInt(8998) + 1001;
		while (find(id) != null) {
			id = random.nextInt(8998) + 1001;
		}
		this.uslugeLista.add(new Usluge(id, usluga));
		this.saveData();
	}
	
	//obrisi uslugu
	public void remove(int id) {
		Usluge s = find(id);
		this.uslugeLista.remove(s);
		this.saveData();
	}
	
}