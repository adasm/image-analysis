/* By Adam Michalowski (c) 2014 */

public class Main {
    public static void main(String arg[]) {
        Image imageA = new Image("7");
        Image imageB = new Image("8");

        Window.init();

        Analysis.compare(imageA, imageB);
    }
}
