package ca.jaddua.bases;

import java.util.List;
import java.util.stream.Collectors;

import ca.jaddua.numbers.Gint;

public class RealRadixNum {

	public List<Long> rep;
	public Base base;
	
	public RealRadixNum(Base base, List<Long> rep) {
		this.rep = rep;
		this.base = base;
	}
	
	@Override
	public String toString() {
		List<Long> num = this.rep;
		if (num.get(num.size()-1) == 0)
			return num.subList(0, num.size() - 1).stream().map(z -> (z).toString()).collect(Collectors.joining(" "));
		
		String pow = " ) * (" + String.valueOf(this.base.base) + ") ^ -" + num.get(num.size() - 2).toString();
		int loopPos = num.get(num.size() - 3).intValue();
		int loopLen = num.get(num.size() - 4).intValue();
		List<Long> intConv = this.base.convert(num.get(0));
		String ans = intConv.subList(0, intConv.size() - 1).stream().map(l -> ((Long) l).toString()).collect(Collectors.joining(" ")) + " . ";
		ans = ans + num.subList(1, loopPos).stream().map(l -> ((Long) l).toString()).collect(Collectors.joining(" ")) + "( ";
		ans = ans + num.subList(loopPos, loopPos + loopLen).stream().map(l -> ((Long) l).toString()).collect(Collectors.joining(" ")) + pow;
		if (ans.contains(". ( 0 )"))
			ans = ans.replace(". ( 0 )", "");
		if (ans.contains("( 0 )"))
			ans = ans.replace("( 0 )", "");
		if (ans.contains(" * (" + this.base.base + ") ^ -0"))
			ans = ans.replace(" * (" + this.base.base + ") ^ -0", "");
		return ans;
	}
}
