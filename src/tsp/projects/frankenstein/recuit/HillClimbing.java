package tsp.projects.frankenstein.recuit;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.frankenstein.Utilities;
import tsp.projects.frankenstein.mutation.*;


/**
 * Heuristique Hill Climbiing
 *
 */
public class HillClimbing extends DemoProject {
    public Path path;
    public double fSi, fSpi, sommeFsi = 0;
    private final Utilities util = Utilities.getInstance();
    private Mutation[] mutList;


    public HillClimbing(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Nous");
        this.setMethodName("Recuit");
    }

    @Override
    public void initialization() {
        int[] chemin = util.getCheminVillePlusProche(this.problem);
        this.path = new Path(chemin);
        fSi = this.evaluation.evaluate(this.path);
        if (problem.getLength() < 300)
            mutList = new Mutation[]{
                    new MutationEchangeRandom(problem, evaluation),
                    new MutationDecalageRandom(problem, evaluation),
                    new MutationInverseOrdreZoneRandom(problem, evaluation),
                    new MutationEchangeVoisin(problem, evaluation)

            };
        else
            mutList = new Mutation[]{
                    new MutationDecalageRandom(problem, evaluation),
                    new MutationInverseOrdreZoneRandom(problem, evaluation)
            };
    }

    @Override
    public void loop() {
        Path temp = new Path(path);
        mutList[util.getRandom().nextInt(mutList.length)].mutate(temp);
        fSpi = evaluation.quickEvaluate(temp);

        if (fSpi < fSi) {
            fSi = fSpi;
            path = temp;
            evaluation.evaluate(path);
        }
    }
}
