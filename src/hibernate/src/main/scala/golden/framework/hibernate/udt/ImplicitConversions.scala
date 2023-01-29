package golden.framework.hibernate.udt

import scala.collection.mutable
import scala.jdk.CollectionConverters.*
import scala.language.implicitConversions

object ImplicitConversions:

  implicit def toSet[T](set: Set[T]): mutable.Set[T] = set.asScala

  implicit def fromSet[T](set: mutable.Set[T]): Set[T] = set.asJava

  implicit def toList[T](list: List[T]): mutable.Buffer[T] = list.asScala

  implicit def fromList[T](list: mutable.Buffer[T]): List[T] = list.asJava

  implicit def toMap[K, V](map: Map[K, V]): mutable.Map[K, V] = map.asScala

  implicit def fromMap[K, V](map: mutable.Map[K, V]): Map[K, V] = map.asJava
