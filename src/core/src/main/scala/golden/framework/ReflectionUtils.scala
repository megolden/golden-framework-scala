package golden.framework

object ReflectionUtils:

  inline def getAnnotations[T, A]: Iterable[A] =
    Macros.getAnnotations[T, A]

  inline def getAnnotations[T]: Iterable[?] =
    Macros.getAllAnnotations[T]

  inline def getPackageAnnotatedTypes[TPackageRoot, A]: Map[TypeInfo, Iterable[?]] =
    Macros.getAnnotatedPackageTypes[TPackageRoot, A].toMap
