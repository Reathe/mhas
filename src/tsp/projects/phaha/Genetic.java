package tsp.projects.phaha;


import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Genetic extends CompetitorProject {

    public static final double MUTATION_RATE = 0.1;
    private int length;
    private Random r = new Random(System.currentTimeMillis());
    private static int POPULATION_SIZE = 100;
    private Path[] population;
    int nbrun = 0;

    public Genetic(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Nous");
        this.setMethodName("Recuit");
    }

    @Override
    public void initialization() {
        this.length = problem.getLength();
        this.population = new Path[POPULATION_SIZE];
        this.generateRandom(evaluation.getProblem().getLength());
        for (int i = 0; i < length / 10; i++) {
            int[] tmp = getCheminVillePlusProche();
            this.population[i] = new Path(tmp);
        }
        sortPopulation();
        evaluation.evaluate(population[0]);
    }

    @Override
    public void loop() {
        nbrun++;
        nextGeneration();
        mutateRandom();
        sortPopulation();
        evaluation.evaluate(population[0]);
        //System.out.println(nbrun);
    }

    public Path opt2(Path path) {
        for (int i = 0; i < length - 3; i++) {
            for (int j = i + 2; j < length - 1; j++) {
                double eval = evaluation.quickEvaluate(path),
                        evalapres;
                Coordinates v = problem.getCoordinates(i),
                        sv = problem.getCoordinates(i + 1),
                        p = problem.getCoordinates(j),
                        sp = problem.getCoordinates(j + 1);
                if (v.distance(sv) + p.distance(sp) > v.distance(p) + sv.distance(sp)) {
                    echangeOrdreEntreIEtJ(i + 1, j, path);
                    evalapres = evaluation.quickEvaluate(path);
                    if (evalapres > eval)
                        echangeOrdreEntreIEtJ(i + 1, j, path);
//                    else
//                        System.out.println("amélio " + eval + "->" + evalapres);
                    //System.out.println("oui");
                }
            }
        }
        return path;
    }

    /**
     * Génère des path randoms pour notre population
     */
    public void generateRandom(int nbVilles) {
        for (int i = 0; i < POPULATION_SIZE; i++)
            population[i] = new Path(Path.getRandomPath(nbVilles));
    }

    /**
     * Mutation aléatoire des éléments de notre population
     */
    public void mutateRandom() {
//        for (int i = 0; i < POPULATION_SIZE; i++) {
//            if (r.nextDouble() < MUTATION_RATE * 2) {
//                opt2(population[i]);
//            }
//        }
        for (int i = POPULATION_SIZE / 2; i < POPULATION_SIZE; i++)
            while (r.nextDouble() < MUTATION_RATE * 2)
                mutate(population[i]);
    }

    public void nextGeneration() {
        Path[] childs;
        for (int i = POPULATION_SIZE - 1; i > POPULATION_SIZE * 3 / 4; i -= 2) {
            childs = crossoverPMX(population[r.nextInt(POPULATION_SIZE / 2)], population[r.nextInt(POPULATION_SIZE / 2)]);
            population[i] = childs[0];
            population[i - 1] = childs[1];
        }
    }

    /**
     * Mutation d'une population p
     *
     * @param p : Population à muter
     */
    public void mutate(Path p) {
        echangeRandom(p);

//        for (int i = 0; i < this.problem.getLength(); i++)
//        echange(i, r.nextInt(this.problem.getLength()), p);
    }

    public Path[] crossoverPMX(Path p1, Path p2) {
        int v = r.nextInt(length);
        int[] child1 = p1.getPath().clone();
        int[] child2 = p2.getPath().clone();

        for (int i = 0; i <= v; i++) {
            echange(i, getIndice(p2.getPath()[i], child1), child1);
            echange(i, getIndice(p1.getPath()[i], child2), child2);
        }

        return new Path[]{new Path(child1), new Path(child2)};
    }

    private int getIndice(int val, int[] arr) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val) return i;
        return -1;
    }


    private int[] getCheminVillePlusProche() {
        int[] chemin = new int[length];
        int[] villeVisite = new int[length];

        chemin[0] = r.nextInt(length);
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

    /**
     * Tri les villes par évalution dans l'ordre croissant
     */
    public void sortPopulation() {
        Arrays.sort(population, new Comparator<Path>() {
            @Override
            public int compare(Path p1, Path p2) {
                return Double.compare(evaluation.quickEvaluate(p1), evaluation.quickEvaluate(p2));
            }
        });
    }

    private void echangeOrdreEntreIEtJ(int i, int j, Path p) {
        while (i < j) {
            echange(i, j, p);
            i++;
            j--;
        }
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
