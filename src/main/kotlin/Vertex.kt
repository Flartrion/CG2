data class Vertex(var x: Double, var y: Double, var z: Double) {

    operator fun plus(b: Vertex) = Vertex(x + b.x, y + b.y, z + b.z)

    operator fun minus(b: Vertex) = Vertex(x - b.x, y - b.y, z - b.z)

    operator fun times(b: Double) = Vertex(x * b, y * b, z * b)

    operator fun div(b: Double) = Vertex(x / b, y / b, z / b)

    override fun toString(): String = "($x, $y, $z)"

    override fun hashCode(): Int {
        return super.hashCode()
    }
}