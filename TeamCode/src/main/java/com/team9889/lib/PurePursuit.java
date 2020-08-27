package com.team9889.lib;


import android.util.Log;

import com.team9889.lib.control.math.cartesian.Pose;

import org.opencv.core.Point;

import java.util.ArrayList;

import static java.lang.Math.*;

/**
 * Created by Eric on 8/23/2020.
 */
public class PurePursuit {
    public ArrayList<Point> lineIntersectWithCircle (Point startOfLine, Point endOfLine, double radius) {
        //calculate the M and X of M * X + B = Y
        double M = (endOfLine.y - startOfLine.y) / (endOfLine.x - startOfLine.x);
        double B = startOfLine.y - (M * startOfLine.x);

        //calculate the a, b, and c of the quadratic equation
        double a = pow(M, 2) + 1;
        double b = M * B * 2;
        double c = pow(B, 2) - pow(radius, 2);

        //solve the quadratic equations
        double quad1 = (-b + sqrt(pow(b, 2) - (4 * a * c))) / a * 2;
        double quad2 = (-b - sqrt(pow(b, 2) - (4 * a * c))) / a * 2;

        Log.i("Quad 0 : ", "" + startOfLine.x);
        Log.i("Quad 1 : ", "" + quad1);
        Log.i("Quad 2 : ", "" + quad2);

        ArrayList<Point> points = new ArrayList<>();

        //make sure the quadratic equations results are less than the radius
        if (quad1 <= radius)
            points.add(new Point(quad1, M * quad1 * B));

        if (quad2 <= radius)
            points.add(new Point(quad2, M * quad2 * B));

        return points;
    }

    public Object[] bestPointToFollow (Point startOfLine, Point endOfLine, Point endOfSecLine, double radius1, double radius2) {
        //get the line intersections with the circle
        ArrayList<Point> lineIntersections1 = lineIntersectWithCircle(startOfLine, endOfLine, radius1);
        ArrayList<Point> lineIntersections2 = lineIntersectWithCircle(endOfLine, endOfSecLine, radius2);

        Point bestPoint = null;
        double bestPointDist = 10000;
        boolean changedLine = false;

        //make sure the line does intersect with the circle. If it does not intersect, return the last point
        if (lineIntersections1.size() > 0) {
            for (int i = 0; i < lineIntersections1.size(); i++) {

                //check if the distance from this point to the end point is less than the current best distance
                if (sqrt(pow(endOfLine.x - lineIntersections1.get(i).x, 2) +
                        pow(endOfLine.y - lineIntersections1.get(i).y, 2)) < bestPointDist) {

                    //update the best distance, point, and what line is the best
                    bestPointDist = sqrt(pow(endOfLine.x - lineIntersections1.get(i).x, 2) +
                            pow(endOfLine.y - lineIntersections1.get(i).y, 2));

                    bestPoint = lineIntersections1.get(i);
                }
            }
        }

        //make sure the line does intersect with the circle
        if (lineIntersections2.size() > 0) {
            for (int i = 0; i < lineIntersections2.size(); i++) {

                //check if the current best point is not from the second line or,
                // if the distance from this point to the end point is less than the current best distance
                if (!changedLine || sqrt(pow(endOfSecLine.x - lineIntersections2.get(i).x, 2) +
                        pow(endOfSecLine.y - lineIntersections2.get(i).y, 2)) < bestPointDist) {

                    //update the best distance, point, and what line is the best
                    bestPointDist = sqrt(pow(endOfSecLine.x - lineIntersections2.get(i).x, 2) +
                            pow(endOfSecLine.y - lineIntersections2.get(i).y, 2));

                    bestPoint = lineIntersections2.get(i);
                    changedLine = true;
                }
            }
        }


        return new Object[] {bestPoint, changedLine};
    }
}
