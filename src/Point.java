/* By Adam Michalowski (c) 2014 */

import java.awt.geom.Point2D;

public class Point extends Point2D.Float {
    public Features features = new Features();

    public Point(String desc) {
        String params[] = desc.split(" ");
        this.x = java.lang.Float.parseFloat(params[0]);
        this.y = java.lang.Float.parseFloat(params[1]);

        for(int i = 0; i < 128; ++i)
            features.data[i] = Integer.parseInt(params[i + 5]);
    }
}
