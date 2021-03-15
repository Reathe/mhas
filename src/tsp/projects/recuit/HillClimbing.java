package tsp.projects.recuit;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.genetic.Utilities;
import tsp.projects.genetic.mutation.Mutation;


/**
 * Heuristique Hill Climbiing
 *
 */
public class HillClimbing extends DemoProject {
    public Path path;
    double fSi, fSpi, sommeFsi = 0;
    private final Utilities util = Utilities.getInstance();
    private final Mutation[] mutList = Mutation.getMutationList(problem, evaluation);


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

        for (int i = 0; i < 100; i++) {
            fSpi = evaluation.evaluate(path);
            sommeFsi += fSpi - fSi;
            fSi = fSpi;
        }
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
