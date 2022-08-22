/*
    A class for utility methods
 */
public class Utilities {

    public static String mapJsonFieldNameToEnumValue(String jsonField) {
        String[] words = jsonField.split("(?=[A-Z])");
        String enumValue = "";
        for(int i = 0; i < words.length; i++){
            if(i == 0)  {
                enumValue = words[i];
                continue;
            }
            enumValue += words[i].replaceAll("([A-Z])", "_$1");
        }

        return enumValue.toUpperCase();
    }

    //todo : getMarketsAndSelectionsFromCSV()

    static String getMarketName(Market market) {
        return market.getClass().getName().replaceAll("([A-Z])", "\s$1").trim();
    }

}
