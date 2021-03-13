package tsp.projects.genetic.mutation;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

public class MutationOptLocale extends Mutation{


    public MutationOptLocale(Problem problem, Evaluation evaluation) {
        super(problem, evaluation);
    }

    @Override
    public void mutate(Path p) {
        opt2(p);
    }

    public Path opt2(Path path) {
        int length = path.getPath().length;
        for (int i = 0; i < length - 2; i++) {
            for (int j = i+2; j < length - 1; j++) {
                double eval = evaluation.quickEvaluate(path),
                        evalapres;
                Coordinates v = problem.getCoordinates(i),
                        sv = problem.getCoordinates(i + 1),
                        p = problem.getCoordinates(j),
                        sp = problem.getCoordinates(j + 1);
                if (v.distance(sv) + p.distance(sp) > v.distance(p) + sv.distance(sp)) {
                    echangeOrdreEntreIEtJ(i, j, path);
                    evalapres = evaluation.quickEvaluate(path);
                    if (evalapres >= eval) {
                        echangeOrdreEntreIEtJ(i, j, path);
                    }
                    else {
                        return path;
                    }

                }
            }
        }
        return path;
    }
}
