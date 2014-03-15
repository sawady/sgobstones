package Gobstones

trait CommonLanguage {
  
  def not(b: Boolean): Boolean = !b
  def error(s: String) = throw new RuntimeException(s)
  
  def rango(start: Int, end: Int): List[Int] = List.range(start, end+1)

  def repeat(n: Int)(block: => Unit): Unit = {
    List.range(0, n).foreach(_ => block)
  }

}