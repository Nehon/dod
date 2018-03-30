package com.jme.experiment.dod;

import java.nio.FloatBuffer;

public class Vec3 {

    private int index = -1;
    private float[] storage;
    private FloatBuffer nStorage;

    public Vec3() {
    }

    public Vec3(int index, float[] storage) {
        setup(index, storage);
    }

    public Vec3(int index, FloatBuffer storage) {
        setup(index, storage);
    }

    public final void setup(int index, float[] storage){
        this.index = index;
        this.storage = storage;
    }

    public final void setup(int index, FloatBuffer storage){
        this.index = index;
        this.nStorage = storage;
    }

    public void set(float x, float y, float z){
        if(nStorage != null){
            nStorage.position(index);
            nStorage.put(x);
            nStorage.put(y);
            nStorage.put(z);
            return;
        }
        storage[index] = x;
        storage[index + 1] = y;
        storage[index + 2] = z;
    }

    public void set(Vec3 v){
        storage[index] = v.storage[v.index];
        storage[index + 1] = v.storage[v.index+1];
        storage[index + 2] = v.storage[v.index+2];
    }

    public void add(Vec3 target, Vec3 vec3){
        int tIndex = target.index;
        float[] tStorage = target.storage;

        tStorage[tIndex] = storage[index] + vec3.storage[vec3.index];
        tStorage[tIndex+1] = storage[index+1] + vec3.storage[vec3.index+1];
        tStorage[tIndex+2] = storage[index+2] + vec3.storage[vec3.index+2];

    }

    public void mult(Vec3 target, Vec3 vec3){
        int tIndex = target.index;
        float[] tStorage = target.storage;

        tStorage[tIndex] = storage[index] * vec3.storage[vec3.index];
        tStorage[tIndex+1] = storage[index+1] * vec3.storage[vec3.index+1];
        tStorage[tIndex+2] = storage[index+2] * vec3.storage[vec3.index+2];

    }

    public static void mult(int targetIndex, float[]targetStore,
                       int idV1, float[]storage1,
                       int idV2, float[]storage2){
        targetStore[targetIndex] = storage1[idV1] * storage2[idV2];
        targetStore[targetIndex+1] = storage1[idV1+1] * storage2[idV2+1];
        targetStore[targetIndex+2] = storage1[idV1+2] * storage2[idV2+2];
    }

    public static void add(int ti, float[]ts,
                            int i1, float[]s1,
                            int i2, float[]s2){
        ts[ti++] = s1[i1++] + s2[i2++];
        ts[ti++] = s1[i1++] + s2[i2++];
        ts[ti] = s1[i1] + s2[i2];
    }

    public static void mult(int targetIndex, FloatBuffer targetStore,
                            int idV1, FloatBuffer storage1,
                            int idV2, FloatBuffer storage2){
        targetStore.put(targetIndex, storage1.get(idV1) * storage2.get(idV2));
        targetStore.put(targetIndex + 1,storage1.get(idV1 + 1) * storage2.get(idV2 +1));
        targetStore.put(targetIndex + 2, storage1.get(idV1 + 1) * storage2.get(idV2 +2));
    }

    public static void add(int targetIndex, FloatBuffer targetStore,
                           int idV1, FloatBuffer storage1,
                           int idV2,  FloatBuffer storage2){

        targetStore.put(targetIndex,storage1.get(idV1) + storage2.get(idV2));
        targetStore.put(targetIndex+1,storage1.get(idV1+1) + storage2.get(idV2+1));
        targetStore.put(targetIndex+2,storage1.get(idV1+2) + storage2.get(idV2+2));
    }


    public float getX(){
        return storage[index];
    }

    public float getY(){
        return storage[index + 1];
    }

    public float getZ(){
        return storage[index + 2];
    }

    public int getIndex() {
        return index;
    }

    public float[] getStorage() {
        return storage;
    }
}
