package com.hyperbolic.geometry.geometry

import kotlin.math.*

/**
 * Equidistant surface in Beltrami-Klein model
 */
class EquidistantSurface(
    val zLevel: Float,
    val hyperbolicDistance: Float
) {
    private val absoluteRadius = 1.0f

    fun getCoshD(): Float = cosh(hyperbolicDistance.toDouble()).toFloat()
    fun getSinhD(): Float = sinh(hyperbolicDistance.toDouble()).toFloat()

    fun getZForEquidistant(x: Float, y: Float): Pair<Float, Float> {
        val p = zLevel
        val coshD = getCoshD()
        val sinhD = getSinhD()

        val A = coshD * coshD - p * p * sinhD * sinhD
        val B = -2.0f * p * coshD
        val C = p * p * coshD * coshD + (1.0f - p * p) * sinhD * sinhD * (x * x + y * y - 1.0f)

        val discriminant = maxOf(B * B - 4.0f * A * C, 0.0f)
        val sqrtDisc = sqrt(discriminant)

        return if (abs(A) < 1e-9f) {
            if (abs(B) > 1e-9f) {
                val z = -C / B
                Pair(z, z)
            } else {
                Pair(p, p)
            }
        } else {
            val z1 = (-B + sqrtDisc) / (2.0f * A)
            val z2 = (-B - sqrtDisc) / (2.0f * A)
            Pair(z1, z2)
        }
    }

    /**
     * Generate base plane surface points
     */
    fun generateBasePlanePoints(segments: Int = 50): List<FloatArray> {
        val points = mutableListOf<FloatArray>()
        val maxRadiusSq = absoluteRadius * absoluteRadius - zLevel * zLevel
        if (maxRadiusSq < 0) return points

        val maxRadius = sqrt(maxRadiusSq)
        val rStep = maxRadius / segments
        val thetaStep = 2 * PI.toFloat() / segments

        for (i in 0..segments) {
            val r = rStep * i
            for (j in 0..segments) {
                val theta = thetaStep * j
                val x = r * cos(theta)
                val y = r * sin(theta)
                points.add(floatArrayOf(x, y, zLevel))
            }
        }

        return points
    }

    /**
     * Generate equidistant surface points
     */
    fun generateEquidistantPoints(segments: Int = 50): List<FloatArray> {
        val points = mutableListOf<FloatArray>()
        val basePlane = generateBasePlanePoints(segments)

        for (point in basePlane) {
            val (z1, z2) = getZForEquidistant(point[0], point[1])
            points.add(floatArrayOf(point[0], point[1], z1))
            if (abs(z1 - z2) > 1e-6f) {
                points.add(floatArrayOf(point[0], point[1], z2))
            }
        }

        return points
    }
}