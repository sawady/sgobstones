package GameEngine

import scala.collection.mutable.Map
import scala.collection.mutable.MutableList
import scala.annotation.meta.getter

class GameScene(@getter val width: Int, @getter val height: Int) {
  
  private val entities: MutableList[Entity] = MutableList()

  def addEntity(e: Entity) {
    entities += e
  }

  def makeWorld(): Array[Array[String]] = {
    val r: Array[Array[String]] = List.range(0, width).map(_ => List.range(0, height).map(_ => "").toArray).toArray

    for (e <- entities) {
      r(e.x).update(e.y, e.id)
    }

    return r
  }

}