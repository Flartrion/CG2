import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        val frame = MainWindow()
        val bezierCurves = BezierCurves()
        for (i in 0 until frame.pointData.size()) {
            bezierCurves.contourPoints.add(frame.pointData[i])
        }
    }
}
