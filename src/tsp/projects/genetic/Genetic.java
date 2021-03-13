package tsp.projects.genetic;


import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.InvalidProjectException;
import tsp.projects.Project;
//import tsp.projects.ants.zColony;
import tsp.projects.genetic.crossover.Crossover;
import tsp.projects.genetic.crossover.CrossoverPMX;
import tsp.projects.genetic.mutation.Mutation;

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
    double scoreDecrease = 0.6;
    private Mutation mut = mutList[u.getRandom().nextInt(mutList.length)];
    private Crossover cross = new CrossoverPMX();


    int nbrun = 0, nbRunSansAmelio = 0, sinceLastMutChange = 0;
    double best = Double.MAX_VALUE;

    public Genetic(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Nous");
        this.setMethodName("Genetic");
    }

    @Override
    public void initialization() {
        this.length = problem.getLength();
        this.population = new Path[POPULATION_SIZE];
        for (Mutation m : mutList) {
            mutScore.put(m,1.0);
        }

        this.generateRandomPop(length);
        for (int i = 0; i < POPULATION_SIZE / 2; i++) {
            int[] tmp = u.getCheminVillePlusProche(problem);
            this.population[i] = new Path(tmp);
        }

        sortPopulation();
        evaluation.evaluate(population[0]);
        System.out.print(" Fin init");
    }

    @Override
    public void loop() {
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

                System.out.println(" NbRunSansAmelio = " + nbRunSansAmelio + "\tmut = " + mut.getClass().getSimpleName() + "\tnewBest = " + temp);
//                System.out.println(mutScore);
                nbRunSansAmelio = 0;
                mutScore.put(mut, mutScore.get(mut)+Math.log(best-temp+1));
                best = temp;
            } else if (nbRunSansAmelio>1000 && nbRunSansAmelio%10 == 0){
                mut = mutList[0];
                for (int i = 0; i < 10; i++) {
                    mutatePop();
                }
            }
            if (sinceLastMutChange > 10) {
                sinceLastMutChange = 0;
                mut = SelectMutBasedOnScore();
//                System.out.println(mut.getClass().getSimpleName());
            }

            //System.out.println(nbrun);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void oubliScoresMutation() {
        for (Mutation m : mutList) {
            if (mutScore.get(m)>1)
                mutScore.put(m, mutScore.get(m)*scoreDecrease);
        }
    }

    private Mutation SelectMutBasedOnScore() {
        double sum = 0;
        for (Mutation m : mutList) {
            sum+=mutScore.get(m);
        }
        double num = u.getRandom().nextDouble()*sum;
        sum = 0;
        for (Mutation m : mutList) {
            sum += mutScore.get(m);
            if (num <= sum)
            {
                return m;
            }
        }
        return null;
    }

    /**
     * mutation de la population
     */
    public void mutatePop() {
        for (int i = POPULATION_SIZE / 3; i < POPULATION_SIZE; i++) {
//            while (u.getRandom().nextDouble() < 0.1* Math.log(i))
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
        for (int i = POPULATION_SIZE-1; i > POPULATION_SIZE/2; i -= 2) {

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
