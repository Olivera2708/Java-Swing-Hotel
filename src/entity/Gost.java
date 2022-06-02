package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Gost extends Korisnik {
	
	public Gost(int id, String ime, String prezime, String pol, Date datum, String telefon, String adresa, String korisnickoIme, String lozinka) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.datumRodjenja = datum;
		this.pol = pol;
		this.telefon = telefon;
		this.adresa = adresa;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}
	
	public String toFileString() {
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String datum_string = datum_formatter.format(datumRodjenja);
	
		return id+";"+ime+";"+prezime+";"+pol+";"+datum_string+";"+telefon+";"+adresa+";"+korisnickoIme+";"+lozinka;
	}
	
}