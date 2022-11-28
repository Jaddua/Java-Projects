package ca.jaddua.bases;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import ca.jaddua.numbers.*;

  

public class GintBase {

	public List<Gint> digitSet;
	public Gint base;
	public Map<Gint, Gint> map;
	public Map<Gint, Gint> modInvs;
	public long dMax;
	public Map<Gint, Set<Gint>> remSets;

	public GintBase(Gint base, List<Gint> digitSet) {
		this.base = base;
		this.digitSet = digitSet;
		this.dMax = Collections.max(digitSet.stream().map(G -> G.norm()).collect(Collectors.toList()));
		this.map = new HashMap<Gint, Gint>();
		this.modInvs = new HashMap<Gint, Gint>();
		this.remSets = new HashMap<Gint, Set<Gint>>();
		if((base.norm() & 1) == 0) {
			for(Gint i : digitSet) {
				if((i.norm() * 2) == base.norm()) {
					this.map.put(new Gint(base.norm() / 2).mod(base), i);
					this.map.put(new Gint(-base.norm() / 2).mod(base), i);
					this.map.put(new Gint(0, base.norm() / 2).mod(base), i);
					this.map.put(new Gint(0, -base.norm() / 2).mod(base), i);
				}
				else {
					this.map.put(i.mod(base), i);
				}
			}
		}
		else {
			for(Gint i : digitSet) {
				this.map.put(i.mod(base), i);
			}
		}
		for (long i = 0; i < base.norm(); i++) {
			modInvs.put(new Gint(i).mod(base),new Gint(egcd(i, base.norm())[1]));
		}
		
		/*if (base.norm() % 2 == 0)
			this.map.put(new Gint(base.norm() / 2).mod(base).neg(), this.map.get((new Gint(base.norm() / 2).mod(base))));*/
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

	public Gint mapper(Gint i) {
		return this.map.get(i);
	}


	public Gint[] chop(Gint i) {
		Gint ans[] = new Gint[2];
		//System.out.println(new Gint(17).mod(this.base));
		ans[0] = mapper(i.mod(this.base));
		ans[1] = i.sub(ans[0]).div(this.base);
		return ans;
	}

	public List<Gint> convert(Gint i) {
		Gint n[] = new Gint[2];
		n = chop(i);
		List<Gint> ans = new ArrayList<Gint>();
		if (!chopBrentRep(i).equals(Gint.Z)) {
			ans.add(Gint.Z);
			ans.add(Gint.Z);
			return ans;
		}
		ans.add(n[0]);
		while(!n[1].equals(Gint.Z)) {
			n = chop(n[1]);
			ans.add(n[0]);
		}
		Collections.reverse(ans);
		ans.add(Gint.Z);
		return ans;
	}
	
	private Gint modInv(Gint w) {
		return this.modInvs.get(w.mod(this.base));
	}
	
	public Gint remFunc(Gint z, Gint w) {
		Gint winv = modInv(w);
		Gint a = mapper(z.neg().mult(winv).mod(this.base));
		return z.add(a.mult(w)).div(this.base);
	}
	
	public List<Gint> chopBrent(Gint n, Set<Gint> banned) {
		List<Gint> ans = new ArrayList<Gint>();
		long power = 1;
		long lam = 1;
		Gint tortoise = n;
		Gint hare = chop(n)[1];
		while (!tortoise.equals(hare)) {
			if (power == lam) {
				tortoise = hare;
				power = power << 1;
				lam = 0;
			}
			hare = chop(hare)[1];
			lam++;
		}
		tortoise = n;
		hare = n;
		Gint[] bits = new Gint[2];
		for (long i = 0; i < lam; i++) {
			bits = chop(hare);
			ans.add(bits[1]);
			hare = chop(hare)[1];
		}
		long mu = 0;
		while (!tortoise.equals(hare)) {
			tortoise = chop(tortoise)[1];
			bits = chop(hare);
			ans.add(bits[1]);
			hare = chop(hare)[1];
			mu++;
		}
		mu++;
		if (!ans.get(ans.size() - 1).equals(Gint.Z))
			ans = null;
		return ans;
		
	}
	
	
	public List<List<Gint>> remBrent(Gint z, Gint w, Map<Gint, Boolean> rMap) {
		List<Gint> ans = new ArrayList<Gint>();
		List<List<Gint>> ANS = new ArrayList<List<Gint>>();
		long power = 1;
		long lam = 1;
		long mu = 0;
		Gint tortoise = z;
		Gint hare = remFunc(z, w);
		while (!tortoise.equals(hare)) {
			if (power == lam) {
				tortoise = hare;
				power = power << 1;
				lam = 0;
			}
			hare = remFunc(hare, w);
			lam++;
		}
		tortoise = z;
		hare = z;
		for (long i = 0; i < lam; i++) {
			ans.add(hare);
			hare = remFunc(hare, w);
			if(rMap.get(hare)) {
				ANS.add(ans);
				ANS.add(Arrays.asList(Gint.Z));
				return ANS;
			}
				
		}
		while (!tortoise.equals(hare)) {
			ans.add(hare);
			tortoise = remFunc(tortoise, w);
			hare = remFunc(hare, w);
			mu++;
			if(rMap.get(hare)) {
				ANS.add(ans);
				ANS.add(Arrays.asList(Gint.Z));
				return ANS;
			}
		}
		ANS.add(ans);
		ANS.add(ans.subList((int) mu, (int) (mu + lam)));
		return ANS;
	}
	
	public Set<Gint> remSet(Gint w) {
		if (this.remSets.containsKey(w))
			return this.remSets.get(w);
		else {
			Set<Gint> ans = new HashSet<Gint>();
			Map<Gint, Boolean> remMap = new HashMap<Gint, Boolean>();
			long remC =(long) (this.dMax * w.norm() / (1 + this.base.norm() - 2 * Math.sqrt(this.base.norm())));
			for(long i = -(long) Math.floor(Math.sqrt(remC)); i <= (long) Math.floor(Math.sqrt(remC)); i++) {
				for(long j = -(long)Math.floor(Math.sqrt(remC - i*i)); j <= (long)Math.floor(Math.sqrt(remC - i*i)); j++) {
					remMap.put(new Gint(i,j), false);
				}
			}
			for (Gint i : remMap.keySet()) {
				if (!remMap.get(i)) {
					List<List<Gint>> temp = remBrent(i, w, remMap);
					ans.addAll(temp.get(1));
					for (Gint j : temp.get(0)) {
						remMap.put(j, true);
					}
				}
			}
			this.remSets.put(w, ans);
			return ans;
		}
	}
	
	public boolean isBasic() {
		Set<Gint> banned = new HashSet<Gint>();
		for (long a = (long) -Math.floor(Math.sqrt(this.dMax)); a <= (long) Math.floor(Math.sqrt(this.dMax)); a++) {
			for (long b = (long) -Math.floor(Math.sqrt(this.dMax - a*a)); b <= (long) Math.floor(Math.sqrt(this.dMax - a*a)); b++) {
				if (!banned.contains(new Gint(a, b))) {
					List<Gint> temp = chopBrent(new Gint(a, b), banned);
					if (temp == null)
						return false;
				banned.addAll(temp);
			}
		}
		}
		return true;
	   }
	
	
	public Gint[] toCoPrimeBase(Gint z) {
		Gint u = Gint.Z;
		Nat count = Nat.ZERO;
		Nat k = Nat.ZERO;
		Comp W1 = Comp.Z;
		Gint w1 = Gint.Z;
		Gint gcd = this.base.gcd(z);
		if (gcd.equals(Gint.O) || gcd.equals(Gint.MO) || gcd.equals(Gint.I) || gcd.equals(Gint.MI)) {
			return new Gint[] {Gint.O , z, Gint.Z};
		}
		else {
			while(true) {
				u = count.NtoN2()[0].NtoZi();
				k = count.NtoN2()[1];
				W1 = Comp.valueOf(z.mult(u)).div(Comp.valueOf(this.base.pow(k.value)));
				if (W1.isGint()) {
					w1 = Gint.valueOf(W1);
					gcd = this.base.gcd(w1);
					if ((gcd.equals(Gint.O) || gcd.equals(Gint.MO) || gcd.equals(Gint.I) || gcd.equals(Gint.MI)) && !w1.equals(Gint.Z))
						break;
					else
						count = count.add(Nat.ONE);
				}
				else
					count = count.add(Nat.ONE);
			}
		}
		return new Gint[] {u, w1, new Gint(k.value)};
	}
	
	
	
	
	
	public List<Gint> convert(Grat num) {
		if (num.equals(new Grat(num.num)))
			return convert(num.num);
		List<Gint> ans = new ArrayList<Gint>();
		Gint pow = Gint.Z;
		/*Gint gcd = v.gcd(w);
		if (gcd.equals(Gint.MO)) {
			gcd = Gint.O;
			v = v.mult(Gint.MO);
			w = w.mult(Gint.MO);
		}
		else if (gcd.equals(Gint.I)) {
			gcd = Gint.O;
			v = v.mult(Gint.MI);
			w = w.mult(Gint.MI);
		}
		else if (gcd.equals(Gint.MI)) {
			gcd = Gint.O;
			v = v.mult(Gint.I);
			w = w.mult(Gint.I);
		}
		v = v .div(gcd);
		w = w.div(gcd);*/
		Gint v = num.num;
		Gint w = num.den;
		Gint gcd = w.gcd(this.base);
		if (!gcd.equals(Gint.O) || !gcd.equals(Gint.MO) || !gcd.equals(Gint.I) || !gcd.equals(Gint.MI)) {
			/*pow = (long) Math.ceil(log(w.norm(), this.base.norm()));
			v = v.mult(this.base.pow(pow)).div(gcd);
			w = w.div(gcd);
			gcd = v.gcd(w);
			v = v.div(gcd);
			w = w.div(gcd);*/
			Gint[] pieces = toCoPrimeBase(w);
			v = v.mult(pieces[0]);
			w = pieces[1];
			pow = pieces[2];
		}
		Gint rem = Gint.Z;
		for (Gint r : remSet(w)) {
			if (v.sub(r).mod(w).equals(Gint.Z)) {
				Gint A = v.sub(r).div(w);
				ans.add(A);
				rem = r;
				break;
			}
		}
		ans.addAll(divBrent(rem, w));
		ans.add(pow);
		ans.add(Gint.O);
		return ans;	
	}
	
	/**
	 * 
	 * @param a Argument
	 * @param b Base
	 * @return Log of a in base b.
	 */
	private double log(double a, double b) {
		return Math.log(a) / Math.log(b);
	}
	
	private Gint[] divMet(Gint rem, Gint w) {
		Gint a = Gint.Z;
		for (Gint a1 : this.digitSet) {
			if (remSet(w).contains(this.base.mult(rem).sub(a1.mult(w)))) {
				rem = this.base.mult(rem).sub(a1.mult(w));
				a = a1;
				break;
			}
		}
		return new Gint[] {a, rem};
	}
	
	public List<Gint> divBrent(Gint rem, Gint w) {
		List<Gint> ans = new ArrayList<Gint>();
		long power = 1;
		long lam = 1;
		Gint tortoise = rem;
		Gint hare = divMet(rem, w)[1];
		while (!tortoise.equals(hare)) {
			if (power == lam) {
				tortoise = hare;
				power = power << 1;
				lam = 0;
			}
			hare = divMet(hare, w)[1];
			lam++;
		}
		tortoise = rem;
		hare = rem;
		Gint[] bits = new Gint[2];
		for (long i = 0; i < lam; i++) {
			bits = divMet(hare, w);
			ans.add(bits[0]);
			hare = divMet(hare, w)[1];
		}
		long mu = 0;
		while (!tortoise.equals(hare)) {
			tortoise = divMet(tortoise, w)[1];
			bits = divMet(hare, w);
			ans.add(bits[0]);
			hare = divMet(hare, w)[1];
			mu++;
		}
		mu++;
		ans.add(new Gint(lam));
		ans.add(new Gint(mu));
		return ans;
		
	}
	
	public String convertToString(List<Gint> num) {
		String pow = " ) * (" + String.valueOf(this.base) + ") ^ -" + num.get(num.size() - 1).toString();
		int loopPos = (int) num.get(num.size() - 2).real;
		int loopLen = (int) num.get(num.size() - 3).real;
		List<Gint> intConv = convert(num.get(0));
		String ans = intConv.subList(0, intConv.size() - 4).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + " . ";
		ans = ans + num.subList(1, loopPos).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + "( ";
		ans = ans + num.subList(loopPos, loopPos + loopLen).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + pow;
		if (ans.contains(". ( 0 )"))
			ans = ans.replace(". ( 0 )", "");
		if (ans.contains(" * (" + this.base.toString() + ") ^ -0"))
			ans = ans.replace(" * (" + this.base.toString() + ") ^ -0", "");
		return ans;
	}
	
	public long bannedSize(long n) {
		long sum = 1;
		n--;
		for (long k =1; k <= n; k += 2)
			sum += n / k * (((k & 3)== 1) ? 1 : -1) << 2;
		return sum;
	}
	
	public Gint chopBrentRep(Gint n) {
		long power = 1;
		long lam = 1;
		Gint tortoise = n;
		Gint hare = chop(n)[1];
		while (!tortoise.equals(hare)) {
			if (power == lam) {
				tortoise = hare;
				power = power << 1;
				lam = 0;
			}
			hare = chop(hare)[1];
			lam++;
		}
		tortoise = n;
		hare = n;
		for (long i = 0; i < lam; i++) {
			hare = chop(hare)[1];
		}
		while (!tortoise.equals(hare)) {
			tortoise = chop(tortoise)[1];
			hare = chop(hare)[1];
		}
		return hare;
		
	}
}