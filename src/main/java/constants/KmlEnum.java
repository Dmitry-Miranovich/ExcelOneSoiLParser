package constants;
public enum KmlEnum {
    HEADER {
        public String openAction(){
            return "<?xml version='1.0' encoding='UTF-8' ?>\r\n";
        }
        public String closeAction(){
            return "";
        }
    },
    KML{
        public String openAction(){
            return "<kml>";
        }
        public String closeAction(){
            return "</kml>";
        }
    },
    DOCUMENT {
        public String openAction(){
            return "<Document>";
        }
        public String closeAction(){
            return "</Document>";
        }
    },
    STYLE{
        public String openAction(){
            return "<Style>";
        }
        public String closeAction(){
            return "</Style>";
        }
    },
    LINESTYLE{
        public String openAction(){
            return "<LineStyle>";
        }
        public String closeAction(){
            return "</LineStyle>";
        }
    },
    COLOR{
        public String openAction(){
            return "<color>";
        }
        public String closeAction(){
            return "</color>";
        }
    },
    WIDTH{
        public String openAction(){
            return "<width>";
        }
        public String closeAction(){
            return "</width>";
        }
    },
    POLYSTYLE{
        public String openAction(){
            return "<PolyStyle>";
        }
        public String closeAction(){
            return "</PolyStyle>";
        }
    },
    FILL{
        public String openAction(){
            return "<fill>";
        }
        public String closeAction(){
            return "</fill>";
        }
    },
    OUTLINE{
        public String openAction(){
            return "<outline>";
        }
        public String closeAction(){
            return "</outline>";
        }
    },
    PLACEMARK{
        public String openAction(){
            return "<Placemark>";
        }
        public String closeAction(){
            return "</Placemark>";
        }
    },
    NAME{
        public String openAction(){
            return "<name>";
        }
        public String closeAction(){
            return "</name>";
        }
    },
    DESCRIPTION{
        public String openAction(){
            return "<description>";
        }
        public String closeAction(){
            return "</description>";
        }
    },
    STYLEURL{
        public String openAction(){
            return "<styleUrl>";
        }
        public String closeAction(){
            return "</styleUrl>";
        }
    },
    POLYGON{
        public String openAction(){
            return "<Polygon>";
        }
        public String closeAction(){
            return "</Polygon>";
        }
    },
    OUTERBOUNDARYIS{
        public String openAction(){
            return "<outerBoundaryIs>";
        }
        public String closeAction(){
            return "</outerBoundaryIs>";
        }
    },
    LINEARRING{
        public String openAction(){
            return "<LinearRing>";
        }
        public String closeAction(){
            return "</LinearRing>";
        }
    },
    COORDINATES{
        public String openAction(){
            return "<coordinates>";
        }
        public String closeAction(){
            return "</coordinates>";
        }
    };

    public abstract String openAction();
    public abstract String closeAction();
}
