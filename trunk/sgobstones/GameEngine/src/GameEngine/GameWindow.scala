package GameEngine

import java.awt.Dimension

import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication

import javax.swing.ImageIcon

abstract class GameWindow(width: Int, height: Int, windowTitle: String) extends SimpleSwingApplication {

  var scene: GameScene = new GameScene(10, 10)

  var showCoords = false
  var showGrid = false

  def cellSize(): Int = 60
  def gridWidth() = cellSize * scene.width
  def gridHeight() = cellSize * scene.height

  def newGrid(): GridPanel = {
    val world = scene.makeWorld()
    return new GridPanel(height, width) {
      preferredSize = new Dimension(gridWidth(), gridHeight())
      for (i <- 1 to scene.width) {
        for (j <- 1 to scene.height) {
          contents += new Label {
            preferredSize = new Dimension(cellSize(), cellSize())
            icon = new ImageIcon("path to the image file")
          }
        }
      }
    }
  }

  def updateWindow() {
    this.top.contents = newGrid()
  }

  /* HOOKS */

  def start()

  def update()

  /* ESTRUCTURAS DE CONROL */

  def range(start: Int, end: Int): List[Int] = List.range(start, end)

  def repeat(n: Int)(block: => Unit): Unit = {
    List.range(0, n).foreach(_ => block)
  }

  def top = new MainFrame {
    title = windowTitle
    contents = newGrid()
    preferredSize = new Dimension(800, 600)
    resizable = true
    peer.setLocationRelativeTo(null)
  }

}