import java.util.ArrayList;

public class ElectricPokemon extends Pokemon {

    public ElectricPokemon() {
        ArrayList<String> doubleTo = new ArrayList<>();
        doubleTo.add("Water");
        doubleTo.add("Flying");

        ArrayList<String> halfTo = new ArrayList<>();
        halfTo.add("Electric");
        halfTo.add("Grass");
        halfTo.add("Dragon");

        ArrayList<String> noTo = new ArrayList<>();
        noTo.add("Ground");

        setDoubleDamageTo(doubleTo);
        setHalfDamageTo(halfTo);
        setNoDamageTo(noTo);
    }

    public ElectricPokemon(ElectricPokemon pokemon) {
        super(pokemon);
    }

    @Override
    public ElectricPokemon copy() {
        return new ElectricPokemon(this);
    }

    @Override
    public String toString() {
        return "[Electric Pokemon]\n" + super.toString();
    }
}
