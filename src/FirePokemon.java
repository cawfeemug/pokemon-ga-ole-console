import java.util.ArrayList;

public class FirePokemon extends Pokemon {

    public FirePokemon() {
        ArrayList<String> doubleTo = new ArrayList<>();
        doubleTo.add("Grass");
        doubleTo.add("Ice");
        doubleTo.add("Bug");
        doubleTo.add("Steel");

        ArrayList<String> halfTo = new ArrayList<>();
        halfTo.add("Fire");
        halfTo.add("Water");
        halfTo.add("Rock");
        halfTo.add("Dragon");

        ArrayList<String> noTo = new ArrayList<>();

        setDoubleDamageTo(doubleTo);
        setHalfDamageTo(halfTo);
        setNoDamageTo(noTo);
    }

    public FirePokemon(FirePokemon pokemon) {
        super(pokemon);
    }

    @Override
    public FirePokemon copy() {
        return new FirePokemon(this);
    }

    @Override
    public String toString() {
        return "[Fire Pokemon]\n" + super.toString();
    }
}
