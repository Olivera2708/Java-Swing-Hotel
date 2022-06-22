package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CenaUsluge extends Cena {
	private Usluge usluge;
	
	public CenaUsluge(int id, Usluge usluge, int cena, Date odDatum, Date doDatum) {
		super(id, cena, odDatum, doDatum);
		this.usluge = usluge;
	}
	
	public Usluge getUsluge() {
		return usluge;
	}

	public void setUsluge(Usluge usluge) {
		this.usluge = usluge;
	}	

	public String toFileString() {
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String od_datum_string = datum_formatter.format(this.getOdDatum());
		String do_datum_string = datum_formatter.format(this.getDoDatum());
	
		return this.getId()+";"+usluge.getId()+";"+this.getCena()+";"+od_datum_string+";"+do_datum_string;
	}
}