package tsp.projects.genetic.mutation;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

public class MutationVide extends Mutation{
    public MutationVide(Problem problem, Evaluation evaluation) {
        super(problem, evaluation);
    }

    @Override
    public void mutate(Path p) {

    }
}