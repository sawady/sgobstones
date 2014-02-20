package Gobstones

object Codificaciones {
  
  case class Codificacion(azul: Int, negro: Int, rojo: Int, verde: Int, recurso: String)
  
  def codificaciones(): List[Codificacion] = List(
      Codificacion(0,0,0,0,"Grass")
  )

}