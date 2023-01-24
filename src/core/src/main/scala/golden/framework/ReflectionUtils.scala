package golden.framework

object ReflectionUtils:
  import scala.quoted.*
  import golden.framework.Type as FType

  inline def annotationsOf[T, A]: Seq[A] =
    ${ Macros.annotationsOf[T, A] }

  inline def annotationsOf[T]: Seq[Any] =
    ${ Macros.annotationsOf[T, Any] }

  inline def annotatedTypesOf[P, A]: Map[FType, Seq[A]] =
    ${ Macros.annotatedTypesOf[P, A] }

  inline def annotatedMembersOf[T, A]: Map[FType.Member, Seq[A]] =
    ${ Macros.annotatedMembersOf[T, A] }
