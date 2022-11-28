
public class Doomsday {

	public int doomsday;
	
	public Doomsday(int year) {
		int n = (year / 100) % 4;
		int d = 5 * (year % 100) / 4;
		this.doomsday = ((n - 1) * (-14 * n * n + 49 * n - 12) / 6 + d) % 7;
	}
	
}
