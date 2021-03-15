package tsp.projects.ants;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.Arrays;
import java.util.Random;

public class Colony extends DemoProject {

    private final static double C = 1;
    private final static double EVAPORATION = 0.99;
    private final static double Q = 500;
//    private final static double antFactor = 0.01;
    private double randomFactor = 0.01;

    private int COLONY_SIZE = 3;
    public final static double ALPHA = 1.00;
    public final static double BETA = 25.00;

    private Random r = new Random(System.currentTimeMillis());
    private int nbVilles;
    private Ant[] colony;
    public double[][] pheromones;
    int nbloop=0;
    private double best = Double.MAX_VALUE;
    public Ant bestAnt;


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
        nbloop++;
        try {
            clearColony();

            // Parcours des fourmis
            colonyParcour();

            // MAJ des pheromones
            updatePheromones();
            evaluation.evaluate(bestAnt.getPath());
//            System.out.println(nbloop);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearColony() {
        for (Ant ant : colony) ant.reset();
    }

    public double[] calculateProbabilities(Ant ant) {
        //La ville actuelle
        double[] probabilities = new double[nbVilles];
        int i = ant.getCurrentCity();
        double alpha;
        double beta;
        double pheromone = Double.MIN_VALUE;
        for (int l = 0; l < nbVilles; l++) {
            if (!ant.visited(l)) {

                double distanceIL = problem.getCoordinates(i).distance(problem.getCoordinates(l));
                alpha = pow(pheromones[i][l], (int) Colony.ALPHA);
                beta = pow(1.0 / distanceIL, (int) Colony.BETA);

                double res = alpha * beta;
                probabilities[l] = res;
                pheromone += res;

            }else  {
                probabilities[l] = 0.0;
            }
        }

        for (int j = 0; j < nbVilles; j++) {
            if (ant.visited(j))
                probabilities[j] = 0.0;
            else {
                probabilities[j] = probabilities[j] / pheromone;
            }
        }

        return probabilities;
    }

    public double pow(double x, int e){
        double res = 1;
        for (int i = 0; i < e; i++) {
            res*=x;
        }
        return res;
    }
    public void updatePheromones() {
        for (int i = 0; i < nbVilles; i++) {
            for (int j = 0; j < nbVilles; j++) {
                if (pheromones[i][j]>C)
                    pheromones[i][j] *= EVAPORATION;
            }
        }
        double eval;
        for (Ant a : colony) {
            Path p = a.getPath();

            eval = evaluation.evaluate(a.getPath());
            if (eval < best) {
                bestAnt = a;
                best = eval;
                System.out.println(best);
            } else if (eval == Double.MAX_VALUE){
                System.out.println("explode");
            }

            double contribution = Q / eval;
            for (int i = 0; i < nbVilles - 1; i++) {
                pheromones[p.getPath()[i]][p.getPath()[i + 1]] += contribution;
            }
            pheromones[p.getPath()[nbVilles - 1]][p.getPath()[0]] += contribution;
        }
    }

    public void colonyParcour() throws Exception {
        for (int i = 0; i < colony.length; i++) {
            for (int j = 0; j < nbVilles - 1; j++) {
                double[] probs = calculateProbabilities(colony[i]);
                colony[i].visitCity(chooseProb(probs));
            }
            if (!evaluation.isValid(colony[i].getPath())) throw new Exception("Chemin invalide");
        }

    }

    private int chooseProb(double[] probs) {
        double sum = 0;
        for (double p : probs)
            sum+=p;

        double num = r.nextDouble()*sum;
        sum = 0;
        for (int i = 0; i < probs.length ; i++) {
            sum += probs[i];
            if (num <= sum)
                return i;
        }
        return -1;
    }

}
