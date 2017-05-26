package com.feresr.imdino;

/**
 * Created by feresr on 26/5/17.
 */

public class Utils {

    // return pdf(x) = standard Gaussian pdf
    public static double gaussian(double x) {
        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
    }

    // return pdf(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
    public static double pdf(double x, double mu, double sigma) {
        return Utils.gaussian((x - mu) / sigma) / sigma;
    }
}
