package tsp.projects.genetic.crossover;

import tsp.evaluation.Path;
import tsp.projects.genetic.Utilities;

/**
 * Classe abstraite de cross over, permettant l'implémentation de plusieur croisement entre deux Path
 *
 */
public abstract class Crossover extends Utilities {
    public abstract Path[] crossover(Path p1, Path p2);
}
