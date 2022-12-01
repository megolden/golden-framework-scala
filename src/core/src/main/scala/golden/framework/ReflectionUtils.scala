package golden.framework

object ReflectionUtils:

  inline def getAnnotatedTypes[T, A]: Map[TypeInfo, Iterable[A]] =
    Macros.getAnnotatedPackageTypes[T, A].map { case (tpe, annotations) => tpe -> annotations }
    .toMap

  inline def getMembersWithAnnotations[T]:  Iterable[MemberDescriptor] =
    Macros.getMembersWithAnnotations[T]
    .map { case (name, tpe, kind, annotations) => MemberDescriptor(name, tpe, kind == 'F', annotations) }
