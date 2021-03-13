package tsp.projects.genetic.mutation;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

public class MutationEchangeRandom extends Mutation {
    private static final double MUTATION_RATE = 0.5;

    public MutationEchangeRandom(Problem problem, Evaluation evaluation) {
        super(problem, evaluation);
    }


    @Override
    public void mutate(Path p) {
//        while (getRandom().nextDouble() < MUTATION_RATE)
            echangeRandom(p);
    }

    private void echangeRandom(Path path) {
        int lastEchangei = getRandom().nextInt(path.getPath().length);
        int lastEchangej = getRandom().nextInt(path.getPath().length);
        echange(lastEchangei, lastEchangej, path);
    }
}
