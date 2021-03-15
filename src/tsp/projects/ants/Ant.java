package tsp.projects.ants;

import tsp.evaluation.Path;
import tsp.projects.genetic.Utilities;

import java.util.Arrays;
import java.util.Random;

public class Ant {

    private final int nbVilles;
    private final boolean[] visited;
    private final int[] path;
    private Path p;
    private final Random random = Utilities.getInstance().getRandom();
    private int currentIndex = 0;

    /**
     * Constructeur de fourmi
     * @param length : Taille du problème / Nombre de villes
     */
    Ant(int length) {
        nbVilles = length;
        visited = new boolean[nbVilles];
        path = new int[nbVilles];
        this.reset();
    }

    /**
     * Réinitialisation de la fourmi
     * Remet son nombre de ville visitées à 0
     * La remet dans une ville aléatoire
     */
    public void reset(){
        currentIndex = 0;
        Arrays.fill(visited, false);
        this.visitCity(random.nextInt(nbVilles));
    }

    /**
     * Visite d'une ville
     * @param city : Ville à visiter
     */
    public void visitCity(int city){
        path[currentIndex] = city;
        visited[city] = true;
        currentIndex++;
    }

    /**
     *
     * @param l : La ville
     * @return : Si la ville a été visitée
     */
    boolean visited(int l) {
        return visited[l];
    }

    /**
     *
     * @return Le path de la fourmi
     */
    public Path getPath(){
        this.p = new Path(path);
        return this.p;
    }

    /**
     *
     * @return Ville actuelle de la fourmi
     */
    public int getCurrentCity() {
        return path[currentIndex-1];
    }

    @Override
    public String toString() {
        return "Ant{" +
                "nbVilles=" + nbVilles +
                ", visited=" + Arrays.toString(visited) +
                ", path=" + Arrays.toString(path) +
                ", p=" + p +
                ", r=" + random +
                ", currentIndex=" + currentIndex +
                '}';
    }
}
