package tsp.projects.genetic;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

import java.util.Random;

public class Utilities {
    public Random rand = new Random(System.currentTimeMillis());

    private static Utilities instance = null;

    protected Utilities() {
    }

    public static Utilities getInstance(){
        if(instance == null)
            instance = new Utilities();
        return instance;
    }

    public Random getRandom() {
        return rand;
    }

    public void echangeOrdreEntreIEtJ(int i, int j, Path p) {
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        while (i < j) {
            echange(i, j, p);
            i++;
            j--;
        }
    }

    public void echange(int i, int j, Path p) {
        int temp = p.getPath()[i];
        p.getPath()[i] = p.getPath()[j];
        p.getPath()[j] = temp;
    }

    public void echange(int i, int j, int[] tab) {
        int temp = tab[i];
        tab[i] = tab[j];
        tab[j] = temp;
    }


    public void decalageDeIversJ(int i, int j, Path p) {
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        int villeADecal = p.getPath()[i];
        int[] path = p.getPath();
        while (i < j) {
            path[i] = path[i + 1];
            i++;
        }
        path[j] = villeADecal;
    }

    /**
     * Donne l'indice de val dans arr, si val n'est pas dans arr, retourne -1
     *
     * @param val
     * @param arr
     * @return
     */
    public int getIndice(int val, int[] arr) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val) return i;
        return -1;
    }

    public int[] getCheminVillePlusProche(Problem problem) {
        int length = problem.getLength();
        int[] chemin = new int[length];
        int[] villeVisite = new int[length];

        chemin[0] = rand.nextInt(length);
        villeVisite[chemin[0]] = 1;

        Coordinates coordi;
        double min;
        int minVille;
        double tempDistance;

        for (int i = 1; i < length; i++) {
            Coordinates coord = problem.getCoordinates(chemin[i - 1]);
            min = Double.MAX_VALUE;
            minVille = -1;
            for (int j = 0; j < chemin.length; j++) {
                if (villeVisite[j] != 0) continue;

                coordi = problem.getCoordinates(j);
                tempDistance = coord.distance(coordi);
                if (tempDistance < min) {
                    min = tempDistance;
                    minVille = j;
                }
            }
            chemin[i] = minVille;
            villeVisite[minVille] = 1;
        }
        return chemin;
    }


    public int getMax(double[] arr) {
        int maxI = 0;
        for (int p = 1; p < arr.length; p++) {
            if (arr[p] > arr[maxI])
                maxI = p;
        }
        return maxI;
    }
}
