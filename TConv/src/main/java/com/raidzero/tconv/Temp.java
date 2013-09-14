package com.raidzero.tconv;

/**
 * Created by raidzero on 9/13/13 2:15 PM
 */
public class Temp {
    private char unit;
    private double value;

    public Temp(double v, char u) {
        this.value = v;
        this.unit = u;
    }

    public double convert(char destUnit)
    {
        double res = 0;
        switch(destUnit) {
            case 'C':
                switch(unit) {
                    case 'F':
                        res = ((value - 32) * 5) / 9.0; // convert F to C
                        break;
                    case 'C':
                        res = value; //already C
                        break;
                    case 'K':
                        res = value - 273.15; // convert K to C
                        break;
                }
                break;
            case 'K':
                switch(unit) {
                    case 'F':
                        res = ((value - 32) / 1.8) + 273.15; // convert F to K
                        break;
                    case 'C':
                        res = value + 273.15; // convert C to K
                        break;
                    case 'K':
                        res = value; // already K
                        break;
                }
                break;
            case 'F':
                switch(unit) {
                    case 'F':
                        res = value; // already F
                        break;
                    case 'C':
                        res = ((value * 9) / 5.0) + 32;// convert C to F
                        break;
                    case 'K':
                        res = ((value - 273.15) * 1.8) + 32; // convert K to F
                        break;
                }
                break;
        }

        // round it to 3 decimal places
        res = Math.round(res * 1000) / 1000;
        return res;
    }
}
