package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank",SweBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, Nordea.getCurrency());
	}

	//Test failed. Lack of AccountExistsException when creating the same account twice
	@Test(expected = AccountExistsException.class)
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		SweBank.openAccount("Bob");
	}

	//Test failed. Lack of AccountDoesNotExistException
	@Test(expected = AccountDoesNotExistException.class)
	public void testDeposit() throws AccountDoesNotExistException, InvalidValueException {
		SweBank.deposit("Fake name",new Money(400, SEK));
	}

	@Test()
	public void testDeposit1() throws AccountDoesNotExistException, InvalidValueException {
		//Test failed. AccountDoesNotExistException thrown.
		SweBank.deposit("Bob",new Money(400, SEK));
		assertEquals(400, SweBank.getBalance("Bob"),0);
	}

	@Test(expected = AccountDoesNotExistException.class)
	public void testWithdraw() throws AccountDoesNotExistException, InvalidValueException {
		Nordea.withdraw("Fake name",new Money(400, SEK));
	}

	@Test
	public void testWithdraw1() throws AccountDoesNotExistException, InvalidValueException {
		Nordea.withdraw("Bob",new Money(400, SEK));
		assertEquals(-400, Nordea.getBalance("Bob"),0);
	}

	@Test(expected = AccountDoesNotExistException.class)
	public void testGetBalance() throws AccountDoesNotExistException {
		assertEquals(10000, Nordea.getBalance("Fake name"),0);
	}

	@Test
	public void testGetBalance1() throws AccountDoesNotExistException, InvalidValueException {
		assertEquals(0, Nordea.getBalance("Bob"),0);
		Nordea.deposit("Bob", new Money(200, SEK));
		assertEquals(new Money(200, SEK).getAmount(), (Nordea.getBalance("Bob")));
	}

	//Account not in sender's bank.
	@Test(expected = AccountDoesNotExistException.class)
	public void testTransfer() throws AccountDoesNotExistException, InvalidValueException, InvalidReceiverException {
		Nordea.transfer("Fake name", Nordea, "Bob", new Money(200, SEK));
	}

	//Account not in receiver's bank.
	@Test(expected = AccountDoesNotExistException.class)
	public void testTransfer1() throws AccountDoesNotExistException, InvalidValueException, InvalidReceiverException {
		Nordea.transfer("Bob", Nordea, "Fake name", new Money(200, SEK));
	}

	//Test of transfer within one bank.
	@Test
	public void testTransfer2() throws AccountDoesNotExistException, InvalidValueException, InvalidReceiverException {
		SweBank.transfer("Bob","Ulrika", new Money(200, SEK));
	}

	//Test of transfer within one bank with a non-existent account
	@Test(expected = AccountDoesNotExistException.class)
	public void testTransfer3() throws AccountDoesNotExistException, InvalidValueException, InvalidReceiverException {
		SweBank.transfer("Fake name","Ulrika", new Money(200, SEK));
	}

	@Test(expected = InvalidReceiverException.class)
	public void testTransfer4() throws AccountDoesNotExistException, InvalidValueException, InvalidReceiverException {
		SweBank.transfer("Bob","Bob", new Money(200, SEK));
	}

	//Test failed. Lack of AccountDoesNotExistException
	@Test(expected = AccountDoesNotExistException.class)
	public void testTimedPayment() throws AccountDoesNotExistException, TimedPaymentExistsException, InvalidValueException {
		Nordea.addTimedPayment("Fake name", "1", 5, 6, new Money(200, SEK), Nordea, "Fake name again");
		fail("Write test case here");
	}

	@Test
	public void testTimedPayment1() throws AccountDoesNotExistException, TimedPaymentExistsException, InvalidValueException {
		Nordea.addTimedPayment("Bob", "1", 5, 6, new Money(200, SEK), SweBank, "Ulrika");
	}

	//Test failed. Could add a payment with an already existing id.
	@Test(expected = TimedPaymentExistsException.class)
	public void testTimedPayment2() throws AccountDoesNotExistException, TimedPaymentExistsException, InvalidValueException {
		Nordea.addTimedPayment("Bob", "1", 5, 6, new Money(200, SEK), SweBank, "Ulrika");
		Nordea.addTimedPayment("Bob", "1", 5, 6, new Money(200, SEK), SweBank, "Ulrika");
	}

}
