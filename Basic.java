import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import ca.jaddua.numbers.GaussianInteger;

  


class BasicException  extends Exception  
{  
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public BasicException (String str)  
{   
   super(str);  
}  
}  

class BaseException  extends Exception  
{  
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public BaseException (String str)  
{   
   super(str);  
}  
}  


class DigitSetException  extends Exception  
{  
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public DigitSetException (String str)  
{   
   super(str);  
}  
}  

public class Basic {
	public List<BigInteger> D;
	public GaussianInteger B;
	
	public Basic() {
		this.B = new GaussianInteger("-10", "0");
		this.D = Arrays.asList(BigInteger.ZERO, BigInteger.ONE, BigInteger.TWO, new BigInteger("3"), new BigInteger("4"), new BigInteger("5"), new BigInteger("6"), new BigInteger("7"), new BigInteger("8"), new BigInteger("9"));
	}
	
	public Basic(GaussianInteger B, List<BigInteger> D) throws BaseException, BasicException, DigitSetException {
		this.B = B;
		this.D = D;
		System.out.println(nPlaceDigitSet(new BigInteger("3")).stream().map(GaussianInteger::toString).collect(Collectors.joining(" ")));
		if (BigInteger.valueOf(this.D.size()).compareTo(new BigDecimal(this.B.norm()).sqrt(new MathContext(100)).pow(1 + B.imagPart().signum()*B.imagPart().signum(), new MathContext(100)).round(new MathContext(1)).toBigInteger()) != 0) {
			//System.out.println(new BigDecimal(this.B.norm()).sqrt(new MathContext(100)).pow(2, new MathContext(100)).round(new MathContext(1)).toBigInteger());
			throw new DigitSetException("Invalid Digit Set. Size of digit set must be |base| if base is an integer or N(base) if base is a gaussian integer. Default Base is -10 and digit set {0,1,2,3,4,5,6,7,8,9}.");
		}
		if (B.norm().compareTo(BigInteger.TWO) == -1) {
			throw new BaseException("Invalid Base.  Default Base is -10 and digit set {0,1,2,3,4,5,6,7,8,9}.");
		}
		if (B.realPart().compareTo(BigInteger.ONE) == 1 && B.imagPart().compareTo(BigInteger.ZERO) == 0) {
			for (BigInteger i = Collections.max(D).multiply(new BigInteger("-1")).divide(B.realPart().subtract(BigInteger.ONE)); i.compareTo(Collections.min(D).multiply(new BigInteger("-1")).divide(B.realPart().subtract(BigInteger.ONE)).add(BigInteger.ONE)) == -1; i = i.add(BigInteger.ONE)) {
				if (!isAcyclic(new GaussianInteger(i, BigInteger.ZERO))) {
					throw new BasicException("The Base and Digit set are not basic. Default base is -10 and digit set {0,1,2,3,4,5,6,7,8,9}.");
				}
			}
		}
		else if (B.realPart().compareTo(BigInteger.ONE) == -1 && B.imagPart().compareTo(BigInteger.ZERO) == 0) {
			for (BigInteger i = Collections.min(D).multiply(new BigInteger("-1").multiply(B.realPart())).subtract(Collections.max(D)).divide(B.realPart().pow(2).subtract(BigInteger.ONE)); i.compareTo(Collections.max(D).multiply(new BigInteger("-1").multiply(B.realPart())).subtract(Collections.min(D)).divide(B.realPart().pow(2).subtract(BigInteger.ONE)).add(BigInteger.ONE)) == -1; i = i.add(BigInteger.ONE)) {
				if (!isAcyclic(new GaussianInteger(i, BigInteger.ZERO))) {
					throw new BasicException("The Base and Digit set are not basic. Default base is -10 and digit set {0,1,2,3,4,5,6,7,8,9}.");
				}
			}
		}
		else {
			throw new BasicException("The Base and Digit set are not basic. Default base is -10 and digit set {0,1,2,3,4,5,6,7,8,9}.");
		}
		
		
	}
	
	public List<GaussianInteger> nPlaceDigitSet(BigInteger n) {
		List<GaussianInteger> answer = new ArrayList<GaussianInteger>();
		GaussianInteger tempAns = GaussianInteger.ZERO;
		for (BigInteger c = BigInteger.ZERO; c.compareTo(BigInteger.valueOf(this.D.size()).pow(n.intValue())) == -1; c = c.add(BigInteger.ONE)) {
			for (BigInteger i = BigInteger.ZERO; i.compareTo(n) == -1; i = i.add(BigInteger.ONE)) {
				tempAns = tempAns.add(new GaussianInteger(this.D.get(c.divide(BigInteger.valueOf(this.D.size()).pow(i.intValue())).mod(BigInteger.valueOf(this.D.size())).intValue()), BigInteger.ZERO).multiply(this.B.pow(i)));
			}
			answer.add(tempAns);
			tempAns = GaussianInteger.ZERO;
		}
		return answer;
	}
	
	/*public List<GaussianInteger> newNPlaceDigitSet(BigInteger n) {
		if (n.compareTo(BigInteger.ONE) == 0) {
			GaussianInteger ab = 
		}
	}*/
	
	public boolean isAcyclic(GaussianInteger i) {
		BigInteger n = BigInteger.ONE;
		List<GaussianInteger> vals = new ArrayList<GaussianInteger>();
		vals.add(i);
		while (true) {
			GaussianInteger temp = (GaussianInteger) phin(n,i).get(0);
			if (temp.equals(GaussianInteger.ZERO)) {
				return true;
			}
			if(vals.contains(temp)) {
				return false;
			}
			vals.add(temp);
			n = n.add(BigInteger.ONE);
		}
	}
	
	public BigInteger digitI(GaussianInteger i) {
		GaussianInteger modVal = i.mod(this.B);
		BigInteger answer = BigInteger.ZERO;
		for (BigInteger d : this.D) {
			if (modVal.equals(new GaussianInteger(d, BigInteger.ZERO).mod(this.B))) {
				answer = d;
			}
		}
		return answer;
	}
	
	public List<Object> phi(GaussianInteger i) {
		//BigInteger[] answer = new BigInteger[2];
		List<Object> answer = new ArrayList<Object>();
		
	//	for (BigInteger d : this.D) {
			//BigInteger[] phiTemp = i.subtract(d).divideAndRemainder(this.B);
			//if(phiTemp[1].compareTo(BigInteger.ZERO) == 0) {
			//	answer[0] = phiTemp[0];
				//answer[1] = d;
				//break;
			//}
		//}
		BigInteger DIGIT = digitI(i);
		answer.add(i.subtract(new GaussianInteger(DIGIT, BigInteger.ZERO)).divide(this.B));
		answer.add(DIGIT);
		return answer;
	}
	
	public List<Object> phin(BigInteger n, GaussianInteger i) {
		/*if(n.compareTo(BigInteger.ZERO) == 0) {
			return new BigInteger[] {i, null};
		}*/
		if (n.compareTo(BigInteger.ONE) == 0) {
			return phi(i);
		}
		else {
			return phi((GaussianInteger) phin(n.subtract(BigInteger.ONE), i).get(0));
		}
	}
	
	public List<BigInteger> convert(GaussianInteger i) {
		List<BigInteger> answer = new ArrayList<BigInteger>();
		BigInteger n = BigInteger.ONE;
		while(true) {
			List<Object> phinTemp = phin(n, i);
			answer.add((BigInteger) phinTemp.get(1));
			if (((GaussianInteger) phinTemp.get(0)).equals(GaussianInteger.ZERO)) {//compareTo(BigInteger.ZERO) == 0) {
				break;
			}
			else {
				n = n.add(BigInteger.ONE);
			}
			
		}
		Collections.reverse(answer);
		return answer;
	}
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		//for (GaussianInteger i = GaussianInteger.ZERO; i.realPart().compareTo(new BigInteger("10")) == -1; i = i.add(GaussianInteger.ONE)) {
		//	System.out.println(i.mod(GaussianInteger.M3Pi).toString());
		//}
		//GaussianInteger c = a.mod((GaussianInteger) b);
		//GaussianInteger.gaussIntFromStr(input.next());
			System.out.println("Enter the base.");
		GaussianInteger base = GaussianInteger.gaussIntFromStr(input.next());
		System.out.println("Enter the digit set seperated by commas.");
		List<BigInteger> digits = Arrays.stream(Arrays.stream(input.next().split(",")).map(Integer::parseInt).collect(Collectors.toList()).toArray(new Integer[0])).map(BigInteger::valueOf).collect(Collectors.toList());
		Basic abc = new Basic();
		try {
			abc = new Basic(base, digits);
		} catch (BaseException | BasicException | DigitSetException e) {
			System.out.println(e);
		}
		while (true) {
		System.out.println(abc.convert(GaussianInteger.gaussIntFromStr(input.next())).stream().map(String::valueOf).collect(Collectors.joining(" ")));
		}
		/*BigInteger cba[];
		for (BigInteger j = BigInteger.ONE; j.compareTo(new BigInteger("10")) == -1; j = j.add(BigInteger.ONE)) {
		cba = abc.phin(j, new BigInteger("99"));
		System.out.print(cba[1].toString() + " ");
		if (cba[0].compareTo(BigInteger.ZERO) == 0) {
			break;
		}
		}*/
		
	}
	/*public static boolean isBasic(List<Integer> D, BigInteger B) {
		int size = D.size();
		BigInteger absB = B.abs();
		
		return true;
	}*/
}
