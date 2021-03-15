package tsp.projects.frankenstein.mutation;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

/**
 * @author Rafaël BACHOURIAN
 * @author Enes USTA
 * Classe de mutation
 * @see Mutation
 *
 * Effectue une inversion de l'ordre des villes entre deux villes choisies aléatoirement
 */
public class MutationInverseOrdreZoneRandom extends Mutation {

    public MutationInverseOrdreZoneRandom(Problem problem, Evaluation evaluation) {
        super(problem, evaluation);
    }

    @Override
    public void mutate(Path p) {
        echangeOrdreEntreIEtJ(getRandom().nextInt(p.getPath().length), getRandom().nextInt(p.getPath().length), p);
    }
}
