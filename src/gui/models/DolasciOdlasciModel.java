package gui.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import entity.Rezervacije;


public class DolasciOdlasciModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] header = { "Gost", "Tip Sobe", "Status"};
	private List<Rezervacije> data;
	
	public DolasciOdlasciModel(List<Rezervacije> data) {
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
			return z.getGost().getKorisnickoIme();
		case 1:
			return z.getTipSobe().getTip();
		case 2:
			if (z.getSoba() == null) {
				return "Nije stigao";
			}
			if (String.valueOf(z.getSoba().getStatus()).equals("ZAUZETO")){
				return "Prijavljen";
			}
			else {
				return "Odjavljen";
			}
		default:
			return null;
		}
	}
}