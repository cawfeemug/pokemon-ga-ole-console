import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.ArrayList;
import java.util.Collections;

public class Battle {
	private int battleScore, battleTurn;
	private ArrayList<Pokemon> playerPokemons, wildPokemons, caughtPokemons;
	
	// Constructor
	public Battle(ArrayList<Pokemon> playerPokemons, ArrayList<Pokemon> wildPokemons) {
		this.playerPokemons = playerPokemons;
		this.wildPokemons = wildPokemons;
		caughtPokemons = new ArrayList<>();
		battleTurn = 1;
	}

	// Getters
    public int getBattleScore() {
		return battleScore;
	}

	public ArrayList<Pokemon> getCaughtPokemons() {
		return caughtPokemons;
	}


	// Repeats a turn until the battle ends 
	public void loopBattle(Scanner input) {
		ArrayList<Pokemon> turnOrder = setTurnOrder();

        do {
			System.out.println(String.format("\n=== Turn %d ===", battleTurn));
			
			// Loops through each pokemon in battle
			for (Pokemon attackingPokemon : turnOrder) {
				if (attackingPokemon.isFainted()) {
					continue;
				}

				// Process pokemon attack
                processTurn(attackingPokemon);
				
				// Exit loop if either side is defeated
                if (isOver()) {
                    break;
                }
				
				System.out.print("ENTER to continue...");
				input.nextLine();
            }

			// Increase battleTurn for every turn
			battleTurn++;
        } while (isOver() == false);
		
		endBattle(input);
    }

	// Checks if all wild pokemons have fainted
	public boolean isVictory() {
		for (Pokemon pokemon : wildPokemons) {
			if (pokemon.isFainted() == false) {
				return false;
			}
		}

		return true;
	}

	// Determines the order of Pokemons in battle
	private ArrayList<Pokemon> setTurnOrder() {
		ArrayList<Pokemon> turnOrder = new ArrayList<>();
		
		for (int i = 0; i < playerPokemons.size(); i++) {
			turnOrder.add(playerPokemons.get(i));
		}
		
		for (int i = 0; i < wildPokemons.size(); i++) {
			turnOrder.add(wildPokemons.get(i));
		}
		
		turnOrder.sort((p1,p2) -> Integer.compare(p2.getSpeed(),p1.getSpeed()));
		
		return turnOrder;
	}

	// Processes the actions in a turn
	private void processTurn(Pokemon attackingPokemon) {
		// If pokemon is wild
		if (attackingPokemon.isWildPokemon()) {
			System.out.println(String.format("\n\n\n=== OPPONENT ===\n%s used %s!\n", attackingPokemon.getPokemonName(), attackingPokemon.getMoveName()));

			// Check if they dodged the attack
			if (dodgeAttack() == false) {
				attackingPokemon.attack(playerPokemons);
			}
		} else {
			System.out.println(String.format("\n\n\n=== PLAYER ===\n%s used %s!\n", attackingPokemon.getPokemonName(), attackingPokemon.getMoveName()));
			attackingPokemon.attack(wildPokemons);
		}
	}

	// Handles the aftermath of a battle
	private void endBattle(Scanner input) {
		// If player won, attempt catch and print score
		if (isVictory()) {
			System.out.println("\n=== BATTLE WON ===\nAll the wild pokemons were defeated!\n");

			System.out.println("""
				=====================
				    CATCH CHANCE!
				=====================
				""");
            Pokeball pokeball = choosePokeball(input);

			System.out.println(String.format("%s was thrown!", pokeball));
            boolean[] catchSuccess = pokeball.catchPokemons(wildPokemons);

            for (int i = 0; i < catchSuccess.length; i++) {
				Pokemon wildPokemon = wildPokemons.get(i);

                if (catchSuccess[i]) {
					System.out.println(String.format("%s was successfully caught! ", wildPokemon.getPokemonName()));
                    caughtPokemons.add(wildPokemon);
                } else {
					System.out.println(String.format("Oh no! %s managed to escape...", wildPokemon.getPokemonName()));
				}
            }

			calculateScore(catchSuccess);
        } else {
			System.out.println("\n=== BATTLE LOST ===\nAll your pokemons have fainted...");
		}
	}
	
	// Calculates battle score
	private int calculateScore(boolean[] caughtSuccess) {
		battleScore = 10000 - ((battleTurn-1) * 75);

		if (battleScore <= 500) {
			battleScore = 500;
		}

		for (boolean success : caughtSuccess) {
			if (success) {
				battleScore += 1000;
			} 
		}
		
		return battleScore;
	}
	
	// Checks if all pokemons on either side have fainted
	private boolean isOver() {
		for (Pokemon pPokemon : playerPokemons) {
			if (pPokemon.isFainted() == false) {
				//If a player pokemon has not fainted, check wild pokemons instead. 
				for (Pokemon wPokemon : wildPokemons) {
					//If a wild pokemon has not fainted, return false.
					if (wPokemon.isFainted() == false) {
						return false;
					}
				}

				//If all wild pokemons fainted, return true.
				return true;
			}
		}

		//If all player pokemons fainted, return true.
		return true;
	}
	
	// Quick-time event for player pokemons to dodge attacks
	private boolean dodgeAttack() {
    	Random random = new Random();
		String[] direction = {"l", "r"};
    	String dodgeDirection = direction[random.nextInt(2)];
    	String choice;
    		
		System.out.print("Choose left or right to dodge ('l' or 'r'): ");
    	choice = getUserInputWithTimeout(5);
		System.out.println();

		if (choice.equals(dodgeDirection)) {
			for (int i = 0; i < playerPokemons.size(); i++) {
				if (playerPokemons.get(i).getCurrentHealth() != 0) {
					System.out.println(String.format("%s successfully dodged. ", playerPokemons.get(i).getPokemonName()));
				}
			}

			return true;
		} else if (choice.equals("l") || choice.equals("r")) {
			System.out.println("Ouch, wrong way. ");

			return false;
		} else {
			System.out.println("Invalid input, failed to dodge.");

			return false;
		}
	}
	
	// Recieves input within a time limit
	private static String getUserInputWithTimeout(int timeoutSeconds) {
		Scanner input = new Scanner(System.in);
	    ExecutorService executor = Executors.newSingleThreadExecutor();

	    Future<String> future = executor.submit(() -> {
	        return input.nextLine();
	    });

	    try {
	        return future.get(timeoutSeconds, TimeUnit.SECONDS);
	    } catch (TimeoutException e) {
	        System.out.println("\nTime's up! You didn't respond in time.");
	        future.cancel(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        executor.shutdownNow();
	    }

		//Return empty string if user did not respond in time
	    return ""; 
	}

	// Generates a pokeball 
	private Pokeball choosePokeball(Scanner input) {
		int choice;
        ArrayList<Pokeball> balls = new ArrayList<>();
		balls.add(new NormalBall());
		balls.add(new GreatBall());
		balls.add(new UltraBall());
		balls.add(new MasterBall());

		Collections.shuffle(balls);

		while (true) {
			try {
				System.out.print("Enter a number to choose a Pokeball: ");
				choice = Integer.parseInt(input.nextLine());
				break;
			} catch (NumberFormatException e) {
                System.out.println("Invalid value! Please enter a number. ");
            }
		}

		return balls.get(Math.max(0, Math.min(choice - 1, balls.size() - 1)));
    }
}
