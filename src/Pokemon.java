import java.util.ArrayList;

public class Pokemon {
    // Attributes
    private int collectionNumber;
    private String pokemonName;
    private int grade;
    private String pokemonType;
    private String moveName;
    private int currentHealth;
    private int maxHealth;
    private int offense;
    private boolean isPhysicalAttack;
    private int defense;
    private int specialDefense;
    private int speed;

    private ArrayList<String> doubleDamageTo = new ArrayList<>();
    private ArrayList<String> halfDamageTo = new ArrayList<>();
    private ArrayList<String> noDamageTo = new ArrayList<>();

    private String owner = null;  // null means wild

    // Constructors
    // Empty constructor
    public Pokemon() {}

    // Constructor to create a pokemon with given values
    public Pokemon(int collectionNumber, String pokemonName, int grade, String pokemonType,
                   String moveName, int maxHealth, int offense, boolean isPhysicalAttack, 
                   int defense, int specialDefense, int speed) {
        this.collectionNumber = collectionNumber;
        this.pokemonName = pokemonName;
        this.grade = grade;
        this.pokemonType = pokemonType;
        this.moveName = moveName;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth; // start at full health
        this.offense = offense;
        this.isPhysicalAttack = isPhysicalAttack;
        this.defense = defense;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.doubleDamageTo = new ArrayList<>();
        this.halfDamageTo = new ArrayList<>();
        this.noDamageTo = new ArrayList<>();
    }

    // Constructor to clone a new Pokemon object from a reference
    public Pokemon(Pokemon pokemon) {
        this.collectionNumber = pokemon.collectionNumber;
        this.pokemonName = pokemon.pokemonName;
        this.grade = pokemon.grade;
        this.pokemonType = pokemon.pokemonType;
        this.moveName = pokemon.moveName;
        this.maxHealth = pokemon.maxHealth;
        this.currentHealth = pokemon.maxHealth;
        this.offense = pokemon.offense;
        this.isPhysicalAttack = pokemon.isPhysicalAttack;
        this.defense = pokemon.defense;
        this.specialDefense = pokemon.specialDefense;
        this.speed = pokemon.speed;
        this.doubleDamageTo = pokemon.doubleDamageTo;
        this.halfDamageTo = pokemon.halfDamageTo;
        this.noDamageTo = pokemon.noDamageTo;
    }
    
    // Copies pokemon class
    public Pokemon copy() {
        return new Pokemon(this);
    }
    
    // Getters and Setters
    public void setCollectionNumber(int collectionNumber) { 
        this.collectionNumber = collectionNumber; 
    }
    
    public int getCollectionNumber() { 
        return collectionNumber; 
    }

    public void setPokemonName(String pokemonName) { 
        this.pokemonName = pokemonName; 
    }
    
    public String getPokemonName() { 
        return pokemonName; 
    }

    public void setGrade(int grade) { 
        this.grade = grade; 
    }
    
    public int getGrade() { 
        return grade; 
    }

    public void setPokemonType(String pokemonType) { 
        this.pokemonType = pokemonType; 
    }

    public String getPokemonType() { 
        return pokemonType; 
    }

    public void setMoveName(String moveName) { 
        this.moveName = moveName; 
    }
    
    public String getMoveName() { 
        return moveName; 
    }

    public void setMaxHealth(int maxHealth) { 
        this.maxHealth = maxHealth; 
    }
    
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setOffense(int offense) { 
        this.offense = offense; 
    }
    
    public void setIsPhysicalAttack(boolean isPhysicalAttack) { 
        this.isPhysicalAttack = isPhysicalAttack; 
    }
    
    public void setDefense(int defense) { 
        this.defense = defense; 
    }
    
    public void setSpecialDefense(int specialDefense) { 
        this.specialDefense = specialDefense; 
    }
    
    public void setSpeed(int speed) { 
        this.speed = speed; 
    }
    
    public int getSpeed() { 
        return speed; 
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDoubleDamageTo(ArrayList<String> types) { 
        this.doubleDamageTo = types; 
    }
    
    public void setHalfDamageTo(ArrayList<String> types) { 
        this.halfDamageTo = types; 
    }
    
    public void setNoDamageTo(ArrayList<String> types) { 
        this.noDamageTo = types; 
    }
    
    // Checks if this pokemon is wild
    public boolean isWildPokemon() {
        if (owner == null) {
            return true;
        } else {
            return false;
        }
    }

    // Checks if this pokemon has fainted
    public boolean isFainted() {
        if (currentHealth == 0) {
            return true;
        } else {
            return false;
        }
    }

    // Fully heals this pokemon
    public void heal() {
        currentHealth = maxHealth;
    }

    // Attacks ooponents
    public void attack(ArrayList<Pokemon> opponentPokemonList) {
        for (Pokemon defender : opponentPokemonList) {
            int totalDamage;
            double baseDamage, multiplier;

            if (isPhysicalAttack) {
                baseDamage = (offense / defender.defense * 22) + 5;
            } else {
                baseDamage = (offense / defender.specialDefense) + 5;
            }

            multiplier = checkEffectiveness(defender.getPokemonType());
            totalDamage = (int)(baseDamage * multiplier);
        
            defender.reduceHealth(totalDamage);

            System.out.println(String.format("%s got hit with %d damage! (%d / %d)", defender.pokemonName, totalDamage, defender.currentHealth, defender.maxHealth));
        }
    }

    // Checks the effectiveness of attack against opponent
    private double checkEffectiveness(String opponentType) {
        if (doubleDamageTo.contains(opponentType)) return 2.0;
        if (halfDamageTo.contains(opponentType)) return 0.5;
        if (noDamageTo.contains(opponentType)) return 0.0;
        return 1.0;
    }

    // Reduce health of pokemon
    private void reduceHealth(int totalDamage) {
        currentHealth -= totalDamage;
        if (currentHealth < 0) currentHealth = 0;
    }

    @Override
    public String toString() {
        return "Pokemon Name: " + pokemonName +
               "\nCollection #: " + collectionNumber +
               "\nType: " + pokemonType +
               "\nGrade: " + grade +
               "\nMove: " + moveName +
               "\nHealth: " + currentHealth + " / " + maxHealth +
               "\n" + (isPhysicalAttack ? "Attack" : "Special Attack") + ": " + offense +
               "\nDefense: " + defense +
               "\nSpecial Defense: " + specialDefense +
               "\nSpeed: " + speed +
               "\nOwner: " + (owner == null ? "None (Wild)" : owner) + 
               "\n";
    }
}