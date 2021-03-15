package tsp.projects.genetic;


import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.InvalidProjectException;
import tsp.projects.Project;
import tsp.projects.ants.Colony;
import tsp.projects.genetic.crossover.Crossover;
import tsp.projects.genetic.crossover.CrossoverPMX;
import tsp.projects.genetic.mutation.Mutation;
import tsp.projects.recuit.Recuit;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Genetic extends Project {

    Utilities u = Utilities.getInstance();
    private int length;
    private static final int POPULATION_SIZE = 200;
    private Path[] population;
    private final Mutation[] mutList = Mutation.getMutationList(problem, evaluation);
    private HashMap<Mutation, Double> mutScore = new HashMap<>();
    double scoreDecrease = 0.99;
    private Mutation mut = mutList[u.getRandom().nextInt(mutList.length)];
    private Crossover cross = new CrossoverPMX();
    private Recuit recuit;

    int nbrun = 0, nbRunSansAmelio = 0, sinceLastMutChange = 0;
    double best = Double.MAX_VALUE;

    public Genetic(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Nous");
        this.setMethodName("Genetic");
    }

    @Override
    public void initialization() {
        try {
            recuit = new Recuit(evaluation);
        } catch (InvalidProjectException e) {
            e.printStackTrace();
        }
        nbRunSansAmelio = 0;
        nbrun = 0;
        sinceLastMutChange = 0;
        this.length = problem.getLength();
        this.population = new Path[POPULATION_SIZE];
        for (Mutation m : mutList) {
            mutScore.put(m, 1.0);
        }

        this.generateRandomPop(length);
        try {
            Colony colony = new Colony(evaluation);
            colony.initialization();

            for (int i = 0; i < 1e4 / length; i++) {
                colony.loop();
                this.population[i] = new Path(colony.getBestAnt().getPath());
            }
        } catch (InvalidProjectException e) {
            e.printStackTrace();
        }
        for (int i = 50; i < POPULATION_SIZE / 2; i++) {
            int[] tmp = u.getCheminVillePlusProche(problem);
            this.population[i] = new Path(tmp);
        }

        sortPopulation();
        recuit.initialization();
        evaluation.evaluate(population[0]);
        System.err.println("Fin init");
    }

    @Override
    public void loop() {
        if (nbRunSansAmelio % 1000 == 100) {
//            System.err.println("\nRecuit start");
            recuit.path = population[0];

//            if (nbRunSansAmelio > 10)
//                recuit.initialization2(population[0]);
            for (int i = 0; i < 20000; i++) {
                recuit.hillClimbingLoop();
            }
            double temp = evaluation.evaluate(recuit.path);
            if (temp < best) {

                System.out.println("Recuit améliore " + temp);
                nbRunSansAmelio = 0;
                best = temp;
                population[population.length / 10] = recuit.path;
            }
        } else
            try {
                nbrun++;
                sinceLastMutChange++;

                nextGeneration();
                mutatePop();
                sortPopulation();


                nbRunSansAmelio++;
                double temp = evaluation.evaluate(population[0]);
                oubliScoresMutation();

                if (temp < best) {

                    System.out.println("NbRunSansAmelio = " + nbRunSansAmelio + "\tmut = " + mut.getClass().getSimpleName() + "\tnewBest = " + temp);
                    mutScore.forEach((mut, score) -> System.out.print(" " + mut.getClass().getSimpleName() + "=" + String.format("%.2f", score)));
                    System.out.println();
                    nbRunSansAmelio = 0;
                    mutScore.put(mut, mutScore.get(mut) + Math.log(best - temp + 1));
                    best = temp;
                }

                //System.out.println(nbrun);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void oubliScoresMutation() {
        for (Mutation m : mutList) {
            mutScore.put(m, Math.max(1, mutScore.get(m) * scoreDecrease));
        }
    }

    private Mutation selectMutBasedOnScore() {
        double sum = 0;
        for (Mutation m : mutList) {
            sum += mutScore.get(m);
        }
        double num = u.getRandom().nextDouble() * sum;
        sum = 0;
        for (Mutation m : mutList) {
            sum += mutScore.get(m);
            if (num <= sum) {
                return m;
            }
        }
        return null;
    }

    /**
     * mutation de la population
     */
    public void mutatePop() {
        for (int i = POPULATION_SIZE / 2; i < POPULATION_SIZE; i++) {
//            while (u.getRandom().nextDouble() < 0.1* Math.log(i))
//            mut = mutList[u.getRandom().nextInt(mutList.length)];
            mut = selectMutBasedOnScore();
            assert mut != null;
            mut.mutate(population[i]);
        }
    }

    /**
     * Génère des path randoms pour notre population
     */
    public void generateRandomPop(int nbVilles) {
        for (int i = 0; i < POPULATION_SIZE; i++)
            population[i] = new Path(Path.getRandomPath(nbVilles));
    }

    /**
     * Création de la prochaine génération grâce à des croisements
     */
    public void nextGeneration() {
        Path[] childs;
        for (int i = POPULATION_SIZE - 1; i > POPULATION_SIZE * 3 / 4; i -= 2) {

            Path ind1 = population[u.rand.nextInt(POPULATION_SIZE / 2)], ind2 = population[u.rand.nextInt(POPULATION_SIZE / 2)];

            childs = cross.crossover(ind1, ind2);
            population[i] = childs[0];
            population[i - 1] = childs[1];
        }
    }

    /**
     * Tri les villes par évalution dans l'ordre croissant
     */
    public void sortPopulation() {
        Arrays.sort(population, Comparator.comparingDouble(p -> evaluation.quickEvaluate(p)));
    }
}
