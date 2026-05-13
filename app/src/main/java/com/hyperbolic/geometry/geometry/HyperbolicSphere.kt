package com.hyperbolic.geometry.geometry

import kotlin.math.*

/**
 * Hyperbolic Sphere model implementation
 */
class HyperbolicSphere(
    val centerX: Float,
    val centerY: Float,
    val centerZ: Float,
    val euclideanRadius: Float
) {
    private val absoluteRadius = 1.0f
    private val center = floatArrayOf(centerX, centerY, centerZ)

    fun getDistanceFromOrigin(): Float {
        return sqrt(centerX * centerX + centerY * centerY + centerZ * centerZ)
    }

    fun getSquashFactor(): Float {
        val dist = getDistanceFromOrigin()
        return sqrt(maxOf(1.0f - dist * dist, 1e-9f))
    }

    fun getRadiusParallel(): Float {
        val dist = getDistanceFromOrigin()
        return if (dist < 1e-6f) {
            euclideanRadius
        } else {
            euclideanRadius * getSquashFactor()
        }
    }

    fun getRadiusPerp(): Float = euclideanRadius

    /**
     * Generate sphere surface points
     * Returns list of (x, y, z) coordinates
     */
    fun generateSurfacePoints(latSegments: Int = 50, lonSegments: Int = 50): List<FloatArray> {
        val points = mutableListOf<FloatArray>()
        val dist = getDistanceFromOrigin()

        if (dist < 1e-6f) {
            // Center at origin - regular sphere
            for (lat in 0..latSegments) {
                val phi = PI.toFloat() * lat / latSegments
                for (lon in 0..lonSegments) {
                    val theta = 2 * PI.toFloat() * lon / lonSegments
                    val x = centerX + euclideanRadius * cos(theta) * sin(phi)
                    val y = centerY + euclideanRadius * sin(theta) * sin(phi)
                    val z = centerZ + euclideanRadius * cos(phi)
                    points.add(floatArrayOf(x, y, z))
                }
            }
        } else {
            // Ellipsoid model
            val radiusParallel = getRadiusParallel()
            val radiusPerp = getRadiusPerp()

            // Generate unit sphere
            for (lat in 0..latSegments) {
                val phi = PI.toFloat() * lat / latSegments
                for (lon in 0..lonSegments) {
                    val theta = 2 * PI.toFloat() * lon / lonSegments
                    var x = radiusPerp * cos(theta) * sin(phi)
                    var y = radiusPerp * sin(theta) * sin(phi)
                    var z = radiusParallel * cos(phi)

                    // Rotate and translate
                    // (simplified - full rotation matrix would be applied here)
                    x += centerX
                    y += centerY
                    z += centerZ

                    points.add(floatArrayOf(x, y, z))
                }
            }
        }

        return points
    }

    /**
     * Generate geodesic lines on the sphere
     */
    fun generateGeodesicLines(numLines: Int = 50): List<Pair<FloatArray, FloatArray>> {
        val lines = mutableListOf<Pair<FloatArray, FloatArray>>()
        val r = absoluteRadius

        for (i in 0 until numLines) {
            val phi = acos(1.0f - 2.0f * (i + 0.5f) / numLines)
            val theta = PI.toFloat() * (1.0f + sqrt(5f)) * (i + 0.5f)

            val dirX = cos(theta) * sin(phi)
            val dirY = sin(theta) * sin(phi)
            val dirZ = cos(phi)

            // Find chord endpoints on absolute sphere
            val a = 1.0f
            val b = 2.0f * (centerX * dirX + centerY * dirY + centerZ * dirZ)
            val c = centerX * centerX + centerY * centerY + centerZ * centerZ - r * r

            val discriminant = b * b - 4 * a * c
            if (discriminant < 0) continue

            val t1 = (-b - sqrt(discriminant)) / (2 * a)
            val t2 = (-b + sqrt(discriminant)) / (2 * a)

            val p1 = floatArrayOf(
                centerX + t1 * dirX,
                centerY + t1 * dirY,
                centerZ + t1 * dirZ
            )
            val p2 = floatArrayOf(
                centerX + t2 * dirX,
                centerY + t2 * dirY,
                centerZ + t2 * dirZ
            )

            lines.add(Pair(p1, p2))
        }

        return lines
    }
}