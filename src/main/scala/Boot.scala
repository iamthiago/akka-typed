import java.util.UUID

import Protocol._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object Boot extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val list = List(
    RequestImages(
      transactionId = UUID.randomUUID(),
      images = List(
        Image("https://cdn.dribbble.com/users/4971/screenshots/2846308/mr-meeseeks_1x.png"),
        Image("http://www.adagio.com/images2/custom_blends/90612.jpg"),
        Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxQd8NiiRL0FdKpWoEkb9_Bonot1PpMvfmJouuQLYSxCSSl5eWxw"),
        Image("https://pre00.deviantart.net/722a/th/pre/f/2017/177/b/c/mr_meeseks_by_ivathehuman-dbe48jv.png")
      )
    ),
    RequestImages(
      transactionId = UUID.randomUUID(),
      images = List(
        Image("https://cdn.dribbble.com/users/4971/screenshots/2846308/mr-meeseeks_1x.png"),
        Image("http://www.adagio.com/images2/custom_blends/90612.jpg"),
        Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxQd8NiiRL0FdKpWoEkb9_Bonot1PpMvfmJouuQLYSxCSSl5eWxw"),
        Image("https://pre00.deviantart.net/722a/th/pre/f/2017/177/b/c/mr_meeseks_by_ivathehuman-dbe48jv.png")
      )
    ),
    RequestImages(
      transactionId = UUID.randomUUID(),
      images = List(
        Image("https://cdn.dribbble.com/users/4971/screenshots/2846308/mr-meeseeks_1x.png"),
        Image("http://www.adagio.com/images2/custom_blends/90612.jpg"),
        Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxQd8NiiRL0FdKpWoEkb9_Bonot1PpMvfmJouuQLYSxCSSl5eWxw"),
        Image("https://pre00.deviantart.net/722a/th/pre/f/2017/177/b/c/mr_meeseks_by_ivathehuman-dbe48jv.png")
      )
    )
  )



  val imagesManager = system.actorOf(ImagesManager.props, ImagesManager.name)

  Source(list)
    .runWith(Sink.actorRefWithAck(imagesManager, Init, Ack, Complete, Error))

}
