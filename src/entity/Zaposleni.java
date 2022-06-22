package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Zaposleni extends Korisnik {
	String pozicija;
	int strucnaSprema;
	int staz;
	double plata;
	
	public Zaposleni(int id, String ime, String prezime, String pol, Date datum, String telefon, String adresa, String korisnickoIme, String lozinka, int strucnaSprema, int staz, String pozicija) {
		super(id, ime, prezime, pol, datum, telefon, adresa, korisnickoIme, lozinka);
		this.strucnaSprema = strucnaSprema;
		this.pozicija = pozicija;
		this.staz = staz;
		this.plata = 50000 * (this.strucnaSprema * 0.3 + 1 + this.staz/100);
	}
	
	public String getPozicija() {
		return pozicija;
	}

	public void setPozicija(String pozicija) {
		this.pozicija = pozicija;
	}

	public int getStrucnaSprema() {
		return strucnaSprema;
	}

	public void setStrucnaSprema(int strucnaSprema) {
		this.strucnaSprema = strucnaSprema;
	}

	public int getStucnaSprema() {
		return strucnaSprema;
	}
	public void setStucnaSprema(int stucnaSprema) {
		this.strucnaSprema = stucnaSprema;
	}
	public int getStaz() {
		return staz;
	}
	public void setStaz(int staz) {
		this.staz = staz;
	}
	public double getPlata() {
		return plata;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	
	public String toFileString() {
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String datum_string = datum_formatter.format(this.getDatumRodjenja());
		
		return this.getId()+";"+this.getIme()+";"+this.getPrezime()+";"+this.getPol()+";"+datum_string+";"+this.getTelefon()+";"+this.getAdresa()+";"+this.getKorisnickoIme()+";"+this.getLozinka()+";"+strucnaSprema+";"+staz+";"+pozicija;
	}
}