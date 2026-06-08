package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInput implements PlayerInput {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    @Override
    public int chooseAction() {
        while(true) {
            try {
                System.out.println(" Choose an action: "); 
                System.out.println("1. Attack");
                System.out.println("2. Heal");
                String input = reader.readLine();
                if (input == null) {
                    throw new IllegalStateException("Input ended.");
                }
                int choice = Integer.parseInt(input);
                if (choice == 1 || choice == 2) {
                    return choice;
            } 
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
        catch (IOException e) {
            System.out.println("An error occurred while reading input. Please try again.");
        } 
        catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

}
}
