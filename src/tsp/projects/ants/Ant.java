package tsp.projects.ants;

import tsp.evaluation.Path;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Ant {

    private int nbVilles;
    private boolean[] visited;
    private int[] path;
    private Path p;
    private Random r = new Random(System.currentTimeMillis());


    private int currentIndex = 0;

    Ant(int length) {
        nbVilles = length;
        visited= new boolean[nbVilles];
        path = new int[nbVilles];
        this.reset();
    }

    public void reset(){
        currentIndex = 0;
        Arrays.fill(visited, false);
        this.visitCity(r.nextInt(nbVilles));
    }

    public void visitCity(int city){

        try{
            if(visited[city]) throw new Exception("Ville n°"+city+" déjà visitée\n"+this.toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }
        path[currentIndex] = city;
        visited[city] = true;
        currentIndex++;
    }



    boolean visited(int l) {
        return visited[l];
    }

    public Path getPath(){
        this.p = new Path(path);
        return this.p;
    }

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
                ", r=" + r +
                ", currentIndex=" + currentIndex +
                '}';
    }
}
