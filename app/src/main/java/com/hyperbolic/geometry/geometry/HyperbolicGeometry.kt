package com.hyperbolic.geometry.geometry

import kotlin.math.*

/**
 * Core hyperbolic geometry calculations
 */
object HyperbolicGeometry {
    const val ABSOLUTE_RADIUS = 1.0f

    /**
     * Calculate hyperbolic sphere parameters
     */
    data class Sphere(
        val centerX: Float,
        val centerY: Float,
        val centerZ: Float,
        val radius: Float
    ) {
        fun getDistanceFromOrigin(): Float =
            sqrt(centerX * centerX + centerY * centerY + centerZ * centerZ)

        fun getSquashFactor(): Float {
            val distFromOrigin = getDistanceFromOrigin()
            return sqrt(maxOf(1.0f - distFromOrigin * distFromOrigin, 1e-9f))
        }
    }

    /**
     * Calculate horosphere parameters
     */
    data class Horosphere(
        val phi: Float,  // in degrees
        val theta: Float, // in degrees
        val kPrime: Float
    ) {
        fun getTouchPoint(): Triple<Float, Float, Float> {
            val phiRad = Math.toRadians(phi.toDouble()).toFloat()
            val thetaRad = Math.toRadians(theta.toDouble()).toFloat()
            val r = ABSOLUTE_RADIUS

            return Triple(
                r * cos(phiRad) * sin(thetaRad),
                r * sin(phiRad) * sin(thetaRad),
                r * cos(thetaRad)
            )
        }

        fun getEllipsoidCenter(): Triple<Float, Float, Float> {
            val (x, y, z) = getTouchPoint()
            val scale = 1.0f / (1.0f + kPrime)
            return Triple(x * scale, y * scale, z * scale)
        }

        fun getRadiusParallel(): Float = kPrime / (1.0f + kPrime)
        fun getRadiusPerp(): Float = sqrt(kPrime) / sqrt(1.0f + kPrime)
    }

    /**
     * Calculate equidistant surface parameters
     */
    data class EquidistantSurface(
        val zLevel: Float,
        val hyperbolicDistance: Float
    ) {
        fun getCoshD(): Float = cosh(hyperbolicDistance.toDouble()).toFloat()
        fun getSinhD(): Float = sinh(hyperbolicDistance.toDouble()).toFloat()

        fun getZForEquidistant(x: Float, y: Float): Pair<Float, Float> {
            val p = zLevel
            val d = hyperbolicDistance
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
    }

    /**
     * Calculate bundle (пучок) parameters
     */
    data class Bundle(
        val type: BundleType,
        val pX: Float,
        val pY: Float,
        val angle: Float,  // in degrees
        val distance: Float
    ) {
        enum class BundleType {
            ELLIPTIC, PARABOLIC, HYPERBOLIC
        }

        fun getIdealPoint(): Pair<Float, Float> {
            val angleRad = Math.toRadians(angle.toDouble()).toFloat()
            val r = ABSOLUTE_RADIUS
            return Pair(
                r * cos(angleRad),
                r * sin(angleRad)
            )
        }
    }

    /**
     * Rotation matrix using Rodrigues' formula
     */
    fun getRotationMatrix(
        fromX: Float, fromY: Float, fromZ: Float,
        toX: Float, toY: Float, toZ: Float
    ): Array<FloatArray> {
        val v = listOf(
            toY * fromZ - toZ * fromY,
            toZ * fromX - toX * fromZ,
            toX * fromY - toY * fromX
        )
        val vNorm = sqrt(v.sumOf { it * it.toDouble() }).toFloat()
        val c = fromX * toX + fromY * toY + fromZ * toZ

        val identity = arrayOf(
            floatArrayOf(1f, 0f, 0f),
            floatArrayOf(0f, 1f, 0f),
            floatArrayOf(0f, 0f, 1f)
        )

        if (vNorm < 1e-9f) {
            return identity
        }

        val vx = arrayOf(
            floatArrayOf(0f, -v[2], v[1]),
            floatArrayOf(v[2], 0f, -v[0]),
            floatArrayOf(-v[1], v[0], 0f)
        )

        // R = I + vx + vx^2 * (1-c) / s^2
        val result = Array(3) { FloatArray(3) }
        for (i in 0..2) {
            for (j in 0..2) {
                result[i][j] = identity[i][j] + vx[i][j] + vx[i][j] * (1 - c) / (vNorm * vNorm)
            }
        }
        return result
    }
}