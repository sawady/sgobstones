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

  var interactivo = false
  var codificaciones = false
  var showCoords = true

  val windowSize = new Dimension(600, 600)
  val windowsWidth = windowSize.getWidth().toInt
  val windowsHeight = windowSize.getHeight().toInt
  val cellWidth = windowsWidth / width
  val cellHeight = windowsHeight / height

  val initialBoard = resultPanel()

  private def toSwingColor(c: Color): java.awt.Color = c match {
    case Rojo => new RGBColor(242, 109, 109)
    case Azul => new RGBColor(109, 114, 242)
    case Negro => new RGBColor(161, 161, 161)
    case Verde => new RGBColor(53, 175, 0)
  }

  private def drawCursor(g: Graphics2D) = {
    g.setStroke(new BasicStroke(2.0f))
    g.setColor(Color.red)
    g.draw(new Rectangle(cursorX * cellWidth, cursorY * cellHeight, cellWidth, cellHeight))
  }

  private def newGobstonesCell(i: Int, j: Int) = new GridPanel(2, 2) {
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

  private def checkCode(i: Int, j: Int): String = {
    val a = getCell(i, j).nroBolitas(Azul)
    val n = getCell(i, j).nroBolitas(Negro)
    val r = getCell(i, j).nroBolitas(Rojo)
    val v = getCell(i, j).nroBolitas(Verde)
    for (c <- Codificaciones.codificaciones()) {
      if (c.azul == a && c.negro == n && c.rojo == r && c.verde == v) {
        return c.recurso
      }
    }
    return ""
  }

  private def newImageCell(s: String) = new Label() {
    icon = new ImageIcon("resources/" + s + ".png")
  }

  private def newBoardPanel() = new GridPanel(height, width) {
    background = Color.white
    preferredSize = windowSize
    focusable = true
    0 to height - 1 foreach { j =>
      0 to width - 1 foreach { i =>
        if (codificaciones) {
          contents += newImageCell(checkCode(i, j))
        } else {
          contents += newGobstonesCell(i, j)
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

  private def resultPanel() = {
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

  def onKeyPress(key: Key.Value) {}

  def defaultKeyPress(key: Key.Value) {
    if (interactivo) {
      println("Se presiono: " + key)
    }
    key match {
      case Key.F10 => volverInteractivo()
      case Key.F11 => ocultarCoordenadas()
      case Key.F12 => verCodificaciones()
      case _ => {}
    }
    if (interactivo) {
      onKeyPress(key)
    }
    resultPage.content = resultPanel()
  }

  def volverInteractivo() {
    if (!interactivo) {
      tabPanels.selection.index = 1
      tabPanels.pages.remove(0)
      resultPage.title = "Game"
      showCoords = false
      codificaciones = true
      interactivo = true
    }
  }

  def verCodificaciones() {
    codificaciones = !codificaciones
  }

  def ocultarCoordenadas() {
    showCoords = !showCoords
  }

  main()
  val initialPage = new Page("Tablero Inicial", initialBoard)
  val resultPage = new Page("Resultado", resultPanel())
  val tabPanels = new TabbedPane {
    pages += initialPage
    pages += resultPage
    focusable = true
    listenTo(keys)
    reactions += {
      case KeyPressed(_, key, _, _) =>
        defaultKeyPress(key)
        repaint()
    }
  }

  def top = new MainFrame {
    title = "Gobstones Program"
    contents = tabPanels
    tabPanels.selection.index = 1
    resizable = true
    peer.setLocationRelativeTo(null)
  }

}