import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Transfer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testet die Klasse {@link bank.Transfer}
 * @author Tobias Schnuerpel
 * @version 4.0
 */
public class TransferTest {

    Transfer t1;

    @BeforeEach
    public void init() {
        t1 = new Transfer("01.01.2020", 100, "Test", "Sender", "Recipient");
    }

    @Test
    public void testConstructor() {
        assertEquals("01.01.2020", t1.getDate());
        assertEquals(100, t1.getAmount());
        assertEquals("Test", t1.getDescription());
        assertEquals("Sender", t1.getSender());
        assertEquals("Recipient", t1.getRecipient());
    }

    @Test
    public void testCopyConstructor() {
        Transfer t2 = new Transfer(t1);
        assertEquals(t1, t2);
        t2.setAmount(42);
        assertNotEquals(t1, t2);
    }

    @Test
    public void testEquals() {
        Transfer t2 = new Transfer("01.01.2020", 100, "Test", "Sender", "Recipient");
        assertEquals(t1, t2);
        t2.setAmount(42);
        assertNotEquals(t1, t2);
    }

    @Test
    public void testCalculate() {
        IncomingTransfer incoming = new IncomingTransfer(t1);
        OutgoingTransfer outgoing = new OutgoingTransfer(t1);
        assertEquals(100, incoming.calculate(), 0.001);
        assertEquals(-100, outgoing.calculate(), 0.001);
    }

    @Test
    public void testToString() {
        String expected =
                "Transfer{date='01.01.2020', amount=100.0, description='Test', sender='Sender', recipient='Recipient'}";
        assertEquals(expected, t1.toString());
    }

}
