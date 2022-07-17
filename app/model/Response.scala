package model

case class Response[A, B, C](input:A, data: B, error: Option[C] = None){
  def isSuccess = error match {
    case Some(error) => false
    case None => true
  }
}