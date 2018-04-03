import java.util.UUID

object Protocol {
  sealed trait BackPressure
  case object Init extends BackPressure
  case object Ack extends BackPressure
  case object Complete extends BackPressure
  case class Error(t: Throwable) extends BackPressure

  case class Image(path: String)
  case class RequestImages(transactionId: UUID, images: List[Image])
  case class DownloadedImages(transactionId: UUID, images: List[Image])
}
