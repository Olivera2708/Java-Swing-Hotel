package test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Gost;
import manage.ManageAll;

public class GostManagerTest {
	public static ManageAll manageAll = ManageAll.getInstance();
	static SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Gost test start");
		
		manageAll.getGostManager().add("Gost", "Gostic", "Muško", datum.parse("1999-12-15"), "0643782578", "Bate Brkica 23, Novi Sad", "gost@gmail.com", "012345678");
		manageAll.getGostManager().add("GostTest", "GosticTest", "Žensko", datum.parse("1989-10-25"), "0647255798", "Dusana Danilovica 27, Novi Sad", "gosttest@gmail.com", "098765432");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Gost test kraj");
	}
	
	@Test
	public void testLogin() {
		Gost korisnik = manageAll.getGostManager().LogIn("recep", "recep123");
		assertTrue(korisnik == null);
		
		korisnik = manageAll.getGostManager().LogIn("gost@gmail.com", "012345678");
		assertTrue(korisnik != null);
		
		korisnik = manageAll.getGostManager().LogIn("gosttest@gmail.com", "098765432");
		assertTrue(korisnik != null);
	}
	
	@Test
	public void testVecPosotojiKorisnickoIme() {
		boolean test = manageAll.getGostManager().vecPostojiKorisnicko("admin");
		assertTrue(test != true);
		
		test = manageAll.getGostManager().vecPostojiKorisnicko("gost@gmail.com");
		assertTrue(test == true);
	}
	
	@Test
	public void testGetNames() {
		String[] test = manageAll.getGostManager().getNames();
		String[] tacno = {"gost@gmail.com", "gosttest@gmail.com"};
		assertArrayEquals(test, tacno);
	}
	
	@Test
	public void testFind() {
		Gost z = manageAll.getGostManager().getAll().get(0);
		Gost test = manageAll.getGostManager().find(z.getId());
		assertTrue(z == test);
	}
	
	@Test
	public void testGetId() {
		Gost g = manageAll.getGostManager().getAll().get(0);
		int test = manageAll.getGostManager().get_id(g.getKorisnickoIme());
		assertTrue(g.getId() == test);
	}
	
	@Test
	public void testFindName() {
		Gost g = manageAll.getGostManager().getAll().get(0);
		Gost test = manageAll.getGostManager().find_name(g.getKorisnickoIme());
		assertTrue(g == test);
	}
	
	@Test
	public void testRemove() {
		Gost z = manageAll.getGostManager().getAll().get(0);
		manageAll.getGostManager().remove(z.getId());
		Gost test = manageAll.getGostManager().find(z.getId());
		assertTrue(test == null);
	}
	
	@Test
	public void testEdit() {
		Gost z = manageAll.getGostManager().getAll().get(0);
		manageAll.getGostManager().edit(z.getId(), "Gostgost", z.getPrezime(), z.getPol(), z.getDatumRodjenja(), z.getTelefon(), z.getAdresa(), z.getKorisnickoIme(), z.getLozinka());
		assertTrue(z.getIme().equals("Gostgost"));
	}
}