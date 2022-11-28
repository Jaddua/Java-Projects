package ca.jaddua.numbers;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Gint
{
	private static final Pattern WHITESPACE_BETWEEN_DIGITS_REGEX = Pattern.compile("\\d\\s+\\d");
	private static final Pattern WHITESPACE_REGEX = Pattern.compile("\\s+");

	private static final Pattern REAL_REGEX = Pattern.compile("([+-]?\\d+)");
	private static final Pattern IMAG_REGEX = Pattern.compile("([+-]?)(\\d*)i");
	private static final Pattern REAL_IMAG_REGEX = Pattern.compile("([+-]?\\d+)([+-])(\\d*)i");
	private static final Pattern IMAG_REAL_REGEX = Pattern.compile("([+-]?)(\\d*)i([+-]\\d+)");
	
	private int hashCode;
	
	public long real;
	
	public long imag;
	
	public static final Gint O = new Gint(1,0);
	
	public static final Gint I = new Gint(0,1);

	public static final Gint MO = new Gint(-1,0);

	public static final Gint MI = new Gint(0,-1);

	public static final Gint Z = new Gint(0,0);
	
	public Gint(long real, long imag) {
	
		this.real = real;
		
		this.imag = imag;
		
		this.hashCode = Objects.hash(real, imag);
	}
	
	public Gint(long num) {
		
		this.real = num;
		
		this.imag = 0;
		
		this.hashCode = Objects.hash(real, imag);
	}
	
	@Override

    public boolean equals(Object o) {
        
		if (o == this)
            
			return true;

        if (!(o instanceof Gint))
            
			return false;
		
		Gint i = (Gint) o;
		
		return Long.compare(this.real, i.real) == 0 && Long.compare(this.imag, i.imag) == 0;		
			
	}
	
	 @Override
	    public int hashCode() {
	        return this.hashCode;
	    }
	
	@Override
	
	public String toString() {
		if (imag == 0)
			return real + "";
		
		else if (real == 0 && imag == 1)
			return "i";
		
		else if (real == 0 && imag == -1)
			return "-i";
		
		else if (real == 0)
			return imag + "i";
		
		else if (real != 0 && imag > 1)
			return real + "+" + imag + "i";
		
		else if (real != 0 && imag < -1)
			return real + "-" + (-imag) + "i";
		
		else if (real != 0 && imag > 0)
			return real + "+" + "i";
		
		else if (real != 0 && imag < 0)
			return real + "-" + "i";
		
		else
			throw new AssertionError();
	}
	
	
	public double modulus() {
		
		return Math.sqrt(this.norm());
	}
	
	public long norm() {
		
		return real*real + imag*imag;
	}
	
	public Gint conj() {
		
		return new Gint(real, -imag);
	}
	
	public Gint neg() {
		
		return new Gint(-this.real, -this.imag);
	}
	
	public Gint add(Gint num) {
		
		return new Gint(this.real + num.real , this.imag + num.imag);
	}
	
	public Gint sub(Gint num) {
		
		return this.add(num.neg());
	}
	
	public Gint mult(Gint num) {
		
		return new Gint(this.real * num.real - this.imag * num.imag , this.real * num.imag + this.imag * num.real);
	}
	
	public Gint div(Gint num) {
		
		Gint tempnum = this.mult(num.conj());
		
		long den = num.norm();
		
		return new Gint(Math.round((double) tempnum.real / den) , Math.round((double) tempnum.imag / den));
	}
	
	public Gint mod(Gint num) {
		
		return this.sub(num.mult(this.div(num)));
	}
	
	private long[] egcd(long p, long q) {
	      if (q == 0)
	         return new long[] { p, 1, 0 };

	      long[] vals = egcd(q, p % q);
	      long d = vals[0];
	      long a = vals[2];
	      long b = vals[1] - (p / q) * vals[2];
	      return new long[] { d, a, b };
	}
	
	public Gint gcd(Gint z) {
		Gint a = this;
		Gint b = z;
		Gint t = b;
		while(!b.equals(Gint.Z)) {
			t = b;
			b = a.mod(b);
			a = t;
		}
		return a;
	}
	
	public Gint modInv(Gint b) {
		long[] bez = new long[2];
		long[] eg = egcd(b.real, b.imag);
		bez[0] = eg[1] * this.imag;
		bez[1] = eg[2] * this.imag;
		Gint toInt = this.sub(b.mult(new Gint(bez[1], bez[0])));
		long intC = (toInt.real < 0)?toInt.real + b.norm():toInt.real;
		long intAns = egcd(intC, b.norm())[1];
		return new Gint((intAns < 0)?intAns + b.norm():intAns);
		
	}
	
	public double arg() {
		
		/*if (this.real == 0) {
			
			if (this.imag < 0)
				
				return -Math.PI / 2;
			
			else if (this.imag > 0)
				
				return Math.PI / 2;
			
			else
				
				return Double.NaN;
		}
		
		else {
			
			return Math.atan((double) this.imag / this.real);
		
		}*/
		return Math.atan2(this.imag, this.real);
	}
	
	public Comp pow(Gint num) {
		
		return new Comp(Math.pow(this.modulus(), num.real) * Math.exp(-num.imag * this.arg()) * Math.cos(num.real * this.arg() + num.imag * Math.log(this.modulus())), Math.pow(this.modulus(), num.real) * Math.exp(-num.imag * this.arg()) * Math.sin(num.real * this.arg() + num.imag * Math.log(this.modulus())));
	}
	
	public Comp pow(Comp num) {
		return new Comp(Math.pow(this.modulus(), num.real) * Math.exp(-num.imag * this.arg()) * Math.cos(num.real * this.arg() + num.imag * Math.log(this.modulus())), Math.pow(this.modulus(), num.real) * Math.exp(-num.imag * this.arg()) * Math.sin(num.real * this.arg() + num.imag * Math.log(this.modulus())));
	}
	
	public Comp pow(double num) {
		return new Comp(Math.pow(this.modulus(), num) * Math.cos(num * this.arg()), Math.pow(this.modulus(), num) * Math.sin(num * this.arg()));
	}

	public Gint pow(long num) {
		if (num < 0)
			throw new ArithmeticException();
		Gint ans = Gint.O;
		for(long i = 0; i < num; i++) {
			ans = ans.mult(this);
		}
		return ans;
	}
	
	/**
	 * Complex natural logarithm (principal value) of this.
	 * @return The complex natural logarithm of this.
	 */
	public Comp ln() {
		return new Comp(Math.log(this.modulus()), this.arg());
	}
	
	/**
	 * Complex logarithm (principal value) of this in base b.
	 * @param The base of the logarithm.
	 * @return The complex logarithm of this in base b.
	 */
	public Comp log(Gint b) {
		return this.ln().div(b.ln());
	}
	
	
	public static Gint valueOf(Comp num) {
		return new Gint((long) num.real, (long) num.imag);
	}
	
	public static Gint valueOf(String s) {
		long real = 0;
		long imag = 0;
		
		// Spaces are not allowed between digits. Otherwise remove all whitespace as preprocessing.
				if (WHITESPACE_BETWEEN_DIGITS_REGEX.matcher(s).matches())
					throw new IllegalArgumentException("Invalid number");
				s = WHITESPACE_REGEX.matcher(s).replaceAll("");
				
				// Match one of the syntax cases
				Matcher m;
				if ((m = REAL_REGEX.matcher(s)).matches()) {  // e.g. 1, +0, -2
					real = Integer.parseInt(m.group(1));
					imag = 0;
				} else if ((m = IMAG_REGEX.matcher(s)).matches()) {  // e.g. i, 4i, -3i
					real = 0;
					imag = Integer.parseInt(m.group(1) + (m.group(2).equals("") ? "1" : m.group(2)));
				} else if ((m = REAL_IMAG_REGEX.matcher(s)).matches()) {  // e.g. 1+2i, -3-4i, +5+i
					real = Integer.parseInt(m.group(1));
					imag = Integer.parseInt(m.group(2) + (m.group(3).equals("") ? "1" : m.group(3)));
				} else if ((m = IMAG_REAL_REGEX.matcher(s)).matches()) {  // e.g. 2i+1, -4i-3, +i+5
					real = Integer.parseInt(m.group(3));
					imag = Integer.parseInt(m.group(1) + (m.group(2).equals("") ? "1" : m.group(2)));
				} else
					throw new IllegalArgumentException("Invalid number");
				
				if (real == Long.MIN_VALUE || imag == Long.MIN_VALUE)
					throw new IllegalArgumentException("Value out of range");
				return new Gint(real, imag);
	}
	
	
	public boolean isDivisibleBy(Gint that) {
		long divisorNorm = that.norm();
		return (this.real * that.real + this.imag * that.imag) % divisorNorm == 0 &&
		       (-this.real * that.imag + this.imag * that.real) % divisorNorm == 0;
	}
	
	public List<Gint> factorize() {
		List<Gint> result = new ArrayList<Gint>();
		if (this.norm() <= 1) {  // 0, 1, -1, i, -i
			result.add(this);
			return result;
		}
		
		Gint temp = this;
		Gint check = Gint.O;
		while (temp.norm() > 1) {
			Gint factor = temp.findPrimeFactor();
			result.add(factor);
			temp = temp.div(factor);
			check = check.mult(factor);
		}
		check = check.mult(temp);
		if (temp.norm() != 1 || check.real != this.real || check.imag != this.imag)
			throw new AssertionError();
		if (temp.real != 1)  // -1, i, -i
			result.add(temp);
		
		Collections.sort(result, new Comparator<Gint>() {
			public int compare(Gint x, Gint y) {
				if      (x.norm() < y.norm()) return -1;
				else if (x.norm() > y.norm()) return +1;
				else if (x.real > y.real) return -1;
				else if (x.real < y.real) return +1;
				else return 0;
			}
		});
		return result;
	}
	
	
	private Gint findPrimeFactor() {
		long norm = this.norm();
		if (norm % 2 == 0)
			return new Gint(1, 1);
		
		for (long i = 3, end = (long) Math.sqrt(norm); i <= end; i += 2) {  // Find factors of norm
			if (norm % i == 0) {
				if (i % 4 == 3)
					return new Gint(i, 0);
				else {
					for (long re = (long) Math.sqrt(i); re > 0; re--) {
						long im = (long) Math.sqrt(i - re * re);
						if (re * re + im * im == i) {
							if (re > Long.MAX_VALUE || im > Long.MAX_VALUE)
								throw new ArithmeticException("Overflow");
							else if (isDivisibleBy(new Gint(re, im)))
								return new Gint(re, im);
						}
					}
				}
			}
		}
		
		// This number itself is prime. Rotate so that the argument is in [0, pi/2)
		Gint temp = this;
		while (temp.real < 0 || temp.imag < 0)
			temp = temp.mult(new Gint(0, 1));
		return temp;
	}
	
}