package manage;

public class ManageAll {
	private static ManageAll manageAll;
	
	private ZaposleniManager zaposleniManager;
	private GostManager gostManager;
	private TipSobeManager tipSobeManager;
	private SobeManager sobeManager;
	private UslugeManager uslugeManager;
	private RezervacijeManager rezervacijeManager;
	private CenovnikSobaManager cenovnikSobaManager;
	private CenovnikUslugaManager cenovnikUslugaManager;
	
	private ManageAll() {
		this.zaposleniManager = new ZaposleniManager();
		this.gostManager = new GostManager();
		this.tipSobeManager = new TipSobeManager();
		this.sobeManager = new SobeManager(tipSobeManager, zaposleniManager);
		this.uslugeManager = new UslugeManager();
		this.cenovnikSobaManager = new CenovnikSobaManager(tipSobeManager);
		this.cenovnikUslugaManager = new CenovnikUslugaManager(uslugeManager);
		this.rezervacijeManager = new RezervacijeManager(tipSobeManager, uslugeManager, gostManager, cenovnikUslugaManager, cenovnikSobaManager, sobeManager);
	}
	
	//dobijanje instanci
	public static ManageAll getInstance() {
		if (manageAll == null) {
			manageAll =  new ManageAll();
		}
		return manageAll;
	}
	
	public void loadData() {
		this.zaposleniManager.loadData();
		this.gostManager.loadData();
		this.tipSobeManager.loadData();
		this.sobeManager.loadData();
		this.uslugeManager.loadData();
		this.cenovnikSobaManager.loadData();
		this.cenovnikUslugaManager.loadData();
		this.rezervacijeManager.loadData();
	}

	public ZaposleniManager getZaposleniManager() {
		return zaposleniManager;
	}

	public GostManager getGostManager() {
		return gostManager;
	}
	
	public TipSobeManager getTipSobeManager() {
		return tipSobeManager;
	}

	public SobeManager getSobeManager() {
		return sobeManager;
	}
	
	public UslugeManager getUslugeManager() {
		return uslugeManager;
	}

	public RezervacijeManager getRezervacijeManager() {
		return rezervacijeManager;
	}
	
	public CenovnikSobaManager getCenovnikSobaManager() {
		return cenovnikSobaManager;
	}

	public CenovnikUslugaManager getCenovnikUslugaManager() {
		return cenovnikUslugaManager;
	}
}