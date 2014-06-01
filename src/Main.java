/* By Adam Michalowski (c) 2014 */

public class Main {
    public static Image imageA = null, imageB = null;
    public static void main(String arg[]) {
        Image imageA = new Image("7");
        Image imageB = new Image("8");

        Main.imageA = imageA;
        Main.imageB = imageB;

        Window.init();
    }
}
