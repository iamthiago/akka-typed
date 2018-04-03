import DomainActor.RequestToDownload
import TransactionActor.TransactionOp
import akka.typed
import akka.typed.scaladsl.Actor
import akka.typed.{ActorRef, Behavior}

import scala.util.matching.Regex

object DomainManagerActor {

  sealed trait DomainManagerOp
  case class SendToDomain(replyTo: ActorRef[TransactionOp], path: String) extends DomainManagerOp

  val validDomain: Regex = "(?<=\\/\\/).[^\\/]+".r
  val validChars: Regex = "([a-z])\\w+".r

  def myBehavior(map: Map[String, typed.ActorRef[DomainActor.DomainOp]]): Behavior[DomainManagerOp] =
    Actor.immutable[DomainManagerOp] { (ctx, msg) =>
      msg match {
        case SendToDomain(replyTo, path) =>
          val domainRegex = validDomain.findFirstMatchIn(path)
          require(domainRegex.isDefined, s"A domain could not be found on $path")

          val domain = domainRegex.get.matched

          map.get(domain) match {
            case Some(domainActorRef) =>
              domainActorRef ! RequestToDownload(replyTo, path)
              Actor.same

            case None =>
              val name = validChars.findAllMatchIn(domain).map(_.matched).mkString("-")
              val domainActorRef = ctx.spawn(DomainActor.myBehavior(), name)
              domainActorRef ! RequestToDownload(replyTo, path)
              val updatedMap = map + (domain -> domainActorRef)
              myBehavior(updatedMap)
          }
      }
    }

}
