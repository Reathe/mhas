package tsp.projects.opt2;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

public class opt2 extends CompetitorProject {
    int length = problem.getLength();
    Path sol;
    public opt2(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
    }

    @Override
    public void initialization() {
//        int[] solint = Path.getRandomPath(length);
        int[] solint = getCheminVillePlusProche();
        sol = new Path(solint);
    }

    @Override
    public void loop() {
        opt2(sol);
        evaluation.evaluate(sol);
    }

    public Path opt2(Path path) {
        for (int i = 0; i < length - 2; i++) {
            for (int j = i+2; j < length - 1; j++) {
                double eval = evaluation.quickEvaluate(path),
                        evalapres;
                Coordinates v = problem.getCoordinates(i),
                        sv = problem.getCoordinates(i + 1),
                        p = problem.getCoordinates(j),
                        sp = problem.getCoordinates(j + 1);
                if (v.distance(sv) + p.distance(sp) > v.distance(p) + sv.distance(sp)) {
                    echangeOrdreEntreIEtJ(i, j, path);
                    evalapres = evaluation.quickEvaluate(path);
                    if (evalapres > eval) {
                        echangeOrdreEntreIEtJ(i + 1, j, path);
                    }
                    else {
                        //return path;
                    }
                    //System.out.println("oui");
                }
            }
        }
        return path;
    }

    private void echangeOrdreEntreIEtJ(int i, int j, Path p) {
        while (i < j) {
            echange(i, j, p);
            i++;
            j--;
        }
    }
    private void echange(int i, int j, Path p) {
        int temp = p.getPath()[i];
        p.getPath()[i] = p.getPath()[j];
        p.getPath()[j] = temp;
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
}
