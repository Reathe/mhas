package tsp.projects.frankenstein.mutation;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

/**
 * @author Rafaël BACHOURIAN
 * @author Enes USTA
 *
 * Classe de mutation
 * @see Mutation
 *
 * Effectue un échange en choisissant aléatoirement une ville et celle qui la suit
 */
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
