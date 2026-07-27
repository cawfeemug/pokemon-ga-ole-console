import java.util.ArrayList;

public class GroundPokemon extends Pokemon {

    public GroundPokemon() {
        ArrayList<String> doubleTo = new ArrayList<>();
        doubleTo.add("Fire");
        doubleTo.add("Electric");
        doubleTo.add("Poison");
        doubleTo.add("Rock");
        doubleTo.add("Steel");
        
        ArrayList<String> halfTo = new ArrayList<>();
        halfTo.add("Grass");
        halfTo.add("Bug");

        ArrayList<String> noTo = new ArrayList<>();
        noTo.add("Flying");

        setDoubleDamageTo(doubleTo);
        setHalfDamageTo(halfTo);
        setNoDamageTo(noTo);
    }
    
    public GroundPokemon(GroundPokemon pokemon) {
        super(pokemon);
    }

    @Override
    public GroundPokemon copy() {
        return new GroundPokemon(this);
    }

    @Override
    public String toString() {
        return "[Ground Pokemon]\n" + super.toString();
    }
}
