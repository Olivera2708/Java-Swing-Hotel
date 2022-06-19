package test;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Usluge;
import manage.ManageAll;

public class UslugeManagerTest {
	public static ManageAll manageAll = ManageAll.getInstance();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Usluge test start");
		
		manageAll.getUslugeManager().add("dorucak");
		manageAll.getUslugeManager().add("rucak");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Usluge test kraj");
	}
	
	@Test
	public void testGetNames() throws ParseException {
		String[] test = manageAll.getUslugeManager().getNames();
		String[] tacno = {"dorucak", "rucak"};
		assertArrayEquals(test, tacno);
	}
	
	@Test
	public void testFind() {
		Usluge ts = manageAll.getUslugeManager().getAll().get(0);
		Usluge test = manageAll.getUslugeManager().find(ts.getId());
		assertTrue(ts == test);
	}
	
	@Test
	public void testGetId() {
		String opis = manageAll.getUslugeManager().getAll().get(0).getTip();
		assertEquals(opis, "dorucak");
	}
	
	@Test
	public void testRemove() {
		Usluge cu = manageAll.getUslugeManager().getAll().get(0);
		manageAll.getUslugeManager().remove(cu.getId());
		Usluge test = manageAll.getUslugeManager().find(cu.getId());
		assertTrue(test == null);
	}
	
	@Test
	public void testEdit() {
		Usluge cu = manageAll.getUslugeManager().getAll().get(1);
		manageAll.getUslugeManager().edit(cu.getId(), "vecera");
		assertEquals(cu.getTip(), "vecera");
	}
}