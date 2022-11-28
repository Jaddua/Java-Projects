import java.util.Calendar;

public class GoodFriday {

	public Calendar date = Calendar.getInstance();

	public GoodFriday(int year) {
		EasterSunday ref = new EasterSunday(year);
		int month = ref.date.get(Calendar.MONTH);
		int day = ref.date.get(Calendar.DAY_OF_MONTH);
		if (day == 1) {
			month -= 1;
			day = 30;
		}
		else if(day == 2) {
			month -= 1;
			day = 31;
		}
		else
			day -= 2;
		
		this.date.set(year, month, day);
	}
}
