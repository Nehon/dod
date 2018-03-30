package com.jme.experiment.dod;

import java.nio.FloatBuffer;

public class Quat {

    private int index = -1;
    private float[] storage;
    private FloatBuffer nStorage;

    public Quat() {
    }

    public Quat(int index, float[] storage) {
        setup(index, storage);
    }

    public final void setup(int index, float[] storage) {
        this.index = index;
        this.storage = storage;
    }

    public Quat(int index, FloatBuffer storage) {
        setup(index, storage);
    }

    public final void setup(int index, FloatBuffer storage) {
        this.index = index;
        this.nStorage = storage;
    }

    public void set(float x, float y, float z, float w) {
        if (nStorage != null) {
            nStorage.position(index);
            nStorage.put(x);
            nStorage.put(y);
            nStorage.put(z);
            nStorage.put(w);
            return;
        }
        storage[index] = x;
        storage[index + 1] = y;
        storage[index + 2] = z;
        storage[index + 3] = w;
    }

    public void set(Quat q) {
        storage[index] = q.storage[q.index];
        storage[index + 1] = q.storage[q.index + 1];
        storage[index + 2] = q.storage[q.index + 2];
        storage[index + 3] = q.storage[q.index + 3];
    }


    public void mult(Quat target, Quat quat) {
        int tIndex = target.index;
        float[] tStorage = target.storage;

        tStorage[tIndex] = storage[index] * quat.storage[quat.index];
        tStorage[tIndex + 1] = storage[index + 1] * quat.storage[quat.index + 1];
        tStorage[tIndex + 2] = storage[index + 2] * quat.storage[quat.index + 2];
        tStorage[tIndex + 3] = storage[index + 3] * quat.storage[quat.index + 3];


    }

    public void mult(Vec3 target, Vec3 v) {
        int tIndex = target.getIndex();
        float[] tStorage = target.getStorage();

        float x = storage[index];
        float y = storage[index + 1];
        float z = storage[index + 2];
        float w = storage[index + 3];

        float vx = v.getX();
        float vy = v.getY();
        float vz = v.getZ();

        if (vx == 0 && vy == 0 && vz == 0) {
            target.set(0, 0, 0);
        } else {
            tStorage[tIndex] = w * w * vx + 2 * y * w * vz - 2 * z * w * vy + x * x
                    * vx + 2 * y * x * vy + 2 * z * x * vz - z * z * vx - y
                    * y * vx;
            tStorage[tIndex + 1] = 2 * x * y * vx + y * y * vy + 2 * z * y * vz + 2 * w
                    * z * vx - z * z * vy + w * w * vy - 2 * x * w * vz - x
                    * x * vy;
            tStorage[tIndex + 2] = 2 * x * z * vx + 2 * y * z * vy + z * z * vz - 2 * w
                    * y * vx - y * y * vz + 2 * w * x * vy - x * x * vz + w
                    * w * vz;
        }

    }


    public static void mult(int ti, FloatBuffer ts,
                            int i1, FloatBuffer s1,
                            int i2, FloatBuffer s2) {

        float qx = s2.get(i2);
        float qy = s2.get(i2 + 1);
        float qz = s2.get(i2 + 2);
        float qw = s2.get(i2 + 3);
        float x = s1.get(i1);
        float y = s1.get(i1 + 1);
        float z = s1.get(i1 + 2);
        float w = s1.get(i1 + 3);

        ts.put(ti, x * qw + y * qz - z * qy + w * qx);
        ts.put(ti + 1, -x * qz + y * qw + z * qx + w * qy);
        ts.put(ti + 2, x * qy - y * qx + z * qw + w * qz);
        ts.put(ti + 3, -x * qx - y * qy - z * qz + w * qw);
    }

    public static void mult(int ti, float[] ts,
                            int i1, float[] s1,
                            int i2, float[] s2) {

        float qw = s2[i2 + 3], qx = s2[i2], qy = s2[i2 + 1], qz = s2[i2 + 2];
        float w = s1[i1 + 3], x = s1[i1], y = s1[i1 + 1], z = s1[i1 + 2];

        ts[ti++] = x * qw + y * qz - z * qy + w * qx;
        ts[ti++] = -x * qz + y * qw + z * qx + w * qy;
        ts[ti++] = x * qy - y * qx + z * qw + w * qz;
        ts[ti] = -x * qx - y * qy - z * qz + w * qw;
    }


    public static void multVec(int ti, float[] ts,
                               int i1, float[] s1,
                               int i2, float[] s2) {


        float x = s1[i1++];
        float y = s1[i1++];
        float z = s1[i1++];
        float w = s1[i1];

        float vx = s2[i2++];
        float vy = s2[i2++];
        float vz = s2[i2];

        if (vx == 0 && vy == 0 && vz == 0) {
            for (int i = ti; i < ti + 3; i++) {
                ts[i] = 0;
            }
        } else {
            ts[ti++] = w * w * vx + 2 * y * w * vz - 2 * z * w * vy + x * x
                    * vx + 2 * y * x * vy + 2 * z * x * vz - z * z * vx - y
                    * y * vx;
            ts[ti++] = 2 * x * y * vx + y * y * vy + 2 * z * y * vz + 2 * w
                    * z * vx - z * z * vy + w * w * vy - 2 * x * w * vz - x
                    * x * vy;
            ts[ti] = 2 * x * z * vx + 2 * y * z * vy + z * z * vz - 2 * w
                    * y * vx - y * y * vz + 2 * w * x * vy - x * x * vz + w
                    * w * vz;
        }
    }

    public static void multVec(int ti, FloatBuffer ts,
                               int i1, FloatBuffer s1,
                               int i2, FloatBuffer s2) {

        float x = s1.get(i1);
        float y = s1.get(i1 + 1);
        float z = s1.get(i1 + 2);
        float w = s1.get(i1 + 3);

        float vx = s2.get(i2);
        float vy = s2.get(i2 + 1);
        float vz = s2.get(i2 + 2);

        if (vx == 0 && vy == 0 && vz == 0) {
            ts.put(ti, 0);
            ts.put(ti + 1, 0);
            ts.put(ti + 2, 0);
        } else {
            ts.put(ti, w * w * vx + 2 * y * w * vz - 2 * z * w * vy + x * x
                    * vx + 2 * y * x * vy + 2 * z * x * vz - z * z * vx - y
                    * y * vx);
            ts.put(ti + 1, 2 * x * y * vx + y * y * vy + 2 * z * y * vz + 2 * w
                    * z * vx - z * z * vy + w * w * vy - 2 * x * w * vz - x
                    * x * vy);
            ts.put(ti + 2, 2 * x * z * vx + 2 * y * z * vy + z * z * vz - 2 * w
                    * y * vx - y * y * vz + 2 * w * x * vy - x * x * vz + w
                    * w * vz);
        }
    }

    public float getX() {
        return storage[index];
    }

    public float getY() {
        return storage[index + 1];
    }

    public float getZ() {
        return storage[index + 2];
    }

    public int getIndex() {
        return index;
    }

    public float[] getStorage() {
        return storage;
    }
}
