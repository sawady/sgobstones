package Gobstones

import scala.swing.MainFrame
import scala.swing.event.KeyPressed
import scala.swing.TabbedPane
import scala.swing.TabbedPane.Page
import scala.swing.Panel

trait SimpleProgram extends Program {
  
  val initialBoard = resultPanel()
  
  main()
  
  def main()
  
  val initialPage = new Page("Tablero Inicial", initialBoard)
  val resultPage  = new Page("Tablero Final", resultPanel())
  val tabsPanel   = new TabbedPane {
    pages += initialPage
    pages += resultPage
  }
  
  def top = new MainFrame {
    title = "Gobstones Program"
    contents = tabsPanel
    tabsPanel.selection.index = 1
    resizable = true
    peer.setLocationRelativeTo(null)
  }

}