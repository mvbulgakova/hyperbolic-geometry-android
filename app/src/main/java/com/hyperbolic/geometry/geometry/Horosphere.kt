package com.hyperbolic.geometry.geometry

import kotlin.math.*

/**
 * Horosphere (Orisphere) model in Beltrami-Klein model
 */
class Horosphere(
    val phi: Float,  // longitude in degrees
    val theta: Float, // latitude in degrees
    val kPrime: Float // size parameter
) {
    private val absoluteRadius = 1.0f

    fun getTouchPoint(): FloatArray {
        val phiRad = Math.toRadians(phi.toDouble()).toFloat()
        val thetaRad = Math.toRadians(theta.toDouble()).toFloat()

        return floatArrayOf(
            absoluteRadius * cos(phiRad) * sin(thetaRad),
            absoluteRadius * sin(phiRad) * sin(thetaRad),
            absoluteRadius * cos(thetaRad)
        )
    }

    fun getEllipsoidCenter(): FloatArray {
        val (x, y, z) = getTouchPoint()
        val scale = 1.0f / (1.0f + kPrime)
        return floatArrayOf(x * scale, y * scale, z * scale)
    }

    fun getRadiusParallel(): Float = kPrime / (1.0f + kPrime)
    fun getRadiusPerp(): Float = sqrt(kPrime) / sqrt(1.0f + kPrime)

    /**
     * Generate surface points for horosphere
     */
    fun generateSurfacePoints(latSegments: Int = 40, lonSegments: Int = 40): List<FloatArray> {
        val points = mutableListOf<FloatArray>()
        val center = getEllipsoidCenter()
        val radiusParallel = getRadiusParallel()
        val radiusPerp = getRadiusPerp()
        val touchPoint = getTouchPoint()

        for (lat in 0..latSegments) {
            val phi = PI.toFloat() * lat / latSegments
            for (lon in 0..lonSegments) {
                val theta = 2 * PI.toFloat() * lon / lonSegments
                
                var x = radiusPerp * cos(theta) * sin(phi)
                var y = radiusPerp * sin(theta) * sin(phi)
                var z = radiusParallel * cos(phi)

                // Translate to center
                x += center[0]
                y += center[1]
                z += center[2]

                points.add(floatArrayOf(x, y, z))
            }
        }

        return points
    }

    /**
     * Generate geodesic lines from touch point
     */
    fun generateGeodesicLines(numLines: Int = 40): List<Pair<FloatArray, FloatArray>> {
        val lines = mutableListOf<Pair<FloatArray, FloatArray>>()
        val r = absoluteRadius
        val touchPoint = getTouchPoint()

        for (i in 0 until numLines) {
            val phi = acos(1.0f - 2.0f * (i + 0.5f) / numLines)
            val theta = PI.toFloat() * (1.0f + sqrt(5f)) * (i + 0.5f)

            val endX = r * cos(theta) * sin(phi)
            val endY = r * sin(theta) * sin(phi)
            val endZ = r * cos(phi)

            lines.add(Pair(
                touchPoint.copyOf(),
                floatArrayOf(endX, endY, endZ)
            ))
        }

        return lines
    }
}