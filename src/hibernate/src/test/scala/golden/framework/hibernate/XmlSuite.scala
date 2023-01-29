package golden.framework.hibernate

import org.dom4j.io.SAXReader
import org.dom4j.*
import java.io.ByteArrayInputStream
import scala.util.Using

trait XmlSuite:

  extension (xmlString: String)

    def asXmlElement: Element = {
      Using.resource
        (new ByteArrayInputStream(xmlString.getBytes))
        (SAXReader.createDefault.read(_).getRootElement)
    }

  end extension

  extension (elem: Element)

    def element(index: Int): Element =
      elem.elements.get(index)

  end extension
