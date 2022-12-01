package golden.framework.application

import golden.framework.{TypeInfo, typeOf}
import golden.framework.domain.Event

trait EventDispatcher:

  inline final def invoke[TEvent <: Event](event: TEvent): Unit =
    invoke[TEvent](event, typeOf[TEvent])

  protected def invoke[TEvent <: Event](event: TEvent, eventType: TypeInfo): Unit
