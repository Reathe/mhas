package tsp.projects.frankenstein.ants;

import tsp.evaluation.Evaluation;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.frankenstein.Utilities;

import java.util.Arrays;

/**
 * @author Rafaël BACHOURIAN
 * @author Enes USTA
 */
public class Colony extends DemoProject {

    private final static double C = 1;
    private final static double EVAPORATION = 0.99;
    private final static double Q = 500;

    private final static int COLONY_SIZE = 2;
    public final static int ALPHA = 1;
    public final static int BETA = 24;

    private final Utilities util = Utilities.getInstance();
    private int nbVilles;
    private Ant[] colony;
    public double[][] pheromones;
    private double best = Double.MAX_VALUE;
    private Ant bestAnt;


    public Colony(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
    }

    @Override
    public void initialization() {
        this.nbVilles = evaluation.getProblem().getLength();
        colony = new Ant[COLONY_SIZE];
        pheromones = new double[nbVilles][nbVilles];

        for (int i = 0; i < COLONY_SIZE; i++)
            colony[i] = new Ant(nbVilles);
        for (int i = 0; i < nbVilles; i++)
            Arrays.fill(pheromones[i], C);
    }

    @Override
    public void loop() {
        clearColony();
        colonyParcour();
        updatePheromones();
        evaluation.evaluate(bestAnt.getPath());
    }

    /**
     * Réinitialise la colonie
     * C'est-à-dire les villes visitées, leur ville courante, et réassignation à une ville aléatoire
     */
    private void clearColony() {
        for (Ant ant : colony) ant.reset();
    }

    /**
     * Calcul des probabilités pour chaque ville vers laquelle une fourmi peut aller depuis sa ville actuelle
     * Si une ville à déjà été visité, la probabilité de celle-ci sera de 0
     *
     * @param ant : Fourmi dont on veut les probabilités
     * @return Tableau des probabilités pour la fourmi ant
     */
    public double[] calculateProbabilities(Ant ant) {
        double[] probabilities = new double[nbVilles];
        int i = ant.getCurrentCity();
        double alpha, beta, distanceIL;
        double pheromone = Double.MIN_VALUE;

        for (int l = 0; l < nbVilles; l++)
            if (!ant.visited(l)) {

                distanceIL = problem.getCoordinates(i).distance(problem.getCoordinates(l));
                alpha = util.pow(pheromones[i][l], Colony.ALPHA);
                beta = util.pow(1.0 / distanceIL, Colony.BETA);

                double res = alpha * beta;
                probabilities[l] = res;
                pheromone += res;

            } else probabilities[l] = 0.0;

        for (int j = 0; j < nbVilles; j++)
            if (ant.visited(j))
                probabilities[j] = 0.0;
            else
                probabilities[j] /= pheromone;

        return probabilities;
    }

    /**
     * Parcour de toute les fourmis
     */
    public void colonyParcour() {
        for (Ant ant : colony)
            for (int j = 0; j < nbVilles - 1; j++) {
                double[] probs = calculateProbabilities(ant);
                ant.visitCity(Utilities.getInstance().chooseProb(probs));
            }
    }

    /**
     * Mise à jour des phéromones
     */
    public void updatePheromones() {
        double contribution;
        double eval;
        for (int i = 0; i < nbVilles; i++)
            for (int j = 0; j < nbVilles; j++)
                if (pheromones[i][j] > C)
                    pheromones[i][j] *= EVAPORATION;

        for (Ant a : colony) {
            int[] p = a.getPath().getPath();

            eval = evaluation.evaluate(a.getPath());
            if (eval < best) {
                bestAnt = a;
                best = eval;
            }

            contribution = Q / eval;
            for (int i = 0; i < nbVilles - 1; i++)
                pheromones[p[i]][p[i + 1]] += contribution;

            pheromones[p[nbVilles - 1]][p[0]] += contribution;
        }
    }

    public Ant getBestAnt() {
        return bestAnt;
    }
}
