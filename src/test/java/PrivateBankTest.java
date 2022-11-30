import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

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
        System.out.println(Paths.get(DIRECTORY).toAbsolutePath());
        try {
            bank = new PrivateBank("Testbank", 0.1, 0.1, DIRECTORY);
            bank.createAccount("Account1");
            bank.addTransaction("Account1", new Payment("01.01.2020", 100, "Payment1", 1, 1));
            bank.addTransaction("Account1", new IncomingTransfer("02.01.2020", 100, "IncomingTransfer1", "abc", "xyz"));
            bank.addTransaction("Account1", new OutgoingTransfer("03.01.2020", 100, "OutgoingTransfer1"));
            bank.createAccount("Account2");
            bank.addTransaction("Account2", new Payment("01.01.2020", 100, "Payment2"));
            bank.addTransaction("Account2", new Payment("01.01.2020", 100, "Payment3"));
            bank.createAccount("Account3");
            bank.addTransaction("Account3", new Payment("01.01.2020", 100, "Payment4"));
            bank.addTransaction("Account3", new OutgoingTransfer("01.01.2020", 100, "OutgoingTransfer2"));
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
    }

    @Test
    public void testAddTransaction() {
        assertDoesNotThrow(() -> bank.addTransaction("Account1", new Payment("01.01.2020", 100, "Payment5")));
        assertThrows(Exception.class, () -> bank.addTransaction("Account1", new Payment("01.01.2020", 100, "Payment5")));
    }

    @Test
    public void testRemoveTransaction() {
        //...
    }

    @Test
    public void testContainsTransaction() {
        //...
    }

    @Test
    public void testGetAccountBalance() {
        //...
    }

    @Test
    public void testGetTransactions() {
        //...
    }

    @Test
    public void testGetTransactionsSorted() {
        //...
    }

    @Test
    public void testGetTransactionsByType() {
        //...
    }

    @Test
    public void testToString() {
        //...
    }
}
