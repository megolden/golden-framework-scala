package golden.framework.application

import golden.framework.domain.Event

trait Subscriber[TEvent <: Event]:
  def subscribe(event: TEvent): Unit
