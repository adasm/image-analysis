/* By Adam Michalowski (c) 2014 */

public class Main {
    public static void main(String arg[]) {
        final String pairs[][] = {  {"rb1", "rb2"},
                                    {"rb1", "rb1"},
                                    {"n1", "n2"}};
        final int pairIndex = 0;

        Image imageA = new Image(pairs[pairIndex][0]);
        Image imageB = new Image(pairs[pairIndex][1]);

        Window.init();

        Analysis.compare(imageA, imageB);
    }
}
