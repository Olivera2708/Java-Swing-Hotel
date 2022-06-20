package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.EnumStatusSobe;

public class Sobe {
	private int brojSobe;
	private TipSobe tipSobe;
	private EnumStatusSobe status;
	private Zaposleni spremacica;
	private List<String[]> datumiSpremanja;
	private String[] sadrzaj;
	
	public Sobe(int id, TipSobe tipSobe, EnumStatusSobe status, Zaposleni spremacica, String[] sadrzaj) {
		this.brojSobe = id;
		this.tipSobe = tipSobe;
		this.status = status;
		this.spremacica = spremacica;
		this.datumiSpremanja = new ArrayList<String[]>();
		this.sadrzaj = sadrzaj;
	}

	public List<String[]> getDatumiSpremanja() {
		return datumiSpremanja;
	}

	public void setDatumiSpremanja(List<String[]> datumiSpremanja) {
		this.datumiSpremanja = datumiSpremanja;
	}

	public int getBrojSobe() {
		return brojSobe;
	}

	public void setBrojSobe(int id) {
		this.brojSobe = id;
	}

	public TipSobe getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}

	public EnumStatusSobe getStatus() {
		return status;
	}

	public void setStatus(EnumStatusSobe status) {
		this.status = status;
	}
	
	public String[] getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String[] sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public Zaposleni getSpremacica() {
		return spremacica;
	}

	public void setSpremacica(Zaposleni spremacica) {
		this.spremacica = spremacica;
	}
	
	public boolean sadrzi(String[] sad) {
		if (sad == null) {
			return true;
		}
		for (String s: sad) {
			if (!Arrays.asList(this.sadrzaj).contains(s)) {
				return false;
			}
		}
		return true;
	}
	
	public String toFileString() {
		int id_spremacice = 0;
		try {
			id_spremacice = spremacica.getId();
		}
		catch (NullPointerException e) {}
		String spremanja = "";
		if (!datumiSpremanja.isEmpty()) {
			for (String[] niz: datumiSpremanja) {
				spremanja += niz[0] + " " + niz[1] + ",";
			}
			spremanja = spremanja.substring(0, spremanja.length() - 1);
		}
		String sadrzaji = "";
		if (sadrzaj != null) {
			for (String s: sadrzaj) {
				sadrzaji += s + ",";
			}
			sadrzaji = sadrzaji.substring(0, sadrzaji.length()-1);
		}
		return brojSobe+";"+tipSobe.getId()+";"+status+";"+id_spremacice+";"+spremanja+";"+sadrzaji;
	}
	
}