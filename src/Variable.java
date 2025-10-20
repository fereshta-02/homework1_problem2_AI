import java.util.*;

public class Variable {
    public String name;
    public Set<Integer> domain;
    public List<Constraint> constraints;

    public Variable(String name, Set<Integer> domain) {
        this.name = name;
        this.domain = new HashSet<>(domain);
        this.constraints = new ArrayList<>();
    }

    public void addConstraint(Constraint c) {
        constraints.add(c);
    }

    public int getDegree(List<Variable> unassigned) {
        int count = 0;
        for (Constraint c : constraints) {
            for (Variable v : c.variables) {
                if (unassigned.contains(v)) count++;
            }
        }
        return count;
    }
}
