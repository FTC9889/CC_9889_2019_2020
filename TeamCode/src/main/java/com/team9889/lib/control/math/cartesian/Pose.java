package com.team9889.lib.control.math.cartesian;

/**
 * Created by joshua9889 on 7/24/2018.
 */

public class Pose {
    Rotation2d rotation2d = new Rotation2d();
    Vector2d vector2D = new Vector2d();

    public Pose(){}

    public Pose(Vector2d vector2d, Rotation2d rotation2d){
        this.vector2D = vector2d;
        this.rotation2d = rotation2d;
    }

    public Vector2d getVector2D() {
        return vector2D;
    }

    public Rotation2d getRotation2d() {
        return rotation2d;
    }

    public void setVector2D(Vector2d vector2D) {
        this.vector2D = vector2D;
    }

    public void setRotation2d(Rotation2d rotation2d) {
        this.rotation2d = rotation2d;
    }

    public static Pose add(Pose p1, Pose p2) {
        Vector2d addedVector = Vector2d.add(p1.getVector2D(), p2.getVector2D());
        Rotation2d addedRotation = Rotation2d.add(p1.getRotation2d(), p2.getRotation2d());

        return new Pose(addedVector, addedRotation);
    }

    public static Pose subtract(Pose p1, Pose p2) {
        Vector2d addedVector = Vector2d.subtract(p1.getVector2D(), p2.getVector2D());
        Rotation2d addedRotation = Rotation2d.subtract(p1.getRotation2d(), p2.getRotation2d());

        return new Pose(addedVector, addedRotation);
    }

    @Override
    public String toString() {
        return getVector2D().toString() + " | " + getRotation2d().toString();
    }
}
