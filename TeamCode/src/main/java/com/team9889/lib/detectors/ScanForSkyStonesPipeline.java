package com.team9889.lib.detectors;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

/**
 * Created by joshua9889 on 11/25/2019.
 */
public class ScanForSkyStonesPipeline extends OpenCvPipeline {

    int positionOfSkyStone = 0;
    boolean debug = false;
    double upperPercentLimit = 0.55, lowerPercentLimit = 0.64;
    int threshold = 60;


    /**
     * Default Config
     */
    public ScanForSkyStonesPipeline(){
        this.debug = false;
        this.upperPercentLimit = 0.1;
        this.lowerPercentLimit = 0.4;
        this.threshold = 60;
    }

    /**
     * Custom Config
     *
     * @param debug
     * @param upperPercentLimit
     * @param lowerPercentLimit
     * @param threshold
     */
    public ScanForSkyStonesPipeline(boolean debug, double upperPercentLimit, double lowerPercentLimit, int threshold) {
        this.debug = false;
        this.upperPercentLimit = upperPercentLimit;
        this.lowerPercentLimit = lowerPercentLimit;
        this.threshold = threshold;
    }

    /**
     * @return Position of center of skystone
     */
    public double getPositionOfSkyStone() {
        return positionOfSkyStone;
    }

    @Override
    public Mat processFrame(Mat input) {
        boolean rotate = true;
        boolean scale = false;
        boolean useGrayScale = false;
        boolean drawRectangles = false;

        Mat rotatedMat = new Mat();
        if(rotate) {
            Core.rotate(input, rotatedMat, Core.ROTATE_180); //ROTATE_180 or ROTATE_90_COUNTERCLOCKWISE
        }

        Mat scaledMat = new Mat();
        if (scale) {
            double scaleDownFactor = 4;

            int height = input.height();
            int width = input.width();

            int scaledHeight = (int)(height / scaleDownFactor);
            int scaledWidth = (int)(width / scaleDownFactor);

            Size scaledSize = new Size(scaledWidth, scaledHeight);
            Imgproc.resize(rotate ? rotatedMat : input, scaledMat, scaledSize, 0, 0, Imgproc.INTER_AREA);
        } else {
            (rotate ? rotatedMat : input).copyTo(scaledMat);
        }

        Mat grayScaleMat = new Mat();
        if(useGrayScale) {
            Imgproc.cvtColor(scaledMat, grayScaleMat, Imgproc.COLOR_BGR2GRAY);
            Imgproc.cvtColor(grayScaleMat, grayScaleMat, Imgproc.COLOR_GRAY2RGB);
        } else {
            Imgproc.cvtColor(scaledMat, grayScaleMat, Imgproc.COLOR_BGR2RGB);
        }

        if (drawRectangles) {
            Imgproc.rectangle(grayScaleMat, new Point(0, 0),
                    new Point(grayScaleMat.width(), (int) (grayScaleMat.height() * upperPercentLimit)),
                    new Scalar(0, 0, 0), 1);

            Imgproc.rectangle(grayScaleMat, new Point(0, (int) (grayScaleMat.height() * lowerPercentLimit)),
                    new Point(grayScaleMat.width(), grayScaleMat.height()),
                    new Scalar(0, 0, 0), 1);
        }

        int columnCount = 0;
        int columnsFound = 0;
        for(int x=0; x<=grayScaleMat.width(); x++) {

            double columnScore = 0;
            for (int y = (int) (grayScaleMat.height() * upperPercentLimit); y <= (int) (grayScaleMat.height() * lowerPercentLimit); y++) {
                try {
                    double color = 0;
                    for (double pixelScale : grayScaleMat.get(y, x)) {
                        color += pixelScale/3.0;
                    }

                    if(color < threshold) {
                        if(debug) grayScaleMat.put(y,x, 0, 255, 0);
                        columnScore++;
                    } else if(debug) {
                        grayScaleMat.put(y, x, color, color, color);
                    }

                } catch (NullPointerException e) {
                    Log.e("Camera: ", e.toString());
                }
            }

            int heightOfScanningArea = (int)((grayScaleMat.height() * lowerPercentLimit)
                    - (grayScaleMat.height() * upperPercentLimit));

            int columnScoreForBlock = (int)(0.5 * heightOfScanningArea);
            if (columnScore > columnScoreForBlock) {
                if(debug) Imgproc.line(grayScaleMat,
                        new Point(x, (int) (grayScaleMat.height() * upperPercentLimit)),
                        new Point(x, (int) (grayScaleMat.height() * lowerPercentLimit)),
                        new Scalar(0, 255, 0), 1);

                columnCount++;
            } else columnCount = 0;

            if (columnCount > 5) {
                if(debug) Imgproc.circle(grayScaleMat, new Point(x, (int)(heightOfScanningArea/2 + grayScaleMat.height() * upperPercentLimit)), 4, new Scalar(255, 0, 0), -1);
                columnsFound++;
                columnCount = 0;
            }

            if(columnsFound == 3) {
                positionOfSkyStone = x;
                Imgproc.circle(grayScaleMat, new Point(positionOfSkyStone,
                                (int)(heightOfScanningArea/2 + grayScaleMat.height() * upperPercentLimit)),
                        4, new Scalar(255, 0, 0), -1);
            }
        }

        Imgproc.putText(grayScaleMat, "Pos: " + positionOfSkyStone, new Point(0,40),
                1, 1, new Scalar(255, 0,0));
        Imgproc.putText(grayScaleMat, "Col: " + columnsFound, new Point(0,50),
                1, 1, new Scalar(255, 0,0));

        return grayScaleMat;
    }
}
