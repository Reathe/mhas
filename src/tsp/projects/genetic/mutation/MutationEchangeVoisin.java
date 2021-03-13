package tsp.projects.genetic.mutation;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

public class MutationEchangeVoisin extends Mutation{


    public MutationEchangeVoisin(Problem problem, Evaluation evaluation) {
        super(problem, evaluation);
    }

    @Override
    public void mutate(Path p) {
        int i = getRandom().nextInt(p.getPath().length-1);
        echange(i,i+1,p);
    }
}
