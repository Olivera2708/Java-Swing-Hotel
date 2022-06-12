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

import entity.CenaUsluge;

public class CenovnikUslugaManager {
	private UslugeManager uslugeManager;
	private List<CenaUsluge> cenovnikUslugaLista;
	
	public CenovnikUslugaManager(UslugeManager uslugeManager) {
		//super(); ne znam jel treba?
		this.cenovnikUslugaLista = new ArrayList<CenaUsluge>();
		this.uslugeManager = uslugeManager;
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/cenovnik.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				if (Integer.parseInt(vrednosti[0]) < 1000) {
					SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
					CenaUsluge cenaSobe = new CenaUsluge(uslugeManager.find(Integer.parseInt(vrednosti[0])), Integer.parseInt(vrednosti[1]), datum.parse(vrednosti[2]), datum.parse(vrednosti[3]));
					this.cenovnikUslugaLista.add(cenaSobe);
				}
			}
			br.close();
		}
		catch (IOException | NumberFormatException | ParseException e1){
			return false;
		}
		return true;
	}
	
	//lista svih soba
	public List<CenaUsluge> getAll(){
		return cenovnikUslugaLista;
	}

	//cuvanje podataka iz objekta nazad u csv
	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("data/cenovnik.csv", false));
			for (CenaUsluge s : cenovnikUslugaLista) {
				pw.println(s.toFileString());
			}
			pw.close();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}

	//pronadji cenu sobe po broju sobe
	public CenaUsluge find(int id) {
		for (int i = 0; i < cenovnikUslugaLista.size(); i++) {
			CenaUsluge s = cenovnikUslugaLista.get(i);
			if (s.getUsluge() == uslugeManager.find(id)) {
				return s;
			}
		}
		return null;
	}
	
	//izmeni sobu
	public void edit(int id, int cena, Date datumOd, Date datumDo) {
		CenaUsluge s = this.find(id);
		s.setUsluge(uslugeManager.find(id));
		s.setCena(cena);
		s.setOdDatum(datumOd);
		s.setDoDatum(datumDo);
	}
	
	//dodaj novu sobu
	public void add(int id, int cena, Date datumOd, Date datumDo) {
		this.cenovnikUslugaLista.add(new CenaUsluge(uslugeManager.find(id), cena, datumOd, datumDo));
		this.saveData();
	}
	
	//obrisi sobu
	public void remove(int id) {
		CenaUsluge s = this.find(id);
		this.cenovnikUslugaLista.remove(s);
		this.saveData();
	}
	
}