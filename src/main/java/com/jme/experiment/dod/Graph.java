package com.jme.experiment.dod;

import com.jme.experiment.Node;

public interface Graph {

    public Node getRootNode();

    public void addNode(Node n, int parentIndex);

    public Node getParent(Node child);


    public void update();


    public void setTranslation(int index, float x, float y, float z);
}
