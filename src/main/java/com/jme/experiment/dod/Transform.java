package com.jme.experiment.dod;

public class Transform {

    private Vec3 position;
    private Quat rotation;
    private Vec3 scale;

    public Transform() {
        position = new Vec3();
        rotation = new Quat();
        scale = new Vec3();
    }

    public Transform(Vec3 position, Quat rotation, Vec3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        init();
    }

    public void init(){
        position.set(0,0,0);
        rotation.set(0,0,0,1);
        scale.set(1,1,1);
    }

    public void setup(int posIndex, float[] posStorage,
                      int rotIndex, float[] rotStorage,
                      int scaleIndex, float[] scaleStorage){
        position.setup(posIndex, posStorage);
        rotation.setup(rotIndex, rotStorage);
        scale.setup(scaleIndex, scaleStorage);
    }

    public void setup(int index, float[] storage){
        position.setup(index, storage);
        rotation.setup(index + 3, storage);
        scale.setup(index + 7, storage);
    }

    public void combine(Transform target, Transform t){
        scale.mult(target.scale, t.scale);
        //applying parent rotation to local rotation.
        t.rotation.mult(target.rotation, rotation);
        //applying parent scale to local translation.
        position.mult(target.position, t.scale);
        //applying parent rotation to local translation, then applying parent translation to local translation.
        //Note that parent.rot.multLocal(translation) doesn't modify "parent.rot" but "translation"
        t.rotation.mult(target.position, target.position);
        target.position.add(target.position, t.position);
    }

    public static void combine(int ti, float[]ts,
                          int i1, float[]s1,
                          int i2, float[]s2){
        //scale
        Vec3.mult(ti + 7, ts,i1 + 7, s1 , i2 + 7, s2);
        //applying parent rotation to local rotation. rotation mult
        Quat.mult(ti + 3, ts,i2 + 3, s2 , i1 + 3, s1);
        //applying parent scale to local translation.
        Vec3.mult(ti, ts, i1 ,s1 ,i2 + 7, s2);
        //applying parent rotation to local translation, then applying parent translation to local translation.
        //Note that parent.rot.multLocal(translation) doesn't modify "parent.rot" but "translation"
        Quat.multVec(ti, ts, i2 + 3, s2, ti, ts);
        Vec3.add(ti, ts, ti, ts, i2, s2);

    }


    public void set(Transform t){
        position.set(t.position);
        rotation.set(t.rotation);
        scale.set(t.scale);
    }
}
