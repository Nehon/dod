package com.jme.experiment.dod;

import com.jme.experiment.Node;
import com.jme3.util.BufferUtils;
import com.jme3.util.SafeArrayList;
import sun.misc.Unsafe;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class Graph2 implements Graph{

    private FloatBuffer localTransforms;
    private FloatBuffer worldTransforms;
    private IntBuffer hierarchy;
    private SafeArrayList<Node> nodes = new SafeArrayList<>(Node.class);
    private Node rootNode;

    public Graph2(Node rootNode, int size) {
        this.rootNode = rootNode;

        ByteBuffer bb = ByteBuffer.allocateDirect((size * 10 + 10) *4);
        bb.order(ByteOrder.nativeOrder());
        localTransforms = bb.asFloatBuffer();

        bb = ByteBuffer.allocateDirect((size * 10 + 10) *4);
        bb.order(ByteOrder.nativeOrder());
        worldTransforms = bb.asFloatBuffer();

        bb = ByteBuffer.allocateDirect((size + 1) *4);
        bb.order(ByteOrder.nativeOrder());
        hierarchy = bb.asIntBuffer();

        addNode(rootNode, -1);
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void addNode(Node n, int parentIndex){
        int index = getNextIndex();
        n.setIndex(index);
        n.setGraph(this);
        hierarchy.put(index, parentIndex);
        nodes.add(n);
        Transform t = new Transform(new Vec3(index, localTransforms),
                new Quat(index + 3, localTransforms),
                new Vec3(index + 7, localTransforms)
        );
        t = new Transform(new Vec3(index, worldTransforms),
                new Quat(index + 3, worldTransforms),
                new Vec3(index + 7, worldTransforms)
        );
    }

    public int getNextIndex(){
        return nodes.size();
    }

    public Node getParent(Node child){
        return nodes.get(hierarchy.get(child.getIndex()));
    }


    public void update(){
        int len = nodes.size();
        worldTransforms.rewind();
        localTransforms.rewind();
        for (int  i = 0; i < 10; i++) {
            worldTransforms.put(i, localTransforms.get(i));
        }
        hierarchy.position(1);
        for (int i = 1; i < len; i++) {
            int parent = hierarchy.get();
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

