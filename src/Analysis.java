/* By Adam Michalowski (c) 2014 */

public class Analysis implements Runnable {
    public static int numPoints = 0;
    public static int matchedPairs = 0;
    public static double percent = 0;
    public static Image imageA = null, imageB = null;
    public static boolean stop = false;
    private static Object lock = new Object();
    public static boolean doneNearest = false;
    public static boolean doNeighbourhoodCohesion = true,
                          doRansac = true,
                          transformPerspective = true;

    private static Image first = null, second = null;

    @Override
    public void run() {
        if(first != null && second != null) {
            synchronized (lock) {
                compare(first, second);
                first = null;
                second = null;
            }
        }
        Progress.hide();
    }

    public static void compare(String first, String second) {
        System.out.println("Comparing " + first + " and " + second);
        stop = false;
        doneNearest = false;
        Analysis.first = new Image(first);
        Analysis.second = new Image(second);
        Window.update(true);
        new Thread(new Analysis()).start();
    }

    public static void waitTillFinished() {
        synchronized (lock) {
        }
    }

    public static void compare(Image imageA, Image imageB) {
        Analysis.imageA = imageA;
        Analysis.imageB = imageB;
        numPoints += imageA.points.size() + imageB.points.size();

        Neighbourhood.findNearestPointMethod(imageA, imageB);
        doneNearest = true;
        update();
        if(stop) return;

        if(doNeighbourhoodCohesion && !doRansac) {
            NeighbourhoodCohesion.analyse(imageA, imageB, 20, 0.5f);
            update();
            if (stop) return;
        }

        if(doRansac) {
            Ransac.analyse(imageA, imageB, 0.01f, 0.4f, 100000, 0.1f, transformPerspective);
            update();
        }
    }



    public static void update() {
        matchedPairs = imageA.points.size();
        percent = (matchedPairs * 2.0f) / (float) numPoints;
        Window.update(true);
    }
}
