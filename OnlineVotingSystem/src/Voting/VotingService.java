package Voting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingService {

    // Store users using HashMap (username -> User object)
    private Map<String, User> users;

    // Map<String, Boolean> to track whether a user has already voted
    private Map<String, Boolean> hasVoted;

    // Map<String, Integer> to store the number of votes received by each candidate
    private Map<String, Integer> candidateVotes;

    // Store candidates (candidateId -> Candidate object)
    private Map<String, Candidate> candidates;

    public VotingService() {
        this.users = new HashMap<>();
        this.hasVoted = new HashMap<>();
        this.candidateVotes = new HashMap<>();
        this.candidates = new HashMap<>();

        // Add initial college election candidates
        addCandidate(new Candidate("101", "Chandra babu Naidu", "TDP"));
        addCandidate(new Candidate("102", "Pawan Kalyan", "JSP"));
        addCandidate(new Candidate("103", "Jagan", "YSRCP"));
    }

    public void addCandidate(Candidate candidate) {
        candidates.put(candidate.getId(), candidate);
        candidateVotes.put(candidate.getName(), 0);
    }

    public void registerUser(String username, String password) throws VotingException {
        if (username == null || username.trim().isEmpty()) {
            throw new VotingException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new VotingException("Password cannot be empty.");
        }
        if (users.containsKey(username)) {
            throw new VotingException("User '" + username + "' is already registered.");
        }

        User newUser = new User(username, password);
        users.put(username, newUser);
        hasVoted.put(username, false);
    }

    public User loginUser(String username, String password) throws VotingException {
        if (username == null || username.trim().isEmpty()) {
            throw new VotingException("Username cannot be empty.");
        }
        if (!users.containsKey(username)) {
            throw new VotingException("User '" + username + "' not found. Please register first.");
        }

        User user = users.get(username);
        if (!user.getPassword().equals(password)) {
            throw new VotingException("Invalid password. Please try again.");
        }

        return user;
    }

    public void displayCandidates() {
        System.out.println("\n--- AVAILABLE CANDIDATES ---");
        if (candidates.isEmpty()) {
            System.out.println("No candidates available.");
            return;
        }
        for (Candidate candidate : candidates.values()) {
            System.out.println(candidate);
        }
    }

    public void castVote(String username, String candidateId) throws VotingException {
        if (username == null || !users.containsKey(username)) {
            throw new VotingException("You must be logged in to cast a vote.");
        }

        if (Boolean.TRUE.equals(hasVoted.get(username))) {
            throw new VotingException("User '" + username + "' has already voted! Duplicate voting is not allowed.");
        }

        if (!candidates.containsKey(candidateId)) {
            throw new VotingException("Invalid candidate selection! Candidate ID '" + candidateId + "' does not exist.");
        }

        Candidate selectedCandidate = candidates.get(candidateId);
        String candidateName = selectedCandidate.getName();

        // Increment candidate vote count in candidateVotes map
        int currentVotes = candidateVotes.getOrDefault(candidateName, 0);
        candidateVotes.put(candidateName, currentVotes + 1);

        // Track that this user has voted in hasVoted map
        hasVoted.put(username, true);

        System.out.println("\nSUCCESS: Vote cast successfully for " + candidateName + "!");
    }

    public void displayTotalVotes() {
        System.out.println("\n--- TOTAL VOTES RECEIVED ---");
        if (candidateVotes.isEmpty()) {
            System.out.println("No candidate records found.");
            return;
        }

        for (Map.Entry<String, Integer> entry : candidateVotes.entrySet()) {
            System.out.println("Candidate: " + entry.getKey() + " | Total Votes: " + entry.getValue());
        }
    }

    public void displayResult() {
        System.out.println("\n==========================================");
        System.out.println("             ELECTION RESULTS             ");
        System.out.println("==========================================");

        int totalVotesCast = 0;
        for (int votes : candidateVotes.values()) {
            totalVotesCast += votes;
        }

        if (totalVotesCast == 0) {
            System.out.println("No votes have been cast yet.");
            System.out.println("==========================================");
            return;
        }

        // Find maximum vote count
        int maxVotes = -1;
        for (int votes : candidateVotes.values()) {
            if (votes > maxVotes) {
                maxVotes = votes;
            }
        }

        // Find candidate(s) with max votes
        List<String> winners = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : candidateVotes.entrySet()) {
            if (entry.getValue() == maxVotes) {
                winners.add(entry.getKey());
            }
        }

        // Find runner-up vote count (second highest)
        int secondMaxVotes = -1;
        for (int votes : candidateVotes.values()) {
            if (votes < maxVotes && votes > secondMaxVotes) {
                secondMaxVotes = votes;
            }
        }

        List<String> runnersUp = new ArrayList<>();
        if (secondMaxVotes > 0) {
            for (Map.Entry<String, Integer> entry : candidateVotes.entrySet()) {
                if (entry.getValue() == secondMaxVotes) {
                    runnersUp.add(entry.getKey());
                }
            }
        }

        // Display Winner/Finalist
        if (winners.size() == 1) {
            System.out.println("WINNER / FINALIST : " + winners.get(0) + " (" + maxVotes + " vote(s))");
        } else {
            System.out.println("WINNER / FINALIST (TIE) : " + String.join(", ", winners) + " (" + maxVotes + " vote(s) each)");
        }

        // Display Runner-Up
        if (runnersUp.isEmpty()) {
            System.out.println("RUNNER-UP         : None (No other candidate received votes)");
        } else if (runnersUp.size() == 1) {
            System.out.println("RUNNER-UP         : " + runnersUp.get(0) + " (" + secondMaxVotes + " vote(s))");
        } else {
            System.out.println("RUNNER-UP (TIE)   : " + String.join(", ", runnersUp) + " (" + secondMaxVotes + " vote(s) each)");
        }

        System.out.println("==========================================");
    }
}
