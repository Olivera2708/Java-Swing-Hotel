package gui.models;

import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

import entity.Zaposleni;


public class SobaricaRadModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] header = {"Ime", "Prezime", "Broj spremljenih soba"};
	private HashMap<Zaposleni, Integer> data;
	
	public SobaricaRadModel(HashMap<Zaposleni, Integer> data) {
		this.data = data;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return this.getValueAt(0, column).getClass();
	}

	@Override
	public int getColumnCount() {
		return this.header.length;
	}

	@Override
	public String getColumnName(int column) {
		return this.header[column];
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Zaposleni s = (Zaposleni) data.keySet().toArray()[rowIndex];
		int br = (int) data.values().toArray()[rowIndex];
		switch (columnIndex) {
		case 0:
			return s.getIme();
		case 1:
			return s.getPrezime();
		case 2:
			return br;
		default:
			return null;
		}
	}
}