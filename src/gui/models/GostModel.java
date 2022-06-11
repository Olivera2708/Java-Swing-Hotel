package gui.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import entity.Gost;


public class GostModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] header = { "Id", "Ime", "Prezime", "Korisničko ime"};
	private List<Gost> data;
	
	public GostModel(List<Gost> data) {
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
		Gost z = data.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return z.getId();
		case 1:
			return z.getIme();
		case 2:
			return z.getPrezime();
		case 3:
			return z.getKorisnickoIme();
		default:
			return null;
		}
	}
}