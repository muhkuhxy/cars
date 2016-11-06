import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Specs {
  implicit val timeout: Duration = 2.seconds

  def await[T](future: Future[T])(implicit timeout: Duration) = Await.result(future, timeout)
}

