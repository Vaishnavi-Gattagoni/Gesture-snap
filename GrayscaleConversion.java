package computervision;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class GrayscaleConversion {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load OpenCV
        
        VideoCapture camera = new VideoCapture(0); // Access webcam
        if (!camera.isOpened()) {
            System.out.println("Error: Camera not found!");
            return;
        }

        Mat frame = new Mat();  // Original frame
        Mat grayFrame = new Mat(); // Grayscale frame

        while (true) {
            camera.read(frame); // Capture frame
            if (frame.empty()) break;

            // Convert to grayscale
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
            
            // Save the grayscale image (for testing)
            Imgcodecs.imwrite("grayscale_image.jpg", grayFrame);
            
            break; // Capture one frame and exit
        }

        camera.release(); // Release webcam
    }
}

