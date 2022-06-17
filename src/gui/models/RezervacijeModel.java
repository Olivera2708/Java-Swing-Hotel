package gui.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import entity.Rezervacije;


public class RezervacijeModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] header = { "Id", "Gost", "Datum od", "Datum do", "Status", "Cena"};
	private List<Rezervacije> data;
	
	public RezervacijeModel(List<Rezervacije> data) {
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
		Rezervacije z = data.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return z.getId();
		case 1:
			return z.getGost().getKorisnickoIme();
		case 2:
			return z.getOdDatum();
		case 3:
			return z.getDoDatum();
		case 4:
			return z.getStatus();
		case 5:
			if (String.valueOf(z.getStatus()).equals("ODBIJENA")) {
				return 0;
			}
			return z.getCena();
		default:
			return null;
		}
	}
}