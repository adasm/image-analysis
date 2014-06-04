/* By Adam Michalowski (c) 2014 */

import javafx.scene.control.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements ActionListener {
    public static float         progress = 0;
    public static String        progressString = "";
    public static JProgressBar  progressBar = null;
    public static ImageDrawer   imageDrawer = null;
    public static boolean       showPoints = true,
                                showAllPoints = false,
                                showNearest = true,
                                showStats = false;

    public Window() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        setSize(xSize, ySize);

        setSize(1000, 600);
        setLocationRelativeTo(null);
        setTitle("Image Analysis : Comparison");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuLoad = new JMenu("Load");
        for(String pairName[] : Main.pairsNames) {
            JMenuItem pairMenu = new JMenuItem(pairName[0] + "-" + pairName[1]);
            pairMenu.putClientProperty("first", pairName[0]);
            pairMenu.putClientProperty("second", pairName[1]);
            pairMenu.setActionCommand("load");
            pairMenu.addActionListener(this);
            menuLoad.add(pairMenu);
        }
        menuBar.add(menuLoad);

        JMenu menuShow = new JMenu("Show");
        JMenuItem menuShowAllPoints = new JMenuItem("Show All Key Points");
        menuShowAllPoints.setActionCommand("show_all_points");
        menuShowAllPoints.addActionListener(this);
        menuShow.add(menuShowAllPoints);
        JMenuItem menuShowPoints = new JMenuItem("Show Filtered Key Points");
        menuShowPoints.setActionCommand("show_points");
        menuShowPoints.addActionListener(this);
        menuShow.add(menuShowPoints);
        JMenuItem menuShowNearest = new JMenuItem("Show Nearest");
        menuShowNearest.setActionCommand("show_nearest");
        menuShowNearest.addActionListener(this);
        menuShow.add(menuShowNearest);
        menuBar.add(menuShow);

        JMenu menuOptions = new JMenu("Options");
        JMenuItem menuOptionsDoCohesion = new JMenuItem(Analysis.doNeighbourhoodCohesion ? "Neighbourhood Cohesion : ON" : "Neighbourhood Cohesion : OFF");
        menuOptionsDoCohesion.setActionCommand("cohesion");
        menuOptionsDoCohesion.addActionListener(this);
        menuOptions.add(menuOptionsDoCohesion);
        JMenuItem menuOptionsRansac = new JMenuItem(Analysis.doRansac ? "RANSAC : ON" : "RANSAC : OFF");
        menuOptionsRansac.setActionCommand("ransac");
        menuOptionsRansac.addActionListener(this);
        menuOptions.add(menuOptionsRansac);
        JMenuItem menuOptionsPerspective = new JMenuItem(Analysis.transformPerspective ? "Transform : Perspective" : "Transform : Affinite");
        menuOptionsPerspective.setActionCommand("transform");
        menuOptionsPerspective.addActionListener(this);
        menuOptions.add(menuOptionsPerspective);
        menuBar.add(menuOptions);

        JMenuItem menuReprocess = new JMenuItem("Reprocess");
        menuReprocess.setActionCommand("restart");
        menuReprocess.addActionListener(this);
        menuBar.add(menuReprocess);

        imageDrawer = new ImageDrawer();
        add(imageDrawer, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setString("");
        progressBar.setStringPainted(true);

        add(progressBar, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("show_points")) {
            showPoints = !showPoints;
        } else if (command.equals("show_all_points")) {
            showAllPoints = !showAllPoints;
        } else if (command.equals("show_nearest")) {
            showNearest = !showNearest;
        } else if (command.equals("cohesion")) {
            Analysis.doNeighbourhoodCohesion = !Analysis.doNeighbourhoodCohesion;
            ((JMenuItem)(e.getSource())).setText(Analysis.doNeighbourhoodCohesion ? "Neighbourhood Cohesion : ON" : "Neighbourhood Cohesion : OFF");
        } else if (command.equals("ransac")) {
            Analysis.doRansac = !Analysis.doRansac;
            ((JMenuItem)(e.getSource())).setText(Analysis.doRansac ? "RANSAC : ON" : "RANSAC : OFF");
        } else if (command.equals("transform")) {
            Analysis.transformPerspective = !Analysis.transformPerspective;
            ((JMenuItem)(e.getSource())).setText(Analysis.transformPerspective ? "Transform : Perspective" : "Transform : Affinite");
        } else if (command.equals("restart")) {
            Analysis.stop = true;
            Analysis.waitTillFinished();
            if(Analysis.imageA != null && Analysis.imageB != null) {
                String first = Analysis.imageA.name;
                String second = Analysis.imageB.name;
                System.out.println("Loading pair " + first + " and " + second);
                Analysis.compare(first, second);
            }
        } else if(command.equals("load")) {
            JMenuItem item = (JMenuItem)e.getSource();
            String first = (String)item.getClientProperty("first");
            String second = (String)item.getClientProperty("second");
            System.out.println("Loading pair " + first + " and " + second);

            Analysis.stop = true;
            Analysis.waitTillFinished();
            Analysis.compare(first, second);
        } else System.out.println(command.equals("load"));

        update(true);
    }

    public static void update(boolean repaint) {
        if(progressBar != null) {
            progressBar.setValue((int) progress);
            progressBar.setString(progressString);
        }
        if(repaint && imageDrawer != null) {
            imageDrawer.repaint(50);
        }
        Thread.yield();
    }

    public static class ImageDrawer extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);


            if(Analysis.imageA == null || Analysis.imageB == null) return;

            Graphics2D g2d = (Graphics2D)g;

            Image A = Analysis.imageA;
            Image B = Analysis.imageB;

            double w = getWidth();
            double h = getHeight();

            double wCoeffIm1 = w / (2 * A.source.getWidth(null));
            double hCoeffIm1 = h / A.source.getHeight(null);
            double ratio1 = (wCoeffIm1 < hCoeffIm1) ? wCoeffIm1 : hCoeffIm1;
            double x1 = 0, y1 = 0;
            double w1 = ratio1 * A.source.getWidth(null),
                   h1 = ratio1 * A.source.getHeight(null);

            double wCoeffIm2 = w / (2 * B.source.getWidth(null));
            double hCoeffIm2 = h / B.source.getHeight(null);
            double ratio2 = (wCoeffIm2 < hCoeffIm2) ? wCoeffIm2 : hCoeffIm2;
            double x2 = w / 2, y2 = 0;
            double w2 = ratio2 * B.source.getWidth(null),
                   h2 = ratio2 * B.source.getHeight(null);

            g2d.drawImage(A.source, (int)x1, (int)y1, (int)w1, (int)h1, Color.WHITE, null);
            g2d.drawImage(B.source, (int)x2, (int)y2, (int)w2, (int)h2, Color.WHITE, null);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(255,0,128, 128));
            g2d.setStroke(new BasicStroke(1));
            if(showNearest && Analysis.doneNearest) {
                drawNearest(g2d, A, B, x1, y1, w1, h1, x2, y2, w2, h2);
            }


            if(showPoints) {
                drawPoints(g2d, A, x1, y1, w1, h1);
                drawPoints(g2d, B, x2, y2, w2, h2);
            }

            if(showStats) {
                g2d.setColor(new Color(198, 198, 198));
                g2d.setStroke(new BasicStroke(1));
                g2d.fillRect(0, 0, (int) w1, 30);

                g2d.setColor(new Color(0, 0, 0));
                g2d.drawString("Matched pairs: " + Analysis.matchedPairs, 0, 12);
                g2d.drawString("Similarity: " + String.format("%.2f %%", Analysis.percent), 0, 27);
            }
        }

        public void drawPoints(Graphics2D g2d, Image image, double dx, double dy, double w, double h) {
            if(showAllPoints) {
                g2d.setColor(new Color(255, 255, 0, 150));
                g2d.setStroke(new BasicStroke(3));
                for (Point p : image.pointsAll) {
                    double x = dx + w * p.x;
                    double y = dy + h * p.y;
                    g2d.drawLine((int) x, (int) y, (int) x, (int) y);
                }
            }

            g2d.setColor(new Color(255,0,0, 150));
            g2d.setStroke(new BasicStroke(5));
            synchronized(image.points) {
                for (Point p : image.points) {
                    double x = dx + w * p.x;
                    double y = dy + h * p.y;
                    g2d.drawLine((int) x, (int) y, (int) x, (int) y);
                }
            }
        }

        public void drawNearest(Graphics2D g2d, Image A, Image B, double dx, double dy, double w, double h, double dx2, double dy2, double w2, double h2) {
            synchronized (A.points) {
                synchronized (B.points) {
                    for (Point p : A.points) {
                        Point n = p.nearest;
                        if (n == null) continue;

                        double x = dx + w * p.x;
                        double y = dy + h * p.y;
                        double x2 = dx2 + w2 * n.x;
                        double y2 = dy2 + h2 * n.y;
                        g2d.drawLine((int) x, (int) y, (int) x2, (int) y2);
                    }
                }
            }
        }
    }

    public static void init() {
        SwingUtilities.invokeLater(() -> new Window().setVisible(true));
    }
}
