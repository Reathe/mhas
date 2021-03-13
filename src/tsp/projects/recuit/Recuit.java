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
    private int lastEchangei, lastEchangej;
    Random r = new Random(System.currentTimeMillis());
    private Utilities utilities = new Utilities();
    private Mutation mut = mutList[u.getRandom().nextInt(mutList.length)];


    /**
     *
     * Let s = s0 For k = 0 through kmax (exclusive): T ← temperature( (k+1)/kmax )
     * Pick a random neighbour, snew ← neighbour(s) If P(E(s), E(snew), T) ≥
     * random(0, 1): s ← snew Output: the final state s
     */

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
        int n = 0;


        length = this.problem.getLength();
        int[] chemin = utilities.getCheminVillePlusProche(this.problem);
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
        mut.mutate(temp);
//        echangeRandom(path);
        fSpi = evaluation.evaluate(temp); // Lesconditions que j'ai pas capté
        double p = Math.min(1, Math.exp(-(fSpi - fSi) / T));

        if (fSpi < fSi || r.nextDouble() < p) {
            //System.out.println("oui");
            fSi = fSpi;
            transfoA++;
            path = temp;
        }// else echange(lastEchangei, lastEchangej, path);
        if (transfoA % 12 == 0 || transfoT % 100 == 0)
            T *= lambda;

    }

    private void echangeOrdreEntreIEtJ(int i, int j, Path p) {
        while (i < j) {
            echange(i, j, p);
            i++;
            j--;
        }
    }

    private void echangeRandom(Path path) {
        lastEchangei = r.nextInt(length);
        lastEchangej = r.nextInt(length);
        echange(lastEchangei, lastEchangej, path);
    }

    private void echange(int i, int j, Path p) {
        int temp = p.getPath()[i];
        p.getPath()[i] = p.getPath()[j];
        p.getPath()[j] = temp;
    }

}
