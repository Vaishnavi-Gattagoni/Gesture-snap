package computervision;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class ThresholdingExample {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        VideoCapture camera = new VideoCapture(0); // Open webcam
        if (!camera.isOpened()) {
            System.out.println("Error: Camera not found!");
            return;
        }

        Mat frame = new Mat();  // Original frame
        Mat grayFrame = new Mat(); // Grayscale frame
        Mat threshFrame = new Mat(); // Thresholded frame

        while (true) {
            camera.read(frame); // Capture frame
            if (frame.empty()) break;

            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY); // Convert to grayscale
            
            // Apply thresholding (Binary Thresholding)
            Imgproc.threshold(grayFrame, threshFrame, 100, 255, Imgproc.THRESH_BINARY);

            // Save thresholded image for testing
            Imgcodecs.imwrite("thresholded_image.jpg", threshFrame);
            
            break; // Capture one frame and exit
        }

        camera.release(); // Release webcam
        System.out.println("Thresholded image saved!");
    }
}
