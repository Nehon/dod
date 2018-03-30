package com.jme.experiment.oop;

import com.jme.experiment.Node;
import com.jme.experiment.Timer;

public class OOP {

    private int nbNodes;
    private int nbWarmup;
    private int nbRounds;

    public OOP(int nbNodes, int nbWarmup, int nbRounds) {
        this.nbNodes = nbNodes;
        this.nbWarmup = nbWarmup;
        this.nbRounds = nbRounds;
    }

    public void start(){
        Timer t = new Timer();
        Node deep = new Node();
        Node.buildDeepGraph(deep, nbNodes);
        Node flat = new Node();
        Node.buildFlatGraph(flat, nbNodes);
        Node balanced = new Node();
        Node.buildBalancedGraph(balanced, nbNodes);

        warmup(deep);
        t.start("OOP, deep graph");
        bench(deep);
        t.stop();

        warmup(flat);
        t.start("OOP, flat graph");
        bench(flat);
        t.stop();

        warmup(balanced);
        t.start("OOP, balanced graph");
        bench(balanced);
        t.stop();

    }


    private void warmup(Node n){
        for (int i = 0; i < nbWarmup; i++) {
            n.update();
        }
    }

    private void bench(Node n){
        for (int i = 0; i < nbRounds; i++) {
            n.update();
        }
    }

}
