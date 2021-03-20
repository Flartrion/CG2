import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
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
        background = Color.white
    }

    fun calculatePoints() {
        tempPoints.addAll(contourPoints)
        for (i in 0..100 step 1) {
            for (j in 0 until contourPoints.size - 1) {
                for (k in 0 until contourPoints.size - j - 1) {
                    tempPoints[k] = tempPoints[k] + (tempPoints[k + 1] - tempPoints[k]) * i.toDouble() / 100.0
                }
            }
            bezierCurvePoints[i] = tempPoints[0]
        }
    }

    private fun drawFigureFrom(g: Graphics2D, p1: Vertex, p2: Vertex) {
        val axis = p2 - p1
        val phi = atan2(axis.y, axis.x)
        val theta = atan2(sqrt(axis.x * axis.x + axis.y * axis.y), axis.z)
        val fov = sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z)

        val objectMatrix = Matrix(nodes.size + 2, 4)
        for (i in nodes.indices) {
            objectMatrix[i + 1, 1] = nodes[i].x
            objectMatrix[i + 1, 2] = nodes[i].y
            objectMatrix[i + 1, 3] = nodes[i].z
            objectMatrix[i + 1, 4] = 1.0
        }
        objectMatrix[nodes.size + 1, 1] = axisP1.x
        objectMatrix[nodes.size + 1, 2] = axisP1.y
        objectMatrix[nodes.size + 1, 3] = axisP1.z
        objectMatrix[nodes.size + 1, 4] = 1.0
        objectMatrix[nodes.size + 2, 1] = axisP2.x
        objectMatrix[nodes.size + 2, 2] = axisP2.y
        objectMatrix[nodes.size + 2, 3] = axisP2.z
        objectMatrix[nodes.size + 2, 4] = 1.0

        val preparationTransform = TransformMatrixFabric.translate(-p1.x, -p1.y, -p1.z) *
                TransformMatrixFabric.rotateZ(-phi) *
                TransformMatrixFabric.rotateY(-theta) *
                TransformMatrixFabric.rotateZ(PI/2)

        val resultMatrix = objectMatrix * preparationTransform * TransformMatrixFabric.perspectiveZ(1.0/fov)
        for (i in 1..resultMatrix.rows)
        {
            resultMatrix[i, 1] = resultMatrix[i, 1]/resultMatrix[i, 4]
            resultMatrix[i, 2] = resultMatrix[i, 2]/resultMatrix[i, 4]
        }

        g.translate(width / 2, height / 2)

        for (edge in edges) {
            val xy1 = Vertex(resultMatrix[edge[0] + 1, 1], resultMatrix[edge[0] + 1, 2], resultMatrix[edge[0] + 1, 3])
            val xy2 = Vertex(resultMatrix[edge[1] + 1, 1], resultMatrix[edge[1] + 1, 2], resultMatrix[edge[1] + 1, 3])
            g.drawLine(
                xy1.x.roundToInt(), xy1.y.roundToInt(),
                xy2.x.roundToInt(), xy2.y.roundToInt()
            )
        }
        for (i in nodes.indices) {
            g.fillOval(resultMatrix[i + 1, 1].roundToInt() - 4, resultMatrix[i + 1, 2].roundToInt() - 4, 8, 8)
            g.color = Color.ORANGE
        }

        if (isAxisVisible) {
            g.drawLine(
                resultMatrix[nodes.size + 1, 1].roundToInt(), resultMatrix[nodes.size + 1, 2].roundToInt(),
                resultMatrix[nodes.size + 2, 1].roundToInt(), resultMatrix[nodes.size + 2, 2].roundToInt()
            )
            g.fillOval(
                resultMatrix[nodes.size + 1, 1].roundToInt() - 4,
                resultMatrix[nodes.size + 1, 2].roundToInt() - 4,
                8,
                8
            )
            g.fillOval(
                resultMatrix[nodes.size + 2, 1].roundToInt() - 4,
                resultMatrix[nodes.size + 2, 2].roundToInt() - 4,
                8,
                8
            )
        }
    }
}