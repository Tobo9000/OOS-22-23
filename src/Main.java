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
        System.out.println("Banksystem gestartet!\n");

        System.out.println("Teste Klasse Payment:");

        // Teste Klasse Payment
        Payment payment1 = new Payment("01.01.2018", 100.0, "Einzahlung");
        System.out.println(payment1.toString());
        Payment payment2 = new Payment("04.10.2022", -50.0, "Auszahlung", 0.01, 0.02);
        System.out.println(payment2.toString());
        Payment payment3 = new Payment(payment2);
        System.out.println(payment3.toString());

        double res = payment1.calculate();
        System.out.println("erwartet = 100.0 / tatsächlich = " + res);
        res = payment2.calculate();
        System.out.println("erwartet = -51.0 / tatsächlich = " + res);

        try {
            payment1.setIncomingInterest(42.0);
        } catch (IllegalArgumentException e) {
            System.out.println("erwartet = IllegalArgumentException / tatsächlich = " + e);
        }

        System.out.println("erwartet = false / Gleichheit payment1 und payment2: " + payment1.equals(payment2));
        System.out.println("erwartet = true / Gleichheit payment2 und payment3: " + payment2.equals(payment3));

        System.out.println("\nTeste Klasse Payment:");

        // Teste Klasse Transfer
        Transfer transfer1 = new Transfer("01.01.2042", 100.0, "Ueberweisung1");
        System.out.println(transfer1.toString());
        Transfer transfer2 = new Transfer("12.12.2012", 42.0, "Ueberweisung2", "DE1234567890", "DE0987654321");
        System.out.println(transfer2.toString());
        Transfer transfer3 = new Transfer(transfer2);
        System.out.println(transfer3.toString());

        res = transfer1.calculate();
        System.out.println("erwartet = 100.0 / tatsächlich = " + res);
        res = transfer2.calculate();
        System.out.println("erwartet = 42.0 / tatsächlich = " + res);

        try {
            transfer1.setAmount(-42.0);
        } catch (IllegalArgumentException e) {
            System.out.println("erwartet = IllegalArgumentException / tatsächlich = " + e);
        }

        System.out.println("erwartet = false / Gleichheit transfer1 und transfer2: " + transfer1.equals(transfer2));
        System.out.println("erwartet = true / Gleichheit transfer2 und transfer3: " + transfer2.equals(transfer3));
    }
}