import java.util.ArrayList;

public class GrassPokemon extends Pokemon {

    public GrassPokemon() {
        ArrayList<String> doubleTo = new ArrayList<>();
        doubleTo.add("Water");
        doubleTo.add("Ground");
        doubleTo.add("Rock");

        ArrayList<String> halfTo = new ArrayList<>();
        halfTo.add("Fire");
        halfTo.add("Grass");
        halfTo.add("Poison");
        halfTo.add("Flying");
        halfTo.add("Bug");
        halfTo.add("Dragon");
        halfTo.add("Steel");

        ArrayList<String> noTo = new ArrayList<>();

        setDoubleDamageTo(doubleTo);
        setHalfDamageTo(halfTo);
        setNoDamageTo(noTo);
    }

    public GrassPokemon(GrassPokemon pokemon) {
        super(pokemon);
    }

    @Override
    public GrassPokemon copy() {
        return new GrassPokemon(this);
    }

    @Override
    public String toString() {
        return "[Grass Pokemon]\n" + super.toString();
    }
}
