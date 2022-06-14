package gui.models;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import entity.CenaSobe;
import entity.Gost;


public class CenovnikSobeModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] header = { "Id", "Usluga", "Datum od", "Datum do", "Cena"};
	private List<CenaSobe> data;
	
	public CenovnikSobeModel(List<CenaSobe> data) {
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
		CenaSobe z = data.get(rowIndex);
		SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
		switch (columnIndex) {
		case 0:
			return z.getId();
		case 1:
			return z.getTipSobe().getTip();
		case 2:
			return datum.format(z.getOdDatum());
		case 3:
			return datum.format(z.getDoDatum());
		case 4:
			return z.getCena();
		default:
			return null;
		}
	}
}