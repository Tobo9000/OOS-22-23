package bank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serialisiert und Deserialisiert {@link Transaction} Objekte
 * und speichert diese am angegebenen Pfad.
 * @author Tobias Schnuerpel
 * @version 4.0
 */
public final class BankFileHandler {

    /** Gson-Instanz (Singleton) */
    private static Gson myGson;

    private BankFileHandler() {
        // private constructor to prevent instantiation
    }

    /**
     * Gibt die Gson Instanz der Klasse zurück bzw. erstellt diese, fallse noch nicht vorhanden.
     * @return Gson Instanz
     */
    private static Gson getGson() {
        // basically a singleton
        if (myGson == null) {
            myGson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(Transaction.class, new TransactionAdapter())
                    .setPrettyPrinting()
                    .create();
        }
        return myGson;
    }

    /**
     * Serialisiert die übergebene Liste von {@link Transaction} Objekten und speichert diese
     * in der Datei [account].json unter dem angegebenen Pfad directory.
     * Gibt bei Erfolg true zurück, ansonsten false.
     * @param directory Pfad, unter dem die Datei gespeichert werden soll
     * @param account Name der Datei
     * @param transactions Liste von {@link Transaction} Objekten
     * @return true bei Erfolg, ansonsten false
     */
    public static boolean writeAccount(String directory, String account, List<Transaction> transactions) {
        String json = "";
        if (transactions != null && !transactions.isEmpty())
            json = getGson().toJson(transactions);
        String fileName = directory + "/" + account + ".json";
        Path path = Paths.get(fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, json);
        } catch(IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Deserialisiert alle Account-Dateien im angegebenen Verzeichnis und speichert diese in einer Map,
     * welche zurückgegeben wird.
     * @param directory Pfad, in dem die Dateien gespeichert sind
     * @return Map mit Accountnamen als Schlüssel und einer Liste von {@link Transaction} Objekten
     */
    public static Map<String, List<Transaction>> readAccounts(String directory) {
        Map<String, List<Transaction>> accounts = new HashMap<>();
        File[] files = new File(directory).listFiles();
        if (files == null) // no accounts stored
            return accounts;

        for (File file : files) {
            if (!file.isFile())
                continue;

            String accountName = file.getName().replace(".json", "");
            List<Transaction> transactions = new ArrayList<>();
            try {
                String json = Files.readString(file.toPath());
                transactions = getGson().fromJson(json, new TypeToken<List<Transaction>>() {}.getType());
            } catch (JsonIOException | JsonSyntaxException | IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
            accounts.putIfAbsent(accountName, transactions);
        }
        return accounts;
    }

}
