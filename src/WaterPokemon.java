import java.util.ArrayList;

public class WaterPokemon extends Pokemon {
    
    public WaterPokemon() {
        ArrayList<String> doubleTo = new ArrayList<>();
        doubleTo.add("Fire");
        doubleTo.add("Ground");
        doubleTo.add("Rock");

        ArrayList<String> halfTo = new ArrayList<>();
        halfTo.add("Water");
        halfTo.add("Grass");
        halfTo.add("Dragon");

        ArrayList<String> noTo = new ArrayList<>();

        setDoubleDamageTo(doubleTo);
        setHalfDamageTo(halfTo);
        setNoDamageTo(noTo);
    }

    public WaterPokemon(WaterPokemon pokemon) {
        super(pokemon);
    }

    @Override
    public WaterPokemon copy() {
        return new WaterPokemon(this);
    }

    @Override
    public String toString() {
        return "[Water Pokemon]\n" + super.toString();
    }
}
