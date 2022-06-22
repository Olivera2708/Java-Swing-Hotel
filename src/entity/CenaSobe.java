 package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CenaSobe extends Cena {
	private TipSobe tipSobe;
	
	public CenaSobe(int id, TipSobe tipSobe, int cena, Date odDatum, Date doDatum) {
		super(id, cena, odDatum, doDatum);
		this.tipSobe = tipSobe;
	}
	
	public TipSobe getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}

	public String toFileString() {
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String od_datum_string = datum_formatter.format(this.getOdDatum());
		String do_datum_string = datum_formatter.format(this.getDoDatum());
	
		return this.getId()+";"+tipSobe.getId()+";"+this.getCena()+";"+od_datum_string+";"+do_datum_string;
	}	
}