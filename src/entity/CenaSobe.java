package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CenaSobe extends Cena {
	TipSobe tipSobe;
	
	public CenaSobe(int id, TipSobe tipSobe, int cena, Date odDatum, Date doDatum) {
		super();
		this.id = id;
		this.tipSobe = tipSobe;
		this.cena = cena;
		this.odDatum = odDatum;
		this.doDatum = doDatum;
	}
	
	public TipSobe getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}

	public String toFileString() {
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String od_datum_string = datum_formatter.format(odDatum);
		String do_datum_string = datum_formatter.format(doDatum);
	
		return id+";"+tipSobe.getId()+";"+cena+";"+od_datum_string+";"+do_datum_string;
	}	
}