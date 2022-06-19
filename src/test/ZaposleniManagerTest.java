package test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Zaposleni;
import manage.ManageAll;

public class ZaposleniManagerTest {
	public static ManageAll manageAll = ManageAll.getInstance();
	static SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Zaposleni test start");
		
		manageAll.getZaposleniManager().add("Recep", "Recepic", "Muško", datum.parse("1999-12-15"), "06437825798", "Bate Brkica 23, Novi Sad", "recep", "recep123", 5, 4, "Recepcioner");
		manageAll.getZaposleniManager().add("Sobarka", "Sobric", "Žensko", datum.parse("1989-10-25"), "06437255798", "Dusana Danilovica 27, Novi Sad", "sobarka", "sobarka123", 4, 15, "Sobarica");
		manageAll.getZaposleniManager().add("Admin", "Adminic", "Muško", datum.parse("1995-11-9"), "0123782578", "Kneza Milosa 17, Novi Sad", "admin", "admin123", 7, 4, "Admin");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Zaposleni test kraj");
	}
	
	@Test
	public void testLogin() {
		Zaposleni korisnik = manageAll.getZaposleniManager().LogIn("recep", "recep123");
		assertTrue(korisnik != null);
		
		korisnik = manageAll.getZaposleniManager().LogIn("sobarka", "sobarka123");
		assertTrue(korisnik != null);
		
		korisnik = manageAll.getZaposleniManager().LogIn("admin", "admin123");
		assertTrue(korisnik != null);
		
		korisnik = manageAll.getZaposleniManager().LogIn("admin123", "admin");
		assertTrue(korisnik == null);
	}
	
	@Test
	public void testVecPosotjiKorisnickoIme() {
		boolean test = manageAll.getZaposleniManager().vecPostojiKorisnicko("admin");
		assertTrue(test == true);
		
		test = manageAll.getZaposleniManager().vecPostojiKorisnicko("admin123");
		assertTrue(test != true);
	}
	
	@Test
	public void testBrojSpremacica() {
		int test = manageAll.getZaposleniManager().brojSpremacica();
		assertTrue(test == 1);
	}
	
	@Test
	public void testFind() {
		Zaposleni z = manageAll.getZaposleniManager().getAll().get(0);
		Zaposleni test = manageAll.getZaposleniManager().find(z.getId());
		assertTrue(z == test);
	}
	
	@Test
	public void testRemove() {
		Zaposleni z = manageAll.getZaposleniManager().getAll().get(0);
		manageAll.getZaposleniManager().remove(z.getId());
		Zaposleni test = manageAll.getZaposleniManager().find(z.getId());
		assertTrue(test == null);
	}
	
	@Test
	public void testEdit() {
		Zaposleni z = manageAll.getZaposleniManager().getAll().get(0);
		manageAll.getZaposleniManager().edit(z.getId(), "Recepcion", z.getPrezime(), z.getPol(), z.getDatumRodjenja(), z.getTelefon(), z.getAdresa(), z.getKorisnickoIme(), z.getLozinka(), z.getStaz(), z.getStrucnaSprema(), z.getPozicija());
		assertTrue(z.getIme().equals("Recepcion"));
	}
}