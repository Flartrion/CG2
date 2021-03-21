import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*

class MainWindow : JFrame() {
    private val pointData = DefaultListModel<Vertex>()
    private val pointList = JList<Vertex>()
    private val pointAddButton = JButton("Добавить")
    private val pointRemoveButton = JButton("Удалить")
    private val pointEditButton = JButton("Изменить")
    private val bezierCurve = BezierCurves()


    init {
        val inputPanel = JPanel()
        inputPanel.layout = BorderLayout()

        val pointInput = JPanel()
        pointInput.layout = BoxLayout(pointInput, BoxLayout.PAGE_AXIS)


        pointData.addElement(Vertex(0.0, 0.0, 0.0))
        pointData.addElement(Vertex(0.0, 0.0, 50.0))
        pointData.addElement(Vertex(78.0, 50.0, 0.0))
        pointData.addElement(Vertex(25.0, 78.0, -50.0))
        pointData.addElement(Vertex(25.0, -25.0, -30.0))
        pointData.addElement(Vertex(-25.0, 25.0, -60.0))
        pointData.addElement(Vertex(-50.0, -80.0, 0.0))
        pointData.addElement(Vertex(0.0, 0.0, 0.0))

        pointList.model = pointData
        pointList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        pointList.addListSelectionListener {
            if (pointList.isSelectionEmpty) {
                pointEditButton.isEnabled = false
                pointRemoveButton.isEnabled = false
            } else {
                pointEditButton.isEnabled = true
                pointRemoveButton.isEnabled = true
            }
        }

        pointEditButton.addActionListener {
            if (pointList.selectedValue != null)
                PointChangeDialogue(this) {
                    pointData[pointList.selectedIndex] = it
                }
            bezierCurve.contourPoints.clear()
            for (i in 0 until pointData.size()) {
                bezierCurve.contourPoints.add(pointData[i])
            }
            bezierCurve.calculatePoints()
            repaint()
        }

        pointAddButton.addActionListener {
            PointChangeDialogue(this) {
                pointData.addElement(it)
            }
            bezierCurve.contourPoints.clear()
            for (i in 0 until pointData.size()) {
                bezierCurve.contourPoints.add(pointData[i])
            }
            bezierCurve.calculatePoints()
            repaint()
        }

        pointRemoveButton.addActionListener {
            if (pointList.selectedValue != null) {
                pointData.remove(pointList.selectedIndex)
                bezierCurve.contourPoints.clear()
                for (i in 0 until pointData.size()) {
                    bezierCurve.contourPoints.add(pointData[i])
                }
            }
            bezierCurve.calculatePoints()
            repaint()
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

        rotationXСonfirm.addActionListener {
            bezierCurve.rotateOnX(rotationX.text.toDouble())
            repaint()
        }
        rotationYСonfirm.addActionListener {
            bezierCurve.rotateOnY(rotationY.text.toDouble())
            repaint()
        }

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

        for (i in 0 until pointData.size()) {
            bezierCurve.contourPoints.add(pointData[i])
        }
        bezierCurve.calculatePoints()

        this.add(inputPanel, BorderLayout.NORTH)
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.title = "Rotating figure"
        this.isResizable = true
        this.add(bezierCurve, BorderLayout.CENTER)
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