package com.example.loginscreen;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Queue;

import jsc.datastructures.PairedData;
import jsc.descriptive.MeanVar;
import jsc.regression.PearsonCorrelation;

public class Control {
    private static double MAX_DERROR = 0.6d;
    private static double MAX_FREQ_RUN = 3.0d;
    private static double MAX_FREQ_WALK = 2.0d;
    private static double MIN_CORR_COEF = 0.8d;
    private static double MIN_FREQ_RUN = 2.5d;
    private static double MIN_FREQ_WALK = 1.6d;
    private static int MIN_VALUES_PEARSON = 3;
    private static double MOVE_VALUE = 100.0d;
    private static double PERIODIC_VALUE = 200.0d;
    public static int RUN = 2;
    private static final int SIZE = 128;
    public static int UNKNOW_MOVEMENT = 0;
    public static int WALK = 1;
    /* renamed from: A */
    public double f100A;
    /* renamed from: B */
    public double f101B;
    /* renamed from: R */
    public double f102R;
    private double amplitude;
    private double[] dataArray;
    private float frequency;
    private double[] img;
    private MeanVar jscArray;
    private Context mContext;
    private int move = UNKNOW_MOVEMENT;
    private boolean periodic;
    private ArrayList<Point> queue;
    private double[] real;
    public ArrayList<Double> relativeMaxArray;
    public ArrayList<Double> samplesArray;
    private float samplesPeriod;

    public Control(Context c, Queue<Point> q) {
        this.mContext = c;
        this.real = new double[128];
        this.img = new double[128];
        this.dataArray = new double[128];
        this.periodic = false;
        this.queue = new ArrayList();
        this.frequency = 0.0f;
        int cont = 0;
        for (Point aux : q) {
            this.queue.add(aux);
            this.real[cont] = aux.getData();
            this.img[cont] = 0.0d;
            this.dataArray[cont] = aux.getData();
            cont++;
        }
    }

    public boolean fft() {
        int freq = 0;
        double max = 0.0d;
        double media = 0.0d;
        new FFT(128).fft2(this.real, this.img);
        for (int i = 1; i < 64; i++) {
            double dato = Math.sqrt((this.real[i] * this.real[i]) + (this.img[i] * this.img[i]));
            media += dato;
            if (dato > max) {
                max = dato;
                freq = i;
            }
        }
        media /= (double) 63;
        this.samplesPeriod = 0.02f;
        this.frequency = ((float) freq) / (128.0f * this.samplesPeriod);
        this.amplitude = max;
        if (max > MOVE_VALUE && ((double) this.frequency) > MIN_FREQ_WALK && ((double) this.frequency) < MAX_FREQ_WALK) {
            this.move = WALK;
        } else if (max > MOVE_VALUE && ((double) this.frequency) > MIN_FREQ_RUN && ((double) this.frequency) < MAX_FREQ_RUN) {
            this.move = RUN;
        }
        if (max > PERIODIC_VALUE) {
            this.periodic = true;
            return true;
        }
        this.periodic = false;
        return false;
    }

    public boolean death() {
        SharedPreferences prefs = this.mContext.getSharedPreferences("FallDetector", 0);
        double averageC = Double.parseDouble(prefs.getString("average", null));
        this.jscArray = new MeanVar(this.dataArray);
        double averageD = this.jscArray.getMean();
        return discriminate(averageC, Double.parseDouble(prefs.getString("deviation", null)), averageD);
    }

    public boolean discriminate(double avC, double devC, double avD) {
        if (1.0d - (Math.min(avC, avD) / Math.max(avC, avD)) < 0.1d) {
            int cont = 0;
            double limit = 4.0d * devC;
            for (int i = 0; i < 128; i++) {
                if (Math.abs(this.dataArray[i] - avD) > limit) {
                    cont++;
                }
            }
            return ((double) cont) / 128.0d < MAX_DERROR;
        }
        return false;
    }

    public boolean isDamped() {
        float period = 1.0f / this.frequency;
        Double relativeMax = 0.0d;
        int sample = 1;
        double time = 0.0d;
        this.relativeMaxArray = new ArrayList();
        this.samplesArray = new ArrayList();
        int i = 0;
        int j = 0;
        while (i < this.queue.size()) {
            if (time >= ((double) period)) {
                this.relativeMaxArray.add(relativeMax);
                this.samplesArray.add((double) sample);
                j++;
                relativeMax = 0.0d;
                time = 0.0d;
            }
            if (((Point) this.queue.get(i)).getData() > relativeMax) {
                relativeMax = ((Point) this.queue.get(i)).getData();
                sample = i;
            }
            i++;
            if (i < this.queue.size()) {
                time = time + (((double) (((Point) this.queue.get(i)).getTime() - ((Point) this.queue.get(i - 1)).getTime())) / 1.0E9d);
            }
        }
        this.relativeMaxArray.add(relativeMax);
        this.samplesArray.add((double) sample);
        if (this.relativeMaxArray.size() > MIN_VALUES_PEARSON) {
            double[] max = new double[this.relativeMaxArray.size()];
            double[] samples = new double[this.samplesArray.size()];
            for (j = 0; j < this.relativeMaxArray.size(); j++) {
                samples[j] = (Double) this.samplesArray.get(j);
                max[j] = Math.log((Double) this.relativeMaxArray.get(j));
            }
            PearsonCorrelation pearson = new PearsonCorrelation(new PairedData(samples, max));
            this.f100A = pearson.getA();
            this.f101B = pearson.getB();
            this.f102R = Math.pow(pearson.getR(), 2.0d);
            return this.f101B < 0.0d && this.f102R > MIN_CORR_COEF;
        }
        return false;
    }

    public boolean isPeriodic() {
        return this.periodic;
    }

    public double getAmplitude() {
        return this.amplitude;
    }

    public Double getAverage() {
        return this.jscArray.getMean();
    }

    public Double getDeviation() {
        return this.jscArray.getSd();
    }

    public float getFrequency() {
        return this.frequency;
    }

    public double getMove() {
        return (double) this.move;
    }
}
