package tsp.projects.genetic;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

import java.util.Random;


/**
 * Classe utilitaire réunissant l'ensemble de fonction utilisée dans notre projet
 *
 * Utilise le Design Pattern Singleton
 */
public class Utilities {
    public Random rand = new Random(System.currentTimeMillis());
    protected Utilities() {}

    private static Utilities instance = null;

    /**
     * Design Pattern : Singleton
     * @return l'unique instance Utilities, pas besoin de plus
     */
    public static Utilities getInstance(){
        if(instance == null)
            instance = new Utilities();
        return instance;
    }

    /**
     *
     * @return Un objet Random pour de l'aléatoire
     */
    public Random getRandom() {
        return rand;
    }

    /**
     * Echange deux ville i et j sur un path p
     *
     * @param i : Ville
     * @param j : Ville
     * @param p : Path sur lequel il faut faire l'échange
     */
    public void echange(int i, int j, Path p) {
        int temp = p.getPath()[i];
        p.getPath()[i] = p.getPath()[j];
        p.getPath()[j] = temp;
    }

    /**
     * Echange deux ville i et j sur un tableau d'entiers tab
     *
     * @param i : Ville
     * @param j : Ville
     * @param tab : Tableau d'entier sur lequel il faut faire l'échange
     */
    public void echange(int i, int j, int[] tab) {
        int temp = tab[i];
        tab[i] = tab[j];
        tab[j] = temp;
    }


    /**
     * Echange l'ordre des villes allant de i à j
     * C'est-à-dire : 1 2 3 4 5 6 avec i= 3 et j = 6
     * => 1 2 6 5 4 3
     *
     * @param i : Ville
     * @param j : Ville
     * @param p : Path qu'il faut modifier
     */
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


    /**
     * Décalagedes villes allant de i à j
     * C'est-à-dire : 1 2 3 4 5 6 avec i= 3 et j = 6
     * => 1 2 6 3 4 5
     *
     * @param i : Ville
     * @param j : Ville
     * @param p : Path qu'il faut modifier
     */
    public void decalageDeIversJ(int i, int j, Path p) {
        int villeADecal = p.getPath()[i];
        int[] path = p.getPath();
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        while (i < j) {
            path[i] = path[i + 1];
            i++;
        }
        path[j] = villeADecal;
    }

    /**
     *
     * @param val : Valeur dont il faut trouver l'indice
     * @param arr : Tableau dans lequel il faut trouver l'indice de val
     * @return  l'indice de val dans arr, si val n'est pas dans arr : retourne -1
     */
    public int getIndice(int val, int[] arr) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val) return i;
        return -1;
    }

    /**
     * Détermine un tableau d'entier pour un Path en utilisant l'heuristique du plus proche voisin
     *
     * @param problem : Problème pour lequel on veut notre path
     * @return : Tableau d'entier (pour un Path)
     */
    public int[] getCheminVillePlusProche(Problem problem) {
        Coordinates coord, coordi;
        double min, tempDistance;
        int minVille;
        int length = problem.getLength();
        int[] chemin = new int[length];
        int[] villeVisite = new int[length];

        chemin[0] = rand.nextInt(length);
        villeVisite[chemin[0]] = 1;

        for (int i = 1; i < length; i++) {
            coord = problem.getCoordinates(chemin[i - 1]);
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


    /**
     * Choisit aléatoirement un index en fonction un tableau de probabilités
     * @param probs : Tableau de probabilités
     * @return : Un index, sinon -1
     */
    public int chooseProb(double[] probs) {
        double sum = 0;
        for (double p : probs)
            sum+=p;

        double num = rand.nextDouble()*sum;
        sum = 0;
        for (int i = 0; i < probs.length ; i++) {
            sum += probs[i];
            if (num <= sum)
                return i;
        }
        return -1;
    }


    /**
     * Calcul la puissance d'un nombre
     * Il est possible d'utiliser la librairie Math.pow
     * @see Math
     * Toutefois Math est plus lourd, puisqu'elle doit gérer des doubles
     *
     * @param x : Entier qu'il faut mettre sous une puissance
     * @param e : Puissance
     * @return Retourne x^e
     */
    public double pow(double x, int e){
        double res = 1;
        for (int i = 0; i < e; i++) {
            res*=x;
        }
        return res;
    }

    /**
     *
     * @param arr : Tableau de double
     * @return : Indice de la valeur maximale du tableau
     */
    public int getMax(double[] arr) {
        int maxI = 0;
        for (int p = 1; p < arr.length; p++) {
            if (arr[p] > arr[maxI])
                maxI = p;
        }
        return maxI;
    }
}
