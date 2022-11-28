package bank;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Serialisiert und Deserialisiert {@link Transaction} Objekte.
 * Implementiert die Interfaces {@link JsonSerializer} und {@link JsonDeserializer}.
 * @author Tobias Schnuerpel
 * @version 4.0
 */
public class TransactionAdapter implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    /**
     * Gson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     *
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
     * non-trivial field of the {@code src} object. However, you should never invoke it on the
     * {@code src} object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).</p>
     *
     * @param src       the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context  Context for serialization
     * @return a JsonElement corresponding to the specified object.
     */
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        JsonObject instance = new JsonObject();

        // Serialize the Transaction object's type
        result.addProperty("CLASSNAME", src.getClass().getSimpleName());

        if (src instanceof Payment p) {
            instance.addProperty("incomingInterest", p.getIncomingInterest());
            instance.addProperty("outgoingInterest", p.getOutgoingInterest());
        } else if (src instanceof Transfer t) {
            instance.addProperty("sender", t.getSender());
            instance.addProperty("recipient", t.getRecipient());
        }

        // Serialize the Transaction object
        instance.addProperty("date", src.getDate());
        instance.addProperty("amount", src.getAmount());
        instance.addProperty("description", src.getDescription());

        result.add("INSTANCE", instance);
        return result;
    }

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context Context for deserialization that is passed to a custom deserializer during invocation
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("CLASSNAME").getAsString();
        JsonObject inner = obj.get("INSTANCE").getAsJsonObject();

        String date, description;
        double amount;
        date = inner.get("date").getAsString();
        amount = inner.get("amount").getAsDouble();
        description = inner.get("description").getAsString();

        if (type.equals("Payment")) {
            double incomingInterest, outgoingInterest;
            incomingInterest = inner.get("incomingInterest").getAsDouble();
            outgoingInterest = inner.get("outgoingInterest").getAsDouble();
            return new Payment(date, amount, description, incomingInterest, outgoingInterest);
        } else {
            String sender, recipient;
            sender = inner.get("sender").getAsString();
            recipient = inner.get("recipient").getAsString();
            return switch (type) {
                case "Transfer" -> new Transfer(date, amount, description, sender, recipient);
                case "IncomingTransfer" -> new IncomingTransfer(date, amount, description, sender, recipient);
                case "OutgoingTransfer" -> new OutgoingTransfer(date, amount, description, sender, recipient);
                default -> throw new JsonParseException("Unknown element type: " + type);
            };
        }
    }
}
