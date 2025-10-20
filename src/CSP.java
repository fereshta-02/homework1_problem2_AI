import java.util.*;
import java.util.stream.Collectors;

public class CSP {
    public List<Variable> variables;
    public List<Constraint> constraints;
    public int colors;

    public CSP(int colors) {
        this.variables = new ArrayList<>();
        this.constraints = new ArrayList<>();
        this.colors = colors;
    }

    public Variable getVariableByName(String name) {
        for (Variable v : variables) {
            if (v.name.equals(name)) return v;
        }
        return null;
    }

    public void addVariable(String name) {
        Set<Integer> domain = new HashSet<>();
        for (int i = 1; i <= colors; i++) domain.add(i);
        variables.add(new Variable(name, domain));
    }

    public void addConstraint(String var1, String var2) {
        Variable v1 = getVariableByName(var1);
        Variable v2 = getVariableByName(var2);
        if (v1 == null || v2 == null) return;
        Constraint c = new Constraint(v1, v2);
        constraints.add(c);
        v1.addConstraint(c);
        v2.addConstraint(c);
    }

    public Map<Variable, Integer> solve() {
        Map<Variable, Integer> assignment = new HashMap<>();
        List<Variable> unassigned = new ArrayList<>(variables);

        // Self-loop check
        for (Constraint c : constraints) {
            if (c.variables.get(0) == c.variables.get(1)) {
                return null; // failure
            }
        }

        // AC-3 preprocessing
        if (!ac3()) return null;

        return backtrack(assignment, unassigned);
    }

    private Map<Variable, Integer> backtrack(Map<Variable, Integer> assignment, List<Variable> unassigned) {
        if (unassigned.isEmpty()) return assignment;

        // MRV
        Variable var = mrv(unassigned, assignment);
        unassigned.remove(var);

        // LCV
        List<Integer> values = lcv(var, assignment);

        for (int value : values) {
            assignment.put(var, value);

            // AC-3 after assignment
            if (ac3()) {
                Map<Variable, Integer> result = backtrack(new HashMap<>(assignment), new ArrayList<>(unassigned));
                if (result != null) return result;
            }

            assignment.remove(var);
            // undo AC-3 changes not needed here due to immutable approach in this version
        }

        unassigned.add(var);
        return null;
    }

    private Variable mrv(List<Variable> unassigned, Map<Variable, Integer> assignment) {
        Variable minVar = null;
        int minSize = Integer.MAX_VALUE;
        for (Variable v : unassigned) {
            int size = v.domain.size();
            if (size < minSize || (size == minSize && v.getDegree(unassigned) > minVar.getDegree(unassigned))) {
                minSize = size;
                minVar = v;
            }
        }
        return minVar;
    }

    private List<Integer> lcv(Variable var, Map<Variable, Integer> assignment) {
        Map<Integer, Integer> conflictCount = new HashMap<>();
        for (int color : var.domain) {
            int count = 0;
            for (Constraint c : var.constraints) {
                Variable neighbor = c.getOther(var);
                if (neighbor != null && !assignment.containsKey(neighbor) && neighbor.domain.contains(color)) {
                    count++;
                }
            }
            conflictCount.put(color, count);
        }
        return conflictCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private boolean ac3() {
        Queue<Arc> queue = new LinkedList<>();
        for (Constraint c : constraints) {
            queue.add(new Arc(c.variables.get(0), c.variables.get(1)));
            queue.add(new Arc(c.variables.get(1), c.variables.get(0)));
        }

        while (!queue.isEmpty()) {
            Arc arc = queue.poll();
            if (revise(arc.from, arc.to)) {
                if (arc.from.domain.isEmpty()) return false;
                for (Constraint c : arc.from.constraints) {
                    Variable neighbor = c.getOther(arc.from);
                    if (neighbor != arc.to) {
                        queue.add(new Arc(neighbor, arc.from));
                    }
                }
            }
        }
        return true;
    }

    private boolean revise(Variable from, Variable to) {
        boolean revised = false;
        Set<Integer> toRemove = new HashSet<>();
        for (int x : from.domain) {
            boolean hasSupport = false;
            for (int y : to.domain) {
                if (x != y) {
                    hasSupport = true;
                    break;
                }
            }
            if (!hasSupport) {
                toRemove.add(x);
                revised = true;
            }
        }
        from.domain.removeAll(toRemove);
        return revised;
    }
}
