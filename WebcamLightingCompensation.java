package computervision;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class WebcamLightingCompensation {
    static {
        // Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // Open webcam (device 0)
        VideoCapture capture = new VideoCapture(0);

        // Check if the webcam is opened
        if (!capture.isOpened()) {
            System.out.println("Error: Cannot open webcam.");
            return;
        }

        Mat frame = new Mat();
        Mat processedFrame = new Mat();

        System.out.println("Press 'Q' to quit.");

        // Capture and display frames in a loop
        while (true) {
            // Read the current frame
            if (!capture.read(frame)) {
                System.out.println("Error: Cannot read frame from webcam.");
                break;
            }

            // Compensate for ambient lighting
            compensateLighting(frame, processedFrame);

            // Display the processed frame
            HighGui.imshow("Webcam - Lighting Compensated", processedFrame);

            // Break loop if 'Q' is pressed
            if (HighGui.waitKey(30) == 'q') {
                break;
            }
        }

        // Release the resources
        capture.release();
        HighGui.destroyAllWindows();
    }

    private static void compensateLighting(Mat inputFrame, Mat outputFrame) {
        // Convert to grayscale
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(inputFrame, grayFrame, Imgproc.COLOR_BGR2GRAY);

        // Apply CLAHE (Contrast Limited Adaptive Histogram Equalization)
        Mat claheFrame = new Mat();
        Imgproc.createCLAHE(2.0, new Size(8, 8)).apply(grayFrame, claheFrame);

        // Merge CLAHE-enhanced grayscale back into the original color frame
        Imgproc.cvtColor(claheFrame, outputFrame, Imgproc.COLOR_GRAY2BGR);
    }
}
