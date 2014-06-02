/* By Adam Michalowski (c) 2014 */

public class Neighbourhood {
    public static void findNearestPointMethod(Image imageA, Image imageB) {
        Progress.setup("Neighbourhood", imageA.points.size() * 2 + imageB.points.size() * 2);

        for(Point p : imageA.points) { p.findMostSimilarPoint(imageB.points); Progress.step(); }
        for(Point p : imageB.points) { p.findMostSimilarPoint(imageA.points); Progress.step(); }

        for(Point p : imageA.points) { p.checkNearest(); Progress.step(); }
        for(Point p : imageB.points) { p.checkNearest(); Progress.step(); }

        Point.removeRejected(imageA.points, imageB.points);
    }
}
