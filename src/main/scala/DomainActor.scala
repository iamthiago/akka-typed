import TransactionActor.{TransactionOp, Downloaded}
import akka.typed.{ActorRef, Behavior}
import akka.typed.scaladsl.Actor

object DomainActor {

  sealed trait DomainOp
  case class RequestToDownload(replyTo: ActorRef[TransactionOp], path: String) extends DomainOp

  def myBehavior(): Behavior[DomainOp] =
    Actor.immutable[DomainOp] { (_, msg) =>
      msg match {
        case RequestToDownload(replyTo, path) =>
          replyTo ! Downloaded(s"$path-download")
          Actor.same
      }
    }
}
