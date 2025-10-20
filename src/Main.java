import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        // Hər iki test faylını array-də təyin et
        String[] inputFiles = {"csp_small.txt", "csp_tight.txt"};

        for (String filename : inputFiles) {
            System.out.println("🔍 Processing: " + filename);
            System.out.println("=".repeat(50));

            CSP csp = parseInput(filename);

            long startTime = System.currentTimeMillis();
            Map<Variable, Integer> solution = csp.solve();
            long endTime = System.currentTimeMillis();

            if (solution == null) {
                System.out.println("❌ failure");
            } else {
                System.out.print("✅ SOLUTION: {");
                List<Variable> sortedVars = csp.variables.stream()
                        .sorted(Comparator.comparing(v -> Integer.parseInt(v.name)))
                        .collect(Collectors.toList());
                for (int i = 0; i < sortedVars.size(); i++) {
                    Variable v = sortedVars.get(i);
                    System.out.print(v.name + ": " + solution.get(v));
                    if (i < sortedVars.size() - 1) System.out.print(", ");
                }
                System.out.println("}");
            }

            System.out.printf("⏱️  Runtime: %d ms\n", endTime - startTime);
            System.out.println("=".repeat(50));
            System.out.println();
        }
    }

    private static CSP parseInput(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        int colors = -1;
        Set<String> nodes = new HashSet<>();
        List<String[]> edges = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.startsWith("colors=")) {
                colors = Integer.parseInt(line.split("=")[1]);
            } else if (line.contains(",")) {
                String[] parts = line.split(",");
                String u = parts[0].trim();
                String v = parts[1].trim();

                // Self-loop check
                if (u.equals(v)) {
                    System.out.println("⚠️  Self-loop detected: " + u + " -> " + v);
                }

                nodes.add(u);
                nodes.add(v);
                edges.add(new String[]{u, v});
            }
        }
        br.close();

        if (colors == -1) throw new IllegalArgumentException("Missing colors=");

        CSP csp = new CSP(colors);
        for (String node : nodes) {
            csp.addVariable(node);
        }
        for (String[] edge : edges) {
            csp.addConstraint(edge[0], edge[1]);
        }

        System.out.println("📊 Graph Info:");
        System.out.println("   - Colors: " + colors);
        System.out.println("   - Nodes: " + nodes.size() + " " + nodes);
        System.out.println("   - Edges: " + edges.size());

        return csp;
    }
}