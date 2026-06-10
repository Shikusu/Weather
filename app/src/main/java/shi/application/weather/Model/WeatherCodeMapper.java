package shi.application.weather.Model;

import shi.application.weather.R;

public class WeatherCodeMapper {

    public static String getDescription(int code) {
        if (code == 0)                return "Ciel dégagé";
        if (code <= 2)                return "Partiellement nuageux";
        if (code == 3)                return "Couvert";
        if (code <= 49)               return "Brouillard";
        if (code <= 57)               return "Bruine";
        if (code <= 67)               return "Pluie";
        if (code <= 77)               return "Neige";
        if (code <= 82)               return "Averses de pluie";
        if (code <= 86)               return "Averses de neige";
        if (code <= 99)               return "Orage";
        return "Inconnu";
    }

    public static String getUVLabel(double uvIndex) {
        if (uvIndex == 0) return "Aucun";
        if (uvIndex <= 2)  return "Faible";
        if (uvIndex <= 5)  return "Modéré";
        if (uvIndex <= 7)  return "Élevé";
        if (uvIndex <= 10) return "Très élevé";
        return "Extrême";
    }

    private String getClothingAdvice(double temp, int weatherCode, double uvIndex) {
        boolean isRainy = weatherCode >= 51;

        StringBuilder advice = new StringBuilder();

        if (temp >= 28) {
            advice.append("Il fait chaud ! Sortez les vêtements légers et n'oubliez pas de bien vous hydrater.");
        } else if (temp >= 22) {
            advice.append("Un t-shirt et une tenue légère feront parfaitement l'affaire aujourd'hui.");
        } else if (temp >= 16) {
            advice.append("Le temps est doux, prenez une petite veste ou un pull léger au cas où.");
        } else if (temp >= 10) {
            advice.append("Le fond de l'air est frais, pensez à superposer quelques couches !");
        } else {
            advice.append("Sortez le gros manteau et l'écharpe, il fait un froid de canard !");
        }

        if (isRainy || uvIndex >= 6) {
            advice.append("\n\nPetits conseils en plus :");

            if (isRainy) {
                advice.append("\n☔ N'oubliez pas votre parapluie ou un bon imperméable.");
            }
            if (uvIndex >= 6) {
                advice.append("\n☀️ Le soleil tape fort aujourd'hui, mettez de la crème solaire !");
            }
        }

        return advice.toString();
    }

}