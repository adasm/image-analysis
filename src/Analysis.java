/* By Adam Michalowski (c) 2014 */

public class Analysis {
    public static int numPoints = 0;
    public static int matchedPairs = 0;
    public static float percent = 0;
    public static Image imageA = null, imageB = null;

    public static void compare(Image imageA, Image imageB) {
        Analysis.imageA = imageA;
        Analysis.imageB = imageB;
        numPoints += imageA.points.size() + imageB.points.size();

        Neighbourhood.findNearestPointMethod(imageA, imageB);
        updateStats();

        //NeighbourhoodCohesion.analyse(imageA, imageB, 10, 0.4f);
        //updateStats();

        Ransac.analyse(imageA, imageB, 5, 50, 10000, 50, true);
        updateStats();
    }

    public static void updateStats() {
        matchedPairs = imageA.points.size();
        percent = (matchedPairs * 2.0f) / (float) numPoints;
    }
}
