package Gobstones

object Codificaciones {
  
  case class Codificacion(negro: Int, azul: Int, rojo: Int, verde: Int, recurso: String)
  
  def codificaciones(): List[Codificacion] = List(
      Codificacion(0,0,0,0,"Empty")
  )

}