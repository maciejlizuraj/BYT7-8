package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
    Currency SEK, DKK, NOK, EUR;
    Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;

    @Before
    public void setUp() throws Exception {
        SEK = new Currency("SEK", 0.15);
        DKK = new Currency("DKK", 0.20);
        EUR = new Currency("EUR", 1.5);
        SEK100 = new Money(10000, SEK);
        EUR10 = new Money(1000, EUR);
        SEK200 = new Money(20000, SEK);
        EUR20 = new Money(2000, EUR);
        SEK0 = new Money(0, SEK);
        EUR0 = new Money(0, EUR);
        SEKn100 = new Money(-10000, SEK);
    }

    @Test
    public void testGetAmount() {
        assertEquals(1000, EUR10.getAmount(), 0);
    }

    @Test
    public void testGetCurrency() {
        assertEquals(SEK, SEK100.getCurrency());
    }

    @Test
    public void testToString() {
        assertEquals("2000 EUR", EUR20.toString());
    }

    @Test
    public void testGlobalValue() {
        assertEquals(3000, EUR20.universalValue(), 0);
    }

    @Test
    public void testEqualsMoney() {
        assertTrue(SEK100.equals(EUR10));
        assertFalse(SEKn100.equals(EUR0));
        assertFalse(SEKn100.equals(SEK100));
    }

    @Test
    public void testAdd() {
        assertEquals(SEK100.universalValue(), SEK100.add(EUR0).universalValue());
        assertEquals(3000, EUR10.add(SEK100).universalValue(), 0);
        assertEquals(EUR0.universalValue(), SEKn100.add(SEK100).universalValue());
    }

    @Test
    public void testSub() {
        assertEquals(SEK100.universalValue(), SEK200.sub(EUR10).universalValue());
        assertEquals(SEKn100.universalValue(), EUR0.sub(SEK100).universalValue());
        assertEquals(EUR20.universalValue(), SEK100.sub(SEKn100).universalValue());
    }

    @Test
    public void testIsZero() {
        assertTrue(SEK0.isZero());
        assertTrue(EUR0.isZero());
        assertFalse(EUR10.isZero());
        assertFalse(SEKn100.isZero());
    }

    @Test
    public void testNegate() {
        assertEquals(0,EUR0.negate().universalValue(),0);
        assertEquals(SEKn100.universalValue(), SEK100.negate().universalValue());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, SEK0.compareTo(EUR0));
        assertEquals(3000, EUR20.compareTo(EUR0));
        assertEquals(-4500, SEKn100.compareTo(EUR20));
    }
}
