package gui.models;

import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

import entity.Sobe;


public class SobeIzvestajModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] header = {"Broj sobe", "Tip sobe", "Broj noćenja", "Prihodi"};
	private HashMap<Sobe, Integer[]> data;
	
	public SobeIzvestajModel(HashMap<Sobe, Integer[]> data) {
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
		Sobe s = (Sobe) data.keySet().toArray()[rowIndex];
		Integer[] lista = (Integer[]) data.values().toArray()[rowIndex];
		switch (columnIndex) {
		case 0:
			return s.getBrojSobe();
		case 1:
			return s.getTipSobe().getTip();
		case 2:
			return lista[0];
		case 3:
			return lista[1];
		default:
			return null;
		}
	}
}