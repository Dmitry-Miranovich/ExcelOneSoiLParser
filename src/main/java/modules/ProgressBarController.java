package modules;

import javafx.scene.control.ProgressBar;

public class ProgressBarController {
    private static double p_value = 0;

    private static void addValue(double value){
        p_value +=value;
    }

    public static void updateProgressBar(ProgressBar bar, double value){
        addValue(value);
        bar.setProgress(p_value);
    }

    public static double getP_value() {
        return p_value;
    }
    
}