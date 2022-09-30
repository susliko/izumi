package izumi

import izumi.distage.constructors.TraitConstructor

class AClass(a: String)

trait ATrait {
  def toWireT: Int
  def xxx: Int = 1
}

abstract class AnAbstractClass(c: Int) {
  def toWireAC: String
//  def bullshit(a: Int): String
}

trait ATrait1 {
  def a: Int
}
class AClass1(override val a: Int) extends ATrait1 {}

class AClass2(cparam: Int) extends AnAbstractClass(cparam) {
  val a = "xxx"
  override def toWireAC: String = a
}

object ConstructorMacroTest {
//  TraitConstructor[AClass1]
//  TraitConstructor[AClass2]
  TraitConstructor[ATrait]
  TraitConstructor[AnAbstractClass]

}