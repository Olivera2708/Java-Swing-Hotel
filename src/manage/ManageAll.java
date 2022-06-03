package manage;

public class ManageAll {
	private static ManageAll manageAll;
	
	private ZaposleniManager zaposleniManager;
	private GostManager gostManager;
	
	public ManageAll() {
		this.zaposleniManager = new ZaposleniManager();
		this.gostManager = new GostManager();
	}
	
	//dobijanje instanci
	public static ManageAll getInstance() {
		if (manageAll == null) {
			manageAll =  new ManageAll();
			manageAll.loadData();
		}
		return manageAll;
	}
	
	public void loadData() {
		this.zaposleniManager.loadData();
		this.gostManager.loadData();
	}

	public ZaposleniManager getZaposleniManager() {
		return zaposleniManager;
	}

	public GostManager getGostManager() {
		return gostManager;
	}
}