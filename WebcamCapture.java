package computervision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;

public class WebcamCapture {
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

        System.out.println("Press 'Q' to quit.");

        // Capture and display frames in a loop
        while (true) {
            // Read the current frame
            if (!capture.read(frame)) {
                System.out.println("Error: Cannot read frame from webcam.");
                break;
            }

            // Display the frame
            HighGui.imshow("Webcam", frame);

            // Break loop if 'Q' is pressed
            if (HighGui.waitKey(30) == 'q') {
                break;
            }
        }

        // Release the resources
        capture.release();
        HighGui.destroyAllWindows();
    }
}
