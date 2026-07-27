import java.util.ArrayList;

public class Pokeball {
    private String pokeballName;
    private double catchRate;

    // Constructor
    public Pokeball(String ballName, double cRate) {
        this.pokeballName = ballName;
        this.catchRate = cRate;
    }

    /**
     * Attempts to catch each Pokemon in the list.
     * Formula:
     * catchValue = (pokemonGrade * 0.1) + (catchRate * 0.5) + optional turn bonus
     * RNG is between 0.0 and 1.0, success if RNG <= catchValue
     *
     * @param pokemons ArrayList of Pokemon
     * @return boolean[] array indicating caught(true) or escaped(false)
     */
    public boolean[] catchPokemons(ArrayList<Pokemon> pokemons) {
        boolean[] results = new boolean[pokemons.size()];

        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon p = pokemons.get(i);
            int catchRange = (int) (Math.pow((5 - p.getGrade()), 2.0) * 5 + 5); // grade impact

            // Optional: add turn bonus if Pokemon has battle info
            /*if (p.hasBattleInfo()) {
                double turnBonus = p.getRemainingTurns() / (double) p.getMaxTurns();
                catchValue += turnBonus * 0.2; // weight of turn bonus
            }*/

            int randomRange = (int) (50 * (8 - p.getGrade()) * (1 - catchRate)) + 1;
            int rng = (int) ((Math.random() * randomRange) + 1);
            results[i] = rng <= catchRange;
        }

        return results;
    }

    @Override
    public String toString() {
        return pokeballName + " (Catch Rate: " + (catchRate * 100) + "%)";
    }
}
