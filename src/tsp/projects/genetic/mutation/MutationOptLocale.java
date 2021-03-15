package tsp.projects.genetic.mutation;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

/**
 * Classe de mutation
 *
 * @see Mutation
 *
 * Cette mutation est une itération de OPT-2
 */
public class MutationOptLocale extends Mutation {

    public MutationOptLocale(Problem problem, Evaluation evaluation) {
        super(problem, evaluation);
    }

    @Override
    public void mutate(Path p) {
        int length = p.getPath().length;
        for (int i = rand.nextInt(length - 2); i < length - 2; i++)
            for (int j = i + 2; j < length - 1; j++) {
                double eval = evaluation.quickEvaluate(p), evalapres;
                Coordinates v = problem.getCoordinates(i),
                        sv = problem.getCoordinates(i + 1),
                        cp = problem.getCoordinates(j),
                        sp = problem.getCoordinates(j + 1);
                if (v.distance(sv) + cp.distance(sp) > v.distance(cp) + sv.distance(sp)) {
                    echangeOrdreEntreIEtJ(i + 1, j, p);
                    evalapres = evaluation.quickEvaluate(p);
                    if (evalapres >= eval)
                        echangeOrdreEntreIEtJ(i + 1, j, p);
                }
            }
    }
}
