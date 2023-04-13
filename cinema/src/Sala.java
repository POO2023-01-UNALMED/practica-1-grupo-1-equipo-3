import java.util.ArrayList;

public class Sala {
	private int noDeSala;
	ArrayList<Asiento>  asientos = new ArrayList<Asiento>();
	public Sala(int noDeSala, int noAsientos) {
		this.noDeSala = noDeSala;
		for(int i=1; i<=noAsientos; i++) {
			this.asientos.add(new Asiento(i));
		}
	}
}
