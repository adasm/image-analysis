/* By Adam Michalowski (c) 2014 */

public class Features {
    public final int FEATURES_SIZE = 128;
    public int data[] = new int[FEATURES_SIZE];

    public float distance(Features features) {
        float d = 0;
        for(int i = 0; i < FEATURES_SIZE; ++i)
            d += Math.abs(data[i] - features.data[i]);
        return d;
    }
}
