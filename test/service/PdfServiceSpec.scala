package service

import org.specs2.mutable.{Specification, Tags}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import test_data.ClaimBuilder
import java.io.{ByteArrayOutputStream, OutputStream}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

/**
 * Test RESTful interface
 * @author Jorge Migueis
 */
class PdfServiceSpec extends Specification with Tags {

  "PDF Service" should {

    "accept XML and return BAD_REQUEST error if unknown XML type"  in {
      val service = new PdfServiceFileImpl {}
      val requestXml = <Invalid>type</Invalid>
      val response = Future(service.outputGeneration(FakeRequest().withXmlBody(requestXml)))
      status(response)  mustEqual BAD_REQUEST
    }
    
    "not accept non XML request" in {
      val service = new PdfServiceFileImpl {}
      val invalidRequest = "Hello"
      val response = Future(service.outputGeneration(FakeRequest().withTextBody(invalidRequest)))
      status(response)  mustEqual UNSUPPORTED_MEDIA_TYPE
    }

    "return Internal error code if could not generate PDF" in {
      val service = new PdfServiceFileImpl {
        override protected val outputStream: OutputStream = null
      }
      val validRequest = ClaimBuilder.goodClaim
      val response = Future(service.outputGeneration(FakeRequest().withXmlBody(validRequest)))
      status(response)  mustEqual INTERNAL_SERVER_ERROR
    }

    "accept valid XML and generate a pdf" in {
      val output = new ByteArrayOutputStream()
      val service = new PdfServiceFileImpl {
        override protected val outputStream: OutputStream = output
      }
      val validRequest = ClaimBuilder.goodClaim
      val response = Future(service.outputGeneration(FakeRequest().withXmlBody(validRequest)))
      status(response)  mustEqual OK
      output.size must be_>(0)
    }

  } section "unit"

}
