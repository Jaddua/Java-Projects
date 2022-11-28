import java.util.Calendar;
import java.util.Date;

public class Main {

	public static void main(String[] args) {
		int year = 1991;
		System.out.println(CalendarTools.stringValue(new EasterSunday(year).date));
		System.out.println(CalendarTools.stringValue(new EasterMonday(year).date));
		System.out.println(CalendarTools.stringValue(new GoodFriday(year).date));
		System.out.println(CalendarTools.stringValue(new VictoriaDay(year).date));
	}

}
