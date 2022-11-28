import java.util.Calendar;

public class EasterSunday {

	public Calendar date = Calendar.getInstance();
	
	public EasterSunday(int year) {
		int a = year % 19;
		int b = year % 4;
		int c = year % 7;
		int k = year / 100;
		int p = (13 + 8*k) / 25;
		int q = k / 4;
		int M = (15 - p + k - q) % 30;
		int N = (4 + k - q) % 7;
		int d = (19*a + M) % 30;
		int e = (2*b + 4*c + 6*d + N) % 7;
		int day = 22 + d + e;
		if (day > 0 && day < 32) {
			
			this.date.set(year, 2, day);
		}
		else {
			day = d + e - 9;
			if (d == 28 && e == 6 && (11*M + 11)%30 < 19)
				day = 18;
			if (d == 29 && e == 6)
				day = 19;
			this.date.set(year, 3, day);
			
		}
	}
}
