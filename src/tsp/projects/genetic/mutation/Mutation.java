package tsp.projects.genetic.mutation;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.projects.genetic.Utilities;

public abstract class Mutation extends Utilities {
    public Problem problem;
    public Evaluation evaluation;
    public Mutation(Problem problem, Evaluation evaluation){
        this.problem = problem;
        this.evaluation = evaluation;
    }
    public abstract void mutate(Path p);

    public static Mutation[] getMutationList(Problem problem, Evaluation evaluation){
        return new Mutation[]{
//                new MutationVide(problem, evaluation),
//                new MutationOptLocale(problem,evaluation),
                new MutationEchangeRandom(problem, evaluation),
                new MutationDecalageRandom(problem, evaluation),
                new MutationInverseOrdreZoneRandom(problem, evaluation),
                new MutationEchangeVoisin(problem, evaluation)

        };
    }
}
