package GameEngine

object ZombieGame extends GameWindow(800,600,"Zombie") {
  
  def start() {
    addEntity(Entity(0,0,"zombie"))
    addEntity(Entity(9,9,"zombie"))
  }
  
  def always() {
    
  }

}