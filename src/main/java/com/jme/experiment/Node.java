package com.jme.experiment;

import com.jme.experiment.dod.Graph;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.util.SafeArrayList;


public class Node {

    private Transform localTransform = new Transform();
    private Transform worldTransform = new Transform();
    private SafeArrayList<Node> children = new SafeArrayList<>(Node.class);
    private Node parent;
    private int index = -1;
    private Graph graph;

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setLocalTranslation(float x, float y, float z){
        if(graph!= null) {
            graph.setTranslation(index, x, y, z);
            return;
        }
        localTransform.setTranslation(x, y, z);
    }


    public Transform getLocalTransform() {
        return localTransform;
    }

    public void setLocalTransform(Transform localTransform) {
        this.localTransform = localTransform;
    }

    public Transform getWorldTransform() {
        return worldTransform;
    }

    public void setWorldTransform(Transform worldTransform) {
        this.worldTransform = worldTransform;
    }

    public void addChild(Node node){
        if(graph != null){
            graph.addNode(node, index);
            return;
        }
        children.add(node);
        node.parent = this;
    }

    public Node getParent() {
        return parent;
    }

    public SafeArrayList<Node> getChildren() {
        return children;
    }

    public void update(){
        if(parent == null ){
            worldTransform.set(localTransform);
        } else {
            combine(worldTransform, localTransform, parent.getWorldTransform());
            //localTransform.combineWithParent(parent.getWorldTransform());
        }
        for (Node child : children.getArray()) {
            child.update();
        }
    }

    public static void combine(Transform t, Transform s1, Transform s2){
        //scale
        multVec(t.getScale(), s1.getScale(), s2.getScale());
        //applying parent rotation to local rotation. rotation mult
        multQuat(t.getRotation(), s2.getRotation(), s1.getRotation());
        //applying parent scale to local translation.
        multVec(t.getTranslation(), s1.getTranslation(), s2.getTranslation());
        //applying parent rotation to local translation, then applying parent translation to local translation.
        //Note that parent.rot.multLocal(translation) doesn't modify "parent.rot" but "translation"
        multVecQuat(t.getTranslation(), s2.getRotation(), t.getTranslation());
        addVec(t.getTranslation(), s2.getTranslation(), t.getTranslation());
    }

    private static void addVec(Vector3f t, Vector3f v1, Vector3f v2){
        t.x = v1.x + v2.x;
        t.y = v1.y + v2.y;
        t.z = v1.z + v2.z;
    }

    private static void multVec(Vector3f t, Vector3f v1, Vector3f v2){
        t.x = v1.x * v2.x;
        t.y = v1.y * v2.y;
        t.z = v1.z * v2.z;
    }

    private static void multVecQuat(Vector3f t, Quaternion q1, Vector3f v){
        float x = q1.getX();
        float y = q1.getY();
        float z = q1.getZ();
        float w = q1.getW();

        if (v.x == 0 && v.y == 0 && v.z == 0) {
            t.set(0, 0, 0);
        } else {
            float vx = v.x, vy = v.y, vz = v.z;
            t.x = w * w * vx + 2 * y * w * vz - 2 * z * w * vy + x * x
                    * vx + 2 * y * x * vy + 2 * z * x * vz - z * z * vx - y
                    * y * vx;
            t.y = 2 * x * y * vx + y * y * vy + 2 * z * y * vz + 2 * w
                    * z * vx - z * z * vy + w * w * vy - 2 * x * w * vz - x
                    * x * vy;
            t.z = 2 * x * z * vx + 2 * y * z * vy + z * z * vz - 2 * w
                    * y * vx - y * y * vz + 2 * w * x * vy - x * x * vz + w
                    * w * vz;
        }
    }

    private static void multQuat(Quaternion t, Quaternion q1, Quaternion q2){
        float qw = q2.getW(), qx = q2.getX(), qy = q2.getY(), qz = q2.getZ();
        float w = q1.getW(), x = q1.getX(), y = q1.getY(), z = q1.getZ();

        float rx = x * qw + y * qz - z * qy + w * qx;
        float ry = -x * qz + y * qw + z * qx + w * qy;
        float rz = x * qy - y * qx + z * qw + w * qz;
        float rw = -x * qx - y * qy - z * qz + w * qw;
        t.set(rx, ry,rz, rw);
    }


    public static void buildDeepGraph(Node root,int nbNodes) {
        Node parent = root;
        for (int i = 0; i < nbNodes; i++) {
            Node child = new Node();
            parent.addChild(child);
            child.setLocalTranslation(FastMath.nextRandomFloat(),FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
            parent = child;
        }
    }

    public static void buildFlatGraph(Node root,int nbNodes) {
        for (int i = 0; i < nbNodes; i++) {
            Node child = new Node();
            root.addChild(child);
            child.setLocalTranslation(FastMath.nextRandomFloat(),FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
        }
    }

    public static void buildBalancedGraph(Node root,int nbNodes) {
        Node parent = root;
        int depth = 0;
        for (int i = 0; i < nbNodes; i++) {
            Node child = new Node();
            parent.addChild(child);
            child.setLocalTranslation(FastMath.nextRandomFloat(),FastMath.nextRandomFloat(), FastMath.nextRandomFloat());

            if (i%4 == 0){
                depth ++;
                parent = child;
            }
//            if( depth > 5 ){
//                parent = parent.getParent();
//            }
        }

    }
}
