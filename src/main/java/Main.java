import bank.*;
import bank.exceptions.*;

import java.io.IOException;

/**
 * Die Main-Klasse dient als Einstiegspunkt in das Programm.
 * @author Tobias Schnuerpel
 * @version 4.0
 */
public class Main {

    /**
     * Startet das Programm. Testet das Bank-System durch Konsolenausgaben.
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args) {
        System.out.println("Banksystem gestartet!\n");
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Testmethode aus Praktikum 3.
     */
    public static void testP3() throws IOException {
        // Teste Methoden aus Interface Bank ------------------------------------
        System.out.println("Teste Klasse PrivateBank:\nKonstruktoren:");

        // Konstruktor
        PrivateBank bank = new PrivateBank("Privatbank", 0.1, 0.2, "src/main/resources/privatebank1/");
        System.out.println(bank);

        // Copy-Konstruktor (und equals)
        PrivateBank bank2 = new PrivateBank(bank);
        bank2.setDirectoryName("src/main/resources/privatebank2/");
        bank2.setName("Privatbank 2");
        System.out.println("Copy-Konstruktor funktioniert: " + (!bank2.equals(bank) ? "JA" : "NEIN"));
        bank2.setName("Privatbank");
        System.out.println("Equals funktioniert: " + (bank2.equals(bank) ? "JA" : "NEIN"));

        System.out.println("\nTeste Account erstellen:");
        try {
            bank.createAccount("Mustermann");
            bank.createAccount("Musterfrau");
            bank.createAccount("Mustermann");
        } catch (AccountAlreadyExistsException e) {
            System.out.println("ERFOLG: Account existiert bereits! (es können keine doppelten erstellt werden)");
        }
        System.out.println("erwartete Accounts = Mustermann, Musterfrau / tatsächliche Accounts = ");
        bank.printAccounts();

        System.out.println("\n\nTeste addTransaction:");
        Payment p1 = new Payment("Mustermann", 30, "Auszahlung");
        System.out.println("erwartet: Erfolg, Fehler / tatsächlich: ");
        try {
            bank.addTransaction("Mustermann", p1);
            bank.addTransaction("Mustermann", p1); // soll fehlschlagen
            bank.addTransaction("123", p1); // soll fehlschlagen
        } catch (TransactionAttributeException | TransactionAlreadyExistException | AccountDoesNotExistException e) {
            System.out.println("Fehler: " + e.getMessage());
        }

        System.out.println("\n\nTeste removeTransaction:");
        System.out.println("erwartet: Erfolg, Fehler / tatsächlich: ");
        try {
            bank.removeTransaction("Mustermann", p1);
            bank.removeTransaction("Mustermann", p1); // soll fehlschlagen
            bank.removeTransaction("123", p1); // soll fehlschlagen
        } catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
            System.out.println("Fehler: " + e.getMessage());
        }

        System.out.println("\n\nTeste containsTransaction:");
        try {
            bank.addTransaction("Mustermann", p1);
        } catch (TransactionAttributeException | TransactionAlreadyExistException | AccountDoesNotExistException e) {
            System.out.println("unerwarteter Fehler: " + e.getMessage());
        }
        System.out.println("erwartet: true, false / tatsächlich: " + bank.containsTransaction("Mustermann", p1) + ", " + bank.containsTransaction("Musterfrau", p1));

        System.out.println("\n\nTeste PrivateBank#getAccountBalance:");
        try {
            bank.createAccount("Anonym");
        } catch (AccountAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        Payment p3 = new Payment("17.11.2022", 30, "Einzahlung");
        Payment p4 = new Payment("17.11.2022", -20, "Auszahlung");
        IncomingTransfer t1 = new IncomingTransfer("17.11.2022", 50, "eingehende Überweisung", "Musterfrau", "Anonym");
        OutgoingTransfer t2 = new OutgoingTransfer("17.11.2022", 42, "ausgehende Überweisung", "Anonym", "Musterfrau");
        try {
            bank.addTransaction("Anonym", p3);
            bank.addTransaction("Anonym", p4);
            bank.addTransaction("Anonym", t1);
            bank.addTransaction("Anonym", t2);
        } catch (TransactionAttributeException | TransactionAlreadyExistException | AccountDoesNotExistException e) {
            System.out.println("unerwarteter Fehler: " + e.getMessage());
        }
        // erwartet = (30 * 0.9) - (20 * 1.2) + 50 - 42 = 11
        System.out.println("erwartet: 11 / tatsächlich: " + bank.getAccountBalance("Anonym"));


        System.out.println("\n\nTeste PrivateBankAlt#getAccountBalance:");
        PrivateBankAlt bankAlt = new PrivateBankAlt("Privatbank alternativ", 0.1, 0.2);
        try {
            bankAlt.createAccount("Anonym");
        } catch (AccountAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        Transfer t3 = new Transfer("17.11.2022", 50, "eingehende Überweisung", "Musterfrau", "Anonym");
        Transfer t4 = new Transfer("17.11.2022", 42, "ausgehende Überweisung", "Anonym", "Musterfrau");

        try {
            bankAlt.addTransaction("Anonym", p3);
            bankAlt.addTransaction("Anonym", p4);
            bankAlt.addTransaction("Anonym", t3);
            bankAlt.addTransaction("Anonym", t4);
        } catch (TransactionAttributeException | TransactionAlreadyExistException | AccountDoesNotExistException e) {
            System.out.println("unerwarteter Fehler: " + e.getMessage());
        }
        // erwartet = (30 * 0.9) - (20 * 1.2) + 50 - 42 = 11
        System.out.println("erwartet: 11 / tatsächlich: " + bankAlt.getAccountBalance("Anonym"));

        System.out.println("\n\nTeste getTransactions:");
        System.out.println("erwartet: 4 / tatsächlich: " + bank.getTransactions("Anonym").size());
        System.out.println(bank.getTransactions("Anonym"));

        System.out.println("\n\nTeste getTransactionsSorted (asc):");
        System.out.println(bank.getTransactionsSorted("Anonym", true));

        System.out.println("\n\nTeste getTransactionsSorted (desc):");
        System.out.println(bank.getTransactionsSorted("Anonym", false));

        System.out.println("\n\nTeste getTransactionsByType (positive):");
        System.out.println(bank.getTransactionsByType("Anonym", true));

        System.out.println("\n\nTeste getTransactionsByType (negative):");
        System.out.println(bank.getTransactionsByType("Anonym", false));

    }

    /**
     * Testmethode aus Praktikum 2.
     */
    public static void testP2() {
        // Teste Klasse Payment ------------------------------------------------
        System.out.println("Teste Klasse Payment:\nKonstruktoren:");

        // Konstruktor 1
        Payment payment1 = new Payment("01.01.2018", 100.0, "Einzahlung");
        System.out.println(payment1);
        // Konstruktor 2
        Payment payment2 = new Payment("04.10.2022", -50.0, "Auszahlung", 0.01, 0.02);
        System.out.println(payment2);
        // Copy-Konstruktor
        Payment payment3 = new Payment(payment2);
        payment2.setAmount(-40.0);
        System.out.println("Copy-Konstruktor funktioniert: " + (!payment3.equals(payment2) ? "JA" : "NEIN"));

        System.out.println("\nTeste Calculate-Methode:");
        double res = payment1.calculate();
        System.out.println("erwartet = 100.0 / tatsächlich = " + res);
        res = payment2.calculate();
        System.out.println("erwartet = -40.8 / tatsächlich = " + res);

        System.out.println("\nTeste Exceptions:");
        try {
            payment1.setIncomingInterest(42.0);
        } catch (IllegalArgumentException e) {
            System.out.println("erwartet = IllegalArgumentException / tatsächlich = " + e);
        }

        System.out.println("\nTeste equals-Methode:");
        payment2.setAmount(-50.0);
        System.out.println("erwartet = false / Gleichheit payment1 und payment2: " + payment1.equals(payment2));
        System.out.println("erwartet = true / Gleichheit payment2 und payment3: " + payment2.equals(payment3));


        // Teste Klasse Transfer ------------------------------------------------
        System.out.println("\n\nTeste Klasse Transfer:\nKonstruktoren:");

        // Konstruktor 1
        Transfer transfer1 = new Transfer("01.01.2042", 100.0, "Ueberweisung1");
        System.out.println(transfer1);
        // Konstruktor 2
        Transfer transfer2 = new Transfer("12.12.2012", 42.0, "Ueberweisung2", "DE1234567890", "DE0987654321");
        System.out.println(transfer2);
        // Copy-Konstruktor
        Transfer transfer3 = new Transfer(transfer2);
        transfer3.setAmount(41.5);
        System.out.println("Copy-Konstruktor funktioniert: " + (!transfer3.equals(transfer2) ? "JA" : "NEIN"));

        System.out.println("\nTeste Calculate-Methode:");
        res = transfer1.calculate();
        System.out.println("erwartet = 100.0 / tatsächlich = " + res);
        res = transfer2.calculate();
        System.out.println("erwartet = 42.0 / tatsächlich = " + res);

        System.out.println("\nTeste Exceptions:");
        try {
            transfer1.setAmount(-42.0);
        } catch (IllegalArgumentException e) {
            System.out.println("erwartet = IllegalArgumentException / tatsächlich = " + e);
        }

        System.out.println("\nTeste equals-Methode:");
        transfer3.setAmount(42.0);
        System.out.println("erwartet = false / Gleichheit transfer1 und transfer2: " + transfer1.equals(transfer2));
        System.out.println("erwartet = true / Gleichheit transfer2 und transfer3: " + transfer2.equals(transfer3));

        // Teste equals für beide Klassen
        System.out.println("erwartet = false / Gleichheit payment1 und transfer1: " + payment1.equals(transfer1));
    }
}