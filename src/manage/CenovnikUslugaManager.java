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
				if (Integer.parseInt(vrednosti[1]) > 1000) {
					SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
					CenaUsluge cenaSobe = new CenaUsluge(Integer.parseInt(vrednosti[0]), uslugeManager.find(Integer.parseInt(vrednosti[1])), Integer.parseInt(vrednosti[2]), datum.parse(vrednosti[3]), datum.parse(vrednosti[4]));
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
	//za potrebe testiranja komentarisati save
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
	
	public boolean appendData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("data/cenovnik.csv", true));
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
	
	public int get_cena(int id_usluge, Date datum) {
		for (CenaUsluge s: cenovnikUslugaLista) {
			if (s.getUsluge().getId() == id_usluge) {
				if(datum.after(s.getOdDatum()) && datum.before(s.getDoDatum()) || datum.equals(s.getDoDatum()) || datum.equals(s.getOdDatum())) {
					return s.getCena();
				}
			}
		}
		return -1;
	}

	//pronadji cenu sobe po broju sobe
	public CenaUsluge find(int id) {
		for (int i = 0; i < cenovnikUslugaLista.size(); i++) {
			CenaUsluge s = cenovnikUslugaLista.get(i);
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	
	//izmeni sobu
	public void edit(int id, int idi, int cena, Date datumOd, Date datumDo) {
		CenaUsluge s = this.find(id);
		s.setUsluge(uslugeManager.find(idi));
		s.setCena(cena);
		s.setOdDatum(datumOd);
		s.setDoDatum(datumDo);
		saveData();
	}
	
	//dodaj novu sobu
	public void add(int idi, int cena, Date datumOd, Date datumDo) {
		Random random = new Random();
		int id = random.nextInt(8998) + 1001;
		while (find(id) != null) {
			id = random.nextInt(8998) + 1001;
		}
		this.cenovnikUslugaLista.add(new CenaUsluge(id, uslugeManager.find(idi), cena, datumOd, datumDo));
		this.saveData();
	}
	
	//obrisi sobu
	public void remove(int id) {
		this.cenovnikUslugaLista.remove(id);
		this.saveData();
	}
	
}