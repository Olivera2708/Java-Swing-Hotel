package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import enums.EnumStatusRezervacije;

public class Rezervacije {
	private int id;
	private TipSobe tipSobe;
	private Usluge usluge;
	private Gost gost;
	private Date odDatum;
	private Date doDatum;
	private int cena;
	private EnumStatusRezervacije status;
	
	public Rezervacije(int id, TipSobe tipSobe, Usluge usluge, Gost gost, Date odDatum, Date doDatum, int cena, EnumStatusRezervacije status) {
		this.id = id;
		this.tipSobe = tipSobe;
		this.usluge = usluge;
		this.gost = gost;
		this.odDatum = odDatum;
		this.doDatum = doDatum;
		this.cena = cena;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public TipSobe getTipSobe() {
		return tipSobe;
	}


	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}


	public Usluge getUsluge() {
		return usluge;
	}


	public void setUsluge(Usluge usluge) {
		this.usluge = usluge;
	}


	public Gost getGost() {
		return gost;
	}


	public void setGost(Gost gost) {
		this.gost = gost;
	}


	public Date getOdDatum() {
		return odDatum;
	}


	public void setOdDatum(Date odDatum) {
		this.odDatum = odDatum;
	}


	public Date getDoDatum() {
		return doDatum;
	}


	public void setDoDatum(Date doDatum) {
		this.doDatum = doDatum;
	}


	public int getCena() {
		return cena;
	}


	public void setCena(int cena) {
		this.cena = cena;
	}


	public EnumStatusRezervacije getStatus() {
		return status;
	}


	public void setStatus(EnumStatusRezervacije status) {
		this.status = status;
	}


	public String toFileString() {
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String od_datum_string = datum_formatter.format(odDatum);
		String do_datum_string = datum_formatter.format(doDatum);
	
		return id+";"+tipSobe.getId()+";"+usluge.getId()+";"+gost.getKorisnickoIme()+";"+od_datum_string+";"+do_datum_string+";"+cena+";"+status;
	}
}