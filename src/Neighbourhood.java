/* By Adam Michalowski (c) 2014 */

import java.util.ArrayList;
import java.util.ListIterator;

public class Neighbourhood {
    public static void findNearestPointMethod(Image imageA, Image imageB) {
        Progress.setup("Neighbourhood", imageA.points.size() * 2 + imageB.points.size() * 2);
        for(Point p : imageA.points) { findNearestPoint(p, imageB.points); Progress.step(); }
        for(Point p : imageB.points) { findNearestPoint(p, imageA.points); Progress.step(); }

        for(Point p : imageA.points) { p.checkNearest(); Progress.step(); }
        for(Point p : imageB.points) { p.checkNearest(); Progress.step(); }

        rejectAlone(imageA.points);
        rejectAlone(imageB.points);

        System.out.println("Found " + imageA.points.size() + " matching points.");
    }

    private static void findNearestPoint(Point source, ArrayList<Point> points) {
        float d = Float.MAX_VALUE, c = 0f;
        for(Point target : points) {
            c = source.featureDistance(target);
            if(c < d) {
                source.nearest = target;
                d = c;
            }
        }
    }

    public static void rejectAlone(ArrayList<Point> points) {
        ListIterator<Point> it = points.listIterator();
        while(it.hasNext()) {
            Point p = it.next();
            if(p == null || p.nearest == null)
                it.remove();
        }
    }

}
