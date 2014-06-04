/* By Adam Michalowski (c) 2014 */

public class Main {
    final public static int startPairIndex = 0;
    final public static String pairsNames[][] =
        {   {"rb1", "rb2"},
            {"rb1", "rb1"},
            {"book", "book2"},
            {"clock", "clock2"}};

    public static void main(String arg[]) {
        Window.init();

        Analysis.compare(Main.pairsNames[startPairIndex][0],
                         Main.pairsNames[startPairIndex][1]);
    }
}
