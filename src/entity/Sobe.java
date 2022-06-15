package entity;

import enums.EnumStatusSobe;

public class Sobe {
	private int brojSobe;
	private TipSobe tipSobe;
	private EnumStatusSobe status;
	private Zaposleni spremacica;
	
	public Sobe(int id, TipSobe tipSobe, EnumStatusSobe status, Zaposleni spremacica) {
		this.brojSobe = id;
		this.tipSobe = tipSobe;
		this.status = status;
		this.spremacica = spremacica;
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
		catch (NullPointerException e) {
			
		}
		return brojSobe+";"+tipSobe.getId()+";"+status+";"+id_spremacice;
	}

	public Zaposleni getSpremacica() {
		return spremacica;
	}

	public void setSpremacica(Zaposleni spremacica) {
		this.spremacica = spremacica;
	}
	
}