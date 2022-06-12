package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.TipSobe;

public class TipSobeManager {
	private List<TipSobe> tipSobeLista;
	
	public TipSobeManager() {
		//super(); ne znam jel treba?
		this.tipSobeLista = new ArrayList<TipSobe>();
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/tipsoba.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				TipSobe tipSobe = new TipSobe(Integer.parseInt(vrednosti[0]), vrednosti[1]);
				this.tipSobeLista.add(tipSobe);
			}
			br.close();
		}
		catch (IOException | NumberFormatException e1){
			return false;
		}
		return true;
	}
	
	//lista svih tipova soba
	public List<TipSobe> getAll(){
		return tipSobeLista;
	}
	
	//vraca listu naziva
	public String[] getNames() {
		String[] string = new String[this.tipSobeLista.size()];
		for (int i = 0; i < this.tipSobeLista.size(); i++) {
			string[i] = this.tipSobeLista.get(i).getTip();
		}
		return string;
	}
	
	//cuvanje podataka iz objekta nazad u csv
	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("data/tipsoba.csv", false));
			for (TipSobe s : tipSobeLista) {
				pw.println(s.toFileString());
			}
			pw.close();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}
	
	//pronadji tip sobe po broju sobe
	public TipSobe find(int id) {
		for (int i = 0; i < tipSobeLista.size(); i++) {
			TipSobe s = tipSobeLista.get(i);
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	
	//pronadji tip sobe po opisu
		public int get_id(String opis) {
			for (int i = 0; i < tipSobeLista.size(); i++) {
				TipSobe s = tipSobeLista.get(i);
				if (s.getTip().equals(opis)) {
					return s.getId();
				}
			}
			return -1;
		}
	
	//dodaj novi tip sobe
	public void edit(int id, String tip) {
		TipSobe s = this.find(id);
		s.setId(id);
		s.setTip(tip);
	}
	
	//izmeni tip sobe
	public void add(String tip) {
		Random random = new Random();
		int id = random.nextInt(100);
		while (find(id) != null) {
			id = random.nextInt(100);
		}
		this.tipSobeLista.add(new TipSobe(id, tip));
		this.saveData();
	}
	
	//obrisi tip sobe
	public void remove(int id) {
		TipSobe s = find(id);
		this.tipSobeLista.remove(s);
		this.saveData();
	}
	
	
}