package tsp.projects.frankenstein.crossover;

import tsp.evaluation.Path;

/**
 * @author Rafaël BACHOURIAN
 * @author Enes USTA
 *
 * @see Crossover
 *
 * Ce croisement ... voir le rapport
 *
 */

public class CrossoverMOC extends Crossover {

    @Override
    public Path[] crossover(Path p1, Path p2) {
        int length = p1.getPath().length;
        int pivot = getRandom().nextInt(length);
        int[] child1 = p1.getPath().clone();
        int[] child2 = p2.getPath().clone();
        boolean[] list1 = new boolean[length];
        boolean[] list2 = new boolean[length];
        int pivot1 = pivot, pivot2 = pivot;

        for (int i = 0; i < pivot; i++) {
            list1[child1[i]] = true;
            list2[child2[i]] = true;
        }

        for (int i = 0; i < length; i++) {
            if (!list2[child1[i]]) {
                child1[i] = p2.getPath()[pivot1];
                pivot1++;
            }
            if (!list1[child2[i]]) {
                child2[i] = p1.getPath()[pivot2];
                pivot2++;
            }
        }

        return new Path[]{new Path(child1), new Path(child2)};
    }
}
