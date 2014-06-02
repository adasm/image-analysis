/* By Adam Michalowski (c) 2014 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NeighbourhoodCohesion {
    public static void analyse(Image imageA, Image imageB, int numNeighbours, float cohesionLevel) {
        Progress.setup("NeighbourhoodCohesion", imageA.points.size());
        final Pair.Value  pairValue = (a, b) -> a.featureDistance(b);

        for(Point p : imageA.points) {
            List<Point> neighboursA = findNeighbours(p, imageA.points, numNeighbours, pairValue);
            List<Point> neighboursB = findNeighbours(p.nearest, imageB.points, numNeighbours, pairValue);

            int cohesion = checkCohesion(neighboursA, neighboursB);
            float curretnCohesionLevel = (float) cohesion / (float) numNeighbours;
            if(curretnCohesionLevel < cohesionLevel)
                p.reject();

            Progress.step();
            if(Analysis.stop)
                break;
        }

        Point.removeRejected(imageA.points, imageB.points);
    }

    public static ArrayList<Point> findNeighbours(Point point, ArrayList<Point> points, int numNeighbours, Pair.Value neighbourValue) {
        ArrayList<Point> neighbours = new ArrayList<>();
        List<Pair> nearestPoints = new ArrayList<>();
        for(Point p : points)
            if(!p.equals(point))
                nearestPoints.add(new Pair(point, p, neighbourValue));
        Collections.sort(nearestPoints);
        nearestPoints = nearestPoints.subList(0, numNeighbours - 1);
        for(Pair pair : nearestPoints)
            neighbours.add(pair.point);
        return neighbours;
    }

    public static int checkCohesion(List<Point> neighboursA, List<Point> neighboursB) {
        int cohesion = 0;
        for(Point point : neighboursA)
            if(neighboursB.contains(point.nearest))
                cohesion++;
        return cohesion;
    }

}
