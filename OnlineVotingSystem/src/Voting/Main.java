package Voting;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        VotingService votingService = new VotingService();
        Scanner scanner = new Scanner(System.in);
        String currentUser = null;
        boolean running = true;

        System.out.println("==========================================");
        System.out.println("      WELCOME TO ONLINE VOTING SYSTEM     ");
        System.out.println("==========================================");

        while (running) {
            System.out.println("\n---------------- MAIN MENU ---------------");
            if (currentUser != null) {
                System.out.println("Status: Logged in as [" + currentUser + "]");
            } else {
                System.out.println("Status: Not logged in");
            }
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Candidates");
            System.out.println("4. Cast Vote");
            System.out.println("5. View Total Votes");
            System.out.println("6. View Result");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleRegister(votingService, scanner);
                    break;
                case "2":
                    currentUser = handleLogin(votingService, scanner);
                    break;
                case "3":
                    votingService.displayCandidates();
                    break;
                case "4":
                    handleCastVote(votingService, scanner, currentUser);
                    break;
                case "5":
                    votingService.displayTotalVotes();
                    break;
                case "6":
                    votingService.displayResult();
                    break;
                case "7":
                    System.out.println("\nExiting Online Voting System. Thank you!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a valid number from 1 to 7.");
                    break;
            }
        }

        scanner.close();
    }

 
    private static void handleRegister(VotingService service, Scanner scanner) {
        System.out.println("\n--- REGISTER ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        try {
            service.registerUser(username, password);
            System.out.println("SUCCESS: Registration successful! You can now login with username '" + username + "'.");
        } catch (VotingException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

  
    private static String handleLogin(VotingService service, Scanner scanner) {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = service.loginUser(username, password);
            System.out.println("SUCCESS: Welcome, " + user.getUsername() + "! You are logged in successfully.");
            return user.getUsername();
        } catch (VotingException e) {
            System.out.println("ERROR: " + e.getMessage());
            return null;
        }
    }

    private static void handleCastVote(VotingService service, Scanner scanner, String currentUser) {
        System.out.println("\n--- CAST VOTE ---");
        if (currentUser == null) {
            System.out.println("ERROR: You must login first before casting a vote! Please select Option 2 (Login).");
            return;
        }

        service.displayCandidates();
        System.out.print("\nEnter Candidate ID to cast your vote: ");
        String candidateId = scanner.nextLine().trim();

        try {
            service.castVote(currentUser, candidateId);
        } catch (VotingException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
