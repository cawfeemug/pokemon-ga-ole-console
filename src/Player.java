import java.util.ArrayList;

public class Player {
	private String playerName;
	private ArrayList<Pokemon> playerPokemons = new ArrayList<>();
	
	// Constructors 
	public Player(String playerName, ArrayList<Pokemon> playerPokemons) {
		this.playerName = playerName;
		this.playerPokemons = playerPokemons;
	}

	public Player(String playerName) {
		this.playerName = playerName;
	}
	
	// Getters
	public String getPlayerName() {
		return playerName;
	}
	
	public ArrayList<Pokemon> getPlayerPokemons() {
		return playerPokemons;
	}


	// Adds a pokemon to playerPokemons
	public void addPokemon(Pokemon pokemon) {
		pokemon.setOwner(playerName);
		this.playerPokemons.add(pokemon);
	}
	
	// Adds an ArrayList of Pokemons to playerPokemons
	public void addPokemon(ArrayList<Pokemon> pokemons) {
		for (Pokemon pokemon : pokemons) {
			pokemon.setOwner(playerName);
			this.playerPokemons.add(pokemon);
		}
	}

	// Returns selected pokemon from playerPokemons, if it exists
	public Pokemon selectPokemon(int index) {
		if (index < 0 || index >= playerPokemons.size()) {
			return null;
		} else {
			return playerPokemons.get(index);
		}
	}
	
	// Releases selected pokemon from playerPokemons
	public void releasePokemon(int index) {
		if (playerPokemons.size() < 3) {
			System.out.println("You need at least 2 pokemons to participate in a battle!");
		} else if (selectPokemon(index) == null) {
			System.out.println("Invalid index selected. No Pokemon was released.");
		} else {
			Pokemon releasedPokemon = playerPokemons.remove(index);
			System.out.println(releasedPokemon.getPokemonName() + " has been released.");
		}
	}

	// Displays a list of pokemons from playerPokemons
	public void displayPlayerPokemons() {
		if (playerPokemons.isEmpty()) {
			System.out.println(playerName + " has not caught any pokemons yet!");
		} else {
			System.out.println("\n=== " + playerName + "'s Caught Pokemons ===");
			for (int i = 0; i < playerPokemons.size(); i++) {
				System.out.println((i+1) + ". " + selectPokemon(i).getPokemonName());
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Player name: " + playerName);
		
		if (playerPokemons.isEmpty()) {
			sb.append(". No pokemons caught");	
		} else {
			for (int i = 0; i < playerPokemons.size(); i++) {
				sb.append(playerPokemons.get(i).getPokemonName()); 

				if (i < playerPokemons.size() - 1) {
					sb.append(", ");			
				}
			}
		}

		return sb.toString();
	}
}