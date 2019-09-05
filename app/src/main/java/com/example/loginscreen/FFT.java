package com.example.loginscreen;

public class FFT {
    double[] cos;
    /* renamed from: m */
    int f103m;
    /* renamed from: n */
    int f104n;
    double[] sin;

    public FFT(int n) {
        this.f104n = n;
        this.f103m = (int) (Math.log((double) n) / Math.log(2.0d));
        if (n != (1 << this.f103m)) {
            throw new RuntimeException("FFT length must be power of 2");
        }
        this.cos = new double[(n / 2)];
        this.sin = new double[(n / 2)];
        for (int i = 0; i < n / 2; i++) {
            this.cos[i] = Math.cos((((double) i) * -6.283185307179586d) / ((double) n));
            this.sin[i] = Math.sin((((double) i) * -6.283185307179586d) / ((double) n));
        }
    }

    public void fft2(double[] x, double[] y) {
        int i;
        int j = 0;
        int n2 = this.f104n / 2;
        for (i = 1; i < this.f104n - 1; i++) {
            int n1 = n2;
            while (j >= n1) {
                j -= n1;
                n1 /= 2;
            }
            j += n1;
            if (i < j) {
                double t1 = x[i];
                x[i] = x[j];
                x[j] = t1;
                t1 = y[i];
                y[i] = y[j];
                y[j] = t1;
            }
        }
        n2 = 1;
        for (i = 0; i < this.f103m; i++) {
            int n1 = n2;
            n2 += n2;
            int a = 0;
            for (j = 0; j < n1; j++) {
                double c = this.cos[a];
                double s = this.sin[a];
                a += 1 << ((this.f103m - i) - 1);
                for (int k = j; k < this.f104n; k += n2) {
                    double t1 = (x[k + n1] * c) - (y[k + n1] * s);
                    double t2 = (x[k + n1] * s) + (y[k + n1] * c);
                    x[k + n1] = x[k] - t1;
                    y[k + n1] = y[k] - t2;
                    x[k] = x[k] + t1;
                    y[k] = y[k] + t2;
                }
            }
        }
    }
}
