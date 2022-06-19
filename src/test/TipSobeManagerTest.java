package test;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.TipSobe;
import manage.ManageAll;

public class TipSobeManagerTest {
	public static ManageAll manageAll = ManageAll.getInstance();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Tip sobe test start");
		
		manageAll.getTipSobeManager().add("jednokrevetna");
		manageAll.getTipSobeManager().add("dvokrevetna");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Tip sobe test kraj");
	}
	
	@Test
	public void testGetNames() throws ParseException {
		String[] test = manageAll.getTipSobeManager().getNames();
		String[] tacno = {"jednokrevetna", "dvokrevetna"};
		assertArrayEquals(test, tacno);
	}
	
	@Test
	public void testFind() {
		TipSobe ts = manageAll.getTipSobeManager().getAll().get(0);
		TipSobe test = manageAll.getTipSobeManager().find(ts.getId());
		assertTrue(ts == test);
	}
	
	@Test
	public void testGetId() {
		String opis = manageAll.getTipSobeManager().getAll().get(0).getTip();
		assertEquals(opis, "jednokrevetna");
	}
	
	@Test
	public void testRemove() {
		TipSobe cu = manageAll.getTipSobeManager().getAll().get(0);
		manageAll.getTipSobeManager().remove(cu.getId());
		TipSobe test = manageAll.getTipSobeManager().find(cu.getId());
		assertTrue(test == null);
	}
	
	@Test
	public void testEdit() {
		TipSobe cu = manageAll.getTipSobeManager().getAll().get(1);
		manageAll.getTipSobeManager().edit(cu.getId(), "trokrevetna");
		assertEquals(cu.getTip(), "trokrevetna");
	}
}