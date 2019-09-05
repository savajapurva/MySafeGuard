package com.example.loginscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Detection {
    private static final int FALL_MODULE_HIGH_SENSIBILITY = 3;
    private static final int FALL_MODULE_LOW_SENSIBILITY = 1;
    private static final int FALL_MODULE_NORMAL_SENSIBILITY = 2;
    private static final int IMPACT_MODULE_HIGH_SENSIBILITY = 15;
    private static final int IMPACT_MODULE_LOW_SENSIBILITY = 19;
    private static final int IMPACT_MODULE_NORMAL_SENSIBILITY = 17;
    private static final double MIN_HEIGHT_HIGH_SENSIBILITY = 0.2d;
    private static final double MIN_HEIGHT_LOW_SENSIBILITY = 0.3d;
    private static final double MIN_HEIGHT_NORMAL_SENSIBILITY = 0.25d;
    private static final int MODE_HIGH_SENSIBILITY = 0;
    private static final int MODE_LOW_SENSIBILITY = 2;
    private static final int MODE_NORMAL_SENSIBILITY = 1;
    private static int SIZE = 128;
    private static final Double SLOPE_MAX = Double.valueOf(150.0d);
    private static final Double SLOPE_MIN = Double.valueOf(100.0d);
    private static final int TIMEOUT = 50;
    private static final int TO_SECS = 1000000000;
    private double acelAverage;
    private double bTime;
    private boolean bounce;
    private ArrayList<Point> fallArray;
    private double fallHeight;
    private int fallModValue;
    private boolean falling;
    private FadeGlobal globals = ((FadeGlobal) this.mContext);
    private Point heightValue;
    private boolean impact;
    private Stack<Point> impactArray;
    private int impactModValue;
    private Point impactPoint;
    private Context mContext;
    private double minHeight;
    private Queue<Point> prev;
    private Point[] prevArray;
    private double rCoef;
    private double rest;
    private boolean restored;
    private boolean revised;
    private Double time;
    private Stack<Point> uArray;
    private Queue<Point> uPrev;
    private Point[] uPrevArray;

    public Detection(Context c, Queue<Point> q, SharedPreferences prefs) {
        this.mContext = c;
        this.acelAverage = Double.parseDouble(prefs.getString("average", ""));
        this.rest = Double.parseDouble(prefs.getString("restauration", ""));
        switch (prefs.getInt("sensibility", 1)) {
            case 0:
                this.fallModValue = 3;
                this.impactModValue = IMPACT_MODULE_HIGH_SENSIBILITY;
                this.minHeight = MIN_HEIGHT_HIGH_SENSIBILITY;
                break;
            case 1:
                this.fallModValue = 2;
                this.impactModValue = IMPACT_MODULE_NORMAL_SENSIBILITY;
                this.minHeight = MIN_HEIGHT_NORMAL_SENSIBILITY;
                break;
            case 2:
                this.fallModValue = 1;
                this.impactModValue = 19;
                this.minHeight = MIN_HEIGHT_LOW_SENSIBILITY;
                break;
        }
        this.prev = q;
        this.prevArray = new Point[SIZE];
        this.fallArray = new ArrayList();
        this.impactArray = new Stack();
        this.uPrev = new LinkedList();
        this.uArray = new Stack();
        initConfig();
    }

    public double bounceTime() {
        Point p1 = (Point) this.impactArray.pop();
        Point p2 = (Point) this.impactArray.pop();
        Double v2 = Double.valueOf(Double.valueOf(p1.getData().doubleValue() - p2.getData().doubleValue()).doubleValue() * Double.valueOf(((double) (p1.getTime() - p2.getTime())) / 1.0E9d).doubleValue());
        while (!this.impactArray.isEmpty()) {
            p1 = p2;
            p2 = (Point) this.impactArray.pop();
            v2 = Double.valueOf(v2.doubleValue() + (Double.valueOf(p1.getData().doubleValue() - p2.getData().doubleValue()).doubleValue() * Double.valueOf(((double) (p1.getTime() - p2.getTime())) / 1.0E9d).doubleValue()));
        }
        this.rCoef = v2.doubleValue() / Math.sqrt((2.0d * this.acelAverage) * this.fallHeight);
        return ((1.0d + this.rCoef) / (1.0d - this.rCoef)) * Math.sqrt((2.0d * this.fallHeight) / this.acelAverage);
    }

    private boolean checkImpact(Point p) {
        if (p.getData().doubleValue() >= this.rest || slope(p, this.impactPoint).doubleValue() >= SLOPE_MAX.doubleValue()) {
            return true;
        }
        return false;
    }

    public int detect(Point p, Point pPrev) {
        if (this.bounce) {
            this.time = Double.valueOf(this.time.doubleValue() + (((double) (p.getTime() - pPrev.getTime())) / 1.0E9d));
            this.fallArray.add(p);
            if (this.time.doubleValue() >= this.bTime) {
                return 2;
            }
            return 5;
        } else if (this.impact) {
            if (slope(p, pPrev).doubleValue() >= 0.0d) {
                this.impactArray.push(p);
            } else if (checkImpact(pPrev)) {
                this.fallHeight = (0.5d * this.acelAverage) * Math.pow(((double) (this.impactPoint.getTime() - this.heightValue.getTime())) / 1.0E9d, 2.0d);
                if (this.fallHeight < this.minHeight) {
                    for (int i = 0; i < this.fallArray.size(); i++) {
                        this.prev.poll();
                        this.prev.offer((Point) this.fallArray.get(i));
                    }
                    initConfig();
                } else {
                    this.bounce = true;
                    this.bTime = bounceTime();
                    this.time = Double.valueOf(0.0d);
                }
            } else {
                this.impact = false;
                Point aux = p;
                while (this.impactArray.size() > 1) {
                    Point aux2 = (Point) this.impactArray.pop();
                    this.time = Double.valueOf(this.time.doubleValue() + (((double) (aux.getTime() - aux2.getTime())) / 1.0E9d));
                    aux = aux2;
                }
                this.revised = true;
                this.impactArray.clear();
            }
            this.fallArray.add(p);
            return 5;
        } else if (this.falling) {
            this.time = Double.valueOf(this.time.doubleValue() + (((double) (p.getTime() - pPrev.getTime())) / 1.0E9d));
            if (this.time.doubleValue() > 50.0d) {
                return 3;
            }
            if (slope(p, pPrev).doubleValue() >= SLOPE_MIN.doubleValue() || p.getData().doubleValue() >= this.rest) {
                this.impact = true;
                this.impactArray.push(pPrev);
                this.impactArray.push(p);
                this.impactPoint = pPrev;
                if (!this.revised) {
                    int i = SIZE - 2;
                    this.heightValue = this.prevArray[i + 1];
                    Point prev = this.prevArray[i];
                    while (i > 0 && this.heightValue.getData().doubleValue() < this.rest) {
                        this.heightValue = prev;
                        prev = this.prevArray[i - 1];
                        i--;
                    }
                    this.revised = false;
                }
            }
            this.fallArray.add(p);
            return 5;
        } else if (!modDetect(p, this.fallModValue)) {
            return 0;
        } else {
            this.falling = true;
            this.fallArray.add(p);
            return 1;
        }
    }

    @SuppressLint("WrongConstant")
    public boolean fallDetect(Point p, Point pPrev) {
        if (p.getData().doubleValue() < this.rest && !this.restored) {
            this.restored = true;
        }
        switch (detect(p, pPrev)) {
            case 0:
                impactDetect(p, pPrev);
                this.prev.poll();
                this.prev.offer(p);
                break;
            case 1:
                this.prev.toArray(this.prevArray);
                break;
            case 2:
                return true;
            case 3:
                Toast.makeText(this.mContext, "Fade: Fall detector, Error. Se ha superado el tiempo máximo de caída", 0).show();
                this.globals.setFallTimeExceeded(true);
                return true;
        }
        return false;
    }

    public void initConfig() {
        this.falling = false;
        this.impact = false;
        this.bounce = false;
        this.revised = false;
        this.time = Double.valueOf(0.0d);
        this.fallArray.clear();
        this.impactArray.clear();
        this.restored = false;
        this.uPrev.clear();
        this.uArray.clear();
    }

    @SuppressLint("WrongConstant")
    public boolean impactDetect(Point p, Point pPrev) {
        if (pPrev.getData().doubleValue() > ((double) this.impactModValue) && slope(p, pPrev).doubleValue() < 0.0d && this.restored) {
            this.restored = false;
            Point[] medianArray = medianfilter_1D((Point[]) this.prev.toArray(new Point[SIZE]), 3);
            int i = SIZE - 2;
            boolean start = false;
            boolean end = false;
            ArrayList<Point> uFA = new ArrayList();
            this.uArray.push(p);
            this.uArray.push(pPrev);
            while (i >= 0 && !end) {
                Point pAux = medianArray[i];
                double dAux = pAux.getData().doubleValue();
                this.uArray.push(pAux);
                if (start) {
                    uFA.add(pAux);
                    if (dAux >= this.rest) {
                        end = true;
                        this.heightValue = pAux;
                    }
                    i--;
                } else {
                    if (dAux < this.rest) {
                        start = true;
                        uFA.add(pAux);
                    }
                    i--;
                }
            }
            if (start) {
                if (!end) {
                    Point uHV = new Point(this.rest, ((Point) uFA.get(uFA.size() - 1)).getTime());
                    uFA.add(uHV);
                    this.uArray.push(uHV);
                    this.heightValue = uHV;
                }
                int j = getMin(uFA);
                for (int k = 1; k < j; k++) {
                    this.uPrev.offer((Point) this.uArray.pop());
                }
                this.uPrevArray = new Point[this.uPrev.size()];
                this.uPrev.toArray(this.uPrevArray);
                boolean detected = false;
                Point uPPrev = (Point) this.uArray.pop();
                while (!this.uArray.isEmpty() && !detected) {
                    Point uP = (Point) this.uArray.pop();
                    switch (uDetect(uP, uPPrev)) {
                        case 2:
                            detected = true;
                            break;
                        case 3:
                            Toast.makeText(this.mContext, "Fade: Fall detector. Error, se ha superado el tiempo máximo de caída", 0).show();
                            this.globals.setFallTimeExceeded(true);
                            detected = true;
                            break;
                        default:
                            break;
                    }
                    j--;
                    uPPrev = uP;
                }
                if (detected) {
                    return true;
                }
            }
            initConfig();
        }
        return false;
    }

    private int getMin(ArrayList<Point> array) {
        double min = this.rest;
        int index = 0;
        for (int i = array.size() - 1; i >= 0; i--) {
            if (((Point) array.get(i)).getData().doubleValue() < min) {
                index = i;
                min = ((Point) array.get(i)).getData().doubleValue();
            }
        }
        return array.size() - index;
    }

    public int uDetect(Point p, Point pPrev) {
        if (this.impact) {
            if (slope(p, pPrev).doubleValue() >= 0.0d) {
                this.impactArray.push(p);
            } else if (checkImpact(pPrev)) {
                this.fallHeight = (0.5d * this.acelAverage) * Math.pow(((double) (this.impactPoint.getTime() - this.heightValue.getTime())) / 1.0E9d, 2.0d);
                if (this.fallHeight < this.minHeight) {
                    initConfig();
                } else {
                    this.bounce = true;
                    this.bTime = bounceTime();
                    this.time = Double.valueOf(0.0d);
                    return 2;
                }
            } else {
                this.impact = false;
                Point aux = p;
                while (this.impactArray.size() > 1) {
                    Point aux2 = (Point) this.impactArray.pop();
                    this.time = Double.valueOf(this.time.doubleValue() + (((double) (aux.getTime() - aux2.getTime())) / 1.0E9d));
                    aux = aux2;
                }
                this.revised = true;
                this.impactArray.clear();
            }
            this.fallArray.add(p);
            return 5;
        }
        this.time = Double.valueOf(this.time.doubleValue() + (((double) (p.getTime() - pPrev.getTime())) / 1.0E9d));
        if (this.time.doubleValue() > 50.0d) {
            return 3;
        }
        if (slope(p, pPrev).doubleValue() >= SLOPE_MIN.doubleValue() || p.getData().doubleValue() >= this.rest) {
            this.impact = true;
            this.impactArray.push(pPrev);
            this.impactArray.push(p);
            this.impactPoint = pPrev;
        }
        this.revised = false;
        this.fallArray.add(p);
        return 5;
    }

    private boolean isOdd(int num) {
        if (num % 2 != 0) {
            return true;
        }
        return false;
    }

    private Point[] medianfilter_1D(Point[] input, int size) {
        Point[] filteredList = null;
        if (isOdd(size)) {
            int i;
            ArrayList<Double> output = new ArrayList();
            for (int k = 0; k < size - 1; k++) {
                output.add(input[k].getData());
            }
            int centralVal = (int) Math.floor((double) (size / 2));
            for (i = centralVal; i <= (SIZE - centralVal) - 1; i++) {
                double[] target = new double[size];
                int cont = 0;
                for (int j = i - centralVal; j <= i + centralVal; j++) {
                    target[cont] = input[j].getData().doubleValue();
                    cont++;
                }
                Arrays.sort(target);
                output.add(Double.valueOf(target[centralVal]));
            }
            int outputSize = output.size();
            filteredList = new Point[outputSize];
            for (i = 0; i < outputSize; i++) {
                filteredList[i] = new Point(((Double) output.get(i)).doubleValue(), input[i].getTime());
            }
        }
        return filteredList;
    }

    public boolean modDetect(Point p, int value) {
        if (p.getData().doubleValue() <= ((double) value)) {
            return true;
        }
        return false;
    }

    public Double slope(Point p, Point prev) {
        return Double.valueOf(Double.valueOf(p.getData().doubleValue() - prev.getData().doubleValue()).doubleValue() / Double.valueOf(((double) (p.getTime() - prev.getTime())) / 1.0E9d).doubleValue());
    }

    public double getFallHeight() {
        return this.fallHeight;
    }

    public Point getImpact() {
        return this.impactPoint;
    }

    public double getrTime() {
        return this.bTime;
    }

    public double getrCoef() {
        return this.rCoef;
    }

    public ArrayList<Point> getFallArray() {
        return this.fallArray;
    }

    public boolean getFalling() {
        return this.falling;
    }
}
