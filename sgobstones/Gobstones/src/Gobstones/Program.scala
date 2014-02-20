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
import java.awt.Color

trait Program extends SimpleSwingApplication with Gobstones {

  type RGBColor = java.awt.Color
  type KeyValue = Key.Value
  val Key = event.Key

  private val windowWidth = 600
  private val windowHeight = 600

  private val windowSize = new Dimension(windowWidth, windowHeight)
  private val cellWidth = windowWidth / width
  private val cellHeight = windowHeight / height
  
  var showCoords = true

  private def toSwingColor(c: Color): java.awt.Color = c match {
    case Rojo  => new RGBColor(242, 109, 109)
    case Azul  => new RGBColor(109, 114, 242)
    case Negro => new RGBColor(161, 161, 161)
    case Verde => new RGBColor(53, 175, 0)
  }

  private def drawCursor(g: Graphics2D) = {
    g.setStroke(new BasicStroke(2.0f))
    g.setColor(Color.red)
    g.draw(new Rectangle(cursorX * cellWidth, cursorY * cellHeight, cellWidth, cellHeight))
  }

  protected def newGobstonesCell(i: Int, j: Int): scala.swing.Component = new GridPanel(2, 2) {
    for (c <- colores()) {
      val n = getCell(i, j).nroBolitas(c)
      if (n > 0) {
        contents += new Label(n.toString) {
          foreground = Color.WHITE

          override def paintComponent(g: Graphics2D) {
            g.setColor(toSwingColor(c))
            g.fillOval(0, 0, this.bounds.width - 1, this.bounds.height - 1)
            super.paintComponent(g)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.BLACK)
            g.drawOval(0, 0, this.bounds.width - 1, this.bounds.height - 1)
          }
        }
      } else {
        contents += new Label("")
      }
    }
    if (cursorX == i && cursorY == (height - 1) - j) {
      background = new java.awt.Color(255, 255, 224)
      border = new LineBorder(new java.awt.Color(255, 165, 0), 3)
    } else {
      border = new LineBorder(Color.BLACK, 1)
    }
  }

  private def newBoardPanel() = new GridPanel(height, width) {
    background = Color.white
    preferredSize = windowSize
    focusable = true
    0 to height - 1 foreach { j =>
      0 to width - 1 foreach { i =>
        contents += newGobstonesCell(i, j)
      }
    }
  }

  private def corner() = new Panel() {
    preferredSize = new Dimension(cellWidth, cellHeight)
  }

  private def yLabels() = new GridPanel(height, 1) {
    preferredSize = new Dimension(cellWidth, windowHeight)
    for (i <- 0 to height - 1) {
      contents += new Label((height - 1 - i).toString) {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def xLabels() = new GridPanel(1, width) {
    preferredSize = new Dimension(windowWidth, cellHeight)
    for (i <- 0 to width - 1) {
      contents += new Label(i.toString) {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def emptyGridEast() = new GridPanel(height, 1) {
    preferredSize = new Dimension(cellWidth, windowHeight)
    for (i <- 0 to height - 1) {
      contents += new Panel() {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def emptyGridNorth() = new GridPanel(1, width) {
    preferredSize = new Dimension(windowWidth, cellHeight)
    for (i <- 0 to width - 1) {
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