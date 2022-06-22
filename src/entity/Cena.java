package entity;

import java.util.Date;

public abstract class Cena{
	private int id;
	private int cena;
	private Date odDatum;
	private Date doDatum;
	
	public Cena(int id, int cena, Date odDatum, Date doDatum) {
		this.id = id;
		this.cena = cena;
		this.odDatum = odDatum;
		this.doDatum = doDatum;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCena() {
		return cena;
	}
	public void setCena(int cena) {
		this.cena = cena;
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
	
}