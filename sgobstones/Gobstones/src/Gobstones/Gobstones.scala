package Gobstones

import scala.annotation.meta.getter
import scala.collection.mutable.Map
import scala.util.Random

trait Gobstones extends CommonLanguage {

  type Board = Array[Array[Cell]]

  @getter var board = makeBoard()
  @getter var cursorX = 0
  @getter var cursorY = 0

  private def makeColumn(): Array[Cell] = List.range(0, Constantes.height).map(_ => new Cell()).toArray

  private def makeBoard(): Board = List.range(0, Constantes.width).map(_ => makeColumn()).toArray

  private def current(): Cell = board(cursorX)(cursorY)
  
  def getCell(i: Int, j: Int): Cell = board(i)((Constantes.height - 1) - j)

  /* Operaciones sobre el cabezal */

  def poner(c: Color) = current().poner(c)
  def sacar(c: Color) = current().sacar(c)
  def nroBolitas(c: Color) = current().nroBolitas(c)
  def hayBolitas(c: Color) = current().hayBolitas(c)
  def reiniciar() = { 
    board = makeBoard()
    cursorX = 0
    cursorY = 0
  }
  
  def mover(d: Dir) = {
    if (!puedeMover(d)) {
      throw new RuntimeException("No es posible moverse hacia la direccion: " + d)
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
      case Norte => cursorY < Constantes.height - 1
      case Sur => cursorY > 0
      case Este => cursorX < Constantes.width - 1
      case Oeste => cursorX > 0
    }
  }

  /* Estructuras de Control */
  
  def colores(): List[Color] = List(Negro, Azul, Verde, Rojo)

  def direcciones(): List[Dir] = List(Norte, Este, Sur, Oeste)
  
  def opuesto(d: Dir): Dir = d match {
    case Norte => Sur
    case Este => Oeste
    case Sur => Norte
    case Oeste => Este
  }  

  class Cell {

    val cellInfo: Map[Color, Int] = Map(colores().map(c => (c, 
    		if (Constantes.randomCells) new Random().nextInt(25) else 0) ): _*)

    def nroBolitas(c: Color): Int = cellInfo(c)

    def hayBolitas(c: Color): Boolean = nroBolitas(c) > 0

    def poner(c: Color) = cellInfo.update(c, cellInfo(c) + 1)

    def sacar(c: Color) = if (nroBolitas(c) > 0) cellInfo.update(c, cellInfo(c) - 1) else error("No hay mas bolitas de color " + c)

  }
}