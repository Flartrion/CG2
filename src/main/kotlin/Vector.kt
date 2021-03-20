data class Vector(var x: Double, var y: Double, var z: Double) {

    operator fun plus(b: Vector) = Vector(x + b.x, y + b.y, z + b.z)

    operator fun minus(b: Vector) = Vector(x - b.x, y - b.y, z - b.z)

    operator fun times(b: Double) = Vector(x * b, y * b, z * b)

    operator fun div(b: Double) = Vector(x / b, y / b, z / b)

    override fun toString(): String = "($x, $y, $z)"

    override fun hashCode(): Int {
        return super.hashCode()
    }
}