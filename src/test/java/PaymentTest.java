import bank.Payment;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testet die Klasse {@link Payment}
 * @author Tobias Schnuerpel
 * @version 4.0
 */
public class PaymentTest {

    private Payment p1;
    private Payment p2;

    @BeforeEach
    public void init() {
        p1 = new Payment("01.01.2020", 100, "Test", 0.05, 0.1);
        p2 = new Payment("01.01.2020", -100, "Test", 0.05, 0.1);
    }

    @Test
    public void testConstructor() {
        assertEquals("01.01.2020", p1.getDate());
        assertEquals(100, p1.getAmount());
        assertEquals("Test", p1.getDescription());
        assertEquals(0.05, p1.getIncomingInterest());
        assertEquals(0.1, p1.getOutgoingInterest());
        assertEquals("01.01.2020", p2.getDate());
        assertEquals(-100, p2.getAmount());
        assertEquals("Test", p2.getDescription());
        assertEquals(0.05, p2.getIncomingInterest());
        assertEquals(0.1, p2.getOutgoingInterest());
    }

    @Test
    public void testCopyConstructor() {
        Payment p3 = new Payment(p1);
        assertEquals(p1, p3);
        p3.setAmount(42);
        assertNotEquals(p1, p3);
    }

    @Test
    public void testEquals() {
        assertNotEquals(p1, p2);
        p1.setAmount(p2.getAmount());
        assertEquals(p1, p2);
    }

    @Test
    public void testCalculate() {
        assertEquals(95, p1.calculate(), 0.001);
        assertEquals(-110, p2.calculate(), 0.001);
    }

    @Test
    public void testToString() {
        String expected =
                "Payment{date='01.01.2020', amount=95.0, description='Test', incomingInterest=0.05, outgoingInterest=0.1}";
        assertEquals(expected, p1.toString());
    }

}
