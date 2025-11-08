import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int maxRounds = 3;
        int totalScore = 0;

        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("You have " + maxRounds + " rounds. Try to guess the number between 1 and 100.");

        for (int round = 1; round <= maxRounds; round++) {
            int numberToGuess = random.nextInt(100) + 1;
            int attempts = 0;
            int maxAttempts = 7;
            boolean guessed = false;

            System.out.println("\nRound " + round + " begins!");
            
            while (attempts < maxAttempts && !guessed) {
                System.out.print("Enter your guess: ");
                int guess = scanner.nextInt();
                attempts++;

                if (guess == numberToGuess) {
                    System.out.println("Correct! You guessed it in " + attempts + " attempts!");
                    int score = (maxAttempts - attempts + 1) * 10;
                    totalScore += score;
                    System.out.println("You earned " + score + " points this round.");
                    guessed = true;
                } else if (guess < numberToGuess) {
                    System.out.println("Too low! Try again.");
                } else {
                    System.out.println("Too high! Try again.");
                }
            }

            if (!guessed) {
                System.out.println("Out of attempts! The number was " + numberToGuess);
            }
        }

        System.out.println("\nGame Over! Your total score is: " + totalScore);
        scanner.close();
    }
}
