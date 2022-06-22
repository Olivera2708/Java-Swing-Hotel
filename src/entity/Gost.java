package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Gost extends Korisnik {
	
	public Gost(int id, String ime, String prezime, String pol, Date datum, String telefon, String adresa, String korisnickoIme, String lozinka) {
		super(id, ime, prezime, pol, datum, telefon, adresa, korisnickoIme, lozinka);
	}
	
	public String toFileString() {
		SimpleDateFormat datum_formatter = new SimpleDateFormat("yyyy-MM-dd");
		String datum_string = datum_formatter.format(this.getDatumRodjenja());
	
		return this.getId()+";"+this.getIme()+";"+this.getPrezime()+";"+this.getPol()+";"+datum_string+";"+this.getTelefon()+";"+this.getAdresa()+";"+this.getKorisnickoIme()+";"+this.getLozinka();
	}
	
}