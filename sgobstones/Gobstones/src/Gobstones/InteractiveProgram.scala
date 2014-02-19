package Gobstones

import scala.swing.BorderPanel
import scala.swing.MainFrame
import scala.swing.event.KeyPressed
import scala.swing.Panel
import scala.swing.BoxPanel
import scala.swing.Orientation

trait InteractiveProgram extends Program {

  main()

  def onKeyPress(key: Key.Value)

  def defaultKeyPress(key: Key.Value) {
    onKeyPress(key)
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
    title = "Interactive Program"
    contents = buildMainPanel
    resizable = true
    peer.setLocationRelativeTo(null)
  }

  def top = mainPanel

}