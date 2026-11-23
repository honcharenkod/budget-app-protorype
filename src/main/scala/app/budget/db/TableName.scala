package app.budget.db

import scala.quoted.*

object TableName:
  transparent inline def of[T]: String = ${ ofImpl[T] }

  private def ofImpl[T: Type](using Quotes): Expr[String] =
    import quotes.reflect.*
    Expr(io.getquill.SnakeCase.table(TypeRepr.of[T].typeSymbol.name))
