import java.math.BigInteger;
import java.math.BigDecimal;
public class Complex {
	private BigDecimal real;
	private BigDecimal imag;
	public Complex(BigDecimal real, BigDecimal imag) {
		this.real = real;
		this.imag = imag;
	}
	public Complex(String real, String imag) {
		this.real = new BigDecimal(real);
		this.imag = new BigDecimal(imag);
	}
	public BigDecimal realPart() {
		return this.real;
	}
	public BigDecimal imagPart() {
		return this.imag;
	}
	public Complex add(Complex addend) {
		BigDecimal augendReal = this.realPart();
		BigDecimal augendImag = this.imagPart();
		BigDecimal addendReal = addend.realPart();
		BigDecimal addendImag = addend.imagPart();
		return new Complex(augendReal.add(addendReal), augendImag.add(addendImag));
	}
	public Complex subtract(Complex subtrahend) {
		BigDecimal minuendReal = this.realPart();
		BigDecimal minuendImag = this.imagPart();
		BigDecimal subtrahendReal = subtrahend.realPart();
		BigDecimal subtrahendImag = subtrahend.imagPart();
		return new Complex(minuendReal.subtract(subtrahendReal), minuendImag.subtract(subtrahendImag));
	}
	public Complex multiply(Complex multiplier) {
		BigDecimal multiplicandReal = this.realPart();
		BigDecimal multiplicandImag = this.imagPart();
		BigDecimal multiplierReal = multiplier.realPart();
		BigDecimal multiplierImag = multiplier.imagPart();
		return new Complex(multiplicandReal.multiply(multiplierReal).subtract(multiplicandImag.multiply(multiplierImag)), multiplicandReal.multiply(multiplierImag).add(multiplicandImag.multiply(multiplierReal)));
	}
	public Complex divide(Complex divisor) {
		BigDecimal dividendReal = this.realPart();
		BigDecimal dividendImag = this.imagPart();
		BigDecimal divisorReal = divisor.realPart();
		BigDecimal divisorImag = divisor.imagPart();
		return new Complex(dividendImag.multiply(divisorReal).subtract(dividendReal.multiply(divisorImag)).divide(divisorReal.pow(2).add(divisorImag.pow(2))), dividendReal.multiply(divisorReal).add(dividendImag.multiply(divisorImag)).divide(divisorReal.pow(2).add(divisorImag.pow(2))));
	}
}
