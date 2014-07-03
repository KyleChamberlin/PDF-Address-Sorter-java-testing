package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jpedal.PdfDecoder;
import pdfProcessor.Pdf;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main extends Application {

    public PdfDecoder pdf;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("PDF Address Sorter");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        FileChooser fc = new FileChooser();
        fc.setTitle("Open PDF file...");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fc.showOpenDialog(primaryStage.getOwner());
        String filename = file.getAbsolutePath();

        // open file.
        pdf = new PdfDecoder();
        pdf.openPdfFile(filename);
        showPage(1);
        pdf.closePdfFile();


    }


    public static void main(String[] args) {

        launch(args);

    }

    /**
     * Update the GUI to show a specified page.
     * @param page
     */
    private void showPage(int page) {

        //Check in range
        if (page > pdf.getPageCount())
            return;
        if (page < 1)
            return;

        //Calculate scale
        int pW = pdf.getPdfPageData().getCropBoxWidth(page);
        int pH = pdf.getPdfPageData().getCropBoxHeight(page);

        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();

        s.width -= 100;
        s.height -= 100;

        double xScale = (double)s.width / pW;
        double yScale = (double)s.height / pH;
        double scale = xScale < yScale ? xScale : yScale;

        //Work out target size
        pW *= scale;
        pH *= scale;

        //Get image and set
        Image i = getPageAsImage(page,pW,pH);
        imageView.setImage(i);

        //Set size of components
        imageView.setFitWidth(pW);
        imageView.setFitHeight(pH);
        stage.setWidth(imageView.getFitWidth()+2);
        stage.setHeight(imageView.getFitHeight()+2);
        stage.centerOnScreen();
    }

    /**
     * Wrapper for usual method since JFX has no BufferedImage support.
     * @param page
     * @param width
     * @param height
     * @return
     */
    private Image getPageAsImage(int page, int width, int height) {

        BufferedImage img;
        try {
            img = pdf.getPageAsImage(page);

            //Use deprecated method since there's no real alternative
            //(for JavaFX 2.2+ can use SwingFXUtils instead).
            if (Image.impl_isExternalFormatSupported(BufferedImage.class))
                return javafx.scene.image.Image.impl_fromExternalImage(img);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
