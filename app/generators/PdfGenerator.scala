package generators

import net.sf.jasperreports.engine.{JasperExportManager, JasperPrint}
import play.api.Logger
import java.io.OutputStream

/**
 * Generates a PDF from a DataSource.
 * It reads the compiled report template, generate a JasperPrint and writes to a stream.
 *
 * @author Jorge Migueis
 */
object PdfGenerator extends ReportGenerator {

  override def exportReportToStream(print: Option[JasperPrint], stream: OutputStream): SuccessOrFailure = {
    try {
      if (print.isDefined) {
        JasperExportManager.exportReportToPdfStream(print.get,stream)
        Logger.debug("PDF Generated.")
        GenerationSuccess()
      }
      else {
        Logger.error("Received an empty print object to convert to PDF.")
        GenerationFailure()
      }
    } catch {
      case e: Throwable =>
        Logger.error(e.getMessage,e)
        GenerationFailure()
    }
  }
}
