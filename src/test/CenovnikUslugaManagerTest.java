package test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.CenaUsluge;
import entity.Usluge;
import manage.ManageAll;

public class CenovnikUslugaManagerTest {
	public static ManageAll manageAll = ManageAll.getInstance();
	static List<Usluge> usluge;
	static SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Cenovnik usluga test start");
		
		manageAll.getUslugeManager().add("doruca");
		manageAll.getUslugeManager().add("rucak");
		usluge = manageAll.getUslugeManager().getAll();
		
		manageAll.getCenovnikUslugaManager().add(usluge.get(0).getId(), 700, datum.parse("2022-8-10"), datum.parse("2022-8-20"));
		manageAll.getCenovnikUslugaManager().add(usluge.get(1).getId(), 500, datum.parse("2022-9-10"), datum.parse("2022-10-10"));
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Cenovnik usluga test kraj");
	}
	
	@Test
	public void testAdd() throws ParseException {
		manageAll.getCenovnikUslugaManager().add(usluge.get(1).getId(), 400, datum.parse("2022-12-10"), datum.parse("2022-12-30"));
		int sve = manageAll.getCenovnikUslugaManager().getAll().size();
		assertTrue(sve == 3);
	}
	
	@Test
	public void getAll() {
		int test = manageAll.getCenovnikUslugaManager().getAll().size();
		assertTrue(test == 3);
	}
	
	@Test
	public void testGetCena() throws ParseException {
		int test = manageAll.getCenovnikUslugaManager().get_cena(usluge.get(0).getId(), datum.parse("2022-08-12"));
		assertTrue(test == 700);
	}
	
	@Test
	public void testFind() {
		CenaUsluge cu = manageAll.getCenovnikUslugaManager().getAll().get(0);
		CenaUsluge test = manageAll.getCenovnikUslugaManager().find(cu.getId());
		assertTrue(cu == test);
	}
	
	@Test
	public void testRemove() {
		CenaUsluge cu = manageAll.getCenovnikUslugaManager().getAll().get(0);
		manageAll.getCenovnikUslugaManager().remove(0);
		CenaUsluge test = manageAll.getCenovnikUslugaManager().find(cu.getId());
		assertTrue(test == null);
	}
	
	@Test
	public void testEdit() {
		CenaUsluge cu = manageAll.getCenovnikUslugaManager().getAll().get(1);
		manageAll.getCenovnikUslugaManager().edit(cu.getId(), cu.getUsluge().getId(), 1000, cu.getOdDatum(), cu.getDoDatum());
		assertTrue(cu.getCena() == 1000);
	}
}