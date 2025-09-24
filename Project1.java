import java.io.*;
import java.util.*;

public class Project1 {

    static class State {
        int id;
        boolean isFinal = false;
        Map<Character, State> transitions = new HashMap<>();
        State(int id) { this.id = id; }
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
                if (line.equals("")) {
                    words.add("");
                } else {
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

        int stateIdCounter = 0;
        State start = new State(stateIdCounter++);

        for (String word : words) {
            State current = start;
            for (char c : word.toCharArray()) {
                current.transitions.putIfAbsent(c, new State(stateIdCounter++));
                current = current.transitions.get(c);
            }
            current.isFinal = true; 
            if (word.equals("")) start.isFinal = true; 
        }
       
        List<State> allStates = new ArrayList<>();
        Map<Integer, State> visited = new HashMap<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(start);
        visited.put(start.id, start);

        while (!queue.isEmpty()) {
            State s = queue.poll();
            allStates.add(s);
            for (State next : s.transitions.values()) {
                if (!visited.containsKey(next.id)) {
                    visited.put(next.id, next);
                    queue.add(next);
                }
            }
        }
        try (PrintWriter out = new PrintWriter(new File(outputFile))) {
            out.println("<structure>");
            out.println("  <type>fa</type>");
            out.println("  <automaton>");
            for (State s : allStates) {
                out.printf("    <state id=\"%d\" name=\"q%d\">\n", s.id, s.id);
                if (s == start) out.println("      <initial/>");
                if (s.isFinal) out.println("      <final/>");

                out.println("      <x>50.0</x>");
                out.println("      <y>50.0</y>");
                out.println("    </state>");
            }
            for (State s : allStates) {
                for (Map.Entry<Character, State> entry : s.transitions.entrySet()) {
                    out.println("    <transition>");
                    out.printf("      <from>%d</from>\n", s.id);
                    out.printf("      <to>%d</to>\n", entry.getValue().id);
                    out.printf("      <read>%c</read>\n", entry.getKey());
                    out.println("    </transition>");
                }
            }
            out.println("  </automaton>");
            out.println("</structure>");
        } catch (Exception e) {
            System.out.println("Problem writing file: " + e.getMessage());
        }

        System.out.println("DFA successfully written to " + outputFile);
    }
}