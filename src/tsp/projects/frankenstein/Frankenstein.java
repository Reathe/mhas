package tsp.projects.frankenstein;


import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.frankenstein.ants.Colony;
import tsp.projects.frankenstein.crossover.Crossover;
import tsp.projects.frankenstein.crossover.CrossoverMOC;
import tsp.projects.frankenstein.mutation.*;
import tsp.projects.frankenstein.recuit.HillClimbing;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author USTA Enes
 * @author BACHOURIAN Rafael
 * Fait des trucs
 */
public class Frankenstein extends CompetitorProject {

    Utilities u = Utilities.getInstance();
    private int length;
    private static final int POPULATION_SIZE = 200;
    private Path[] population;
    private Mutation[] mutList;
    private final HashMap<Mutation, Double> mutScore = new HashMap<>();
    double scoreDecrease = 0.99;
    private Mutation mut;
    private final Crossover cross = new CrossoverMOC();
    private HillClimbing hillClimbing;

    int nbrun = 0, nbRunSansAmelio = 0, sinceLastMutChange = 0;
    double best = Double.MAX_VALUE;

    public Frankenstein(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("BACHOURIAN Rafael");
        this.addAuthor("USTA Enes");
        this.setMethodName("Frankenstein");
    }

    @Override
    public void initialization() {
        initMutation();
        initHillClimbing();

        nbRunSansAmelio = 0;
        nbrun = 0;
        sinceLastMutChange = 0;
        this.length = problem.getLength();
        this.population = new Path[POPULATION_SIZE];

        this.generateRandomPop(length);
        initPop();
        sortPopulation();
        evaluation.evaluate(population[0]);
    }

    private void initPop() {
        try {
            Colony colony = new Colony(evaluation);
            colony.initialization();

            for (int i = 0; i < 50; i++) {
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
    }

    private void initHillClimbing() {
        try {
            hillClimbing = new HillClimbing(evaluation);
            hillClimbing.initialization();
        } catch (InvalidProjectException e) {
            e.printStackTrace();
        }
    }

    private void initMutation() {
        mutList = new Mutation[]{
                new MutationEchangeRandom(problem, evaluation),
                new MutationDecalageRandom(problem, evaluation),
                new MutationInverseOrdreZoneRandom(problem, evaluation),
                new MutationEchangeVoisin(problem, evaluation)
        };
        mut = mutList[u.getRandom().nextInt(mutList.length)];
        for (Mutation m : mutList) {
            mutScore.put(m, 1.0);
        }
    }

    @Override
    public void loop() {
        if (nbRunSansAmelio % 1000 == 100) {
            hillClimbing.path = population[0];
            hillClimbing.fSi = best;
            for (int i = 0; i < 20000; i++) {
                hillClimbing.loop();
            }
            double temp = evaluation.evaluate(hillClimbing.path);
            if (temp < best) {
                nbRunSansAmelio = 0;
                best = temp;
                population[population.length / 10] = hillClimbing.path;
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
                    nbRunSansAmelio = 0;
                    mutScore.put(mut, mutScore.get(mut) + Math.log(best - temp + 1));
                    best = temp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Réduit le score de toutes les mutations
     */
    private void oubliScoresMutation() {
        for (Mutation m : mutList) {
            mutScore.put(m, Math.max(1, mutScore.get(m) * scoreDecrease));
        }
    }

    /**
     * Selectionne aléatoirement une mutations
     * La proba de selection est proportionnelle au score relatif aux autres mutation
     *
     * @return the mutation
     */
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
        return mutList[0];
    }

    /**
     * mutation de la population
     */
    public void mutatePop() {
        for (int i = POPULATION_SIZE / 2; i < POPULATION_SIZE; i++) {
            mut = selectMutBasedOnScore();
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
