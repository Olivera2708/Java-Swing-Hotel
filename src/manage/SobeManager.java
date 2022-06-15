package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import enums.EnumStatusSobe;
import entity.Sobe;
import entity.Zaposleni;

public class SobeManager {
	private TipSobeManager tipSobeManager;
	private ZaposleniManager zaposleniManager;
	private List<Sobe> sobeLista;
	
	public SobeManager(TipSobeManager tipSobeManager, ZaposleniManager zaposleniManager) {
		//super(); ne znam jel treba?
		this.sobeLista = new ArrayList<Sobe>();
		this.tipSobeManager = tipSobeManager;
		this.zaposleniManager = zaposleniManager;
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/sobe.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				Zaposleni spremacica = null;
				try {
					int idSpremacice = Integer.parseInt(vrednosti[3]);
					spremacica = zaposleniManager.find(idSpremacice);
				}
				catch (ArrayIndexOutOfBoundsException e) {
				}
				Sobe sobe = new Sobe(Integer.parseInt(vrednosti[0]), tipSobeManager.find(Integer.parseInt(vrednosti[1])), EnumStatusSobe.valueOf(vrednosti[2]), spremacica);
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
	
	public void dodeliSobuSpremacici(int id) {
		//za svaku proveriti koliko ima soba da sprema
		HashMap<Zaposleni, Integer> mapa = new HashMap<Zaposleni, Integer>();
		for (Sobe s: sobeLista) {
			if (s.getSpremacica() != null) {
				if (mapa.containsKey(s.getSpremacica())) {
					mapa.put(s.getSpremacica(), mapa.get(s.getSpremacica())+1);
				}
				else {
					mapa.put(s.getSpremacica(), 1);
				}
			}
		}
		//odredi koja ima najmanje
		Zaposleni najmanje = null;
		
		for (Zaposleni z: zaposleniManager.getAll()) {
			if (z.getPozicija().equals("Sobarica")) {
				if (!mapa.containsKey(z)){
					mapa.put(z, 0);
					najmanje = z;
				}
			}
		}
		
		int min = 0;
		for (Zaposleni z: mapa.keySet()) {
			if (mapa.get(z) <= min) {
				min = mapa.get(z);
				najmanje = z;
			}
		}
		
		this.edit(id, id, this.find(id).getTipSobe().getId(), "SPREMANJE", najmanje.getId());
	}
	
	public List<Integer> getSlobodneSobe(int tipSobe){
		List<Integer> slobodne = new ArrayList<Integer>();
		for (Sobe s: sobeLista) {
			if (String.valueOf(s.getStatus()).equals("SLOBODNA") && s.getTipSobe().getId()== tipSobe) {
				slobodne.add(s.getBrojSobe());
			}
		}
		return slobodne;
	}
	
	public List<Sobe> getPosao(Zaposleni sobarica){
		List<Sobe> sobe = new ArrayList<Sobe>();
		for (Sobe s: sobeLista) {
			if (s.getSpremacica() == sobarica) {
				sobe.add(s);
			}
		}
		return sobe;
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
	public void edit(int id, int novi_id, int tipSobe, String status, int spremacicaId) {
		Sobe s = this.find(id);
		s.setBrojSobe(novi_id);
		s.setTipSobe(tipSobeManager.find(tipSobe));
		s.setStatus(EnumStatusSobe.valueOf(status));
		s.setSpremacica(zaposleniManager.find(spremacicaId));
		saveData();
	}
	
	//dodaj novu sobu
	public void add(int id, int tipSobe, String status) {
		this.sobeLista.add(new Sobe(id, tipSobeManager.find(tipSobe), EnumStatusSobe.valueOf(status), null));
		this.saveData();
	}
	
	//obrisi sobu
	public void remove(int id) {
		Sobe s = find(id);
		this.sobeLista.remove(s);
		this.saveData();
	}
	
}