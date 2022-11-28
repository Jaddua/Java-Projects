package ca.jaddua.numbers;

public class Comp
{
	public double real;
	public double imag;
	private int hashCode; 
	
public static final Comp O = new Comp(1);
	
	public static final Comp I = new Comp(0,1);

	public static final Comp MO = new Comp(-1);

	public static final Comp MI = new Comp(0,-1);

	public static final Comp Z = new Comp(0);
	
	public Comp(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}
	
	public Comp(double num) {
		this.real = num;
		this.imag = 0;
	}
	
	@Override

    public boolean equals(Object o) {
        
		if (o == this)
            
			return true;

        if (!(o instanceof Gint))
            
			return false;
		
		Gint i = (Gint) o;
		
		return Double.compare(this.real, i.real) == 0 && Double.compare(this.imag, i.imag) == 0;		
			
	}
	
	 @Override
	    public int hashCode() {
	        return this.hashCode;
	    }
	 
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
			
			else if (real != 0 && imag == 1)
				return real + "+" + "i";
			
			else if (real != 0 && imag == -1)
				return real + "-" + "i";
			
			else if (real != 0 && imag > 0)
				return real + "+" + imag + "i";
			
			else if (real != 0 && imag < 0)
				return real + "-" + (-imag) + "i";
			
			else
				throw new AssertionError();
		}
	
	public double modulus() {
		return Math.sqrt(this.norm());
	}

	public double norm() {
		return real*real + imag*imag;
	}

	public Comp conj() {
		return new Comp(real, -imag);
	}

	public Comp neg() {
		return new Comp(-this.real, -this.imag);
	}

	public Comp add(Comp num) {
		return new Comp(this.real + num.real , this.imag + num.imag);
	}

	public Comp sub(Comp num) {
		return this.add(num.neg());
	}

	public Comp mult(Comp num) {
		return new Comp(this.real * num.real - this.imag * num.imag , this.real * num.imag + this.imag * num.real);
	}

	public Comp div(Comp num) {
		Comp tempnum = this.mult(num.conj());
		double den = num.norm();
		return new Comp(tempnum.real / den , tempnum.imag / den);
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
			return Math.atan(this.imag / this.real);
		}*/
		return Math.atan2(this.imag, this.real);
	}
	
	public Comp pow(Comp num) {
		return new Comp(Math.pow(this.modulus(), num.real) * Math.exp(-num.imag * this.arg()) * Math.cos(num.real * this.arg() + num.imag * Math.log(this.modulus())), Math.pow(this.modulus(), num.real) * Math.exp(-num.imag * this.arg()) * Math.sin(num.real * this.arg() + num.imag * Math.log(this.modulus())));
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
	public Comp log(Comp b) {
		return this.ln().div(b.ln());
	}
	
	public Comp ceil() {
		return new Comp(Math.ceil(this.real), Math.ceil(this.imag));
	}
	
	public static Comp valueOf(Gint num) {
		return new Comp(num.real, num.imag);
	}
	
		public boolean isGint() {
			if ((long) this.real == this.real && (long) this.imag == this.imag)
				return true;
			else
				return false;
		}
	
	
}
