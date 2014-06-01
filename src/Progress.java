/* By Adam Michalowski (c) 2014 */
public class Progress {
    public static String progressName = "";
    public static int updateRatio = 1;
    public static int progressMax = 100;
    public static int progress = 0;

    public static void setup(String name, int progressMax) {
        Progress.progressName = name;
        Progress.progressMax = progressMax;
        Progress.progress = 0;
        Window.progressString = name;
    }
    public static void step() {
        progress++;
        int p = (int)(100*(float)progress / (float)progressMax);
        if(Window.progress != p) {
            Window.progress = p;
            if (Window.progress % updateRatio == 0)
                Window.update();
        }
    }
}
