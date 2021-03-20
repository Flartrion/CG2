import java.awt.*
import javax.swing.*
import kotlin.math.*

class RotatingFigure : JPanel() {
    private val nodes = arrayOf(
        Vector(-1.0, -1.0, -1.0),
        Vector(-1.0, 1.0, 1.0),
        Vector(1.0, -1.0, 1.0),
        Vector(1.0, 1.0, -1.0)
    )
    private val edges = arrayOf(
        intArrayOf(0, 1),
        intArrayOf(1, 2),
        intArrayOf(2, 0),
        intArrayOf(0, 3),
        intArrayOf(3, 2),
        intArrayOf(3, 1)
    )

    var axisP1 = Vector(0.0, 0.0, 0.0)
    var axisP2 = Vector(0.0, 0.0, 0.0)
    private var isAxisVisible = false

    init {
        preferredSize = Dimension(640, 640)
        background = Color.white
        scale(100.0)
        rotateFigure(Math.PI / 3.0, atan(sqrt(2.0)))
        Timer(17) {
//            rotateFigure(Math.PI / 180.0, 0.0)
            repaint()
        }.start()
    }

    private fun scale(s: Double) {
        for (node in nodes) {
            node.x *= s
            node.y *= s
            node.z *= s
        }
    }

    private fun rotateFigure(angleX: Double, angleY: Double) {
        val sinX = sin(angleX)
        val cosX = cos(angleX)
        val sinY = sin(angleY)
        val cosY = cos(angleY)
        for (node in nodes) {
            val x = node.x
            val y = node.y
            var z = node.z
            node.x = x * cosX - z * sinX
            node.z = z * cosX + x * sinX
            z = node.z
            node.y = y * cosY - z * sinY
            node.z = z * cosY + y * sinY
        }
    }

    fun rotateAroundAxis(angle: Double, axisP1: Vector, axisP2: Vector) {
        val axis = axisP2 - axisP1
        val phi = atan2(axis.y, axis.x)
        val theta = atan2(sqrt(axis.x * axis.x + axis.y * axis.y), axis.z)

        val objectMatrix = Matrix(nodes.size, 4)
        for (i in nodes.indices) {
            objectMatrix[i + 1, 1] = nodes[i].x
            objectMatrix[i + 1, 2] = nodes[i].y
            objectMatrix[i + 1, 3] = nodes[i].z
            objectMatrix[i + 1, 4] = 1.0
        }
        val preparationTransform = TransformMatrixFabric.translate(-axisP1.x, -axisP1.y, -axisP1.z) *
                TransformMatrixFabric.rotateZ(-phi) *
                TransformMatrixFabric.rotateY(-theta)
        val returnTransform = TransformMatrixFabric.rotateY(theta) *
                TransformMatrixFabric.rotateZ(phi) *
                TransformMatrixFabric.translate(axisP1.x, axisP1.y, axisP1.z)
        val resultMatrix = objectMatrix * preparationTransform * TransformMatrixFabric.rotateZ(angle) * returnTransform

        for (i in nodes.indices) {
            nodes[i].x = resultMatrix[i + 1, 1]
            nodes[i].y = resultMatrix[i + 1, 2]
            nodes[i].z = resultMatrix[i + 1, 3]
        }
    }

    private fun drawFigure(g: Graphics2D) {
        g.translate(width / 2, height / 2)
        for (edge in edges) {
            val xy1 = nodes[edge[0]]
            val xy2 = nodes[edge[1]]
            g.drawLine(
                xy1.x.roundToInt(), xy1.y.roundToInt(),
                xy2.x.roundToInt(), xy2.y.roundToInt()
            )
        }
        for (node in nodes) {
            g.fillOval(node.x.roundToInt() - 4, node.y.roundToInt() - 4, 8, 8)
            g.color = Color.ORANGE
        }

        if (isAxisVisible) {
            g.drawLine(
                axisP1.x.roundToInt(), axisP1.y.roundToInt(),
                axisP2.x.roundToInt(), axisP2.y.roundToInt()
            )
            g.fillOval(axisP1.x.roundToInt() - 4, axisP1.y.roundToInt() - 4, 8, 8)
            g.fillOval(axisP2.x.roundToInt() - 4, axisP2.y.roundToInt() - 4, 8, 8)
        }
    }

    private fun drawFigureFrom(g: Graphics2D, p1: Vector, p2: Vector) {
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
            val xy1 = Vector(resultMatrix[edge[0] + 1, 1], resultMatrix[edge[0] + 1, 2], resultMatrix[edge[0] + 1, 3])
            val xy2 = Vector(resultMatrix[edge[1] + 1, 1], resultMatrix[edge[1] + 1, 2], resultMatrix[edge[1] + 1, 3])
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

    public override fun paintComponent(gg: Graphics) {
        super.paintComponent(gg)
        val g = gg as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.color = Color.blue
//        drawFigure(g)
        drawFigureFrom(g, Vector(200.0, 200.0, 200.0), Vector(50.0, 50.0, 50.0))
    }

    fun showRotationAxis(point1: Vector, point2: Vector) {
        isAxisVisible = true
        axisP1 = point1
        axisP2 = point2
    }
}

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        val frame = MainWindow()
    }
}