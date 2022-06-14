package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import enums.EnumStatusSobe;
import entity.Sobe;

public class SobeManager {
	private TipSobeManager tipSobeManager;
	private List<Sobe> sobeLista;
	
	public SobeManager(TipSobeManager tipSobeManager) {
		//super(); ne znam jel treba?
		this.sobeLista = new ArrayList<Sobe>();
		this.tipSobeManager = tipSobeManager;
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/sobe.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				Sobe sobe = new Sobe(Integer.parseInt(vrednosti[0]), tipSobeManager.find(Integer.parseInt(vrednosti[1])), EnumStatusSobe.valueOf(vrednosti[2]));
				this.sobeLista.add(sobe);
			}
			br.close();
		}
		catch (IOException | NumberFormatException e1){
			return false;
		}
		return true;
	}
	
	//lista svih soba
	public List<Sobe> getAll(){
		return sobeLista;
	}
	
	//cuvanje podataka iz objekta nazad u csv
	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("data/sobe.csv", false));
			for (Sobe s : sobeLista) {
				pw.println(s.toFileString());
			}
			pw.close();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}
	
	//pronadji sobu po broju sobe
	public Sobe find(int id) {
		for (int i = 0; i < sobeLista.size(); i++) {
			Sobe s = sobeLista.get(i);
			if (s.getBrojSobe() == id) {
				return s;
			}
		}
		return null;
	}
	
	//izmeni sobu
	public void edit(int id, int novi_id, int tipSobe, String status) {
		Sobe s = this.find(id);
		s.setBrojSobe(novi_id);
		s.setTipSobe(tipSobeManager.find(tipSobe));
		s.setStatus(EnumStatusSobe.valueOf(status));
		saveData();
	}
	
	//dodaj novu sobu
	public void add(int id, int tipSobe, String status) {
		this.sobeLista.add(new Sobe(id, tipSobeManager.find(tipSobe), EnumStatusSobe.valueOf(status)));
		this.saveData();
	}
	
	//obrisi sobu
	public void remove(int id) {
		Sobe s = find(id);
		this.sobeLista.remove(s);
		this.saveData();
	}
	
}