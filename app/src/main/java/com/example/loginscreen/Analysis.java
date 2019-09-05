package com.example.loginscreen;

public class Analysis {
    public static int UNKNOW_MOVEMENT = Control.UNKNOW_MOVEMENT;
    private Detection fall;
    private Control postFall;
    private Control prevFall;

    public Analysis(Control f1, Detection f2, Control f3) {
        this.prevFall = f1;
        this.fall = f2;
        this.postFall = f3;
    }

    public int isFall() {
        if (this.postFall.fft()) {
            if (this.postFall.isDamped()) {
                return 10;
            }
            if (this.prevFall.fft()) {
                if (sameMove()) {
                    return 1;
                }
                return 0;
            } else if (sameMove()) {
                return 7;
            } else {
                return 2;
            }
        } else if (this.postFall.death()) {
            if (this.prevFall.fft()) {
                return 3;
            }
            return 4;
        } else if (this.prevFall.fft()) {
            if (sameMove()) {
                return 8;
            }
            return 5;
        } else if (sameMove()) {
            return 9;
        } else {
            return 6;
        }
    }

    public boolean sameMove() {
        return !(this.postFall.getMove() == ((double) UNKNOW_MOVEMENT)) && !(this.postFall.getMove() != this.prevFall.getMove());
    }
}
