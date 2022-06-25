package test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Gost;
import entity.Rezervacije;
import entity.TipSobe;
import entity.Usluge;
import manage.ManageAll;

public class RezervacijeManagerTest {
	public static ManageAll manageAll = ManageAll.getInstance();
	static List<TipSobe> tipovi;
	static List<Usluge> usluge;
	static SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
	
	static String[] sad1 = {"TV", "Klima"};
	static String[] sad2 = {"Balkon, Klima"};
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Rezervacije test start");
		
		//tip sobe
		manageAll.getTipSobeManager().add("jednokrevetna");
		manageAll.getTipSobeManager().add("dvokrevetna");
		tipovi = manageAll.getTipSobeManager().getAll();
		
		//cena soba
		manageAll.getCenovnikSobaManager().add(tipovi.get(0).getId(), 3000, datum.parse("2022-8-10"), datum.parse("2022-8-20"));
		manageAll.getCenovnikSobaManager().add(tipovi.get(1).getId(), 2300, datum.parse("2022-9-10"), datum.parse("2022-10-10"));
		manageAll.getCenovnikSobaManager().add(tipovi.get(1).getId(), 2000, datum.parse("2022-5-10"), datum.parse("2022-7-18"));
		
		//usluge
		manageAll.getUslugeManager().add("doruca");
		manageAll.getUslugeManager().add("rucak");
		usluge = manageAll.getUslugeManager().getAll();
		
		//cena usluga
		manageAll.getCenovnikUslugaManager().add(usluge.get(0).getId(), 700, datum.parse("2022-8-10"), datum.parse("2022-8-20"));
		manageAll.getCenovnikUslugaManager().add(usluge.get(1).getId(), 500, datum.parse("2022-6-10"), datum.parse("2022-10-10"));
		
		//gosti
		manageAll.getGostManager().add("Gost", "Gostic", "Muško", datum.parse("1999-12-15"), "0643782578", "Bate Brkica 23, Novi Sad", "gost@gmail.com", "012345678");
		manageAll.getGostManager().add("GostTest", "GosticTest", "Žensko", datum.parse("1989-10-25"), "0647255798", "Dusana Danilovica 27, Novi Sad", "gosttest@gmail.com", "098765432");
		
		//sobe
		manageAll.getSobeManager().add(202, manageAll.getTipSobeManager().getAll().get(0).getId(), "SLOBODNA", null);
		manageAll.getSobeManager().add(203, manageAll.getTipSobeManager().getAll().get(0).getId(), "SLOBODNA", sad1);
		manageAll.getSobeManager().add(204, manageAll.getTipSobeManager().getAll().get(1).getId(), "SLOBODNA", sad2);
		
		//zaposleni
		manageAll.getZaposleniManager().add("Recep", "Recepic", "Muško", datum.parse("1999-12-15"), "06437825798", "Bate Brkica 23, Novi Sad", "recep", "recep123", 5, 4, "Recepcioner");
		manageAll.getZaposleniManager().add("Sobarka", "Sobric", "Žensko", datum.parse("1989-10-25"), "06437255798", "Dusana Danilovica 27, Novi Sad", "sobarka", "sobarka123", 4, 15, "Sobarica");
		manageAll.getZaposleniManager().add("Admin", "Adminic", "Muško", datum.parse("1995-11-9"), "0123782578", "Kneza Milosa 17, Novi Sad", "admin", "admin123", 7, 4, "Admin");
		
		int[] usluge_upis = {};
		//postavka za rezervacije
		Gost g = manageAll.getGostManager().getAll().get(0);
		//rezervacije
		manageAll.getRezervacijeManager().add(tipovi.get(0).getId(), usluge_upis, g.getId(), datum.parse("2022-08-11"), datum.parse("2022-08-16"), "NA_CEKANJU", sad1);
		
		manageAll.getRezervacijeManager().add(tipovi.get(1).getId(), usluge_upis, g.getId(), datum.parse("2022-05-11"), datum.parse("2022-05-16"), "NA_CEKANJU", sad1);
		
		//postavka za rezervacije
		usluge_upis = new int[1];
		usluge_upis[0] = usluge.get(1).getId();
		g = manageAll.getGostManager().getAll().get(1);
		manageAll.getRezervacijeManager().add(tipovi.get(1).getId(), usluge_upis, g.getId(), datum.parse("2022-09-10"), datum.parse("2022-09-20"), "NA_CEKANJU", null);
		
		manageAll.getRezervacijeManager().add(tipovi.get(1).getId(), usluge_upis, g.getId(), new Date(), datum.parse("2022-07-12"), "POTVRDJENA", null);
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Rezervacije test kraj");
	}
	
	@Test
	public void testAdd() throws ParseException {
		int[] usluge_upis = {};
		//postavka za rezervacije
		Gost g = manageAll.getGostManager().getAll().get(0);
		//rezervacije
		manageAll.getRezervacijeManager().add(tipovi.get(0).getId(), usluge_upis, g.getId(), datum.parse("2022-08-15"), datum.parse("2022-08-19"), "NA_CEKANJU", sad2);
		int sve = manageAll.getRezervacijeManager().getAll().size();
		assertTrue(sve == 5);
	}
	
	@Test
	public void getAll() {
		int test = manageAll.getRezervacijeManager().getAll().size();
		assertTrue(test == 5);
	}
	
	@Test
	public void testUpdate() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(1);
		manageAll.getRezervacijeManager().update();
		assertEquals(String.valueOf(r.getStatus()), "ODBIJENA");
	}
	
	@Test
	public void testEditStatus() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(0);
		manageAll.getRezervacijeManager().editStatus(r.getId(), "ODBIJENA");
		assertEquals(String.valueOf(r.getStatus()), "ODBIJENA");
	}
	
	@Test
	public void testDodelaSobe() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(3);
		manageAll.getRezervacijeManager().dodelaSobe(r.getId(), 203);
		assertTrue(r.getSoba().getBrojSobe() == 203);
	}
	
	@Test
	public void testPromeniSobe() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(0);
		manageAll.getRezervacijeManager().promeniSobe(202, 904);
		assertTrue(r.getSoba().getBrojSobe() == 904);
 	}
	
	@Test
	public void testPromeniGost() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(1);
		manageAll.getRezervacijeManager().promeniGosta("gost@gmail.com", "gostic@gmail.com");
		assertEquals(r.getGost().getKorisnickoIme(), "gostic@gmail.com");
	}
	
	@Test
	public void testDodajDatumKraja() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(1);
		manageAll.getRezervacijeManager().dodajDatumKraja(r);
		assertEquals(datum.format(r.getKonacanDatum()), datum.format(new Date()));
	}
	
	@Test
	public void testUcitajDatumKraja() throws ParseException {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(1);
		manageAll.getRezervacijeManager().ucitajDatumKraja(datum.parse("2022-06-19"), r);
		assertEquals(r.getKonacanDatum(), datum.parse("2022-06-19"));
	}
	
	@Test
	public void testGetRezervacije() {
		Gost g = manageAll.getGostManager().getAll().get(1);
		List<Rezervacije> rezervacije = new ArrayList<>();
		rezervacije.add(manageAll.getRezervacijeManager().getAll().get(2));
		rezervacije.add(manageAll.getRezervacijeManager().getAll().get(3));
		List<Rezervacije> test = manageAll.getRezervacijeManager().getRezervacije(g);
		assertEquals(rezervacije, test);
	}
	
	@Test
	public void testGetRezervacijeNaCekanju() {
		List<Rezervacije> rezervacije = new ArrayList<>();
		rezervacije.add(manageAll.getRezervacijeManager().getAll().get(2));
		List<Rezervacije> test = manageAll.getRezervacijeManager().getRezervacijeNaCekanju();
		assertEquals(rezervacije, test);
	}
	
	@Test
	public void testGetRezervacijePotvrdjene() {
		List<Rezervacije> rezervacije = new ArrayList<>();
		List<Rezervacije> test = manageAll.getRezervacijeManager().getRezervacijePotvrdjene();
		assertEquals(rezervacije, test);
	}
	
	@Test
	public void testGetDolasci() {
		List<Rezervacije> rezervacije = new ArrayList<>();
		rezervacije.add(manageAll.getRezervacijeManager().getAll().get(3));
		List<Rezervacije> test = manageAll.getRezervacijeManager().getDolasci();
		assertEquals(rezervacije, test);
	}
	
	@Test
	public void testGetOdlasci() {
		List<Rezervacije> rezervacije = new ArrayList<>();
		List<Rezervacije> test = manageAll.getRezervacijeManager().getOdlasci();
		assertEquals(rezervacije, test);
	}
	
	@Test
	public void testGetRezervacijePotvrdjeneCheckOut() {
		List<Rezervacije> rezervacije = new ArrayList<>();
		List<Rezervacije> test = manageAll.getRezervacijeManager().getRezervacijePotvrdjeneCheckOut();
		assertEquals(rezervacije, test);
	}
	
	@Test
	public void testGetPrihodi() throws ParseException {
		int test = manageAll.getRezervacijeManager().getPrihodi(datum.parse("2022-06-01"), datum.parse("2022-07-30"));
		assertTrue(test == 42500);
	}
	
	@Test
	public void testGetRashodi() throws ParseException {
		int test = manageAll.getRezervacijeManager().getRashodi(datum.parse("2022-06-01"), datum.parse("2022-07-30"));
		assertTrue(test == 390000);
	}
	
	@Test
	public void testGetBrojNaCekanju() throws ParseException {
		int test = manageAll.getRezervacijeManager().getBrojNaCekanju(datum.parse("2022-04-01"), datum.parse("2022-10-10"));
		assertEquals(test, 4);
	}
	
	@Test
	public void testGetBrojPotvrdjenih() throws ParseException {
		int test = manageAll.getRezervacijeManager().getBrojPotvrdjenih(datum.parse("2022-04-01"), datum.parse("2022-10-10"));
		assertEquals(test, 1);
	}
	
	@Test
	public void testGetBrojOdbijenih() throws ParseException {
		int test = manageAll.getRezervacijeManager().getBrojOdbijenih(datum.parse("2022-04-01"), datum.parse("2022-10-10"));
		assertEquals(test, 0);
	}
	
	@Test
	public void testGetBrojOtkazanih() throws ParseException {
		int test = manageAll.getRezervacijeManager().getBrojOtkazanih(datum.parse("2022-04-01"), datum.parse("2022-10-10"));
		assertEquals(test, 0);
	}
	
	@Test
	public void testFind() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(0);
		Rezervacije test = manageAll.getRezervacijeManager().find(r.getId());
		assertTrue(r == test);
	}
	
	@Test
	public void testCena() throws ParseException {
		int[] usluge_upis = new int[1];
		usluge_upis[0] = usluge.get(1).getId();
		int test = manageAll.getRezervacijeManager().cena(tipovi.get(0).getId(), usluge_upis, datum.parse("2022-08-10"), datum.parse("2022-08-12"), false);
		assertTrue(test == 10500);
	}
	
	@Test
	public void testCenaUsluge() throws ParseException {
		int test = manageAll.getRezervacijeManager().cenaUsluge(usluge.get(1).getId(), datum.parse("2022-08-10"), datum.parse("2022-08-12"));
		assertTrue(test == 1500);
	}
	
	@Test
	public void testBrojSlobodnihSoba() throws ParseException {
		int test = manageAll.getRezervacijeManager().brojSlobodnihSoba(tipovi.get(0).getId(), datum.parse("2022-08-10"), datum.parse("2022-08-12"), sad2);
		assertTrue(test == 0);
		test = manageAll.getRezervacijeManager().brojSlobodnihSoba(tipovi.get(0).getId(), datum.parse("2022-08-10"), datum.parse("2022-08-12"), null);
		assertTrue(test == 1);
	}
	
	@Test
	public void testGetSlobodneSobe() throws ParseException {
		List<String> sve = new ArrayList<String>();
		sve.add(tipovi.get(0).getTip());
		List<String> test = manageAll.getRezervacijeManager().getSlobodneSobe(datum.parse("2022-08-10"), datum.parse("2022-08-12"), sad1);
		assertEquals(sve, test);
	}
	
	@Test
	public void testGetUsluge() throws ParseException {
		List<String> sve = new ArrayList<String>();
		sve.add(manageAll.getUslugeManager().getAll().get(0).getTip());
		sve.add(manageAll.getUslugeManager().getAll().get(1).getTip());
		List<String> test = manageAll.getRezervacijeManager().getUsluge(datum.parse("2022-08-10"), datum.parse("2022-08-12"));
		assertEquals(sve, test);
	}
	
	@Test
	public void testEdit() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(0);
		int[] usluge = new int[0];
		manageAll.getRezervacijeManager().edit(r.getId(), r.getTipSobe().getId(), usluge, r.getGost().getId(), r.getOdDatum(), r.getDoDatum(), String.valueOf(r.getStatus()), manageAll.getSobeManager().getAll().get(0));
		assertTrue(r.getUsluge().isEmpty());
	}
	
	@Test
	public void testRemove() {
		Rezervacije r = manageAll.getRezervacijeManager().getAll().get(4);
		manageAll.getRezervacijeManager().remove(r.getId());
		Rezervacije test = manageAll.getRezervacijeManager().find(r.getId());
		assertTrue(test == null);
	}
}