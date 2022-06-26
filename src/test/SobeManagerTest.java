package test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Sobe;
import entity.Zaposleni;
import manage.ManageAll;

public class SobeManagerTest {
	public static ManageAll manageAll = ManageAll.getInstance();
	static SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
	
	static String[] sad1 = {"TV", "Klima"};
	static String[] sad2 = {"Balkon, Klima"};
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Sobe test start");
		
		manageAll.getTipSobeManager().add("jednokrevetna");
		manageAll.getTipSobeManager().add("dvokrevetna");
		
		manageAll.getZaposleniManager().add("Sobarka", "Sobric", "Žensko", datum.parse("1989-10-25"), "06437255798", "Dusana Danilovica 27, Novi Sad", "sobarka", "sobarka123", 4, 15, "Sobarica");
		
		manageAll.getSobeManager().add(202, manageAll.getTipSobeManager().getAll().get(0).getId(), "SLOBODNA", null);
		manageAll.getSobeManager().add(203, manageAll.getTipSobeManager().getAll().get(1).getId(), "SLOBODNA", sad1);
		manageAll.getSobeManager().add(204, manageAll.getTipSobeManager().getAll().get(0).getId(), "SLOBODNA", sad2);
		manageAll.getSobeManager().add(206, manageAll.getTipSobeManager().getAll().get(1).getId(), "ZAUZETO", sad2);
		manageAll.getSobeManager().add(207, manageAll.getTipSobeManager().getAll().get(0).getId(), "ZAUZETO", sad1);
		manageAll.getSobeManager().add(208, manageAll.getTipSobeManager().getAll().get(1).getId(), "ZAUZETO", null);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Sobe test kraj");
	}
	
	@Test
	public void testAdd() throws ParseException {
		manageAll.getSobeManager().add(209, manageAll.getTipSobeManager().getAll().get(0).getId(), "ZAUZETO", sad2);
		int sve = manageAll.getSobeManager().getAll().size();
		assertTrue(sve == 7);
	}
	
	@Test
	public void getAll() {
		int test = manageAll.getSobeManager().getAll().size();
		assertTrue(test == 7);
	}
	
	@Test
	public void testDodeliSobuSpremacici() {
		Sobe test = manageAll.getSobeManager().getAll().get(0);
		manageAll.getSobeManager().dodeliSobuSpremacici(test.getBrojSobe());
		assertTrue(test.getSpremacica() != null);
	}
	
	@Test
	public void testUcitajIstoriju() {
		Date danas = new Date();
		Sobe s = manageAll.getSobeManager().getAll().get(2);
		String istorija = manageAll.getZaposleniManager().getAll().get(0).getId() + " " + datum.format(danas);
		manageAll.getSobeManager().ucitajIstoriju(istorija, s);
		assertTrue(s.getDatumiSpremanja() != null);
	}
	
	@Test
	public void testDodajSpremanje() {
		Sobe s = manageAll.getSobeManager().getAll().get(1);
		manageAll.getSobeManager().dodajSpremanje(manageAll.getZaposleniManager().getAll().get(0).getId(), s);
		assertTrue(s.getDatumiSpremanja() != null);
	}
	
	@Test
	public void testGetBrojSobaPoSobarici() {
		Zaposleni z = manageAll.getZaposleniManager().getAll().get(0);
		Date danas = new Date();
		HashMap<Zaposleni, Integer> test = manageAll.getSobeManager().getBrojSobaPoSobarici(danas, danas);
		assertTrue(test.get(z) == 0);
	}
	
	@Test
	public void testGetSlobodneSobe() {
		List<Integer> tacno = new ArrayList<Integer>();
		tacno.add(manageAll.getSobeManager().getAll().get(2).getBrojSobe());
		List<Integer> test = manageAll.getSobeManager().getSlobodneSobe(manageAll.getTipSobeManager().getAll().get(0).getId(), sad2);
		assertEquals(tacno ,test);
	}
	
	@Test
	public void testGetSlobodneSobeSlicne() {
		List<Integer> tacno = new ArrayList<Integer>();
		tacno.add(manageAll.getSobeManager().getAll().get(0).getBrojSobe());
		tacno.add(manageAll.getSobeManager().getAll().get(2).getBrojSobe());
		List<Integer> test = manageAll.getSobeManager().getSlobodneSobeSlicne(manageAll.getTipSobeManager().getAll().get(0).getId());
		assertEquals(tacno ,test);
	}
	
	@Test
	public void testBrojZauzetihSoba() {
		int test = manageAll.getSobeManager().brojZauzetihSoba();
		assertTrue(test == 3);
	}
	
	@Test
	public void testGetPosao() {
		Zaposleni z = manageAll.getZaposleniManager().getAll().get(0);
		List<Sobe> tacno = new ArrayList<Sobe>();
		tacno.add(manageAll.getSobeManager().getAll().get(0));
		List<Sobe> test = manageAll.getSobeManager().getPosao(z);
		assertEquals(test, tacno);
	}
	
	@Test
	public void testFind() {
		Sobe soba = manageAll.getSobeManager().getAll().get(0);
		Sobe test = manageAll.getSobeManager().find(202);
		assertTrue(soba == test);
	}
	
	@Test
	public void testRemove() {
		Sobe soba = manageAll.getSobeManager().getAll().get(3);
		manageAll.getSobeManager().remove(soba.getBrojSobe());
		Sobe test = manageAll.getSobeManager().find(soba.getBrojSobe());
		assertTrue(test == null);	
	}
	
	@Test
	public void testEdit() {
		Sobe soba = manageAll.getSobeManager().getAll().get(4);
		manageAll.getSobeManager().edit(soba.getBrojSobe(), soba.getBrojSobe(), soba.getTipSobe().getId(), "SLOBODNA", 0, soba.getSadrzaj());
		assertEquals(String.valueOf(soba.getStatus()), "SLOBODNA");
	}
}