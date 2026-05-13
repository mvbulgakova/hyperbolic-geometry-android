package com.hyperbolic.geometry.rendering

import android.opengl.GLSurfaceView
import android.opengl.GLU
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.*

/**
 * OpenGL ES Renderer for 3D hyperbolic geometry visualization
 */
class GLRenderer : GLSurfaceView.Renderer {
    private var cameraX = 1.5f
    private var cameraY = 1.5f
    private var cameraZ = 1.5f
    private var rotationX = 0f
    private var rotationY = 0f

    // Model parameters
    var sphereCenterX = 0.2f
    var sphereCenterY = -0.1f
    var sphereCenterZ = 0.3f
    var sphereRadius = 0.4f
    var showAxes = true

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Set clear color to white with transparency
        gl?.glClearColor(0.95f, 0.95f, 0.95f, 1f)
        
        // Enable depth testing
        gl?.glEnable(GL10.GL_DEPTH_TEST)
        gl?.glDepthFunc(GL10.GL_LEQUAL)
        gl?.glDepthMask(true)
        
        // Enable lighting
        gl?.glEnable(GL10.GL_LIGHTING)
        gl?.glEnable(GL10.GL_LIGHT0)
        gl?.glEnable(GL10.GL_COLOR_MATERIAL)
        
        // Setup light parameters
        val lightAmbient = floatArrayOf(0.5f, 0.5f, 0.5f, 1f)
        val lightDiffuse = floatArrayOf(1f, 1f, 1f, 1f)
        val lightPosition = floatArrayOf(2f, 2f, 2f, 0f)
        
        gl?.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0)
        gl?.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0)
        gl?.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0)
        
        // Enable smooth shading
        gl?.glShadeModel(GL10.GL_SMOOTH)
    }

    override fun onDrawFrame(gl: GL10?) {
        gl?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl?.glLoadIdentity()

        // Set camera position
        GLU.gluLookAt(
            gl,
            cameraX, cameraY, cameraZ,  // Camera position
            0f, 0f, 0f,                  // Look at point
            0f, 1f, 0f                   // Up vector
        )

        // Apply rotations
        gl?.glRotatef(rotationX, 1f, 0f, 0f)
        gl?.glRotatef(rotationY, 0f, 1f, 0f)

        // Draw absolute sphere (boundary)
        drawAbsoluteSphere(gl)
        
        // Draw hyperbolic sphere
        drawHyperbolicSphere(gl)
        
        // Draw geodesic lines
        drawGeodesicLines(gl)
        
        // Draw axes if enabled
        if (showAxes) {
            drawAxes(gl)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
        gl?.glMatrixMode(GL10.GL_PROJECTION)
        gl?.glLoadIdentity()
        
        val ratio = width.toFloat() / height.toFloat()
        GLU.gluPerspective(gl, 45f, ratio, 0.1f, 100f)
        
        gl?.glMatrixMode(GL10.GL_MODELVIEW)
        gl?.glLoadIdentity()
    }

    private fun drawAbsoluteSphere(gl: GL10?) {
        val radius = 1.0f
        val latSegments = 30
        val lonSegments = 30

        // Set material properties
        val blueMaterial = floatArrayOf(0f, 0f, 1f, 0.2f)
        gl?.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_DIFFUSE, blueMaterial, 0)

        gl?.glBegin(GL10.GL_TRIANGLE_STRIP)
        for (lat in 0 until latSegments) {
            val phi1 = PI.toFloat() * lat / latSegments
            val phi2 = PI.toFloat() * (lat + 1) / latSegments

            for (lon in 0..lonSegments) {
                val theta = 2 * PI.toFloat() * lon / lonSegments

                // First vertex
                val x1 = (radius * cos(theta) * sin(phi1)).toFloat()
                val y1 = (radius * sin(theta) * sin(phi1)).toFloat()
                val z1 = (radius * cos(phi1)).toFloat()
                gl?.glNormal3f(x1, y1, z1)
                gl?.glVertex3f(x1, y1, z1)

                // Second vertex
                val x2 = (radius * cos(theta) * sin(phi2)).toFloat()
                val y2 = (radius * sin(theta) * sin(phi2)).toFloat()
                val z2 = (radius * cos(phi2)).toFloat()
                gl?.glNormal3f(x2, y2, z2)
                gl?.glVertex3f(x2, y2, z2)
            }
        }
        gl?.glEnd()
    }

    private fun drawHyperbolicSphere(gl: GL10?) {
        val radius = sphereRadius
        val latSegments = 20
        val lonSegments = 20

        // Set material properties - green
        val greenMaterial = floatArrayOf(0f, 1f, 0f, 0.7f)
        gl?.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_DIFFUSE, greenMaterial, 0)

        gl?.glBegin(GL10.GL_TRIANGLE_STRIP)
        for (lat in 0 until latSegments) {
            val phi1 = PI.toFloat() * lat / latSegments
            val phi2 = PI.toFloat() * (lat + 1) / latSegments

            for (lon in 0..lonSegments) {
                val theta = 2 * PI.toFloat() * lon / lonSegments

                // First vertex
                val x1 = sphereCenterX + radius * cos(theta) * sin(phi1)
                val y1 = sphereCenterY + radius * sin(theta) * sin(phi1)
                val z1 = sphereCenterZ + radius * cos(phi1)
                gl?.glNormal3f((x1 - sphereCenterX) / radius, (y1 - sphereCenterY) / radius, (z1 - sphereCenterZ) / radius)
                gl?.glVertex3f(x1, y1, z1)

                // Second vertex
                val x2 = sphereCenterX + radius * cos(theta) * sin(phi2)
                val y2 = sphereCenterY + radius * sin(theta) * sin(phi2)
                val z2 = sphereCenterZ + radius * cos(phi2)
                gl?.glNormal3f((x2 - sphereCenterX) / radius, (y2 - sphereCenterY) / radius, (z2 - sphereCenterZ) / radius)
                gl?.glVertex3f(x2, y2, z2)
            }
        }
        gl?.glEnd()
    }

    private fun drawGeodesicLines(gl: GL10?) {
        val numLines = 30
        val redMaterial = floatArrayOf(1f, 0f, 0f, 1f)
        gl?.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_DIFFUSE, redMaterial, 0)
        gl?.glDisable(GL10.GL_LIGHTING)

        gl?.glLineWidth(1.5f)
        for (i in 0 until numLines) {
            val phi = acos(1.0f - 2.0f * (i + 0.5f) / numLines)
            val theta = PI.toFloat() * (1.0f + sqrt(5f)) * (i + 0.5f)

            val dirX = cos(theta) * sin(phi)
            val dirY = sin(theta) * sin(phi)
            val dirZ = cos(phi)

            gl?.glBegin(GL10.GL_LINES)
            gl?.glVertex3f(
                sphereCenterX + 2f * dirX,
                sphereCenterY + 2f * dirY,
                sphereCenterZ + 2f * dirZ
            )
            gl?.glVertex3f(
                sphereCenterX - 2f * dirX,
                sphereCenterY - 2f * dirY,
                sphereCenterZ - 2f * dirZ
            )
            gl?.glEnd()
        }
        gl?.glLineWidth(1f)
        gl?.glEnable(GL10.GL_LIGHTING)
    }

    private fun drawAxes(gl: GL10?) {
        gl?.glDisable(GL10.GL_LIGHTING)
        gl?.glLineWidth(2f)
        val axisLength = 1.2f

        // X axis - red
        gl?.glColor4f(1f, 0f, 0f, 1f)
        gl?.glBegin(GL10.GL_LINES)
        gl?.glVertex3f(-axisLength, 0f, 0f)
        gl?.glVertex3f(axisLength, 0f, 0f)
        gl?.glEnd()

        // Y axis - green
        gl?.glColor4f(0f, 1f, 0f, 1f)
        gl?.glBegin(GL10.GL_LINES)
        gl?.glVertex3f(0f, -axisLength, 0f)
        gl?.glVertex3f(0f, axisLength, 0f)
        gl?.glEnd()

        // Z axis - blue
        gl?.glColor4f(0f, 0f, 1f, 1f)
        gl?.glBegin(GL10.GL_LINES)
        gl?.glVertex3f(0f, 0f, -axisLength)
        gl?.glVertex3f(0f, 0f, axisLength)
        gl?.glEnd()

        gl?.glLineWidth(1f)
        gl?.glEnable(GL10.GL_LIGHTING)
    }

    fun rotateCamera(deltaX: Float, deltaY: Float) {
        rotationX += deltaY
        rotationY += deltaX
    }

    fun zoomCamera(scale: Float) {
        cameraX *= scale
        cameraY *= scale
        cameraZ *= scale
    }
}