package ca.jaddua.bases;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ca.jaddua.numbers.Gint;
import ca.jaddua.numbers.Rat;


public class Base {

	public List<Long> digitSet;
	public long base;
	public Map<Long, Long> map;
	public double remConst;
	public Map<Long, Set<Long>> remSets;
	public long dMax;
	public long dMin;

	public Base(long base, List<Long> digitSet) {
		this.base = base;
		this.digitSet = digitSet;
		this.dMax = Collections.max(digitSet);
		this.dMin = Collections.min(digitSet);
		this.map = new HashMap<Long, Long>();
		for(long i : digitSet) {
			this.map.put(mod(i, base), i);
		}
		Supplier<Stream<Long>> streamSupplier = () -> digitSet.stream().map(l -> Math.abs(l));
		long max = streamSupplier.get().max(Long::compare).get();
		this.remConst = (double) max * streamSupplier.get().filter(l -> l == max).count() / (Math.abs(base) - 1);
		this.remSets = new HashMap<Long, Set<Long>>();
	}
	
	public Base(long base, Long... digitSet) {
		this.base = base;
		this.digitSet = Arrays.asList(digitSet);
		this.dMax = Collections.max(this.digitSet);
		this.dMin = Collections.min(this.digitSet);
		this.map = new HashMap<Long, Long>();
		for(long i : digitSet) {
			this.map.put(mod(i, base), i);
		}
		Supplier<Stream<Long>> streamSupplier = () -> this.digitSet.stream().map(l -> Math.abs(l));
		long max = streamSupplier.get().max(Long::compare).get();
		this.remConst = (double) max * streamSupplier.get().filter(l -> l == max).count() / (Math.abs(base) - 1);
		this.remSets = new HashMap<Long, Set<Long>>();
	}

	public long mapper(long i) {
		return this.map.get(i);
	}

	private long mod(long x, long y)
	{
	    long result = x % y;
	    if (result < 0)
	    {
	        result += Math.abs(y);
	    }
	    return result;
	} 

	public long[] chop(long i) {
		long ans[] = new long[2];
		ans[0] = mapper(mod(i, this.base));
		ans[1] = (i - ans[0]) / this.base;
		return ans;
	}

	public List<Long> convert(long i) {
		
		long n[] = new long[2];
		n = chop(i);
		List<Long> ans = new ArrayList<Long>();
		if (chopBrentRep(i) != 0) {
			ans.add(0l);
			ans.add(0l);
			return ans;
		}
		ans.add(n[0]);
		while(n[1] != 0) {
			n = chop(n[1]);
			ans.add(n[0]);
		}
		Collections.reverse(ans);
		ans.add(0l);
		return ans;
	}
	
	public List<Long> convert(Rat i) {
		List<Long> ans = new ArrayList<Long>();
		long v = i.num;
		long w = i.den;
		long pow = 0;
		long gcd = gcd(v, w);
		v = v / gcd * (long) Math.signum(w);
		w = Math.abs(w) / gcd;
		gcd = gcd(w, this.base);
		if (gcd != 1) {
			pow = (long) Math.ceil(log(w, Math.abs(this.base)));
			v = v * (long) Math.pow(this.base, pow) / gcd;
			w = w / gcd;
			gcd = gcd(v,w);
			v = v / gcd * (long) Math.signum(w);
			w = Math.abs(w) / gcd;
		}
		if (!this.remSets.containsKey(w))
			this.remSets.put(w, calcRemSet(w));
		Set<Long> remSet = this.remSets.get(w);
		long rem = 0;
		for (long r : remSet) {
			if ((v - r) % w == 0) {
				long A = (v - r) / w;
				ans.add(A);
				rem = r;
				break;
			}
		}
		ans.addAll(divBrent(rem, w, remSet));
		ans.add(pow);
		ans.add(1l);
		return ans;	
	}
	
	public long remFunc(long z, long w, long winv) {
		long a = mapper(mod(-z * winv, this.base));
		return (z + a * w) / this.base;
	}
	//First two elements are lambda and then mu, then the sequence follows. Fist length of repeating sequence lambda then position
	//starting at 0 of where the sequence starts to repeat mu
	public List<Long> remBrent(long z, long w, long winv) {
		List<Long> ans = new ArrayList<Long>();
		long power = 1;
		long lam = 1;
		long tortoise = z;
		long hare = remFunc(z, w, winv);
		while (tortoise != hare) {
			if (power == lam) {
				tortoise = hare;
				power = power << 1;
				lam = 0;
			}
			hare = remFunc(hare, w, winv);
			lam++;
		}
		tortoise = z;
		hare = z;
		for (long i = 0; i < lam; i++) {
			ans.add(hare);
			hare = remFunc(hare, w, winv);
		}
		long mu = 0;
		while (tortoise != hare) {
			tortoise = remFunc(tortoise, w, winv);
			ans.add(hare);
			hare = remFunc(hare, w, winv);
			mu++;
		}
		ans.add(0, mu);
		ans.add(0, lam);
		return ans;
	}
	
	public long[] egcd(long p, long q) {
	      if (q == 0)
	         return new long[] { p, 1, 0 };

	      long[] vals = egcd(q, p % q);
	      long d = vals[0];
	      long a = vals[2];
	      long b = vals[1] - (p / q) * vals[2];
	      return new long[] { d, a, b };
	   }
	
	public long modInv(long a, long b) {
		/*long oldR = a;
		long r = b;
		long tempR = b;
		long oldS = 1;
		long s = 0;
		long tempS = 0;	
		long q = 0;
		while (r != 0) {
			q = (oldR - mod(oldR, r)) / r;
			tempR = r;
			r = oldR - q * tempR;
			oldR = tempR;
			tempS = s;
			s = oldS - q * s;
			oldS = tempS;
		}
		if (oldS < 0)
			oldS = oldS + b;
		return oldS;*/
		long ans = egcd(a,b)[1];
		return (ans < 0)?ans+b:ans;
	}
	
	public Set<Long> calcRemSet(long w) {
		long remC = (long) Math.ceil(this.remConst * w);
		long winv = modInv(w, this.base);
		Set<Long> banned = new HashSet<Long>();
		Set<Long> remSet = new HashSet<Long>();
		List<Long> gVals = new ArrayList<Long>();
		long N = remC << 1 + 1;
		for (long i = -remC + 1; i < remC + 1; i++) {
			if (banned.size() == N)
				break;
			if (!banned.contains(i)) {
				gVals = remBrent(i, w, winv);
				banned.addAll(gVals.subList(2, gVals.size() - 1));
				remSet.addAll(gVals.subList(gVals.get(1).intValue() + 2, gVals.get(1).intValue() + gVals.get(0).intValue() + 2));
			}
		}
		return remSet;
	}

	public long gcd(long a, long b) {
		if (b==0)
			return Math.abs(a);
		return gcd(b,a%b);
	}
	private double log(double a, double b) {
		return Math.log(a) / Math.log(b);
	}
	
	public List<Long> longDiv(long v, long w) {
		List<Long> ans = new ArrayList<Long>();
		long pow = 0;
		long gcd = gcd(v, w);
		v = v / gcd * (long) Math.signum(w);
		w = Math.abs(w) / gcd;
		gcd = gcd(w, this.base);
		if (gcd != 1) {
			pow = (long) Math.ceil(log(w, Math.abs(this.base)));
			v = v * (long) Math.pow(this.base, pow) / gcd;
			w = w / gcd;
			gcd = gcd(v,w);
			v = v / gcd * (long) Math.signum(w);
			w = Math.abs(w) / gcd;
		}
		if (!this.remSets.containsKey(w))
			this.remSets.put(w, calcRemSet(w));
		Set<Long> remSet = this.remSets.get(w);
		long rem = 0;
		for (long r : remSet) {
			if ((v - r) % w == 0) {
				long A = (v - r) / w;
				ans.add(A);
				rem = r;
				break;
			}
		}
		ans.addAll(divBrent(rem, w, remSet));
		ans.add(pow);
		return ans;	
	}
	
	
	public long chopBrent(long z) {
		long power = 1;
		long lam = 1;
		long tortoise = z;
		long hare = chop(z)[1];
		while (tortoise != hare) {
			if (power == lam) {
				tortoise = hare;
				power = power << 1;
				lam = 0;
			}
			hare = chop(hare)[1];
			lam++;
		}
		tortoise = z;
		hare = z;
		for (long i = 0; i < lam; i++) {
			hare = chop(hare)[1];
		}
		while (tortoise != hare) {
			tortoise = chop(tortoise)[1];
			hare = chop(hare)[1];
		}
		return hare;
	}
	
	public long chopBrentRep(long z) {
		long power = 1;
		long lam = 1;
		long tortoise = z;
		long hare = chop(z)[1];
		while (tortoise != hare) {
			if (power == lam) {
				tortoise = hare;
				power = power << 1;
				lam = 0;
			}
			hare = chop(hare)[1];
			lam++;
		}
		tortoise = z;
		hare = z;
		for (long i = 0; i < lam; i++) {
			hare = chop(hare)[1];
		}
		while (tortoise != hare) {
			tortoise = chop(tortoise)[1];
			hare = chop(hare)[1];
		}
		return hare;
	}
	
	public boolean isBasic() {
		if (this.base > 0) {
			for (long i = -this.dMax / (this.base - 1); i <= -this.dMin / (this.base - 1); i++) {
				if (chopBrent(i) != 0)
					return false;
			}
			return true;
		}
		else {
			for (long i = (-this.dMin * this.base - this.dMax) / (this.base * this.base - 1); i <= (-this.dMax * this.base - this.dMin) / (this.base * this.base - 1); i++) {
				if (chopBrent(i) != 0)
					return false;
			}
			return true;
			}
		}
	
	private long[] divMet(long rem, long w, Set<Long> remSet) {
		long a = 0;
		for (long a1 : this.digitSet) {
			if (remSet.contains(Long.valueOf(this.base * rem - a1 * w))) {
				rem = this.base * rem - a1 * w;
				a = a1;
				break;
			}
		}
		return new long[] {a, rem};
	}
	
	public List<Long> divBrent(long rem, long w, Set<Long> remSet) {
		List<Long> ans = new ArrayList<Long>();
		long power = 1;
		long lam = 1;
		long tortoise = rem;
		long hare = divMet(rem, w, remSet)[1];
		while (tortoise != hare) {
			if (power == lam) {
				tortoise = hare;
				power = power << 1;
				lam = 0;
			}
			hare = divMet(hare, w, remSet)[1];
			lam++;
		}
		tortoise = rem;
		hare = rem;
		long[] bits = new long[2];
		for (long i = 0; i < lam; i++) {
			bits = divMet(hare, w, remSet);
			ans.add(bits[0]);
			hare = divMet(hare, w, remSet)[1];
		}
		long mu = 0;
		while (tortoise != hare) {
			tortoise = divMet(tortoise, w, remSet)[1];
			bits = divMet(hare, w, remSet);
			ans.add(bits[0]);
			hare = divMet(hare, w, remSet)[1];
			mu++;
		}
		mu++;
		ans.add(lam);
		ans.add(mu);
		return ans;
		
	}
	
	public String longDivToString(List<Long> num) {
		String pow = " ) * " + String.valueOf(this.base) + " ^ -" + num.get(num.size() - 1).toString();
		int loopPos = num.get(num.size() - 2).intValue();
		int loopLen = num.get(num.size() - 3).intValue();
		String ans = convert(num.get(0)).stream().map(l -> ((Long) l).toString()).collect(Collectors.joining(" ")) + " . ";
		ans = ans + num.subList(1, loopPos).stream().map(l -> ((Long) l).toString()).collect(Collectors.joining(" ")) + "( ";
		ans = ans + num.subList(loopPos, loopPos + loopLen).stream().map(l -> ((Long) l).toString()).collect(Collectors.joining(" ")) + pow;
		return ans;
	}

}
