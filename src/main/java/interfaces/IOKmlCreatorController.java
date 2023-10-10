package interfaces;

import constants.KmlEnum;

public interface IOKmlCreatorController {
    public void addProp(KmlEnum token, String value);
    public void addProp(KmlEnum token, String[] value);
    public void setProp(KmlEnum token, String value);
}
