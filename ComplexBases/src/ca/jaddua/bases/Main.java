package ca.jaddua.bases;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ca.jaddua.numbers.*;

public class Main {

	public static void main(String[] args) {
		List<Gint> digits = new ArrayList<Gint>();
		digits.add(Gint.Z);
		digits.add(Gint.O);
		digits.add(new Gint(2));
		digits.add(new Gint(3));
		for(long l = 4; l < 10; l++) {
			digits.add(new Gint(l));
		}
		
		GintBase B = new GintBase(new Gint(-3,1), digits);
		List<Long> digs = new ArrayList<Long>();
		/*for (long i = 0; i < 10; i++) {
			digs.add(i);
		}*/
		digs.add(0l);
		digs.add(-1l);
		digs.add(91l);
		Base RB = new Base(3l, digs);
		Gint num = new Gint(355);
		Gint den = new Gint(113);
		Grat frac = new Grat(num, den);
		//System.out.println(new CompRadixNum(B, B.convert(frac)));
		System.out.println(B.bannedSize(1));
		//System.out.println(CompRadixNum.valueOf("4 . ( 5 8 1 7 8 6 7 4 4 4 2 2 5 6 3 4 5 2 )", B.base));
		//B.chopBrentRep(new Gint(-25)));
		/*for (Gint i : rems) {
			System.out.printf("Remainder: %s , Modulo base: %s , Modulo num: %s%n", i.toString(), i.mod(B.base).toString(), i.mod(tester).toString());
		}*/
		//System.out.println(B.remSet(tester));
		
		
		/*Polynomial zero = new Polynomial(0, 0);

        Polynomial p1   = new Polynomial(4, 3);
        Polynomial p2   = new Polynomial(3, 2);
        Polynomial p3   = new Polynomial(1, 0);
        Polynomial p4   = new Polynomial(2, 1);
        Polynomial p    = p1.plus(p2).plus(p3).plus(p4);   // 4x^3 + 3x^2 + 1

        Polynomial q1   = new Polynomial(3, 2);
        Polynomial q2   = new Polynomial(5, 0);
        Polynomial q    = q1.plus(q2);                     // 3x^2 + 5


        Polynomial r    = p.plus(q);
        Polynomial s    = p.times(q);
        Polynomial t    = p.compose(q);
        Polynomial u    = p.minus(p);
        System.out.println(s);*/
		//System.out.println(B.longDivToString(B.longDiv(new Gint(1), new Gint(1, 1))));
		/*
		Base b = new Base(3l,0l,-1l,1l);
		System.out.println(B.bannedSize(17359221));
		long boop = 17359221;
		boop--;
		List<Gint> slims = new ArrayList<Gint>();
		for(long i = -(long) Math.floor(Math.sqrt(boop)); i <= (long) Math.floor(Math.sqrt(boop)); i++) {
			for(long j = -(long)Math.floor(Math.sqrt(boop - i*i)); j <= (long)Math.floor(Math.sqrt(boop - i*i)); j++) {
				slims.add(new Gint(i,j));
			}
		}*/
		//System.out.println(slims);
		//System.out.println(slims.size());
		//System.out.println(b.modInv(5,10));//new Gint(2).modInv(new Gint(-2,1)));
		//System.out.println(B.isBasic());
	}

}
