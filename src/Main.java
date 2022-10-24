import bank.Payment;
import bank.Transfer;

/**
 * Die Main-Klasse dient als Einstiegspunkt in das Programm.
 * @author Tobias Schnuerpel
 * @version 1.0
 */
public class Main {
    /**
     * Startet das Programm. Testet die Klassen {@link bank.Payment} und {@link bank.Transfer}
     * durch Konsolenausgaben.
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args) {
        System.out.println("Banksystem gestartet!");

        // Teste Klasse Payment
        Payment payment1 = new Payment("01.01.2018", 100.0, "Einzahlung");
        payment1.printObject();
        Payment payment2 = new Payment("04.10.2022", -50.0, "Auszahlung", 0.01, 0.02);
        payment2.printObject();
        Payment payment3 = new Payment(payment2);
        payment3.printObject();

        // Teste Klasse Transfer
        Transfer transfer1 = new Transfer("01.01.2042", 100.0, "Ueberweisung1");
        transfer1.printObject();
        Transfer transfer2 = new Transfer("12.12.2012", 42.0, "Ueberweisung2", "DE1234567890", "DE0987654321");
        transfer2.printObject();

        Transfer transfer3 = new Transfer(transfer2);
        transfer3.setAmount(55);
        transfer2.printObject();
        transfer3.printObject();

    }
}