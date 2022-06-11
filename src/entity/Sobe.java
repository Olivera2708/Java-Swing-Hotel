package entity;

import enums.EnumStatusSobe;

public class Sobe {
	private int brojSobe;
	private TipSobe tipSobe;
	private EnumStatusSobe status;
	
	public Sobe(int id, TipSobe tipSobe, EnumStatusSobe status) {
		this.brojSobe = id;
		this.tipSobe = tipSobe;
		this.status = status;
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
		return brojSobe+";"+tipSobe.getId()+";"+status;
	}
	
}