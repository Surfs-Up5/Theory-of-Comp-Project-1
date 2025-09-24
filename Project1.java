import java.io.*;
import java.util.*;

public class Project1 {
    
    static class State{
        int id;
        boolean isFinal = false;
        Map<Character, State> transitions = new HashMap<>();
        State(int id) { this.id = id;}
    }
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Project1 <input.txt> <output.jff>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        List<String> words = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(inputFile))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();

                // blank line = empty string λ
                if (line.equals("")) {
                    words.add(""); 
                } else {
                    // check only lowercase letters
                    if (!line.matches("[a-z]+")) {
                        throw new IllegalArgumentException("Bad symbol in: " + line);
                    }
                    words.add(line);
                }
            }
        } catch (Exception e) {
            System.out.println("Problem reading file: " + e.getMessage());
            return;
        }
    }
}