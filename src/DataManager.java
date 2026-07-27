import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataManager {
    private static final String PLAYER_FILE_PATH = "src/data/players.txt";
    private static final String POKEMON_FILE_PATH = "src/data/pokemon.txt";
    private static final String LEADERBOARD_FILE_PATH = "src/data/leaderboard.txt";
    
    // General method to save data to file
    private static void saveFile(String filePath, String data) {
        File file = new File(filePath);
        
        try {
            if (!(file.exists())) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file, false);

            fileWriter.write(data);

            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Something went wrong. " + e.getStackTrace());
        }// If you want to print something for no files
    }

    // General method to load data from file
    private static ArrayList<String[]> loadFile(String filePath) {
        File file = new File(filePath);
        ArrayList<String[]> dataArr = new ArrayList<>();

        if (file.exists()) {
            try {
                Scanner fileReader = new Scanner(file);

                while (fileReader.hasNextLine()) {
                    String[] lineArr = fileReader.nextLine().split(", ");
                    dataArr.add(lineArr);
                }

                fileReader.close();
            } catch (IOException e) {
                System.out.println("Something went wrong. " + e.getStackTrace());
            }
        }

        return dataArr;
    }
    
    public static void savePlayerFile(ArrayList<Player> playerList) {
         String data = "";
            
        for (Player player : playerList) {
            String playerName = player.getPlayerName();
            String playerPokemon = "";

            for (Pokemon pokemon : player.getPlayerPokemons()) {
                playerPokemon += String.format("%d, ", pokemon.getCollectionNumber());
            }

            data += String.format("%s\n%s\n", playerName, playerPokemon);
        }

        saveFile(PLAYER_FILE_PATH, data);
    }

    public static ArrayList<Player> loadPlayerFile() {
        ArrayList<String[]> playerData = loadFile(PLAYER_FILE_PATH);
        ArrayList<Player> playerList = new ArrayList<Player>();
        ArrayList<Pokemon> pokemonList = loadPokemonFile();

        for (int i = 0; i < playerData.size(); i+=2) {
            ArrayList<Pokemon> caughtPokemon = new ArrayList<Pokemon>();
            String playerName = playerData.get(i)[0];
            String[] caughtPokemonString = playerData.get(i+1);

            Player player = new Player(playerName, caughtPokemon);

            for (String colNumString : caughtPokemonString) {
                int colNum = Integer.parseInt(colNumString);
                Pokemon pokemon = pokemonList.get(colNum-1);

                player.addPokemon(pokemon);
            }

            playerList.add(player);
        }

        return playerList;
    }

    public static ArrayList<Pokemon> loadPokemonFile() {
        ArrayList<String[]> pokemonData = loadFile(POKEMON_FILE_PATH);
        ArrayList<Pokemon> pokemonList = new ArrayList<Pokemon>();

        for (int i = 0; i < pokemonData.size(); i++) {
            String[] pokemonStats = pokemonData.get(i);
            Pokemon pokemon = new Pokemon();

            switch (pokemonStats[3]) {
                case "Fire":
                    pokemon = new FirePokemon();
                    break;
                case "Water":
                    pokemon = new WaterPokemon();
                    break;
                case "Grass":
                    pokemon = new GrassPokemon();
                    break;
                case "Flying":
                    pokemon = new FlyingPokemon();
                    break;
                case "Electric":
                    pokemon = new ElectricPokemon();
                    break;
                case "Ground":
                    pokemon = new GroundPokemon();
                    break;
            }

            pokemon.setCollectionNumber(Integer.parseInt(pokemonStats[0]));
            pokemon.setPokemonName(pokemonStats[1]);
            pokemon.setGrade(Integer.parseInt(pokemonStats[2]));
            pokemon.setPokemonType(pokemonStats[3]);
            pokemon.setMoveName(pokemonStats[4]);
            pokemon.setMaxHealth(Integer.parseInt(pokemonStats[5]));
            pokemon.setCurrentHealth(Integer.parseInt(pokemonStats[5]));
            pokemon.setOffense(Integer.parseInt(pokemonStats[6]));
            pokemon.setIsPhysicalAttack(Boolean.parseBoolean(pokemonStats[7]));
            pokemon.setDefense(Integer.parseInt(pokemonStats[8]));
            pokemon.setSpecialDefense(Integer.parseInt(pokemonStats[9]));
            pokemon.setSpeed(Integer.parseInt(pokemonStats[10]));

            pokemonList.add(pokemon);
        }

        return pokemonList;
    }

    public static void saveLeaderboardFile(Leaderboard leaderboard) {
        ArrayList<String[]> leaderboardList = leaderboard.getLeaderboardList();
        String data = "";
            
        for (String[] rank : leaderboardList) {
            data += String.format("%s, %s\n", rank[0], rank[1]);
        }

        saveFile(LEADERBOARD_FILE_PATH, data);
    }

    public static Leaderboard loadLeaderboardFile() {
        return new Leaderboard(loadFile(LEADERBOARD_FILE_PATH));

    }
}