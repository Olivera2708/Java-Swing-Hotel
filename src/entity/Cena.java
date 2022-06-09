package entity;

import java.util.Date;

public abstract class Cena{
	int cena;
	Date odDatum;
	Date doDatum;
	
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