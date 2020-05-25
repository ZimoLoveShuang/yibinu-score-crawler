package wiki.zimo.scorecrawler.helper;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class TesseractOCRHelper {
    public static String doOcr(String path) throws TesseractException {
        ITesseract instance = new Tesseract();
        String result = instance.doOCR(new File(path));
        result = result.replaceAll("\\s+", "");
        return result;
    }
}
