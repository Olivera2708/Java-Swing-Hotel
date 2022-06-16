package entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import enums.EnumStatusRezervacije;

public class Rezervacije {
	private int id;
	private TipSobe tipSobe;
	private List<Usluge> usluge;
	private Gost gost;
	private Date odDatum;
	private Date doDatum;
	private Sobe soba;
	private Date konacanDatum;
	private int cena;
	private EnumStatusRezervacije status;
	
	public Rezervacije(int id, TipSobe tipSobe, List<Usluge> usluge, Gost gost, Date odDatum, Date doDatum, int cena, EnumStatusRezervacije status, Sobe soba) {
		this.id = id;
		this.tipSobe = tipSobe;
		this.usluge = usluge;
		this.gost = gost;
		this.odDatum = odDatum;
		this.doDatum = doDatum;
		this.cena = cena;
		this.status = status;
		this.soba = soba;
		this.konacanDatum = null;
	}
	
	public Date getKonacanDatum() {
		return konacanDatum;
	}

	public void setKonacanDatum(Date konacanDatum) {
		this.konacanDatum = konacanDatum;
	}

	public Sobe getSoba() {
		return soba;
	}

	public void setSoba(Sobe soba) {
		this.soba = soba;
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


	public List<Usluge> getUsluge() {
		return usluge;
	}


	public void setUsluge(List<Usluge> lista_usluga) {
		this.usluge = lista_usluga;
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
		
		String us = "";
		if (!usluge.isEmpty()) {
			for(Usluge u: usluge) {
				us += u.getId() + ",";
			}
			us = us.substring(0, us.length()-1);
		}
		
		String brojSobe = "";
		if (soba != null) {
			brojSobe = String.valueOf(soba.getBrojSobe());
		}
		String kDatum = "";
		if (konacanDatum != null) {
			kDatum = datum_formatter.format(konacanDatum);
		}
		return id+";"+tipSobe.getId()+";"+us+";"+gost.getKorisnickoIme()+";"+od_datum_string+";"+do_datum_string+";"+cena+";"+status+";"+brojSobe+";"+kDatum;
	}
}