package gui.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import entity.Sobe;

public class SobeModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] header = { "Broj sobe", "Tip sobe", "Status", "Sadržaji"};
	private List<Sobe> data;
	
	public SobeModel(List<Sobe> data) {
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
		Sobe z = data.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return z.getBrojSobe();
		case 1:
			return z.getTipSobe().getTip();
		case 2:
			return z.getStatus();
		case 3:
			String sadrzaji = "";
			if (z.getSadrzaj() == null) {
				return sadrzaji;
			}
			for (String s: z.getSadrzaj()) {
				sadrzaji += s + ", ";
			}
			sadrzaji = sadrzaji.substring(0, sadrzaji.length()-2);
			return sadrzaji;
		default:
			return null;
		}
	}
}