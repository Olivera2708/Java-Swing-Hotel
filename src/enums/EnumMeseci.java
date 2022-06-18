package enums;

public enum EnumMeseci{
	JANUAR(1),
	FEBRUAR(2),
	MART(3),
	APRIL(4),
	MAJ(5),
	JUN(6),
	JUL(7),
	AVGUST(8),
	SEPTEMBAR(9),
	OKTOBAR(10),
	NOVEMBAR(11),
	DECEMBAR(12);
	
	private int broj;
	
	EnumMeseci(int broj){
		this.broj = broj;
	}
	
	public static EnumMeseci Int(int id) {
        for (EnumMeseci mesec : values()) {
            if (mesec.broj == id) {
                return mesec;
            }
        }
        return null;
    }
	
	public int getValue() {
		return broj;
	}
}