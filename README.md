# GestureSnap – Real-Time Gesture Recognition System

GestureSnap is a real-time gesture recognition system built using Java and OpenCV.  
It captures webcam input, processes video frames through a modular computer vision pipeline, and detects hand gestures reliably under varying lighting conditions.

## 🚀 Features
- Real-time webcam frame capture
- Lighting compensation for low-light environments
- Image preprocessing (grayscale, thresholding, edge detection)
- Modular hand detection pipeline
- Low-latency, extensible system design

## 🧠 System Design
The pipeline follows a staged processing approach:
1. Webcam frame capture
2. Lighting normalization
3. Grayscale conversion
4. Edge detection and thresholding
5. Hand contour detection

This modular design allows easy tuning and extension of individual stages.

## 🛠️ Tech Stack
- **Language:** Java  
- **Computer Vision:** OpenCV  
- **Input:** Webcam video stream  

## 📂 Project Structure
src/main/java/gesturesnap/
├── WebcamCapture.java
├── GrayscaleConversion.java
├── EdgeDetectionExample.java
├── ThresholdingExample.java
├── HandDetection.java
├── WebcamLightingCompensation.java
├── WebcamCaptureWithLightCompensation.java


## ▶️ How to Run
1. Install Java (JDK 8+)
2. Install OpenCV and configure native libraries
3. Clone this repository
4. Run:
```bash
javac *.java
java WebcamCaptureWithLightCompensation


