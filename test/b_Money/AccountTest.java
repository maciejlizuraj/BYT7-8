package b_Money;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {
    Currency SEK, DKK;
    Bank Nordea;
    Bank DanskeBank;
    Bank SweBank;
    Account testAccount;

    @Before
    public void setUp() throws Exception {
        SEK = new Currency("SEK", 0.15);
        SweBank = new Bank("SweBank", SEK);
        SweBank.openAccount("Alice");
        testAccount = new Account("Hans", SEK);
        testAccount.deposit(new Money(10000000, SEK));

        SweBank.deposit("Alice", new Money(1000000, SEK));
    }

    @Test
    public void testAddRemoveTimedPayment() throws TimedPaymentExistsException, TimedPaymentDoesNotExistException, InvalidValueException {
        testAccount.addTimedPayment("1", 2, 4, new Money(5000, SEK), SweBank, "Alice");
        assertTrue(testAccount.timedPaymentExists("1"));
        testAccount.removeTimedPayment("1");
        assertFalse(testAccount.timedPaymentExists("1"));
    }

    @Test(expected = TimedPaymentDoesNotExistException.class)
    public void testRemoveTimedPayment() throws TimedPaymentDoesNotExistException {
        testAccount.removeTimedPayment("1");
    }

    @Test(expected = TimedPaymentExistsException.class)
    public void testAddRemoveTimedPayment1() throws TimedPaymentExistsException, InvalidValueException {
        testAccount.addTimedPayment("1", 2, 4, new Money(5000, SEK), SweBank, "Alice");
        testAccount.addTimedPayment("1", 2, 4, new Money(5000, SEK), SweBank, "Alice");
    }

    //Test failed. Balance reduced in a tick where it shouldn't have.
    @Test
    public void testTimedPayment() throws AccountDoesNotExistException, TimedPaymentExistsException, InvalidValueException {
        testAccount.addTimedPayment("1", 1, 1, new Money(1000000, SEK), SweBank, "Alice");
        testAccount.tick();
        assertEquals(10000000, testAccount.getBalance().getAmount(), 0);
        testAccount.tick();
        assertEquals(9000000, testAccount.getBalance().getAmount(), 0);
        testAccount.tick();
        assertEquals(9000000, testAccount.getBalance().getAmount(), 0);
        testAccount.tick();
        assertEquals(8000000, testAccount.getBalance().getAmount(), 0);
    }

    @Test
    public void testDeposit() throws AccountDoesNotExistException, InvalidValueException {
        SweBank.deposit("Alice", new Money(1000000, SEK));
        assertEquals(2000000, SweBank.getBalance("Alice"), 0);
    }

    @Test(expected = InvalidValueException.class)
    public void testDeposit1() throws AccountDoesNotExistException, InvalidValueException {
        SweBank.deposit("Alice", new Money(-1000000, SEK));
        assertEquals(2000000, SweBank.getBalance("Alice"), 0);
    }

    //Test failed. Money was added, not substracted.
    @Test
    public void testWithdraw() throws AccountDoesNotExistException, InvalidValueException {
        SweBank.withdraw("Alice", new Money(1000000, SEK));
        assertEquals(0, SweBank.getBalance("Alice"), 0);

    }

    @Test
    public void testGetBalance() throws AccountDoesNotExistException {
        assertEquals(10000000, testAccount.getBalance().getAmount(), 0);
        assertEquals(1000000, SweBank.getBalance("Alice"), 0);
    }
}
