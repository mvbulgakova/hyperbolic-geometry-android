package com.hyperbolic.geometry.geometry

import kotlin.math.*

/**
 * Bundle of lines in Cayley-Klein model
 */
class BundleOfLines(
    val type: BundleType,
    val pX: Float = 0f,
    val pY: Float = 0f,
    val angle: Float = 0f,  // in degrees
    val distance: Float = 0f
) {
    enum class BundleType {
        ELLIPTIC,    // Intersecting lines
        PARABOLIC,   // Parallel lines
        HYPERBOLIC   // Diverging lines
    }

    private val absoluteRadius = 1.0f

    fun getIdealPoint(): Pair<Float, Float> {
        val angleRad = Math.toRadians(angle.toDouble()).toFloat()
        return Pair(
            absoluteRadius * cos(angleRad),
            absoluteRadius * sin(angleRad)
        )
    }

    /**
     * Generate lines for elliptic bundle (intersecting lines)
     */
    fun generateEllipticLines(numLines: Int = 20): List<Pair<FloatArray, FloatArray>> {
        val lines = mutableListOf<Pair<FloatArray, FloatArray>>()
        val centerX = minOf(maxOf(pX, -0.9f), 0.9f)
        val centerY = minOf(maxOf(pY, -0.9f), 0.9f)
        val r = absoluteRadius

        for (i in 0 until numLines) {
            val angle = i * PI.toFloat() / numLines
            val dirX = cos(angle)
            val dirY = sin(angle)

            val a = 1.0f
            val b = 2.0f * (centerX * dirX + centerY * dirY)
            val c = centerX * centerX + centerY * centerY - r * r

            val discriminant = b * b - 4 * a * c
            if (discriminant >= 0) {
                val t1 = (-b - sqrt(discriminant)) / (2 * a)
                val t2 = (-b + sqrt(discriminant)) / (2 * a)

                lines.add(Pair(
                    floatArrayOf(centerX + t1 * dirX, centerY + t1 * dirY),
                    floatArrayOf(centerX + t2 * dirX, centerY + t2 * dirY)
                ))
            }
        }

        return lines
    }

    /**
     * Generate lines for parabolic bundle (parallel lines)
     */
    fun generateParabolicLines(numLines: Int = 20): List<Pair<FloatArray, FloatArray>> {
        val lines = mutableListOf<Pair<FloatArray, FloatArray>>()
        val (idealX, idealY) = getIdealPoint()
        val r = absoluteRadius

        for (i in 0..numLines) {
            val endAngle = Math.toRadians(angle.toDouble()).toFloat() + PI.toFloat() / 2 +
                    (PI.toFloat() * (i + 0.5f) / (numLines + 1))
            val endX = r * cos(endAngle)
            val endY = r * sin(endAngle)

            lines.add(Pair(
                floatArrayOf(idealX, idealY),
                floatArrayOf(endX, endY)
            ))
        }

        return lines
    }

    /**
     * Generate lines for hyperbolic bundle (diverging lines)
     */
    fun generateHyperbolicLines(numLines: Int = 20): List<Pair<FloatArray, FloatArray>> {
        val lines = mutableListOf<Pair<FloatArray, FloatArray>>()
        val angleRad = Math.toRadians(angle.toDouble()).toFloat()
        val r = absoluteRadius
        val midpoint = floatArrayOf(
            distance * cos(angleRad),
            distance * sin(angleRad)
        )
        val direction = floatArrayOf(-sin(angleRad), cos(angleRad))
        val halfChordLen = sqrt(maxOf(r * r - distance * distance, 0f))

        val p1 = floatArrayOf(
            midpoint[0] - halfChordLen * direction[0],
            midpoint[1] - halfChordLen * direction[1]
        )
        val p2 = floatArrayOf(
            midpoint[0] + halfChordLen * direction[0],
            midpoint[1] + halfChordLen * direction[1]
        )

        val denominator = p1[0] * p2[1] - p2[0] * p1[1]
        if (abs(denominator) > 1e-9f) {
            val poleX = r * r * (p2[1] - p1[1]) / denominator
            val poleY = r * r * (p1[0] - p2[0]) / denominator
            val poleDist = sqrt(poleX * poleX + poleY * poleY)

            if (poleDist > r) {
                val angleToP = atan2(poleY, poleX)
                val halfAngle = asin(r / poleDist)
                val startAngle = angleToP - halfAngle
                val endAngle = angleToP + halfAngle

                for (i in 0 until numLines) {
                    val angle = startAngle + (endAngle - startAngle) * i / numLines
                    val lineDir = floatArrayOf(cos(angle), sin(angle))
                    val a = 1.0f
                    val b = 2.0f * (poleX * lineDir[0] + poleY * lineDir[1])
                    val c = poleX * poleX + poleY * poleY - r * r

                    val discriminant = b * b - 4 * a * c
                    if (discriminant >= 0) {
                        val t1 = (-b - sqrt(discriminant)) / (2 * a)
                        val t2 = (-b + sqrt(discriminant)) / (2 * a)

                        lines.add(Pair(
                            floatArrayOf(poleX + t1 * lineDir[0], poleY + t1 * lineDir[1]),
                            floatArrayOf(poleX + t2 * lineDir[0], poleY + t2 * lineDir[1])
                        ))
                    }
                }
            }
        }

        return lines
    }
}