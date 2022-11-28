package ca.jaddua.numbers;

public class Nat {
	public long value;
	public static final Nat ONE = new Nat(1);
	public static final Nat ZERO = new Nat(0);
	
	public Nat(long n) {
		if (n < 0) {
			throw new ArithmeticException();
		}
		else {
			this.value = n;
		}
	}
	public Nat add(Nat addend) {
		return new Nat(this.value + addend.value);
	}
	public Nat subtract(Nat subtrahend) {
		return new Nat(this.value - subtrahend.value);
	}
	public Nat multiply(Nat multiplier) {
		return new Nat(this.value * multiplier.value);
	}
	public String toString() {
		return String.valueOf(this.value);
	}
	
	public Nat[] NtoN2() {
		 Nat[] mapping = new Nat[2];
		long w = (long) Math.floor((Math.sqrt(this.value * 8 + 1) - 1) / 2);
		long t = (w * w + w) / 2;
		long y0 = this.value - t;
		Nat y = new Nat(y0);
		Nat x = new Nat(w - y0);
		mapping[0] = x;
		mapping[1] = y;
		return mapping;
	}
	public long NtoZ() {
		if ((this.value & 1) == 0) {
			return this.value / 2;
		}
		else {
			return (-this.value - 1) / 2;
		}
	}
	public Gint NtoZi() {
		return new Gint(this.NtoN2()[0].NtoZ(), this.NtoN2()[1].NtoZ());
	}
}
