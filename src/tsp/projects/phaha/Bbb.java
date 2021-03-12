package tsp.projects.phaha;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.Random;

public class Bbb extends CompetitorProject {

    private int cptI, cptJ;
    private Path path;
    private int length;
    private Random r = new Random(System.currentTimeMillis());

    public Bbb(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Nous");
        this.setMethodName("Recuit");
    }

    @Override
    public void initialization() {
        this.length = problem.getLength();
        this.path = new Path(getCheminVillePlusProche());

    }

    @Override
    public void loop() {
        opt2();
    }




    public void opt2(){


            double oldL = evaluation.quickEvaluate(path);
            echange(a + 1, c, path);
            double newL = evaluation.evaluate(path);
            if (newL < oldL)
                echange(a + 1, c, path);
    }

    private int[] getCheminVillePlusProche() {
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

    private void echangeRandom(Path path) {
        int lastEchangei = r.nextInt(path.getPath().length);
        int lastEchangej = r.nextInt(path.getPath().length);
        echange(lastEchangei, lastEchangej, path);
    }

    private void echange(int i, int j, Path p) {
        int temp = p.getPath()[i];
        p.getPath()[i] = p.getPath()[j];
        p.getPath()[j] = temp;
    }

    private void echange(int i, int j, int[] tab) {
        int temp = tab[i];
        tab[i] = tab[j];
        tab[j] = temp;
    }
}
