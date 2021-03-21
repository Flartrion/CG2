import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.Border
import javax.swing.event.MouseInputAdapter
import kotlin.math.PI

class MainWindow : JFrame() {
    val pointData = DefaultListModel<Vertex>()
    private val pointList = JList<Vertex>()
    private val pointAddButton = JButton("Добавить")
    private val pointRemoveButton = JButton("Удалить")
    private val pointEditButton = JButton("Изменить")
    val rotatingFigure = BezierCurves()


    init {
        val inputPanel = JPanel()
        inputPanel.layout = BorderLayout()

        val pointInput = JPanel()
        pointInput.layout = BoxLayout(pointInput, BoxLayout.PAGE_AXIS)


        pointData.addElement(Vertex(0.0, 0.0, 0.0))
        pointData.addElement(Vertex(0.0, 0.0, 50.0))
        pointData.addElement(Vertex(50.0, 50.0, 0.0))
        pointData.addElement(Vertex(25.0, 25.0, -50.0))
        pointData.addElement(Vertex(25.0, -25.0, -30.0))
        pointData.addElement(Vertex(-25.0, 25.0, -60.0))
        pointData.addElement(Vertex(-50.0, -50.0, 0.0))
        pointData.addElement(Vertex(0.0, 0.0, 0.0))

        pointList.model = pointData
        pointList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        pointEditButton.addActionListener {
            if (pointList.selectedValue != null)
            PointChangeDialogue(this) {
                pointData[pointList.selectedIndex] = it
                rotatingFigure.contourPoints[pointList.selectedIndex] = it
            }
            rotatingFigure.calculatePoints()
//            rotatingFigure.repaint()
//            pointList.invalidate()
        }

        pointAddButton.addActionListener {
            PointChangeDialogue(this) {
                pointData.addElement(it)
                rotatingFigure.contourPoints.add(it)
            }
            rotatingFigure.calculatePoints()
//            rotatingFigure.repaint()
//            pointList.invalidate()
        }

        pointRemoveButton.addActionListener {
            if (pointList.selectedValue != null) {
                pointData.remove(pointList.selectedIndex)
                rotatingFigure.contourPoints.clear();
                for (i in 0 until pointData.size()) {
                    rotatingFigure.contourPoints.add(pointData[i])
                }
            }
            rotatingFigure.calculatePoints()
//            pointList.invalidate()
        }

        pointInput.add(JScrollPane(pointList))
        pointInput.add(pointAddButton)
        pointInput.add(pointEditButton)
        pointInput.add(pointRemoveButton)

        val rotations = JPanel()
        rotations.layout = GridLayout(2, 3)

        val rotationX = JTextField("5")
        val rotationY = JTextField("5")
        val rotationXСonfirm = JButton("Повернуть")
        val rotationYСonfirm = JButton("Повернуть")

        rotations.add(JLabel("X: ", 0))
        rotations.add(rotationX)
        rotations.add(rotationXСonfirm)
        rotations.add(JLabel("Y: ", 0))
        rotations.add(rotationY)
        rotations.add(rotationYСonfirm)

        val topLabels = JPanel()
        topLabels.layout = GridLayout(1, 2)
        topLabels.add(JLabel("Точки кривой", 0))
        topLabels.add(JLabel("Повороты", 0))

        val inputZone = JPanel()
        inputZone.layout = GridLayout(1, 2)
        inputZone.add(pointInput)
        inputZone.add(rotations)

        inputPanel.add(topLabels, BorderLayout.NORTH)
        inputPanel.add(inputZone, BorderLayout.SOUTH)
//        inputPanel.add(JLabel("X первой точки"))
//        inputPanel.add(firstPointX)
//        inputPanel.add(JLabel("Y первой точки"))
//        inputPanel.add(firstPointY)
//        inputPanel.add(JLabel("Z первой точки"))
//        inputPanel.add(firstPointZ)
//        inputPanel.add(JLabel("X второй точки"))
//        inputPanel.add(secondPointX)
//        inputPanel.add(JLabel("Y второй точки"))
//        inputPanel.add(secondPointY)
//        inputPanel.add(JLabel("Z второй точки"))
//        inputPanel.add(secondPointZ)
//        inputPanel.add(JLabel("Угол"))
//        inputPanel.add(angle)
//        inputPanel.add(rotateButton)
//        inputPanel.add(showAxisButton)

        for (i in 0 until pointData.size()) {
            rotatingFigure.contourPoints.add(pointData[i])
        }
        rotatingFigure.calculatePoints()

        this.add(inputPanel, BorderLayout.NORTH)
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.title = "Rotating figure"
        this.isResizable = true
        this.add(rotatingFigure, BorderLayout.CENTER)
        this.pack()
        this.setLocationRelativeTo(null)
        this.isVisible = true

//        showAxisButton.addActionListener {
//            rotatingFigure.showRotationAxis(
//                Vector(firstPointX.text.toDouble(), firstPointY.text.toDouble(), firstPointZ.text.toDouble()),
//                Vector(secondPointX.text.toDouble(), secondPointY.text.toDouble(), secondPointZ.text.toDouble())
//            )
//        }
//
//        rotateButton.addActionListener {
//            rotatingFigure.rotateAroundAxis(
//                angle.text.toDouble() / 180 * PI,
//                Vector(firstPointX.text.toDouble(), firstPointY.text.toDouble(), firstPointZ.text.toDouble()),
//                Vector(secondPointX.text.toDouble(), secondPointY.text.toDouble(), secondPointZ.text.toDouble())
//            )
//            rotatingFigure.repaint()
//            rotatingFigure.showRotationAxis(
//                Vector(firstPointX.text.toDouble(), firstPointY.text.toDouble(), firstPointZ.text.toDouble()),
//                Vector(secondPointX.text.toDouble(), secondPointY.text.toDouble(), secondPointZ.text.toDouble())
//            )
//        }
    }
}