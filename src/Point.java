/* By Adam Michalowski (c) 2014 */

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ListIterator;

public class Point extends Point2D.Double {
    public Features features    = new Features();
    public Point    nearest     = null;

    public Point(String desc, double imageWidth, double imageHeight) {
        String params[] = desc.split(" ");

        this.x = java.lang.Double.parseDouble(params[0]) / imageWidth;
        this.y = java.lang.Double.parseDouble(params[1]) / imageHeight;

        for(int i = 0; i < 128; ++i)
            features.data[i] = Integer.parseInt(params[i + 5]);
    }

    public double featureDistance(Point point) {
        return features.distance(point.features);
    }

    public void checkNearest() {
        if(nearest == null || nearest.nearest != this)
            reject();
    }

    public void reject() {
        if(nearest != null && nearest.nearest == this)
            nearest.nearest = null;
        nearest = null;
    }

    public void findMostSimilarPoint(ArrayList<Point> points) {
        double d = java.lang.Double.MAX_VALUE, c;
        nearest = null;
        for (Point target : points) {
            c = featureDistance(target);
            if (c < d) {
                nearest = target;
                d = c;
            }
        }
    }

    public static void removeRejected(ArrayList<Point> points) {
        synchronized(points) {
            ListIterator<Point> it = points.listIterator();
            while (it.hasNext()) {
                Point p = it.next();
                if (p == null || p.nearest == null)
                    it.remove();
            }
        }
    }

    public static void removeRejected(ArrayList<Point> pointsA, ArrayList<Point> pointsB) {
        removeRejected(pointsA);
        removeRejected(pointsB);
    }
}
