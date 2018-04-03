import DomainManagerActor.DomainManagerOp
import TransactionActor.Images
import akka.actor.{Actor, ActorLogging, Props}
import akka.typed
import akka.typed.scaladsl.adapter._

class ImagesManager extends Actor with ActorLogging {

  import Protocol._

  val domainManagerActor: typed.ActorRef[DomainManagerOp] =
    context.spawn(DomainManagerActor.myBehavior(Map.empty), "domain-manager-actor")

  def receive = {
    case Init =>
      log.info("Starting Load")
      sender() ! Ack

    case RequestImages(transactionId, images) =>
      val transactionActorRef =
        context.spawn(
          TransactionActor.myBehavior(transactionId, Nil, Nil, domainManagerActor, self),
          s"transaction-id-actor-${transactionId.toString}")

      transactionActorRef ! Images(images)
      sender() ! Ack

    case DownloadedImages(transactionId, images) =>
      images.foreach(img => log.info(s"$transactionId - ${img.path}"))

    case Error(t) =>
      log.error("Load error", t)

    case Complete =>
      log.info("Load Complete")

    case _ =>
  }
}

object ImagesManager {
  val name = "images-manager"
  def props = Props(new ImagesManager)
}
