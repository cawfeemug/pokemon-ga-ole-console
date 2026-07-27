import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private Player player;
    private int playerIndex;
    private ArrayList<Player> playerList;
    private final ArrayList<Pokemon> POKEMON_LIST;
    private Leaderboard leaderboard;

    // Constructor
    public Game() {
        playerList = DataManager.loadPlayerFile();
        POKEMON_LIST = DataManager.loadPokemonFile();
        leaderboard = DataManager.loadLeaderboardFile();
    }

    public void startGame() {
        Scanner input = new Scanner(System.in);
        
        // Switch between main and game menu until system terminated
        while (true) {
            handleMainMenu(input);
            handleGameMenu(input);
        }
    }

    // Determine game player
    private void handleMainMenu(Scanner input) {
        while (true) {    
            System.out.println("""
            
            ============================
             WELCOME TO Pokemon Ga-CLI!
            ============================
            1. New Game
            2. Load Game
            3. Exit
            """);

            System.out.print("Select an option: ");

            // Selects the player
            switch (input.nextLine()) {
                case "1":
                    //New Game: Create new player
                    System.out.println("\n=== NEW PLAYER ===\n");
                    System.out.print("Please enter player name: ");
                    String name = input.nextLine();
                    player = new Player(name); 
                    player.addPokemon(POKEMON_LIST.get(0).copy());
                    player.addPokemon(POKEMON_LIST.get(3).copy());
                    player.addPokemon(POKEMON_LIST.get(6).copy());

                    playerList.add(player);
                    playerIndex = playerList.size() - 1;
                    break;

                case "2":
                    //Load Game: Load save data, user selects a player
                    System.out.println("\n=== PLAYERS ===");

                    if (playerList.size() == 0) {
                        System.out.println("There are no players yet. Start a new game!");
                        continue;
                    }

                    for (int i = 0; i < playerList.size(); i++) {
                        Player player = playerList.get(i);

                        System.out.println(String.format("%d. %s", i+1, player.getPlayerName()));
                    }

                    while (true) {
                        try {
                            System.out.print("\nSelect a player: ");

                            playerIndex = Integer.parseInt(input.nextLine()) - 1;

                            if (playerIndex < 0 || playerIndex >= playerList.size()) {
                                System.out.println("Player doesn't exist. ");
                                continue;
                            } else {
                                player = playerList.get(playerIndex);
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number. ");
                        }
                    }

                    break;

                case "3":
                    System.out.println("Program terminated.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid selection.");
                    continue;

            }
            break; 
        }
        
    }

    // Handle game actions
    private void handleGameMenu(Scanner input) {
        while (true) {
            System.out.println("""
                
                =======================
                       GAME MENU    
                =======================
                1. Manage Pokemons
                2. View Leaderboard
                3. Start Battle
                4. Save Game
                5. Return to Main Menu

                * WARNING: Quitting before saving will result in unsaved data! *
                """);
            System.out.print("Select an option: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    managePokemons(input);
                    break;

                case "2":
                    System.out.println(leaderboard);
                    break;

                case "3":
                    startBattle(input);
                    break;

                case "4":
                    saveGame();
                    break;

                case "5":
                    while (true) {
                        System.out.print("Do you wish to save? (Y/N): ");
                        String answer = input.nextLine().toLowerCase();

                        if (answer.equals("y")) {
                            saveGame();
                        }
                        
                        if (answer.equals("y") || answer.equals("n")) {
                            return;
                        } else {
                            System.out.println("Invalid input.");
                        }
                    }
                    

                default:
                    System.out.println("Invalid option. ");
            }

            System.out.print("\nENTER to continue...");
            input.nextLine();
        }
    }

    // Initiate battle, spawn wild pokemons
    private void startBattle(Scanner input) {
        ArrayList<Pokemon> playerPokemons, wildPokemons;
        wildPokemons = chooseWildPokemons();

        System.out.print("""
            
            ====================
                BATTLE START
            ====================
            """);
        System.out.print("Wild pokemons are hidden in the grass. Randomly pick one to catch (1-3): ");
        while (true) {
            try {
                int index = Integer.parseInt(input.nextLine()) - 1;

                if (index < 0 || index >= wildPokemons.size()) {
                    System.out.print("Please select a number between 1 to " + wildPokemons.size() + ": ");
                    continue;
                }

                wildPokemons.get(index).setOwner(player.getPlayerName());
                player.addPokemon(wildPokemons.get(index));

                System.out.println(String.format("Successfully caught %s!", wildPokemons.get(index).getPokemonName()));
                wildPokemons.remove(index);

                System.out.print("\nENTER to continue...");
                input.nextLine();
                
                System.out.println("\n=== Wild Pokemons ===");
                
                for (int i = 0; i < wildPokemons.size(); i++) {
                    Pokemon pokemon = wildPokemons.get(i);
                    System.out.println(String.format("%d. %s (Type: %s)", i+1, pokemon.getPokemonName(), pokemon.getPokemonType()));
                }

                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }

        System.out.println();
        playerPokemons = choosePlayerPokemons(input);

        Battle battle = new Battle(playerPokemons, wildPokemons);
        battle.loopBattle(input);
        
        if(battle.isVictory()) {
            player.addPokemon(battle.getCaughtPokemons());

            if (leaderboard.isTopScore(battle.getBattleScore())) {
                System.out.println("\n=== NEW HIGH SCORE! ===");
                leaderboard.addTopScore(player.getPlayerName(), battle.getBattleScore());
            }

            System.out.println("Battle Score: " + battle.getBattleScore());
        }
    }

    // Display player pokemons and select for battle
    private ArrayList<Pokemon> choosePlayerPokemons(Scanner input) {
        player.displayPlayerPokemons();

        ArrayList<Pokemon> selectedPokemons = new ArrayList<>();
        int index1, index2;

        while (true) {
            try {
                System.out.print("Select your first Pokemon: ");
                index1 = Integer.parseInt(input.nextLine()) - 1;

                if (player.selectPokemon(index1) == null) {
                    System.out.println("Pokemon doesn't exist. ");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection! Please enter a number. ");
            }
        }

        while (true) {
            try {
                System.out.print("Select your second Pokemon: ");
                index2 = Integer.parseInt(input.nextLine()) - 1;

                if (index1 == index2) {
                    System.out.println("You cannot pick the same Pokemon!");
                } else if (player.selectPokemon(index2) == null) {
                    System.out.println("Pokemon doesn't exist. ");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection! Please enter a number. ");
            }
        }

        Pokemon pokemon1 = player.selectPokemon(index1);
        Pokemon pokemon2 = player.selectPokemon(index2);
        pokemon1.heal();
        pokemon2.heal();

        selectedPokemons.add(pokemon1);
        selectedPokemons.add(pokemon2);

        return selectedPokemons;
    }

    // Wild Pokemons are randomly selected
    private ArrayList<Pokemon> chooseWildPokemons() {
        ArrayList<Pokemon> selectedPokemons = new ArrayList<>();
        int index;

        for (int i = 0; i < 3; i++) {
            do {
                index = (int) (Math.random() * POKEMON_LIST.size());
            } while (selectedPokemons.contains(POKEMON_LIST.get(index)));

            selectedPokemons.add(POKEMON_LIST.get(index).copy());
        }

        return selectedPokemons;
    }

    // View and release pokemons
    private void managePokemons(Scanner input) {
        System.out.println("""

            =======================
                MANAGE POKEMONS
            =======================
            1. View Pokemon Details
            2. Release Pokemons
            3. Return
            """);
        System.out.print("Select an option: ");

        switch (input.nextLine()) {
            case "1":
                player.displayPlayerPokemons();

                while (true) {
                    System.out.print("\nSelect a pokemon to view (-1 to cancel): ");
                    int index = Integer.parseInt(input.nextLine());

                    System.out.println();
                    if (index == -1) {
                        System.out.println("Operation canceled. ");
                    } else if (player.selectPokemon(index-1) == null) {
                        System.out.println("Invalid index selected.");
                    } else {
                        System.out.println(player.selectPokemon(index-1));
                        break;
                    }
                }

                break;

            case "2":
                player.displayPlayerPokemons();
                
                while (true) {
                    try {
                        System.out.print("Select a pokemon to be released (-1 to cancel): ");
                        int index = Integer.parseInt(input.nextLine());

                        if (index == -1) {
                            System.out.println("Operation canceled. ");
                        } else {
                            player.releasePokemon(index-1);
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. ");
                    }
                }
                break;

            case "3":
                return;

            default:
                System.out.println("Invalid option. ");
        }
    }

    // Update player data and save to file
    private void saveGame() {
        playerList.set(playerIndex, player);
        DataManager.savePlayerFile(playerList);
        DataManager.saveLeaderboardFile(leaderboard);

        System.out.println("Game successfully saved! ");
    }
}