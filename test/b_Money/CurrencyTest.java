package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK",SEK.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals(1.5, EUR.getRate(), 0);
	}
	
	@Test
	public void testSetRate() {
		assertEquals(1.5, EUR.getRate(), 0);
		EUR.setRate(2.1);
		assertEquals(2.1, EUR.getRate(), 0);
	}
	
	@Test
	public void testGlobalValue() {
		assertEquals(200, DKK.universalValue(1000), 0);
	}
	
	@Test
	public void testValueInThisCurrency() {
		assertEquals(400, SEK.valueInThisCurrency(40, EUR), 0);
	}

}
