package entity;

import java.util.ArrayList;
import java.util.List;

import enums.EnumStatusSobe;

public class Sobe {
	private int brojSobe;
	private TipSobe tipSobe;
	private EnumStatusSobe status;
	private Zaposleni spremacica;
	private List<String[]> datumiSpremanja;
	
	public Sobe(int id, TipSobe tipSobe, EnumStatusSobe status, Zaposleni spremacica) {
		this.brojSobe = id;
		this.tipSobe = tipSobe;
		this.status = status;
		this.spremacica = spremacica;
		this.datumiSpremanja = new ArrayList<String[]>();
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
		return brojSobe+";"+tipSobe.getId()+";"+status+";"+id_spremacice+";"+spremanja;
	}

	public Zaposleni getSpremacica() {
		return spremacica;
	}

	public void setSpremacica(Zaposleni spremacica) {
		this.spremacica = spremacica;
	}
	
}