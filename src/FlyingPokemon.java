import java.util.ArrayList;

public class FlyingPokemon extends Pokemon {

    public FlyingPokemon() {
        ArrayList<String> doubleTo = new ArrayList<>();
        doubleTo.add("Grass");
        doubleTo.add("Fighting");
        doubleTo.add("Bug");

        ArrayList<String> halfTo = new ArrayList<>();
        halfTo.add("Electric");
        halfTo.add("Rock");
        halfTo.add("Steel");

        ArrayList<String> noTo = new ArrayList<>();

        setDoubleDamageTo(doubleTo);
        setHalfDamageTo(halfTo);
        setNoDamageTo(noTo);
    }

    public FlyingPokemon(FlyingPokemon pokemon) {
        super(pokemon);
    }

    @Override
    public FlyingPokemon copy() {
        return new FlyingPokemon(this);
    }

    @Override
    public String toString() {
        return "[Flying Pokemon]\n" + super.toString();
    }
}
