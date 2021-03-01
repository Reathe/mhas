package tsp.projects.recuit;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.Random;

public class Recuit extends DemoProject {
    private int length;
    private int transfoA, transfoT = 99;
    private Path path;
    private double T;
    private double lambda = 0.99;
    double fSi, fSpi, sommeFsi = 0;
    private double p0 = 0.8;
    private int lastEchangei, lastEchangej;
    Random r = new Random(System.currentTimeMillis());

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

        int[] chemin = getCheminVillePlusProche();
        this.path = new Path(chemin);
        fSi = this.evaluation.evaluate(this.path);

        for (int i = 0; i < 100; i++) {
            path = echangeRandom(path);
            fSpi = evaluation.evaluate(path);
            sommeFsi += fSpi - fSi;
            fSi = fSpi;
        }

        this.T = -(sommeFsi / 100) / Math.log(p0);

    }

    private int[] getCheminVillePlusProche() {
        this.length = this.problem.getLength();
        int[] chemin = new int[length];
        int[] villeVisite = new int[length];

        chemin[0] = 0;
        Coordinates coordi;
        double min;
        int minVille = 0;
        double tempDistance;

        for (int i = 1; i < length; i++) {
            Coordinates coord = problem.getCoordinates(i - 1);
            min = Double.MAX_VALUE;
            for (int j = 1; j < chemin.length; j++) {
                coordi = problem.getCoordinates(j);
                tempDistance = coord.distance(coordi);
                if (tempDistance < min && villeVisite[j] == 0) {
                    min = tempDistance;
                    minVille = j;
                }
            }
            chemin[i] = minVille;
            villeVisite[minVille] = i;
        }
        return chemin;
    }

    @Override
    public void loop() {

        transfoT++;
        Path temp = echangeRandom(path);
        fSpi = evaluation.evaluate(temp); // Lesconditions que j'ai pas capté
        double p = Math.min(1, Math.exp(-(fSpi - fSi) / T));

        if (fSpi < fSi || r.nextDouble() < (1 - p)) {
            //System.out.println("oui");
            path = temp;
            fSi = fSpi;
            transfoA++;
        }
        if (transfoA % 12 == 0 || transfoT % 100 == 0)
            T *= lambda;

    }

    private Path echangeRandom(Path path) {
        Path newPath = new Path(path.getPath().clone());
        lastEchangei = r.nextInt(length);
        lastEchangej = r.nextInt(length);
        echange(lastEchangei, lastEchangej, newPath);

        return newPath;
    }

    private void echange(int i, int j, Path p) {
        int temp = p.getPath()[i];
        p.getPath()[i] = p.getPath()[j];
        p.getPath()[j] = temp;
    }

}
