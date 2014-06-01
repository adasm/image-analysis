/* By Adam Michalowski (c) 2014 */

public class Analysis {
    public static Image imageA = null, imageB = null;

    public static void compare(Image imageA, Image imageB) {
        Analysis.imageA = imageA;
        Analysis.imageB = imageB;

        Neighbourhood.findNearestPointMethod(imageA, imageB);
    }
}
