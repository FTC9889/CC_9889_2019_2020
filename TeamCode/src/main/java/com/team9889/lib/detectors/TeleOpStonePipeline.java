package com.team9889.lib.detectors;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/11/2020.
 */
public class TeleOpStonePipeline extends OpenCvPipeline {
    //Outputs
    private Mat cvResizeOutput = new Mat();
    private Mat blurOutput = new Mat();
    private Mat hsvThresholdOutput = new Mat();
    private Mat maskOutput = new Mat();

    private Point point = new Point(1e10, 1e10);

    public Point getMinPoint() {
        return point;
    }

    /**
     * This is the primary method that runs the entire pipeline and updates the outputs.
     */
    @Override
    public Mat processFrame(Mat source0) {
        Imgproc.cvtColor(source0, source0, Imgproc.COLOR_RGB2HSV);
        // Step CV_resize0:
        Mat cvResizeSrc = source0;
        Size cvResizeDsize = new Size(0, 0);
        double cvResizeFx = 0.25;
        double cvResizeFy = 0.25;
        int cvResizeInterpolation = Imgproc.INTER_NEAREST;
        cvResize(cvResizeSrc, cvResizeDsize, cvResizeFx, cvResizeFy, cvResizeInterpolation, cvResizeOutput);

        // Step Blur0:
        Mat blurInput = cvResizeOutput;
        BlurType blurType = BlurType.get("Box Blur");
        double blurRadius = 0.0;
        blur(blurInput, blurType, blurRadius, blurOutput);

        // Step HSV_Threshold0:
        Mat hsvThresholdInput = blurOutput;
        double[] hsvThresholdHue = {5.0, 45.0};
        double[] hsvThresholdSaturation = {200, 255.0};
        double[] hsvThresholdValue = {0.0, 255.0};
        hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

        // Step Mask0:
        Mat maskInput = blurOutput;
        Mat maskMask = hsvThresholdOutput;
        mask(maskInput, maskMask, maskOutput);

        // Step Find Contours
        Mat contourInput = new Mat();

        Imgproc.cvtColor(maskOutput, contourInput, Imgproc.COLOR_HSV2RGB);
        Imgproc.cvtColor(contourInput, contourInput, Imgproc.COLOR_RGB2GRAY);

        Mat cannyOutput = new Mat();
        Imgproc.Canny(contourInput, cannyOutput, 500, 1000);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierachy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierachy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        List<Moments> mu = new ArrayList<>();

        for (int i = 0; i < contours.size(); i++) {
            mu.add(Imgproc.moments(contours.get(i)));
        }

        List<Point> mc = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            mc.add(new Point(mu.get(i).m10 / mu.get(i).m00 + 1e-5, mu.get(i).m01 / mu.get(i).m00 + 1e-5));
        }
        Imgproc.cvtColor(contourInput, contourInput, Imgproc.COLOR_GRAY2RGB);

        for (int i = 0; i < contours.size(); i++) {
            Imgproc.circle(contourInput, mc.get(i), 4, new Scalar(255, 0, 0), -1);
        }

        double minDistance = 1e10;
        Point minPoint = new Point(contourInput.width()/2, contourInput.height());
        Imgproc.circle(contourInput, minPoint, 1, new Scalar(0, 0, 255), -1);
        for (int i = 0; i < contours.size(); i++) {
            double x = mc.get(i).x;
            double y = mc.get(i).x;

            double c_x = contourInput.width()/2;
            double c_y = contourInput.height();

            double dist = Math.pow((x - c_x), 2) + Math.pow((y - c_y), 2);

            if(dist < minDistance) {
                minDistance = dist;
                minPoint = mc.get(i);
            }
        }

        point = minPoint;
        Imgproc.circle(contourInput, minPoint, 1, new Scalar(0, 255, 0), -1);

        cvResize(contourInput, cvResizeDsize, 3, 3, cvResizeInterpolation, contourInput);

        return contourInput;
    }

    /**
     * Resizes an image.
     * @param src The image to resize.
     * @param dSize size to set the image.
     * @param fx scale factor along X axis.
     * @param fy scale factor along Y axis.
     * @param interpolation type of interpolation to use.
     * @param dst output image.
     */
    private void cvResize(Mat src, Size dSize, double fx, double fy, int interpolation,
                          Mat dst) {
        if (dSize==null) {
            dSize = new Size(0,0);
        }
        Imgproc.resize(src, dst, dSize, fx, fy, interpolation);
    }

    /**
     * An indication of which type of filter to use for a blur.
     * Choices are BOX, GAUSSIAN, MEDIAN, and BILATERAL
     */
    enum BlurType{
        BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
        BILATERAL("Bilateral Filter");

        private final String label;

        BlurType(String label) {
            this.label = label;
        }

        public static BlurType get(String type) {
            if (BILATERAL.label.equals(type)) {
                return BILATERAL;
            }
            else if (GAUSSIAN.label.equals(type)) {
                return GAUSSIAN;
            }
            else if (MEDIAN.label.equals(type)) {
                return MEDIAN;
            }
            else {
                return BOX;
            }
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    /**
     * Softens an image using one of several filters.
     * @param input The image on which to perform the blur.
     * @param type The blurType to perform.
     * @param doubleRadius The radius for the blur.
     * @param output The image in which to store the output.
     */
    private void blur(Mat input, BlurType type, double doubleRadius,
                      Mat output) {
        int radius = (int)(doubleRadius + 0.5);
        int kernelSize;
        switch(type){
            case BOX:
                kernelSize = 2 * radius + 1;
                Imgproc.blur(input, output, new Size(kernelSize, kernelSize));
                break;
            case GAUSSIAN:
                kernelSize = 6 * radius + 1;
                Imgproc.GaussianBlur(input,output, new Size(kernelSize, kernelSize), radius);
                break;
            case MEDIAN:
                kernelSize = 2 * radius + 1;
                Imgproc.medianBlur(input, output, kernelSize);
                break;
            case BILATERAL:
                Imgproc.bilateralFilter(input, output, -1, radius, radius);
                break;
        }
    }

    /**
     * Segment an image based on hue, saturation, and value ranges.
     *
     * @param input The image on which to perform the HSL threshold.
     * @param hue The min and max hue
     * @param sat The min and max saturation
     * @param val The min and max value
     * @paramThe image in which to store the output.
     */
    private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), out);
    }

    /**
     * Filter out an area of an image using a binary mask.
     * @param input The image on which the mask filters.
     * @param mask The binary image that is used to filter.
     * @param output The image in which to store the output.
     */
    private void mask(Mat input, Mat mask, Mat output) {
        mask.convertTo(mask, CvType.CV_8UC1);
        Core.bitwise_xor(output, output, output);
        input.copyTo(output, mask);
    }


}
