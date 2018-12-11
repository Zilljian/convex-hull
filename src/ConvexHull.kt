import javax.swing.JFrame

class ConvexHull(number: Int, weight: Int, height: Int) {
    private val points = HullBuilder(number)
    private val frame = JFrame("Convex Hull")
    init {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.add(points)
        frame.setSize(weight, height)
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}