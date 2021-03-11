package tsp.projects.recuit;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

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
        int[] chemin = Path.getRandomPath(length);//getCheminVillePlusProche();
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

    public Path opt2(Path path) {
        for (int i = 0; i < length - 2; i++) {
            for (int j = i + 2; j < length - 1; j++) {
                double eval = evaluation.quickEvaluate(path),
                        evalapres;
                Coordinates v = problem.getCoordinates(i),
                        sv = problem.getCoordinates(i + 1),
                        p = problem.getCoordinates(j),
                        sp = problem.getCoordinates(j + 1);
                if (v.distance(sv) + p.distance(sp) > v.distance(p) + sv.distance(sp)) {
                    Path newp = new Path(path);
                    echangeOrdreEntreIEtJ(i + 1, j, newp);
                    evalapres = evaluation.quickEvaluate(path);
                    if (evalapres > eval) {

                    } else {
                        path = newp;
                        //System.out.println("amélio " + eval + "->" + evalapres);
                    }
                    //System.out.println("oui");
                }
            }
        }
        return path;
    }

    @Override
    public void loop() {

        transfoT++;
        echangeRandom(path);
        fSpi = evaluation.evaluate(path); // Lesconditions que j'ai pas capté
        double p = Math.min(1, Math.exp(-(fSpi - fSi) / T));

        if (fSpi < fSi || r.nextDouble() < p) {
            //System.out.println("oui");
            fSi = fSpi;
            transfoA++;
        } else echange(lastEchangei, lastEchangej, path);
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
