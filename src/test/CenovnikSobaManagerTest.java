package test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.CenaSobe;
import entity.TipSobe;
import manage.ManageAll;

public class CenovnikSobaManagerTest {
	public static ManageAll manageAll = ManageAll.getInstance();
	static List<TipSobe> tipovi;
	static SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Cenovnik soba test start");
		
		manageAll.getTipSobeManager().add("jednokrevetna");
		manageAll.getTipSobeManager().add("dvokrevetna");
		tipovi = manageAll.getTipSobeManager().getAll();
		
		manageAll.getCenovnikSobaManager().add(tipovi.get(0).getId(), 3000, datum.parse("2022-8-10"), datum.parse("2022-8-20"));
		manageAll.getCenovnikSobaManager().add(tipovi.get(1).getId(), 2300, datum.parse("2022-9-10"), datum.parse("2022-10-10"));
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Cenovnik soba test kraj");
	}
	
	@Test
	public void testGetCena() throws ParseException {
		int test = manageAll.getCenovnikSobaManager().get_cena(tipovi.get(0).getId(), datum.parse("2022-08-12"));
		assertTrue(test == 3000);
	}
	
	@Test
	public void testFind() {
		CenaSobe cs = manageAll.getCenovnikSobaManager().getAll().get(0);
		CenaSobe test = manageAll.getCenovnikSobaManager().find(cs.getId());
		assertTrue(cs == test);
	}
	
	@Test
	public void testRemove() {
		CenaSobe cs = manageAll.getCenovnikSobaManager().getAll().get(0);
		manageAll.getCenovnikSobaManager().remove(0);
		CenaSobe test = manageAll.getCenovnikSobaManager().find(cs.getId());
		assertTrue(test == null);
	}
	
	@Test
	public void testEdit() {
		CenaSobe cs = manageAll.getCenovnikSobaManager().getAll().get(1);
		manageAll.getCenovnikSobaManager().edit(cs.getId(), cs.getTipSobe().getId(), 1500, cs.getOdDatum(), cs.getDoDatum());
		assertTrue(cs.getCena() == 1500);
	}
}