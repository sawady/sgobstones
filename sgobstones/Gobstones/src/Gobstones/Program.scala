package Gobstones

import java.awt.BasicStroke
import java.awt.Color
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

import javax.swing.border.LineBorder

trait Program extends SimpleSwingApplication with Gobstones {

  val windowSize = new Dimension(600, 600)
  val windowsWidth = windowSize.getWidth().toInt
  val windowsHeight = windowSize.getHeight().toInt
  val cellWidth = windowsWidth / width
  val cellHeight = windowsHeight / height

  private def toSwingColor(c: Color): java.awt.Color = c match {
    case Rojo => new java.awt.Color(242, 109, 109)
    case Azul => new java.awt.Color(109, 114, 242)
    case Negro => new java.awt.Color(161, 161, 161)
    case Verde => new java.awt.Color(53, 175, 0)
  }

  private def drawCursor(g: Graphics2D) = {
    g.setStroke(new BasicStroke(2.0f))
    g.setColor(Color.red)
    g.draw(new Rectangle(cursorX * cellWidth, cursorY * cellHeight, cellWidth, cellHeight))
  }

  private def newBoardPanel() = new GridPanel(height, width) {
    background = Color.white
    preferredSize = windowSize
    focusable = true
    0 to height - 1 foreach { j =>
      0 to width - 1 foreach { i =>
        contents += new GridPanel(2, 2) {
          for (c <- colores()) {
            val n = board(i)((height - 1) - j).nroBolitas(c)
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
      }
    }
  }

  private def corner() = new Panel() {
    preferredSize = new Dimension(cellWidth, cellHeight)
  }

  private def yLabels() = new GridPanel(height, 1) {
    preferredSize = new Dimension(cellWidth, windowsHeight)
    for (i <- 0 to height - 1) {
      contents += new Label((height - 1 - i).toString) {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def xLabels() = new GridPanel(1, width) {
    preferredSize = new Dimension(windowsWidth, cellHeight)
    for (i <- 0 to width - 1) {
      contents += new Label(i.toString) {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def emptyGridEast() = new GridPanel(height, 1) {
    preferredSize = new Dimension(cellWidth, windowsHeight)
    for (i <- 0 to height - 1) {
      contents += new Panel() {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def emptyGridNorth() = new GridPanel(1, width) {
    preferredSize = new Dimension(windowsWidth, cellHeight)
    for (i <- 0 to width - 1) {
      contents += new Panel() {
        preferredSize = new Dimension(cellWidth, cellHeight)
      }
    }
  }

  private def boardPanel(b: Panel) = {
    new BorderPanel() {
      add(yLabels(), BorderPanel.Position.West)
      add(new BoxPanel(Orientation.Horizontal) {
        contents += corner()
        contents += xLabels()
        contents += corner()
      }, BorderPanel.Position.South)
      add(emptyGridEast(), BorderPanel.Position.East)
      add(emptyGridNorth(), BorderPanel.Position.North)
      add(b, BorderPanel.Position.Center)
    }
  }

  def top = new MainFrame {
    title = "Gobstones Program"
    contents = new TabbedPane {
      pages += new Page("Tablero Inicial", boardPanel(newBoardPanel()))
      main()
      pages += new Page("Resultado", boardPanel(newBoardPanel()))
    }
    resizable = true
    peer.setLocationRelativeTo(null)
  }

}