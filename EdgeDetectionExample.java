package computervision;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class EdgeDetectionExample {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        VideoCapture camera = new VideoCapture(0); // Open webcam
        if (!camera.isOpened()) {
            System.out.println("Error: Camera not found!");
            return;
        }

        Mat frame = new Mat();  // Original frame
        Mat grayFrame = new Mat(); // Grayscale frame
        Mat edgeFrame = new Mat(); // Edge-detected frame

        while (true) {
            camera.read(frame); // Capture frame
            if (frame.empty()) break;

            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY); // Convert to grayscale
            
            // Apply Canny edge detection
            Imgproc.Canny(grayFrame, edgeFrame, 100, 200);

            // Save edge-detected image for testing
            Imgcodecs.imwrite("edge_detected_image.jpg", edgeFrame);
            
            break; // Capture one frame and exit
        }

        camera.release(); // Release webcam
        System.out.println("Edge-detected image saved!");
    }
}

