/* By Adam Michalowski (c) 2014 */

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class Ransac {
    public static void analyse(Image imageA, Image imageB, float r, float R, int iterations, float difference, boolean perspective) {
        Transform model = findBestModel(imageA, r, R, iterations, perspective);
        if (model == null) {
            System.out.println("Ransac failed");
            return;
        }
        for (Point p : imageA.points) {
            Point2D.Float o = model.transform(p);
            if (o.distance(p.nearest) > difference)
                p.reject();
        }

        Point.removeRejected(imageA.points, imageB.points);
    }

    private static Transform findBestModel(Image imageA, float r, float R, int iterations, boolean perspective) {
        Progress.setup("Ransac", iterations);
        Transform bestModel = null;
        float error, minError = Float.MAX_VALUE;

        for (int i = 0; i < iterations; ++i) {
            Progress.step();
            Poly poly = randomPoly(imageA, r, R, perspective);
            if (poly == null) continue;
            Transform model = new Transform(poly);
            if (!model.valid) continue;
            error = calculateError(imageA, model);
            if (error < minError) {
                minError = error;
                bestModel = model;
                System.out.println("Ransac curr error: " + minError);
            }
        }

        return bestModel;
    }

    private static float calculateError(Image imageA, Transform model) {
        float error = 0;
        for (Point p : imageA.points) {
            Point2D.Float o = model.transform(p);
            error += o.distanceSq(p.nearest);
        }
        return error;
    }

    private static Poly randomPoly(Image imageA, float r, float R, boolean perspective) {
        ArrayList<Point> copyPoints = new ArrayList(imageA.points);
        Random random = new Random();

        Point firstPoint = copyPoints.get(random.nextInt(copyPoints.size()));
        //removeFarPoints(copyPoints, firstPoint, null, null, r, R);
        if (copyPoints.size() == 0) return null;

        Point secondPoint = copyPoints.get(random.nextInt(copyPoints.size()));
        //removeFarPoints(copyPoints, firstPoint, secondPoint, null, r, R);
        if (copyPoints.size() == 0) return null;

        Point thirdPoint = copyPoints.get(random.nextInt(copyPoints.size()));

        Point fourthPoint = null;
        if(perspective) {
            //removeFarPoints(copyPoints, firstPoint, secondPoint, thirdPoint, r, R);
            if (copyPoints.size() == 0) return null;
            fourthPoint = copyPoints.get(random.nextInt(copyPoints.size()));
        }

        return (fourthPoint == null) ?
                new Poly(firstPoint, secondPoint, thirdPoint) :
                new Poly(firstPoint, secondPoint, thirdPoint, fourthPoint);
    }

    private static void removeFarPoints(ArrayList<Point> copyPoints, Point firstPoint, Point secondPoint, Point thirdPoint, float r, float R) {
        ListIterator<Point> iterator = copyPoints.listIterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            boolean matchesFirstPoint = passTest(firstPoint.x, firstPoint.y, point.x, point.y, r, R);
            boolean matchesFirstImage = passTest(firstPoint.nearest.x, firstPoint.nearest.y, point.nearest.x, point.nearest.y, r, R);
            boolean matchesSecondPoint = (secondPoint == null) ? true : passTest(secondPoint.x, secondPoint.y, point.x, point.y, r, R);
            boolean matchesSecondImage = (secondPoint == null) ? true : passTest(secondPoint.nearest.x, secondPoint.nearest.y, point.nearest.x, point.nearest.y, r, R);
            boolean matchesThirdPoint = (thirdPoint == null) ? true : passTest(thirdPoint.x, thirdPoint.y, point.x, point.y, r, R);
            boolean matchesThirdImage = (thirdPoint == null) ? true : passTest(thirdPoint.nearest.x, thirdPoint.nearest.y, point.nearest.x, point.nearest.y, r, R);

            if (!matchesFirstPoint || !matchesFirstImage ||
                //!matchesSecondPoint || !matchesSecondImage ||
               // !matchesThirdPoint || !matchesThirdImage ||
                 point.equals(firstPoint) || point.equals(secondPoint) || point.equals(thirdPoint))
                iterator.remove();
        }
    }

    private static boolean passTest(float x1, float y1, float x2, float y2, float r, float R) {
        return r*r<=(x1-x2)*(x1-x2)+(y1-y2)*(y1-y2) && (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)<=R*R;
    }
}
