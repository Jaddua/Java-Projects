import java.util.Calendar;

public class EasterMonday {

	public Calendar date = Calendar.getInstance();

	public EasterMonday(int year) {
		EasterSunday ref = new EasterSunday(year);
		int month = ref.date.get(Calendar.MONTH);
		int day = ref.date.get(Calendar.DAY_OF_MONTH);
		if (day == 31) {
			month += 1;
			day = 1;
		}
		else
			day += 1;
		
		this.date.set(year, month, day);
	}
}
