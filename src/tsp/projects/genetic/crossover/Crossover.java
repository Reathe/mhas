package tsp.projects.genetic.crossover;

import tsp.evaluation.Path;
import tsp.projects.genetic.Utilities;

public abstract class Crossover extends Utilities {
    public abstract Path[] crossover(Path p1, Path p2);
}
