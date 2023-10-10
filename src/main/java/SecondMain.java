import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.graphbuilder.struc.LinkedList;

import constants.KmlEnum;
import modules.KmlCreatorController;

public class SecondMain {
    public static void main(String[] args) {
        // KmlCreatorController controller = new KmlCreatorController();
        // String headerProp = "xmlns=\"http://www.opengis.net/kml/2.2\"";
        // String styleURL = "#AREA_FFFFFFFF";
        // String styleID = "AREA_FFFFFFFF";
        // String styleColorLine = "FFFFFFFF";
        // String styleColorPoly = "80FFFFFF";
        // String widthStyle = "2";
        // String fillStyle = "1";
        // String outlineStyle = "1";
        // controller.setProp(KmlEnum.KML, headerProp);
        // controller.createKmlStyle(styleID, styleColorLine, widthStyle, styleColorPoly, fillStyle, outlineStyle);
        // controller.createSpecialFieldPlacemark("Some s", "Some shit", styleURL, args);

        // System.out.println(controller.getFullKMLString());
        float[][] floatArray = {
            {1.1f, 2.2f, 3.3f},
            {4.4f, 5.5f, 6.6f},
            {7.7f, 8.8f, 9.9f}
        };
        Stream<String> floatStr = Arrays.stream(floatArray).map((float[] row) ->{
            String s = "";
            for(float n : row){
                s+= String.format("%s,", n);
            }
            return s;
        });
        List<String> list = floatStr.collect(Collectors.toList());
        list.forEach((elem) -> {
            System.out.println(elem);
        });
        
    }
}
