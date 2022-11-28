package ca.jaddua.bases;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ca.jaddua.numbers.Gint;
import ca.jaddua.numbers.Grat;

public class CompRadixNum {

	public List<Gint> rep;
	public GintBase base;
	
	public CompRadixNum(GintBase base, List<Gint> rep) {
		this.rep = rep;
		this.base = base;
	}
	
	@Override
	public String toString() {
		List<Gint> num = this.rep;
		//System.out.println(num);
		String ans = "";
		if (num.get(num.size()-1).equals(Gint.Z))
			ans = num.subList(0, num.size() - 1).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" "));
		
		else {
			List<Gint> intPart = this.base.convert(num.get(0));
			intPart = intPart.subList(0, intPart.size()-1);
			//System.out.println(intPart);
			num = num.subList(1,num.size());
			num.addAll(0, intPart);
			long pow = num.get(num.size() - 2).real;
			long mu = num.get(num.size() - 3).real + intPart.size() - 1;
			long lam = num.get(num.size() - 4).real;
			long size = intPart.size();
			//System.out.println(size);
			//System.out.println(pow);
			for (long i = size; i <= pow; i++) {
				num.add(0, Gint.Z);
				mu++;
				size++;
			}
			//System.out.println(size);
			//System.out.println(pow);
			//System.out.println(mu);
			ans = num.subList(0, (int) (size - pow)).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + " . " + num.subList((int) (size - pow), (int) mu).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + " ( " + num.subList((int) mu, (int) (mu + lam)).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + " )";
			//ans = ans.substring(0, (int) (size - 2 * pow - 3)) + ". " + ans.substring((int) (size - 2 * pow - 1), (int) mu * 2) + "( " + ans.substring((int) mu * 2 - 2) + " )";
		
		}
			/*
		}
		String pow = " ) * (" + String.valueOf(this.base.base) + ") ^ -" + num.get(num.size() - 2).toString();
		int loopPos = (int) num.get(num.size() - 3).real;
		int loopLen = (int) num.get(num.size() - 4).real;
		List<Gint> intConv = this.base.convert(num.get(0));
		String ans = intConv.subList(0, intConv.size() - 1).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + " . ";
		ans = ans + num.subList(1, loopPos).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + "( ";
		ans = ans + num.subList(loopPos, loopPos + loopLen).stream().map(z -> ((Gint) z).toString()).collect(Collectors.joining(" ")) + pow;
		*/
		if (ans.contains("  "))
			ans = ans.replace("  ", " ");
		if (ans.contains(". ( 0 )"))
			ans = ans.replace(". ( 0 )", "");
		if (ans.contains("( 0 )"))
			ans = ans.replace("( 0 )", "");
		//if (ans.contains(". 0 "))
			//ans = ans.replace(". 0 ", "");
		//if (ans.contains(" * (" + this.base.base.toString() + ") ^ -0"))
			//ans = ans.replace(" * (" + this.base.base.toString() + ") ^ -0", "");
		return ans;
}
	
	public static Grat valueOf(String rep, Gint base) {
		List<String> num = Arrays.asList(rep.split(" "));
		Grat ans = new Grat(Gint.Z);
		if (num.contains(".")) {
			int radix = num.indexOf(".");
			int left = 0;
			int right = 0;
			List<Gint> intPart = num.subList(0, radix).stream().map(s -> Gint.valueOf(s)).collect(Collectors.toList());
			List<Gint> repeat = Arrays.asList(Gint.Z);
			if (num.contains("(")) {
				left = num.indexOf("(");
				right = num.indexOf(")");
				List<Gint> b4Repeat =  num.subList(radix + 1, left).stream().map(s -> Gint.valueOf(s)).collect(Collectors.toList());
				repeat = num.subList(left + 1, right).stream().map(s -> Gint.valueOf(s)).collect(Collectors.toList());
				long pow = 1;
				for (Gint i : b4Repeat) {
					ans = ans.add(new Grat(i, base.pow(pow)));
					pow++;
				}
				long pow2 = 0;
				Gint basePow = base;
				Collections.reverse(repeat);
				Grat tempAns = new Grat(Gint.Z);
				for (Gint i : repeat) {
					tempAns = tempAns.add(new Grat(i.mult(basePow)));
					System.out.println(tempAns);
					basePow = basePow.mult(base);
					pow2++;
				}
				ans = ans.add(tempAns.div(new Grat(base.pow(pow2).sub(Gint.O))).div(new Grat(base.pow((long) b4Repeat.size()))));
			}
			else {
				List<Gint> b4Repeat =  num.subList(radix + 1, num.size()).stream().map(s -> Gint.valueOf(s)).collect(Collectors.toList());
				long pow = 1;
				for (Gint i : b4Repeat) {
					ans = ans.add(new Grat(i, base.pow(pow)));
					pow++;
				}
			}
			Collections.reverse(intPart);
			long pow = 0;
			for (Gint i : intPart) {
				ans = ans.add(new Grat(i.mult(base.pow(pow))));
				pow++;
			}
			
		}
		else {
			List<Gint> intPart = num.stream().map(s -> Gint.valueOf(s)).collect(Collectors.toList());
			long pow = 0;
			for (Gint i : intPart) {
				ans = ans.add(new Grat(i.mult(base.pow(pow))));
				pow++;
			}
		}
		return ans;
	}
}
