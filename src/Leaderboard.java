import java.util.*;

public class Leaderboard {
    private ArrayList<String[]> leaderboardList;

    // Constructor
    public Leaderboard(ArrayList<String[]> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    // Getter
    public ArrayList<String[]> getLeaderboardList() {
        return leaderboardList;
    }

    // Add new score
    public void addTopScore(String playerName, int score) {
        String[] entry = new String[]{playerName, String.valueOf(score)};
        leaderboardList.add(entry);

        // Sort descending by score
        leaderboardList.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));

        // Keep only top 5
        if (leaderboardList.size() > 5) {
            leaderboardList.remove(5);
        }
    }

    // Check if score qualifies for leaderboard
    public boolean isTopScore(int score) {
        if (leaderboardList.size() < 5) return true;
        int lowestScore = Integer.parseInt(leaderboardList.get(leaderboardList.size() - 1)[1]);
        return score > lowestScore;
    }

    // toString for display
    public String toString() {
        StringBuilder sb = new StringBuilder();
        System.out.println();
        sb.append("""
            
            ===================
                LEADERBOARD
            ===================
            """);
        for (int i = 0; i < leaderboardList.size(); i++) {
            String[] entry = leaderboardList.get(i);
            sb.append((i + 1) + ". " + entry[0] + " - " + entry[1] + "\n");
        }
        return sb.toString();
    }
}