import java.util.Calendar;

public class VictoriaDay {
	
	public Calendar date = Calendar.getInstance();
	
	public VictoriaDay(int year) {
		this.date.set(year, 4, 24 - new Doomsday(year).doomsday);		
	}
}
