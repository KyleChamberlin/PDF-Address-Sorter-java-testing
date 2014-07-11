package pdfProcessor;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by kylec on 7/3/2014.
 */
public class Pdf {

    public static String[] parsePdf(String pdf, Rectangle rectangle) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        RenderFilter filter = new RegionTextRenderFilter(rectangle);
        TextExtractionStrategy strategy;
        String[] output = new String[reader.getNumberOfPages()];
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
            output[i-1] = PdfTextExtractor.getTextFromPage(reader, i, strategy);
        }
        return output;
    }
}
