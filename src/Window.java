/* By Adam Michalowski (c) 2014 */

import javafx.scene.control.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements ActionListener {
    public static float progress = 0;
    public static String progressString = "";
    public static JProgressBar progressBar = null;
    public static boolean showPoints = true;

    public Window() {
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setTitle("Image Analysis : Comparison");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuShow = new JMenu("Show");
        JMenuItem menuShowPoints = new JMenuItem("Show Key Points");
        menuShowPoints.setActionCommand("show_points");
        menuShowPoints.addActionListener(this);
        menuShow.add(menuShowPoints);
        menuBar.add(menuShow);

        add(new ImageDrawer(), BorderLayout.CENTER);


        progressBar = new JProgressBar();
        progressBar.setString("");
        progressBar.setStringPainted(true);
        //progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        add(progressBar, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("show_points")) {
            showPoints = !showPoints;
        }
    }

    public static class ImageDrawer extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            progressBar.setValue((int)progress);
            progressBar.setString(progressString);

            if(Main.imageA == null || Main.imageB == null) return;

            Graphics2D g2d = (Graphics2D)g;

            Image A = Main.imageA;
            Image B = Main.imageB;

            float w = getWidth();
            float h = getHeight();

            float wCoeffIm1 = w / (2 * A.source.getWidth(null));
            float hCoeffIm1 = h / A.source.getHeight(null);
            float ratio1 = (wCoeffIm1 < hCoeffIm1) ? wCoeffIm1 : hCoeffIm1;
            float x1 = 0, y1 = 0;
            float w1 = ratio1 * A.source.getWidth(null),
                  h1 = ratio1 * A.source.getHeight(null);

            float wCoeffIm2 = w / (2 * B.source.getWidth(null));
            float hCoeffIm2 = h / B.source.getHeight(null);
            float ratio2 = (wCoeffIm2 < hCoeffIm2) ? wCoeffIm2 : hCoeffIm2;
            float x2 = w / 2, y2 = 0;
            float w2 = ratio2 * B.source.getWidth(null),
                  h2 = ratio2 * B.source.getHeight(null);

            g2d.drawImage(A.source, (int)x1, (int)y1, (int)w1, (int)h1, Color.WHITE, null);
            g2d.drawImage(B.source, (int)x2, (int)y2, (int)w2, (int)h2, Color.WHITE, null);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(2));

            if(showPoints) {
                drawPoints(g2d, A, x1, y1, ratio1);
                drawPoints(g2d, B, x2, y2, ratio2);
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repaint();
        }

        public void drawPoints(Graphics2D g2d, Image image, float dx, float dy, float ratio) {
            for(int i = 0; i < image.points.size(); ++i) {
                Point p = image.points.get(i);
                float x = dx + ratio * p.x;
                float y = dy + ratio * p.y;
                g2d.drawLine((int)x, (int)y, (int)x, (int)y);
            }
        }
    }

    public static void init() {
        SwingUtilities.invokeLater(() -> new Window().setVisible(true));
    }
}
