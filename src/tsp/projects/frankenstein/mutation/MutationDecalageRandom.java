package tsp.projects.frankenstein.mutation;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

/**
 * Classe de mutation
 * @see Mutation
 *
 * Effectue un décalage d'une ville aléatoire i vers une autre ville aléatoire j
 */
public class MutationDecalageRandom extends Mutation{

    public MutationDecalageRandom(Problem problem, Evaluation evaluation) {
        super(problem, evaluation);
    }

    @Override
    public void mutate(Path p) {
//        do {
            decalageDeIversJ(getRandom().nextInt(p.getPath().length), getRandom().nextInt(p.getPath().length), p);
//        }while (getRandom().nextDouble() < 0.5);
    }
}
