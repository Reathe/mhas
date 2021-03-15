package tsp.projects.genetic.mutation;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.projects.genetic.Utilities;

/**
 * Classe abstraite permettant l'implémentation de mutation différentes
 *
 */
public abstract class Mutation extends Utilities {
    public Problem problem;
    public Evaluation evaluation;

    public Mutation(Problem problem, Evaluation evaluation){
        super();
        this.problem = problem;
        this.evaluation = evaluation;
    }

    /**
     * Effectue une mutation
     * @param p : Path sur lequel il faut faire la mutation
     */
    public abstract void mutate(Path p);

    /**
     *
     * @param problem Probleme pour lequel, il sera nécessaire de faire des mutations
     * @param evaluation : Evaluation pour laquel, il sera nécessaire de faire des mutations
     * @return : Liste de mutations déjà initialisée
     */
    public static Mutation[] getMutationList(Problem problem, Evaluation evaluation){
        return new Mutation[]{
//                new MutationVide(problem, evaluation),
//                new MutationOptLocale(problem,evaluation),
//                new MutationEchangeRandom(problem, evaluation),
                new MutationDecalageRandom(problem, evaluation),
                new MutationInverseOrdreZoneRandom(problem, evaluation),
//                new MutationEchangeVoisin(problem, evaluation)

        };
    }
}
