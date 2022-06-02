package manage;

public class ManageAll {
	private ZaposleniManager zaposleniManager;
	private GostManager gostManager;
	
	public ManageAll() {
		this.zaposleniManager = new ZaposleniManager();
		this.gostManager = new GostManager();
	}
	
	public void loadData() {
		this.zaposleniManager.loadData();
		this.gostManager.loadData();
	}
}