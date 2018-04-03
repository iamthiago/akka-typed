import java.util.UUID

import DomainManagerActor.{DomainManagerOp, SendToDomain}
import Protocol.{DownloadedImages, Image}
import akka.typed.scaladsl.Actor
import akka.typed.{ActorRef, Behavior}

object TransactionActor {

  sealed trait TransactionOp
  case class Images(images: List[Image]) extends TransactionOp
  case class Downloaded(path: String) extends TransactionOp

  def myBehavior(transactionId: UUID,
                 imagesPath: List[String],
                 downloadedImages: List[String],
                 domainManagerActor: ActorRef[DomainManagerOp],
                 replyTo: akka.actor.ActorRef): Behavior[TransactionOp] =

    Actor.immutable[TransactionOp] { (ctx, msg) =>
      msg match {
        case Images(images) =>
          val imagesPath = images.map(_.path)
          imagesPath.foreach(path => domainManagerActor ! SendToDomain(ctx.self, path))
          myBehavior(transactionId, imagesPath, Nil, domainManagerActor, replyTo)

        case Downloaded(path) =>
          val downloadedUpdated = path :: downloadedImages
          if (imagesPath.size == downloadedUpdated.size) {
            replyTo ! DownloadedImages(transactionId, downloadedUpdated.map(Image))
            Actor.stopped
          } else {
            myBehavior(transactionId, imagesPath, downloadedUpdated, domainManagerActor, replyTo)
          }
      }
    }

}
