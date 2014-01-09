package Gobstones

import scala.annotation.meta.getter
import scala.collection.mutable.Map

trait Gobstones {

  val width: Int = 15 //new Random().nextInt(19) + 1
  val height: Int = 15 //new Random().nextInt(19) + 1
  type Board = Array[Array[Cell]]

  @getter val board = makeBoard()
  @getter var cursorX = 0
  @getter var cursorY = 0

  private def makeColumn(): Array[Cell] = List.range(0, height).map(_ => new Cell()).toArray

  private def makeBoard(): Board = List.range(0, width).map(_ => makeColumn()).toArray

  private def current(): Cell = board(cursorX)(cursorY)

  /* Operaciones sobre el cabezal */

  def poner(c: Color) = current().poner(c)
  def sacar(c: Color) = current().sacar(c)
  def nroBolitas(c: Color) = current().nroBolitas(c)
  def hayBolitas(c: Color) = current().hayBolitas(c)
  def error(s: String) = throw new RuntimeException(s)
  def main()

  def mover(d: Dir) = {
    if (!puedeMover(d)) {
      throw new RuntimeException("No es posible moverse hacia la dirección: " + d)
    }
    d match {
      case Norte => cursorY += 1
      case Sur => cursorY -= 1
      case Este => cursorX += 1
      case Oeste => cursorX -= 1
    }
  }

  def puedeMover(d: Dir): Boolean = {
    d match {
      case Norte => cursorY < height - 1
      case Sur => cursorY > 0
      case Este => cursorX < width - 1
      case Oeste => cursorX > 0
    }
  }

  /* Estructuras de Control */

  def repeat(n: Int)(block: => Unit): Unit = {
    List.range(0, n).foreach(_ => block)
  }

  trait Color
  case object Azul extends Color
  case object Negro extends Color
  case object Rojo extends Color
  case object Verde extends Color

  def colores(): List[Color] = List(Negro, Azul, Verde, Rojo)

  def opuesto(d: Dir): Dir = d match {
    case Norte => Sur
    case Este => Oeste
    case Sur => Norte
    case Oeste => Este
  }

  trait Dir
  case object Norte extends Dir
  case object Sur extends Dir
  case object Este extends Dir
  case object Oeste extends Dir

  def direcciones(): List[Dir] = List(Norte, Este, Sur, Oeste)

  class Cell {

    val cellInfo: Map[Color, Int] = Map(colores().map(c => (c, 0)): _*)

    def nroBolitas(c: Color): Int = cellInfo(c)

    def hayBolitas(c: Color): Boolean = nroBolitas(c) > 0

    def poner(c: Color) = cellInfo.update(c, cellInfo(c) + 1)

    def sacar(c: Color) = if (nroBolitas(c) > 0) cellInfo.update(c, cellInfo(c) - 1) else throw new RuntimeException("No hay más bolitas de color " + c)

  }
}