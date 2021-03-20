import java.awt.Dialog
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.*

class PointChangeDialogue(owner: JFrame, onCompletion: (Vector) -> Unit) : JDialog(owner, true) {
    private val xTextBox = JTextField()
    private val yTextBox = JTextField()
    private val zTextBox = JTextField()
    private val cancelButton = JButton("ОтменаCOCK")
    private val confirmButton = JButton("ДобавитьDICK")

    init {
        this.preferredSize = Dimension(400, 400)
        this.layout = GridLayout(4,2)
        this.title = "Добавление точки"
        this.isResizable = true

        this.add(JLabel("X: ", 0))
        this.add(xTextBox)

        this.add(JLabel("Y: ", 0))
        this.add(yTextBox)

        this.add(JLabel("Z: ", 0))
        this.add(zTextBox)

        confirmButton.addActionListener { print(it.actionCommand) }
        cancelButton.addActionListener { print(it.actionCommand) }

        this.add(confirmButton)
        this.add(cancelButton)

        this.pack()
        this.isVisible = true


    }
}