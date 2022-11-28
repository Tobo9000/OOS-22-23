package bank;

/**
 * Interface CalculateBill. Wird von den Klassen
 * {@link Payment} und {@link Transfer} implementiert.
 * @author Tobias Schnuerpel
 * @version 2.0
 */
public interface CalculateBill {

    /**
     * Falls Zinsen anfallen, werden diese vom Betrag abgezogen bzw. addiert.
     * @return Betrag nach Abzug bzw. Addition der Zinsen
     */
    double calculate();

}
