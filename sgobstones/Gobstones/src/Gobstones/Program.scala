package Gobstones

import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import scala.swing.BorderPanel
import scala.swing.BoxPanel
import scala.swing.Dimension
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.Orientation
import scala.swing.Panel
import scala.swing.SimpleSwingApplication
import scala.swing.TabbedPane
import scala.swing.TabbedPane.Page
import scala.swing.event
import scala.swing.event.KeyPressed
import javax.swing.ImageIcon
import javax.swing.border.LineBorder

trait Program extends SimpleSwingApplication with Gobstones {
  
  import java.awt.{Color => RGBColor}
  
  type KeyValue = Key.Value
  val Key = event.Key

  private val windowSize = new Dimension(Constantes.windowWidth, Constantes.windowHeight)
  private val cellWidth = Constantes.windowWidth / Constantes.width
  private val cellHeight = Constantes.windowHeight / Constantes.height
  
  var showCoords = true

  private def toSwingColor(c: Color): java.awt.Color = c match {
    case Rojo  => new RGBColor(242, 109, 109)
    case Azul  => new RGBColor(109, 114, 242)
    case Negro => new RGBColor(161, 161, 161)
    case Verde => new RGBColor(53, 175, 0)
  }

  private def drawCursor(g: Graphics2D) = {
    g.setStroke(new BasicStroke(2.0f))
    g.setColor(RGBColor.red)
    g.draw(new Rectangle(cursorX * cellWidth, cursorY * cellHeight, cellWidth, cellHeight))
  }

  protected def newGobstonesCell(i: Int, j: Int): scala.swing.Component = new GridPanel(2, 2) {
    for (c <- colores()) {
      val n = getCell(i, j).nroBolitas(c)
      if (n > 0) {
        contents += new Label(n.toString) {
          foreground = RGBColor.WHITE

          override def paintComponent(g: Graphics2D) {
            g.setColor(toSwingColor(c))
            g.fillOval(0, 0, this.bounds.width - 1, this.bounds.height - 1)
            super.paintComponent(g)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(java.awt.Color.BLACK)
            g.drawOval(0, 0, this.bounds.width - 1, this.bounds.height - 1)
          }
        }
      } else {
        contents += new Label("")
      }
    }
    if (cursorX == i && cursorY == (Constantes.height - 1) - j) {
      background = new RGBColor(255, 255, 224)
      border = new LineBorder(new java.awt.Color(255, 165, 0), 3)
    } else {
      border = new LineBorder(java.awt.Color.BLACK, 1)
    }
  }

  private def newBoardPanel() = new GridPanel(Constantes.height, Constantes.width) {
    background = RGBColor.white
    preferredSize = windowSize
    focusable = true
    0 to Constantes.height - 1 foreach { j =>
      0 to Constantes.width - 1 foreach { i =>
        contents += newGobstonesCell(i, j)
      }
    }
  }

  private def corner() = new Panel() {
    preferredSize = new Dimension(cellWidth, cellHeight)
  }

  private def yLabels() = new GridPanel(Constantes.height, 1) {
    preferredSize = new Dimension(cellWidth, Constantes.windowHeight)
    for (i <- 0 to Constantes.height - 1) {
      contents += new Label((Constantes.height - 1 - i).toString) {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def xLabels() = new GridPanel(1, Constantes.width) {
    preferredSize = new Dimension(Constantes.windowWidth, cellHeight)
    for (i <- 0 to Constantes.width - 1) {
      contents += new Label(i.toString) {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def emptyGridEast() = new GridPanel(Constantes.height, 1) {
    preferredSize = new Dimension(cellWidth, Constantes.windowHeight)
    for (i <- 0 to Constantes.height - 1) {
      contents += new Panel() {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def emptyGridNorth() = new GridPanel(1, Constantes.width) {
    preferredSize = new Dimension(Constantes.windowWidth, cellHeight)
    for (i <- 0 to Constantes.width - 1) {
      contents += new Panel() {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  protected def resultPanel(): BorderPanel = {
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
      }
      add(newBoardPanel(), BorderPanel.Position.Center)
    }
  }
  
}