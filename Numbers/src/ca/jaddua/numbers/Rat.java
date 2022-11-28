package ca.jaddua.numbers;

public class Rat {
	
	public long num;
	public long den;
	private int hashCode;
	
	public Rat(long num, long den) {
		this.num = num;
		this.den = den;
	}
	
	public Rat(int num, int den) {
		this.num = num;
		this.den = den;
	}
	
	public Rat(long num) {
		this.num = num;
		this.den = 1;
	}
	
	public Rat(int num) {
		this.num = num;
		this.den = 1;
	}
	
	public Rat(double num) {
		String temp = String.valueOf(num);
		long pow = 0;
		if (temp.contains(".")) {
			pow = temp.substring(temp.indexOf(".") + 1).length();
		}
		long powTen = (long) Math.pow(10, pow);
		this.num = (long) (num * powTen);
		this.den = powTen;
	}
	
	
	@Override

    public boolean equals(Object o) {
        
		if (o == this)
            
			return true;

        if (!(o instanceof Rat))
            
			return false;
		
		Rat i = (Rat) o;
		
		return Long.compare(this.reduce().num, i.reduce().num) == 0 && Long.compare(this.reduce().den, i.reduce().den) == 0;		
			
	}
	
	 @Override
	    public int hashCode() {
	        return this.hashCode;
	    }
	
	@Override
	
	public String toString() {
		if (this.den == 1)
			return String.valueOf(this.num);
		else		
			return this.num + " / " + this.den;
	}
	
	private long gcd(long a, long b) {
		if (b==0)
			return Math.abs(a);
		return gcd(b,a%b);
	}
	
	public Rat reduce() {
		long gcd = gcd(this.num, this.den);
		return new Rat(this.num / gcd, this.den / gcd);
	}
	
	public Rat add(Rat that) {
		return new Rat(this.num * that.den + that.num * this.den, this.den * that.den).reduce();
	}
	
	public Rat sub(Rat that) {
		return new Rat(this.num * that.den - that.num * this.den, this.den * that.den).reduce();
	}
	
	public Rat mult(Rat that) {
		return new Rat(this.num * that.num, this.den * that.den).reduce();
	}
	
	public Rat div(Rat that) {
		return new Rat(this.num * that.den, this.den * that.num).reduce();
	}
	
	public static Rat valueOf(String num) {
		num.replaceAll(" ", "");
		if (num.contains("/"))
			return new Rat(Double.valueOf(num.substring(0, num.indexOf("/")))).div(new Rat(Double.valueOf(num.substring(num.indexOf("/") + 1))));
		return new Rat(Double.valueOf(num));
	}
	
}