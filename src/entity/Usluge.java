package entity;


public class Usluge {
	private int id;
	private String tip;
	
	public Usluge(int id, String tip) {
		this.id = id;
		this.tip = tip;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
	
	public String toFileString() {
		return id+";"+tip;
	}
	
}