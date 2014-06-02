/* By Adam Michalowski (c) 2014 */

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Image {
    public String           name = "";
    public String           sourcePath = "";
    public String           paramsPath = "";
    public java.awt.Image   source = null;
    public ArrayList<Point> points = new ArrayList<Point>();

    public Image(String name) {
        this.name = name;
        this.sourcePath = "bin/" + name + ".png";
        this.paramsPath = "bin/" + name + ".png.haraff.sift";
        loadSource();
        loadPoints();
    }

    public void loadSource() {
        try {
            source = ImageIO.read(new File(sourcePath));

            System.out.println("Loaded source image " + sourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPoints() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(paramsPath));

            scanner.nextLine();
            int size = Integer.parseInt(scanner.nextLine());
            while(size-- > 0)
                points.add(new Point(scanner.nextLine()));

            System.out.println("Loaded " + points.size() + " points from " + paramsPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.close();
    }
}
