package com.jme.experiment.dod;

import com.jme.experiment.Node;
import com.jme.experiment.Timer;

public class DOD {

    private int nbNodes;
    private int nbWarmup;
    private int nbRounds;

    public DOD(int nbNodes, int nbWarmup, int nbRounds) {
        this.nbNodes = nbNodes;
        this.nbWarmup = nbWarmup;
        this.nbRounds = nbRounds;
    }

    public void start(){
        Timer t = new Timer();

        Graph deep = new Graph1(new Node(), nbNodes);
        Node.buildDeepGraph(deep.getRootNode(), nbNodes);
        Graph flat =  new Graph1(new Node(), nbNodes);
        Node.buildFlatGraph(flat.getRootNode(),nbNodes);
        Graph balanced = new Graph1(new Node(), nbNodes);
        Node.buildBalancedGraph(balanced.getRootNode(),nbNodes);

        warmup(deep);
        t.start("DOD, deep graph");
        bench(deep);
        t.stop();

        warmup(flat);
        t.start("DOD, flat graph");
        bench(flat);
        t.stop();

        warmup(balanced);
        t.start("DOD, balanced graph");
        bench(balanced);
        t.stop();

    }

    public void start2(){
        Timer t = new Timer();

        Graph deep = new Graph2(new Node(), nbNodes);
        Node.buildDeepGraph(deep.getRootNode(),nbNodes);
        Graph flat =  new Graph2(new Node(), nbNodes);
        Node.buildFlatGraph(flat.getRootNode(),nbNodes);
        Graph balanced = new Graph2(new Node(), nbNodes);
        Node.buildBalancedGraph(balanced.getRootNode(),nbNodes);

        warmup(deep);
        t.start("DOD, deep graph");
        bench(deep);
        t.stop();

        warmup(flat);
        t.start("DOD, flat graph");
        bench(flat);
        t.stop();

        warmup(balanced);
        t.start("DOD, balanced graph");
        bench(balanced);
        t.stop();

    }


    private void warmup(Graph g){
        for (int i = 0; i < nbWarmup; i++) {
            g.update();
        }
    }

    private void bench(Graph g){
        for (int i = 0; i < nbRounds; i++) {
            g.update();
        }
    }
}
