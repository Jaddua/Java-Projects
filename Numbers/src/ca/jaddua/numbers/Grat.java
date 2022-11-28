package ca.jaddua.numbers;

public class Grat {
	
	public Gint num;
	public Gint den;
	private int hashCode;
	
	public Grat(Gint num, Gint den) {
		this.num = num;
		this.den = den;
	}
	
	public Grat(long num, long den) {
		this.num = new Gint(num);
		this.den = new Gint(den);
	}
	
	public Grat(long num) {
		this(num, 1);
	}
	
	public Grat(Rat num) {
		this(num.num, num.den);
	}
	
	public Grat(Gint num) {
		this(num, Gint.O);
	}
	
	public Grat(Rat a, Rat b) {
		Grat temp = new Grat(a).add(new Grat(b).mult(new Grat(Gint.I)));
		this.num = temp.num;
		this.den = temp.den;	
	}
	
	public Grat(double a, double b) {
		this(new Rat(a), new Rat(b));
	}
	
	public Grat(Rat a, double b) {
		Rat B = new Rat(b);
		Grat temp = new Grat(a).add(new Grat(B).mult(new Grat(Gint.I)));
		this.num = temp.num;
		this.den = temp.den;	
	}
	
	public Grat(double a, Rat b) {
		Rat A = new Rat(a);
		Grat temp = new Grat(A).add(new Grat(b).mult(new Grat(Gint.I)));
		this.num = temp.num;
		this.den = temp.den;	
	}
	
	public Grat(Comp num) {
		this(num.real, num.imag);
	}
	
	
	@Override

    public boolean equals(Object o) {
        
		if (o == this)
            
			return true;

        if (!(o instanceof Rat))
            
			return false;
		
		Grat i = (Grat) o;
		
		return this.reduce().num.equals(i.reduce().num) && this.reduce().den.equals(i.reduce().den);		
			
	}
	
	 @Override
	    public int hashCode() {
	        return this.hashCode;
	    }
	
	@Override
	
	public String toString() {
		if (this.den.equals(Gint.O))
			return String.valueOf(this.num);
		else if (this.den.equals(Gint.MO))
			return String.valueOf(this.num.neg());
		else if (this.den.equals(Gint.I))
			return String.valueOf(this.num.mult(Gint.I));
		else if (this.den.equals(Gint.MI))
			return String.valueOf(this.num.mult(Gint.MI));
		else		
			return this.num + " / " + this.den;
	}
	
	public Grat reduce() {
		Gint gcd = this.num.gcd(this.den);
		return new Grat(this.num.div(gcd), this.den.div(gcd));
	}
	
	public Grat add(Grat that) {
		return new Grat(this.num.mult(that.den).add(that.num.mult(this.den)), this.den.mult(that.den)).reduce();
	}
	
	public Grat sub(Grat that) {
		return new Grat(this.num.mult(that.den).sub(that.num.mult(this.den)), this.den.mult(that.den)).reduce();
	}
	
	public Grat mult(Grat that) {
		return new Grat(this.num.mult(that.num), this.den.mult(that.den)).reduce();
	}
	
	public Grat div(Grat that) {
		return new Grat(this.num.mult(that.den), this.den.mult(that.num)).reduce();
	}
	
	
	
}