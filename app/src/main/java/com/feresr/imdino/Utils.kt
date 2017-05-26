package com.feresr.imdino

/**
 * Created by feresr on 26/5/17.
 */

object Utils {

    // return pdf(x) = standard Gaussian pdf
    fun gaussian(x: Double): Double {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI)
    }

    // return pdf(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
    fun pdf(x: Double, mu: Double, sigma: Double): Double {
        return Utils.gaussian((x - mu) / sigma) / sigma
    }
}
