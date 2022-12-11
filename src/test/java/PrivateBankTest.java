import bank.*;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionDoesNotExistException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testet die Klasse {@link PrivateBank}
 * @author Tobias Schnuerpel
 * @version 4.0
 */
public class PrivateBankTest {

    private static final String DIRECTORY = "src/test/resources/privatebanktest";
    private PrivateBank bank;

    @BeforeEach
    public void init() {
        //System.out.println(Paths.get(DIRECTORY).toAbsolutePath());
        try {
            bank = new PrivateBank("Testbank", 0.1, 0.1, DIRECTORY);

            bank.createAccount("Account1");
            bank.addTransaction("Account1", new Payment("01.01.2020", 100, "AC1 - Payment1", 1, 1));
            bank.addTransaction("Account1", new IncomingTransfer("02.01.2020", 100, "AC1 - IncomingTransfer1", "abc", "xyz"));
            bank.addTransaction("Account1", new OutgoingTransfer("03.01.2020", 100, "AC1 - OutgoingTransfer1"));

            bank.createAccount("Account2");
            bank.addTransaction("Account2", new Payment("01.01.2020", 100, "AC2 - Payment2"));
            bank.addTransaction("Account2", new Payment("01.01.2020", 100, "AC2 - Payment3"));

            bank.createAccount("Account3");
            bank.addTransaction("Account3", new Payment("01.01.2020", 100, "AC3 - Payment4"));
            bank.addTransaction("Account3", new OutgoingTransfer("01.01.2020", 100, "AC3 - OutgoingTransfer2"));

        } catch (AccountAlreadyExistsException e) {
            System.out.println("Account already exists");
        } catch (Exception e) {
            fail("Konstruktor wirft Exception!", e);
        }
    }

    @AfterEach
    public void removeFiles() {
        Path pathToBeDeleted = Paths.get(DIRECTORY);
        try {
            Files.walk(pathToBeDeleted)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception e) {
            fail("Konnte Dateien nicht löschen!", e);
        }
        bank = null;
    }

    @Test
    public void testConstructor() {
        assertEquals("Testbank", bank.getName());
        assertEquals(0.1, bank.getIncomingInterest());
        assertEquals(0.1, bank.getOutgoingInterest());
        assertEquals(DIRECTORY, bank.getDirectoryName());
    }

    @Test
    public void testCopyConstructor() {
        PrivateBank bank2;
        try {
            bank2 = new PrivateBank(bank);
        } catch(Exception e) {
            fail("Copy-Konstruktor wirft Exception!", e);
            return;
        }
        assertEquals(bank, bank2);
        bank2.setName("Testbank 2");
        assertNotEquals(bank, bank2);
    }

    @Test
    public void testEquals() {
        PrivateBank bank2;
        try {
            bank2 = new PrivateBank("Testbank", 0.1, 0.1, DIRECTORY);
        } catch(Exception e) {
            fail("Konstruktor wirft Exception!", e);
            return;
        }
        assertEquals(bank, bank2);
        bank2.setName("Testbank 2");
        assertNotEquals(bank, bank2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"newAccount1", "newAccount2", "newAccount3"})
    public void testCreateAccount(String accountName) {
        assertDoesNotThrow(() -> bank.createAccount(accountName));
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount(accountName));
        // check if account exist on file system
        assertTrue(Files.exists(Paths.get(DIRECTORY, accountName + ".json")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"newAccount1", "newAccount2", "newAccount3"})
    public void testDeleteAccount(String accountName) {
        assertDoesNotThrow(() -> bank.createAccount(accountName));
        assertDoesNotThrow(() -> bank.deleteAccount(accountName));
        assertThrows(AccountDoesNotExistException.class, () -> bank.deleteAccount(accountName));
        // check if account exist on file system
        assertFalse(Files.exists(Paths.get(DIRECTORY, accountName + ".json")));
    }

    @Test
    public void testAddTransaction() {
        assertDoesNotThrow(() -> bank.addTransaction("Account1", new Payment("01.01.2020", 100, "AC1 - Payment5")));
        assertThrows(TransactionAlreadyExistException.class, () -> bank.addTransaction("Account1", new Payment("01.01.2020", 100, "AC1 - Payment5")));
        // check if account still exists on file system
        assertTrue(Files.exists(Paths.get(DIRECTORY, "Account1.json")));
        assertThrows(AccountDoesNotExistException.class, () -> bank.addTransaction("ExistiertNicht", new Payment("01.01.2020", 100, "ExistiertNicht - Payment6")));
    }

    @Test
    public void testRemoveTransaction() {
        assertEquals(3, bank.getTransactions("Account1").size());
        Payment p = (Payment) bank.getTransactions("Account1").get(0);
        assertDoesNotThrow(() -> bank.removeTransaction("Account1", p));
        assertEquals(2, bank.getTransactions("Account1").size());

        assertThrows(TransactionDoesNotExistException.class, () -> bank.removeTransaction("Account1", new Payment("12.12.2012", 123, "ExistiertNicht")));
        assertThrows(AccountDoesNotExistException.class, () -> bank.removeTransaction("ExistiertNicht", p));

        // check if account still exists on file system
        assertTrue(Files.exists(Paths.get(DIRECTORY, "Account1.json")));
    }

    @Test
    public void testContainsTransaction() {
        List<Transaction> transactions = bank.getTransactions("Account1");
        for (Transaction t : transactions) {
            assertTrue(bank.containsTransaction("Account1", t));
        }
        assertFalse(bank.containsTransaction("Account1", new Payment("12.12.2012", 123, "ExistiertNicht")));
        assertFalse(bank.containsTransaction("Unbekannt", transactions.get(0)));
    }

    @Test
    public void testGetAllAccounts() {
        List<String> accounts = bank.getAllAccounts();
        assertEquals(3, accounts.size());
        assertTrue(accounts.contains("Account1"));
        assertTrue(accounts.contains("Account2"));
        assertTrue(accounts.contains("Account3"));
    }

    @Test
    public void testGetAccountBalance() {
        assertEquals(90, bank.getAccountBalance("Account1"));
        assertEquals(180, bank.getAccountBalance("Account2"));
        assertEquals(-10, bank.getAccountBalance("Account3"));
        assertEquals(0, bank.getAccountBalance("Unbekannt"));
    }

    @Test
    public void testGetTransactions() {
        assertEquals(3, bank.getTransactions("Account1").size());
        assertEquals(2, bank.getTransactions("Account2").size());
        assertEquals(2, bank.getTransactions("Account3").size());
        assertEquals(0, bank.getTransactions("Unbekannt").size());
    }

    @Test
    public void testGetTransactionsSorted() {
        List<Transaction> transactions = bank.getTransactionsSorted("Account1", true);
        assertEquals(3, transactions.size());
        assertEquals("AC1 - OutgoingTransfer1", transactions.get(0).getDescription());
        assertEquals("AC1 - Payment1", transactions.get(1).getDescription());
        assertEquals("AC1 - IncomingTransfer1", transactions.get(2).getDescription());


        transactions = bank.getTransactionsSorted("Account2", false);
        assertEquals(2, transactions.size());
        assertEquals("AC2 - Payment2", transactions.get(0).getDescription());
        assertEquals("AC2 - Payment3", transactions.get(1).getDescription());

        transactions = bank.getTransactionsSorted("Account3", true);
        assertEquals(2, transactions.size());
        assertEquals("AC3 - OutgoingTransfer2", transactions.get(0).getDescription());
        assertEquals("AC3 - Payment4", transactions.get(1).getDescription());

        assertEquals(0, bank.getTransactionsSorted("Unbekannt", true).size());
    }

    @Test
    public void testGetTransactionsByType() {
        List<Transaction> transactions = bank.getTransactionsByType("Account1", true);
        assertEquals(2, transactions.size());
        assertEquals("AC1 - Payment1", transactions.get(0).getDescription());
        assertEquals("AC1 - IncomingTransfer1", transactions.get(1).getDescription());

        transactions = bank.getTransactionsByType("Account1", false);
        assertEquals(1, transactions.size());
        assertEquals("AC1 - OutgoingTransfer1", transactions.get(0).getDescription());

        transactions = bank.getTransactionsByType("Account2", true);
        assertEquals(2, transactions.size());
        assertEquals("AC2 - Payment2", transactions.get(0).getDescription());
        assertEquals("AC2 - Payment3", transactions.get(1).getDescription());

        assertEquals(0, bank.getTransactionsByType("Unbekannt", true).size());
    }

    @Test
    public void testToString() {
        String expected = "PrivateBank{name='Testbank', incomingInterest=0.1, outgoingInterest=0.1, directoryName='" + DIRECTORY + "'}";
        assertEquals(expected, bank.toString());
    }
}
