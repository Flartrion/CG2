import java.awt.*
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.JPanel
import kotlin.collections.ArrayList
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

class BezierCurves : JPanel() {
    val contourPoints = ArrayList<Vertex>()

    val tempPoints = ArrayList<Vertex>()

    val bezierCurvePoints = ArrayList<Vertex>()

    init {
        preferredSize = Dimension(640, 640)
        javax.swing.Timer(10) {
            repaint()
        }.start()
    }

    fun calculatePoints() {
        tempPoints.clear()
        tempPoints.addAll(contourPoints)
        bezierCurvePoints.clear()
        for (i in 0..100 step 1) {
            for (j in 0 until contourPoints.size - 1) {
                for (k in 0 until contourPoints.size - j - 1) {
                    tempPoints[k] = tempPoints[k] + (tempPoints[k + 1] - tempPoints[k]) * i.toDouble() / 100.0
                }
            }
            bezierCurvePoints.add(tempPoints[0])
        }
    }

    private fun drawFigureFrom(g: Graphics2D, p1: Vertex, p2: Vertex) {
        val axis = p2 - p1
        val phi = atan2(axis.y, axis.x)
        val theta = atan2(sqrt(axis.x * axis.x + axis.y * axis.y), axis.z)
        val fov = sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z)

        val objectMatrix = Matrix(contourPoints.size + bezierCurvePoints.size, 4)
        for (i in contourPoints.indices) {
            objectMatrix[i + 1, 1] = contourPoints[i].x
            objectMatrix[i + 1, 2] = contourPoints[i].y
            objectMatrix[i + 1, 3] = contourPoints[i].z
            objectMatrix[i + 1, 4] = 1.0
        }

        for (i in bezierCurvePoints.indices) {
            objectMatrix[i + 1 + contourPoints.size, 1] = bezierCurvePoints[i].x
            objectMatrix[i + 1 + contourPoints.size, 2] = bezierCurvePoints[i].y
            objectMatrix[i + 1 + contourPoints.size, 3] = bezierCurvePoints[i].z
            objectMatrix[i + 1 + contourPoints.size, 4] = 1.0
        }

        val preparationTransform = TransformMatrixFabric.translate(-p1.x, -p1.y, -p1.z) *
                TransformMatrixFabric.rotateZ(-phi) *
                TransformMatrixFabric.rotateY(-theta) *
                TransformMatrixFabric.rotateZ(PI / 2)

        val resultMatrix = objectMatrix * preparationTransform * TransformMatrixFabric.scale(1.0,1.0,0.0) // TransformMatrixFabric.perspectiveZ(1.0 / fov)
//        for (i in 1..resultMatrix.rows) {
//            resultMatrix[i, 1] = resultMatrix[i, 1] / resultMatrix[i, 4]
//            resultMatrix[i, 2] = resultMatrix[i, 2] / resultMatrix[i, 4]
//        }

        g.translate(width / 2, height / 2)

        g.color = Color.WHITE
        for (i in 0 until contourPoints.size - 1) {

            val xy1 = Vertex(resultMatrix[i + 1, 1], resultMatrix[i + 1, 2], resultMatrix[i + 1, 3])
            val xy2 = Vertex(resultMatrix[i + 2, 1], resultMatrix[i + 2, 2], resultMatrix[i + 2, 3])
            g.drawLine(
                xy1.x.roundToInt(), xy1.y.roundToInt(),
                xy2.x.roundToInt(), xy2.y.roundToInt()
            )
        }

        g.color = Color.CYAN
        for (i in contourPoints.size until contourPoints.size + bezierCurvePoints.size - 1) {
            val xy1 = Vertex(resultMatrix[i + 1, 1], resultMatrix[i + 1, 2], resultMatrix[i + 1, 3])
            val xy2 = Vertex(resultMatrix[i + 2, 1], resultMatrix[i + 2, 2], resultMatrix[i + 2, 3])
            g.drawLine(
                xy1.x.roundToInt(), xy1.y.roundToInt(),
                xy2.x.roundToInt(), xy2.y.roundToInt()
            )
        }
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        val gg = g as Graphics2D
        background = Color.BLACK
        drawFigureFrom(gg, Vertex(-10.0, 0.0, 0.0), Vertex(-1.0, 0.0, 0.0))
//        repaint()
//        gg.translate(width/2, height/2)
//        gg.drawImage(a,0,0,this)
    }
}