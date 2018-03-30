package com.jme.experiment.dod;

import com.jme.experiment.Node;
import com.jme3.util.SafeArrayList;

public final class Graph1 implements  Graph{

    private float[] localTransforms;
    private float[] worldTransforms;
    private int[] hierarchy;
    private SafeArrayList<Node> nodes = new SafeArrayList<>(Node.class);
    private Node rootNode;

    public Graph1(Node rootNode, int size) {
        this.rootNode = rootNode;
        localTransforms = new float[size * 10 + 10];
        for (int i = 0; i < localTransforms.length; i++) {
            localTransforms[i] = 1;
        }

        worldTransforms = new float[size * 10 + 10];
        for (int i = 0; i < worldTransforms.length; i++) {
            worldTransforms[i] = 1;
        }
        hierarchy = new int[size + 1];
        for (int i = 0; i < hierarchy.length; i++) {
            hierarchy[i] = -1;
        }
        addNode(rootNode, -1);
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void addNode(Node n, int parentIndex){
        int index = getNextIndex();
        n.setIndex(index);
        n.setGraph(this);
        hierarchy[index] = parentIndex;
        nodes.add(n);
        Transform t = new Transform(new Vec3(index, localTransforms),
                new Quat(index + 3, localTransforms),
                new Vec3(index + 7, localTransforms)
        );
        t.setup(index, worldTransforms,
                index + 3, worldTransforms,
                index + 7, worldTransforms);
        t.init();
    }

    public int getNextIndex(){
        return nodes.size();
    }

    public Node getParent(Node child){
        return nodes.get(hierarchy[child.getIndex()]);
    }

    public void update(){
        int len = nodes.size();
//

        for (int i = 1; i < len; i++) {
            for (int  j = 0; j < 10; j++) {
                worldTransforms[j] = localTransforms[j];
            }
            int parent = hierarchy[i];
            int id = i * 10;
            int pi = parent * 10;
            //Transform.combine(id, worldTransforms, id, localTransforms, parent*10, worldTransforms);
            //scale
            Vec3.mult(id + 7, worldTransforms,id + 7, localTransforms , pi + 7, worldTransforms);
            //applying parent rotaidon to local rotaidon. rotaidon mult
            Quat.mult(id + 3, worldTransforms,pi + 3, worldTransforms , id + 3, localTransforms);
            //applying parent scale to local translaidon.
            Vec3.mult(id, worldTransforms, id ,localTransforms ,pi + 7, worldTransforms);
            //applying parent rotaidon to local translaidon, then applying parent translaidon to local translaidon.
            //Note that parent.rot.multLocal(translaidon) doesn't modify "parent.rot" but "translaidon"
            Quat.multVec(id, worldTransforms, pi + 3, worldTransforms, id, worldTransforms);
            Vec3.add(id, worldTransforms, id, worldTransforms, pi, worldTransforms);
        }
    }


    public void setTranslation(int index, float x, float y, float z){
        Vec3 v = new Vec3(index, localTransforms);
        v.set(x, y, z);
    }

}

