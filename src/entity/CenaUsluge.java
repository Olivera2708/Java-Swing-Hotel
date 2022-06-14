package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CenaUsluge extends Cena {
	Usluge usluge;
	
	public CenaUsluge(int id, Usluge usluge, int cena, Date odDatum, Date doDatum) {
		super();
		this.id = id;
		this.usluge = usluge;
		this.cena = cena;
		this.odDatum = odDatum;
		this.doDatum = doDatum;
	}
	
	public Usluge getUsluge() {
		return usluge;
	}

	public void setUsluge(Usluge usluge) {
		this.usluge = usluge;
	}	

	public String toFileString() {
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String od_datum_string = datum_formatter.format(odDatum);
		String do_datum_string = datum_formatter.format(doDatum);
	
		return id+";"+usluge.getId()+";"+cena+";"+od_datum_string+";"+do_datum_string;
	}
}