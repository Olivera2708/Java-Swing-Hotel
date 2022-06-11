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

import entity.CenaSobe;

public class CenovnikSobaManager {
	private TipSobeManager tipSobeManager;
	private List<CenaSobe> cenovnikSobaLista;
	
	public CenovnikSobaManager(TipSobeManager tipSobeManager) {
		//super(); ne znam jel treba?
		this.cenovnikSobaLista = new ArrayList<CenaSobe>();
		this.tipSobeManager = tipSobeManager;
	}
	
	//ucitavanje podataka u objekat
	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader("data/cenovnik.csv"))){
			String linija = null;
			while((linija = br.readLine()) != null) {
				String[] vrednosti = linija.split(";");
				if (Integer.parseInt(vrednosti[0]) < 1000) {
					SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
					CenaSobe cenaSobe = new CenaSobe(tipSobeManager.find(Integer.parseInt(vrednosti[0])), Integer.parseInt(vrednosti[1]), datum.parse(vrednosti[2]), datum.parse(vrednosti[3]));
					this.cenovnikSobaLista.add(cenaSobe);
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
	public List<CenaSobe> getAll(){
		return cenovnikSobaLista;
	}

	//cuvanje podataka iz objekta nazad u csv
	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("data/zaposleni.csv", false));
			for (CenaSobe s : cenovnikSobaLista) {
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
	public CenaSobe find(int id) {
		for (int i = 0; i < cenovnikSobaLista.size(); i++) {
			CenaSobe s = cenovnikSobaLista.get(i);
			if (s.getTipSobe() == tipSobeManager.find(id)) {
				return s;
			}
		}
		return null;
	}
	
	//izmeni sobu
	public void edit(int id, int cena, Date datumOd, Date datumDo) {
		CenaSobe s = this.find(id);
		s.setTipSobe(tipSobeManager.find(id));
		s.setCena(cena);
		s.setOdDatum(datumOd);
		s.setDoDatum(datumDo);
	}
	
	//dodaj novu sobu
	public void add(int id, int cena, Date datumOd, Date datumDo) {
		this.cenovnikSobaLista.add(new CenaSobe(tipSobeManager.find(id), cena, datumOd, datumDo));
		this.saveData();
	}
	
	//obrisi sobu
	public void remove(int id) {
		CenaSobe s = this.find(id);
		this.cenovnikSobaLista.remove(s);
		this.saveData();
	}
	
}