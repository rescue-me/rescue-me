package rescueme.com.rescue_me_backoffice.dog.infrastructure.endpoint

import rescueme.com.rescue_me_backoffice.dog.domain.Dog
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait DogJsonFormatMarshaller extends DefaultJsonProtocol {
  implicit val dogFormat: RootJsonFormat[Dog] = jsonFormat2(Dog)
}
