package tsp.projects.recuit;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.genetic.Utilities;
import tsp.projects.genetic.mutation.Mutation;

import java.util.Random;

public class Recuit extends DemoProject {
    private int length;
    private int transfoA = 0, transfoT = 0;
    private Path path;
    private double T;
    private final double lambda = 0.99;
    double fSi, fSpi, sommeFsi = 0;
    private final double p0 = 0.8;
    Random r = new Random(System.currentTimeMillis());
    private final Utilities u = Utilities.getInstance();
    private final Mutation[] mutList = Mutation.getMutationList(problem, evaluation);


    /**
     * Méthode d'évaluation de la solution
     *
     * @param evaluation
     * @throws InvalidProjectException
     */
    public Recuit(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Nous");
        this.setMethodName("Recuit");
    }

    @Override
    public void initialization() {

        length = this.problem.getLength();
        int[] chemin = u.getCheminVillePlusProche(this.problem);
        this.path = new Path(chemin);
        fSi = this.evaluation.evaluate(this.path);

        for (int i = 0; i < 100; i++) {
            echangeRandom(path);
            fSpi = evaluation.evaluate(path);
            sommeFsi += fSpi - fSi;
            fSi = fSpi;
        }

        this.T = -Math.abs(sommeFsi / 100) / Math.log(p0);

    }

    @Override
    public void loop() {

        transfoT++;
        Path temp = new Path(path);

        mutList[r.nextInt(mutList.length)].mutate(temp);

        fSpi = evaluation.evaluate(temp);
        double p = Math.min(1, Math.exp(-(fSpi - fSi) / T));

        if (fSpi < fSi || r.nextDouble() < p) {
            fSi = fSpi;
            transfoA++;
            path = temp;
        }
        if (transfoA % 12 == 0 || transfoT % 100 == 0)
            T *= lambda;

    }

    private void echangeRandom(Path path) {
        u.echange(r.nextInt(length), r.nextInt(length), path);
    }


}
