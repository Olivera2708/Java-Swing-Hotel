package gui.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import entity.Rezervacije;
import entity.Usluge;


public class PotvrdaRezervacijeModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] header = {"Id", "Tip sobe", "Datum od", "Datum do", "Cena", "Dodatne usluge"};
	private List<Rezervacije> data;
	
	public PotvrdaRezervacijeModel(List<Rezervacije> data) {
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
			return z.getTipSobe().getTip();
		case 2:
			return z.getOdDatum();
		case 3:
			return z.getDoDatum();
		case 4:
			return z.getCena();
		case 5:
			String usluge = "";
			if (z.getUsluge().isEmpty()) {
				return usluge;
			}
			for (Usluge u: z.getUsluge()) {
				usluge += u.getTip() + ", ";
			}
			return usluge.substring(0, usluge.length()-2);
		default:
			return null;
		}
	}
}