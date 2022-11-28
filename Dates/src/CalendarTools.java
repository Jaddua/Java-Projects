import java.util.Calendar;
import java.util.Locale;

public class CalendarTools {
	
	public static String stringValue(Calendar date) {
		return String.format("%s , %d / %s / %d", date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()), date.get(Calendar.YEAR), date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()), date.get(Calendar.DAY_OF_MONTH));
		}
}
