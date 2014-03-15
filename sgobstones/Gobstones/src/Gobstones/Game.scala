package Gobstones

import scala.swing.Label
import javax.swing.ImageIcon
import scala.swing.BorderPanel
import scala.swing.MainFrame
import scala.swing.event.KeyPressed
import scala.swing.Panel
import scala.swing.BoxPanel
import scala.swing.Orientation

trait Game extends Program {
  
  def render()
  def always()
  
  var codificaciones = Constantes.codificaciones
  showCoords = Constantes.showCoords
  
  def verCodificaciones() {
    codificaciones = !codificaciones
  }

  def ocultarCoordenadas() {
    showCoords = !showCoords
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

  override def newGobstonesCell(i: Int, j: Int): scala.swing.Component = {
    if (codificaciones) {
      return newImageCell(checkCode(i, j))
    } else {
      return super.newGobstonesCell(i, j)
    }
  }
  
  def onKeyPress(key: Key.Value)

  def defaultKeyPress(key: Key.Value) {
    always()
    key match {
      case Key.F11 => ocultarCoordenadas()
      case Key.F12 => verCodificaciones()
      case _       => {}
    }
    onKeyPress(key)
    render()
    buildMainPanel.contents.remove(0)
    buildMainPanel.contents += resultPanel()
    buildMainPanel.revalidate()
  }
  
  val buildMainPanel = new BoxPanel(Orientation.Vertical) {
    contents += resultPanel()
    focusable = true
    listenTo(keys)
    reactions += {
      case KeyPressed(_, key, _, _) =>
        defaultKeyPress(key)
        repaint()
    }
  }
  
  val mainPanel: scala.swing.MainFrame = new MainFrame {
    title = "Game"
    contents = buildMainPanel
    resizable = true
    peer.setLocationRelativeTo(null)
  }
  
  def top = mainPanel

}