package golden.framework

import java.util.Properties
import scala.jdk.CollectionConverters.{MapHasAsScala, MapHasAsJava}

object PropertiesUtils:
  extension (props: Properties)
    def withPrefix(prefix: String): Properties =
      if (prefix.isEmpty)
        return props

      val fullPrefix = prefix + "."
      val newProps = new Properties
      newProps.putAll(
        props.asScala.collect {
          case (name: String, value) if name.startsWith(fullPrefix) =>
            name.substring(fullPrefix.length) -> value
        }.asJava
      )
      newProps
