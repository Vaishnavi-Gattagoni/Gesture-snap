package computervision;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class HandDetection {
    private JLabel imageLabel;

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HandDetection app = new HandDetection();
        app.startCamera();
    }

    public HandDetection() {
        JFrame frame = new JFrame("Hand Detection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        imageLabel = new JLabel();
        frame.add(imageLabel);
        frame.setVisible(true);
    }

    public void startCamera() {
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Error: Camera not found!");
            return;
        }

        Mat frame = new Mat();

        while (true) {
            camera.read(frame);
            if (frame.empty()) break;

            // Detect hand and draw bounding box
            Mat processedFrame = detectHand(frame);

            // Convert and display the image
            ImageIcon image = new ImageIcon(matToBufferedImage(processedFrame));
            imageLabel.setIcon(image);
            imageLabel.repaint();
        }

        camera.release();
    }

    private Mat detectHand(Mat frame) {
        Mat hsvFrame = new Mat();
        Mat mask = new Mat();
        Mat hierarchy = new Mat();

        // Convert to HSV for better skin detection
        Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);
        Scalar lowerSkin = new Scalar(0, 20, 100);  // Reduce lower bound for lighter tones
        Scalar upperSkin = new Scalar(20, 150, 200); // Increase upper bound for bright skin

        Core.inRange(hsvFrame, lowerSkin, upperSkin, mask);

        // Morphological Transformations to remove noise
        Imgproc.erode(mask, mask, new Mat(), new Point(-1, -1), 1);
        Imgproc.dilate(mask, mask, new Mat(), new Point(-1, -1), 3);

        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (!contours.isEmpty()) {
            MatOfPoint maxContour = Collections.max(contours, (c1, c2) -> Double.compare(Imgproc.contourArea(c1), Imgproc.contourArea(c2)));

            // Ignore small objects
            double contourArea = Imgproc.contourArea(maxContour);
            if (contourArea > 7000) {
                Rect boundingBox = Imgproc.boundingRect(maxContour);

                // Ignore face, neck, and body by checking Y position
                if (boundingBox.y > frame.rows() / 4) {
                	// Convex Hull to confirm hand shape
                    MatOfInt hull = new MatOfInt();
                    Imgproc.convexHull(maxContour, hull);

                 // Count fingers
                    int fingerCount = countFingers(maxContour, hull);

                    // Check for specific gesture (Index Up + Thumb Perpendicular)
                    if (fingerCount == 2 && isThumbPerpendicular(maxContour, hull)) {
                        System.out.println("Gesture detected! Capturing Image...");
                        captureImage(frame);
                    }

                    Imgproc.rectangle(frame, boundingBox.tl(), boundingBox.br(), new Scalar(255, 0, 0), 2);
                }
            }
        }
        return frame;
    }

 // ✅ Corrected countFingers Method
    private int countFingers(MatOfPoint contour, MatOfInt hull) {
        MatOfInt4 defects = new MatOfInt4();
        Imgproc.convexityDefects(contour, hull, defects);

        int count = 0;
        List<Integer> defectList = defects.toList();
        
        for (int i = 0; i < defectList.size(); i += 4) {
            double depth = defectList.get(i + 3);  // Distance between convex hull and defect
            if (depth > 10000) { // Adjust this threshold if needed
                count++;
            }
        }
        return count;
    }

    // ✅ Updated isThumbPerpendicular Method
    private boolean isThumbPerpendicular(MatOfPoint contour, MatOfInt hull) {
        // Get convex hull points
        List<Point> hullPoints = new ArrayList<>();
        for (Integer index : hull.toList()) {
            hullPoints.add(contour.toList().get(index));
        }

        if (hullPoints.size() < 5) return false; // Not enough points to determine thumb angle

        // Find two farthest points (potential thumb and wrist base)
        double maxDistance = 0;
        Point thumbTip = null;
        Point wristBase = null;

        for (Point p1 : hullPoints) {
            for (Point p2 : hullPoints) {
                double distance = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
                if (distance > maxDistance) {
                    maxDistance = distance;
                    thumbTip = p1;
                    wristBase = p2;
                }
            }
        }

        if (thumbTip == null || wristBase == null) return false;

        // Compute the angle between thumb and wrist base
        double angle = Math.toDegrees(Math.atan2(thumbTip.y - wristBase.y, thumbTip.x - wristBase.x));

        // Check if angle is near 90 degrees (thumb perpendicular)
        return angle > 60 && angle < 120;
    }

    // ✅ Capture and save image
    private void captureImage(Mat frame) {
        String filename = "gesture_capture.jpg";
        Imgcodecs.imwrite(filename, frame);
        System.out.println("Image saved: " + filename);
    }

    // ✅ Convert Mat to BufferedImage for display
    private BufferedImage matToBufferedImage(Mat frame) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (frame.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = frame.channels() * frame.cols() * frame.rows();
        byte[] buffer = new byte[bufferSize];
        frame.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(frame.cols(), frame.rows(), type);
        final byte[] targetPixels = ((java.awt.image.DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}