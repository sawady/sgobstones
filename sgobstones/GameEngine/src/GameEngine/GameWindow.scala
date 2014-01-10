package GameEngine

import java.awt.Dimension
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication
import javax.swing.ImageIcon
import scala.swing.Panel
import scala.swing.BorderPanel
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.event.KeyPressed

import swing._
import event._

abstract class GameWindow(width: Int, height: Int, windowTitle: String) extends SimpleSwingApplication {

  import event.Key._

  var scene: GameScene = new GameScene(10, 10)

  def addEntity(e: Entity) {
    scene.addEntity(e)
  }

  var showCoords = false

  def cellSize(): Int = 60

  def gridWidth() = cellSize * scene.width
  def gridHeight() = cellSize * scene.height

  def newGrid(): GridPanel = {
    val world = scene.makeWorld()
    println(world)
    return new GridPanel(scene.height, scene.width) {
      preferredSize = new Dimension(gridWidth(), gridHeight())
      for (j <- 0 to scene.height - 1) {
        for (i <- 0 to scene.width - 1) {
          contents += new Label {
            preferredSize = new Dimension(cellSize(), cellSize())
            val imgname = world(i)(scene.height - 1 - j)
            if (imgname.isEmpty()) {
              icon = new ImageIcon("resources/default.png")
            } else {
              icon = new ImageIcon("resources/" + imgname + ".png")
            }
          }
        }
      }
    }
  }

  /* HOOKS */

  def start()

  def always()

  def onKeyPress(keyCode: Value) {
    println(keyCode)
  }

  /* ESTRUCTURAS DE CONROL */

  def range(start: Int, end: Int): List[Int] = List.range(start, end)

  def repeat(n: Int)(block: => Unit): Unit = {
    List.range(0, n).foreach(_ => block)
  }

  /* PANELS */

  private def corner() = new Panel() {
    preferredSize = new Dimension(cellSize(), cellSize())
  }

  private def yLabels() = new GridPanel(scene.height, 1) {
    preferredSize = new Dimension(cellSize(), gridHeight())
    for (i <- 0 to scene.height - 1) {
      contents += new Label((scene.height - 1 - i).toString) {
        preferredSize = new Dimension(cellSize(), cellSize())
      }
    }
  }

  private def xLabels() = new GridPanel(1, scene.width) {
    preferredSize = new Dimension(gridWidth(), cellSize())
    for (i <- 0 to scene.width - 1) {
      contents += new Label(i.toString) {
        preferredSize = new Dimension(cellSize(), cellSize())
      }
    }
  }

  private def emptyGridEast() = new GridPanel(height, 1) {
    preferredSize = new Dimension(cellSize(), gridHeight())
    for (i <- 0 to scene.height - 1) {
      contents += new Panel() {
        preferredSize = new Dimension(cellSize(), cellSize())
      }
    }
  }

  private def emptyGridNorth() = new GridPanel(1, width) {
    preferredSize = new Dimension(gridWidth(), cellSize())
    for (i <- 0 to scene.width - 1) {
      contents += new Panel() {
        preferredSize = new Dimension(cellSize(), cellSize())
      }
    }
  }

  private def updateWindow() {
    this.top.contents = newBoardPanel
  }

  private def newBoardPanel() = {

    new BorderPanel() {
      if (showCoords) {
        add(yLabels(), BorderPanel.Position.West)
        add(new BoxPanel(Orientation.Horizontal) {
          contents += corner()
          contents += xLabels()
          contents += corner()
        }, BorderPanel.Position.South)
        add(emptyGridEast(), BorderPanel.Position.East)
        add(emptyGridNorth(), BorderPanel.Position.North)
        add(newGrid(), BorderPanel.Position.Center)
        focusable = true
        listenTo(keys)
        reactions += {
          case KeyPressed(_, key, _, _) =>
            onKeyPress(key)
            repaint
        }
      } else {
        add(newGrid(), BorderPanel.Position.Center)
      }
    }
  }

  def top = new MainFrame {
    start()
    title = windowTitle
    contents = newBoardPanel()
    preferredSize = new Dimension(800, 600)
    resizable = false
    peer.setLocationRelativeTo(null)
  }

}