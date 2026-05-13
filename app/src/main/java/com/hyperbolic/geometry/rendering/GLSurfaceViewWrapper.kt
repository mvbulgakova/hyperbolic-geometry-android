package com.hyperbolic.geometry.rendering

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

/**
 * Wrapper for GLSurfaceView with touch handling
 */
class GLSurfaceViewWrapper(context: Context) : GLSurfaceView(context) {
    private var lastX = 0f
    private var lastY = 0f
    private var lastDistance = 0f
    private val renderer = GLRenderer()

    init {
        setEGLContextClientVersion(2)
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        return when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - lastX
                val deltaY = event.y - lastY
                renderer.rotateCamera(deltaX * 0.5f, deltaY * 0.5f)
                lastX = event.x
                lastY = event.y
                requestRender()
                true
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    lastDistance = getDistance(event)
                }
                true
            }
            MotionEvent.ACTION_POINTER_MOVE -> {
                if (event.pointerCount == 2) {
                    val distance = getDistance(event)
                    if (lastDistance > 0) {
                        val scale = distance / lastDistance
                        renderer.zoomCamera(1.0f / scale)
                        requestRender()
                    }
                    lastDistance = distance
                }
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun getDistance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return kotlin.math.sqrt(x * x + y * y)
    }

    fun updateSphereParameters(
        centerX: Float,
        centerY: Float,
        centerZ: Float,
        radius: Float,
        showAxes: Boolean
    ) {
        renderer.sphereCenterX = centerX
        renderer.sphereCenterY = centerY
        renderer.sphereCenterZ = centerZ
        renderer.sphereRadius = radius
        renderer.showAxes = showAxes
        requestRender()
    }
}