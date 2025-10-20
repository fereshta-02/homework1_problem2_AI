import java.util.*;

public class Constraint {
    public List<Variable> variables;

    public Constraint(Variable a, Variable b) {
        this.variables = Arrays.asList(a, b);
    }

    public boolean isSatisfied(Map<Variable, Integer> assignment) {
        if (!assignment.containsKey(variables.get(0)) || !assignment.containsKey(variables.get(1)))
            return true;
        return !assignment.get(variables.get(0)).equals(assignment.get(variables.get(1)));
    }

    public Variable getOther(Variable v) {
        if (variables.get(0) == v) return variables.get(1);
        if (variables.get(1) == v) return variables.get(0);
        return null;
    }
}