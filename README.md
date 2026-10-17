# Pokémon Ga-Ole Console

A turn-based, console version of the Pokémon Ga-Ole arcade game, written in plain Java with no external libraries. You pick two Pokémon, fight three wild ones, dodge their attacks in a timed quick-time event, and try to catch what you beat with a blind-picked Poké Ball. Progress and a top-5 leaderboard are saved to disk.

## Running it

Requires JDK 17+ (the code uses text blocks). Run from the project root, since data files are read from `src/data/`.

```bash
javac -d out src/*.java
java -cp out Main
```

## How it plays

1. **New Game** gives you three starters (Bulbasaur, Charmander, Squirtle). **Load Game** restores a saved player.
2. **Start Battle** spawns three wild Pokémon hidden "in the grass". You pick one blind and it joins your team for free.
3. The remaining two fight you. You choose two of your Pokémon, and turns are ordered by speed.
4. When a wild Pokémon attacks, you get **5 seconds** to guess the dodge direction (`l` or `r`).
5. Win, and you throw a Poké Ball at the survivors. The balls are shuffled first, so you choose blind.
6. Score is `10000 − 75 × turns` (floored at 500), plus `1000` per catch. The top 5 go on the leaderboard.

## Architecture

```
Main ──► Game ──┬──► Player ──► Pokemon ◄── Fire / Water / Grass /
                │                            Electric / Flying / Ground
                ├──► Battle ──► Pokeball ◄── Normal / Great / Ultra / Master
                ├──► Leaderboard
                └──► DataManager ──► src/data/*.txt
```

- **`Game`** handles menus and flow only.
- **`Battle`** owns one fight: turn order, damage, dodging, catching and scoring.
- **`Pokemon`** and its type subclasses hold stats and attack logic.
- **`Pokeball`** and its subclasses hold catch logic.
- **`Player`** and **`Leaderboard`** hold their own state.
- **`DataManager`** is the only class that touches files.

## Design decisions

**Inheritance for types.** Each element (`FirePokemon`, `WaterPokemon`, …) extends `Pokemon` and defines its own matchups. The base `attack()` just asks for the effectiveness multiplier, so it contains no type-by-type `if` chains.

**Copy, don't share.** `pokemon.txt` is loaded once into a master roster, and every Pokémon in play is a copy of one of those entries. Damage taken in a fight never touches the template. `copy()` is overridden in each subclass, so a cloned `FirePokemon` is still a `FirePokemon`.

**Wild is a state, not a class.** A Pokémon is wild while it has no owner. Since a wild Pokémon becomes owned mid-game and an object can't change class, a field fits better than a `WildPokemon` subclass.

**Poké Balls differ only in data.** The catch formula lives once in `Pokeball`, and each ball just supplies a name and catch rate. At a rate of 1.0 the formula makes the Master Ball a guaranteed catch with no special case.

**Separation of concerns.** `Battle` knows nothing about menus or files, so the rules can change without touching the UI.

**Saves store references, not copies.** Save files hold only each player's Pokémon collection numbers and look the stats up on load. Saves stay tiny, and rebalancing `pokemon.txt` applies everywhere.

**Timed input.** `Scanner.nextLine()` blocks indefinitely, so the dodge prompt runs it on an `ExecutorService` and waits with `Future.get(5, SECONDS)`. A timeout counts as a failed dodge.

## What I'd improve

- Replace the `String` types with an `enum`, and make the matchup tables data-driven instead of one subclass each.
- Fix the damage formula, which uses integer division and truncates the stat ratio.
- Fix the dodge prompt, where a timed-out read can swallow the next line typed.
- Inject file paths into `DataManager` instead of using static, relative ones.
- Add unit tests for `Battle` and `Pokeball` with a seeded RNG.

## Project layout

```
src/
├── Main.java, Game.java, Battle.java
├── Pokemon.java + {Fire,Water,Grass,Electric,Flying,Ground}Pokemon.java
├── Pokeball.java + {Normal,Great,Ultra,Master}Ball.java
├── Player.java, Leaderboard.java, DataManager.java
└── data/   pokemon.txt (40 Pokémon), players.txt, leaderboard.txt
```

*Pokémon is a trademark of Nintendo, Game Freak and Creatures Inc. This is an unofficial, non-commercial fan project made for learning.*
