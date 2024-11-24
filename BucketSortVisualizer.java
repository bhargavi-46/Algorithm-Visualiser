import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BucketSortVisualizer extends JFrame {
    private VisualizerPanel panel;

    public BucketSortVisualizer(float[] array) {
        setTitle("Bucket Sort Visualizer");
        setSize(800, 800);
        panel = new VisualizerPanel(array);
        add(panel);
        setVisible(true);

        new Thread(() -> panel.bucketSort()).start();
    }

    public static void BUCKETSORT() {
        float[] array = {0.897f, 0.565f, 0.656f, 0.1234f, 0.665f, 0.3434f};
        new BucketSortVisualizer(array);
    }

    public class VisualizerPanel extends JPanel {
        private float[] array;
        private List<List<Float>> buckets;
        private int highlightedBucket = -1;
        private int highlightedIndex = -1;

        public VisualizerPanel(float[] array) {
            this.array = array;
            buckets = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                buckets.add(new ArrayList<>());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int boxWidth = 100;
            int boxHeight = 60;
            int startX = 50;
            int startY = 100;

            // Draw buckets
            for (int i = 0; i < buckets.size(); i++) {
                int bucketStartX = startX + i * (boxWidth + 20);
                g.setColor(Color.BLACK);
                g.drawRect(bucketStartX, startY - 20, boxWidth, boxHeight + 40);
                g.drawString("Bucket " + i, bucketStartX + 10, startY - 25);

                // Draw elements in buckets
                List<Float> bucket = buckets.get(i);
                for (int j = 0; j < bucket.size(); j++) {
                    if (i == highlightedBucket && j == highlightedIndex) {
                        g.setColor(Color.RED); // Highlight the current element
                    } else {
                        g.setColor(Color.YELLOW);
                    }
                    g.fillRect(bucketStartX + 5, startY + j * (boxHeight + 5), boxWidth - 10, boxHeight);
                    g.setColor(Color.BLACK);
                    g.drawRect(bucketStartX + 5, startY + j * (boxHeight + 5), boxWidth - 10, boxHeight);

                    // Draw the number
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.drawString(String.format("%.2f", bucket.get(j)),
                            bucketStartX + 20, startY + j * (boxHeight + 5) + boxHeight / 2);
                }
            }

            // Draw the main array
            int arrayStartY = startY + buckets.size() * (boxHeight + 10);
            g.drawString("Array:", startX - 50, arrayStartY + 25);
            for (int i = 0; i < array.length; i++) {
                g.setColor(Color.GREEN);
                g.fillRect(startX + i * boxWidth, arrayStartY, boxWidth, boxHeight);
                g.setColor(Color.BLACK);
                g.drawRect(startX + i * boxWidth, arrayStartY, boxWidth, boxHeight);

                // Draw the number
                g.setFont(new Font("Arial", Font.BOLD, 24));
                FontMetrics metrics = g.getFontMetrics();
                int textX = startX + i * boxWidth + (boxWidth - metrics.stringWidth(String.valueOf(array[i]))) / 2;
                int textY = arrayStartY + ((boxHeight - metrics.getHeight()) / 2) + metrics.getAscent();
                g.drawString(String.format("%.2f", array[i]), textX, textY);
            }
        }

        public void bucketSort() {
            // Step 1: Distribute elements into buckets
            for (int i = 0; i < array.length; i++) {
                int bucketIndex = (int) (array.length * array[i]); // Determine bucket index
                buckets.get(bucketIndex).add(array[i]);
                highlightedBucket = bucketIndex;
                highlightedIndex = buckets.get(bucketIndex).size() - 1;
                repaint();
                sleep(500);
            }

            // Step 2: Sort individual buckets using insertion sort
            for (int i = 0; i < buckets.size(); i++) {
                highlightedBucket = i;
                List<Float> bucket = buckets.get(i);
                for (int j = 1; j < bucket.size(); j++) {
                    float key = bucket.get(j);
                    int k = j - 1;

                    while (k >= 0 && bucket.get(k) > key) {
                        bucket.set(k + 1, bucket.get(k));
                        highlightedIndex = k;
                        repaint();
                        sleep(500);
                        k--;
                    }
                    bucket.set(k + 1, key);
                    highlightedIndex = k + 1;
                    repaint();
                    sleep(500);
                }
            }

            // Step 3: Concatenate buckets back to the array
            int index = 0;
            for (int i = 0; i < buckets.size(); i++) {
                for (float num : buckets.get(i)) {
                    array[index++] = num;
                    highlightedBucket = i;
                    highlightedIndex = buckets.get(i).indexOf(num);
                    repaint();
                    sleep(500);
                }
            }

            // Reset highlights
            highlightedBucket = -1;
            highlightedIndex = -1;
            repaint();
        }

        private void sleep(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}