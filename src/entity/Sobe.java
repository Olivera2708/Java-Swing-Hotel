package entity;


public class Sobe {
	private int id;
	private TipSobe tipSobe;
	private String status;
	
	public Sobe(int id, TipSobe tipSobe, String status) {
		this.id = id;
		this.tipSobe = tipSobe;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipSobe getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toFileString() {
		return id+";"+tipSobe.getId()+";"+status;
	}
	
}