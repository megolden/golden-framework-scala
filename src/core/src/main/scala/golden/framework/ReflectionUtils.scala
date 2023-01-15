package golden.framework

object ReflectionUtils:

  inline def getAnnotations[T, A]: Iterable[A] =
    Macros.getAnnotations[T, A]

  inline def getAnnotations[T]: Iterable[?] =
    Macros.getAllAnnotations[T]

  inline def getAnnotatedMembers[T, A]: Map[String, (Type, Iterable[A])] =
    Macros.getAnnotatedMembers[T, A].map { case (name, tpe, annotations) => name -> (tpe, annotations) }.toMap

  inline def getPackageAnnotatedTypes[TPackageRoot, A]: Map[Type, Iterable[A]] =
    Macros.getAnnotatedPackageTypes[TPackageRoot, A].toMap
