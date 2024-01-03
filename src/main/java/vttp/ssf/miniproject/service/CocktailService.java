package vttp.ssf.miniproject.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import vttp.ssf.miniproject.model.Cocktail;

@Service
public class CocktailService {
    
    private String base_url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=";

    private String search_url = "https://www.thecocktaildb.com/api/json/v1/1/search.php";

    public ResponseEntity<String> getCocktail(String input) {
        String url = UriComponentsBuilder
            .fromUriString(base_url)
            .queryParam("f", input)
            .build(false)
            .toUriString();

        RequestEntity<Void> request = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = null;
        
        try {
            response = template.exchange(request, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return response;
    }

    public List<Cocktail> getAllCocktails() {
        List<Cocktail> cocktailsList = new ArrayList<>();
        
        // Iterate over the alphabet (from 'a' to 'z')
        for (char letter = 'a'; letter <= 'z'; letter++) {
            String input = String.valueOf(letter);
        
            ResponseEntity<String> cocktailsData = getCocktail(input);
            String payload = cocktailsData.getBody();
        
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject result = reader.readObject();
    
            // Check if "drinks" is a JsonArray before attempting to cast
            JsonValue drinksValue = result.get("drinks");
            if (drinksValue != null && drinksValue.getValueType() == JsonValue.ValueType.ARRAY) {
                JsonArray drinksArray = (JsonArray) drinksValue;
    
                for (int i = 0; i < drinksArray.size(); i++) {
                    JsonObject drinkObject = drinksArray.getJsonObject(i);
                    Cocktail drink = parseDrink(drinkObject);
                    cocktailsList.add(drink);
                }
            }
        }
        return cocktailsList;
    }
    
    private Cocktail parseDrink(JsonObject cocktailObject) {
        Cocktail cocktail = new Cocktail();
    
        // Set fields for Cocktail object
        cocktail.setId(cocktailObject.getString("idDrink"));
        cocktail.setName(cocktailObject.getString("strDrink", "No name"));
        cocktail.setCategory(cocktailObject.getString("strCategory", "No category"));
        cocktail.setInstructions(cocktailObject.getString("strInstructions", "No instructions"));
        cocktail.setThumbnail(cocktailObject.getString("strDrinkThumb", "https://d1nhio0ox7pgb.cloudfront.net/_img/o_collection_png/green_dark_grey/512x512/plain/drink.png"));
    
        // Set ingredients and measures
        for (int i = 1; i <= 15; i++) {
            // Check if the ingredient and measure are not null before using them
            JsonValue ingredientJson = cocktailObject.get("strIngredient" + i);
            JsonValue measureJson = cocktailObject.get("strMeasure" + i);
    
            if (ingredientJson != null && ingredientJson.getValueType() == JsonValue.ValueType.STRING &&
                measureJson != null && measureJson.getValueType() == JsonValue.ValueType.STRING) {
    
                String ingredient = ((JsonString) ingredientJson).getString();
                String measure = ((JsonString) measureJson).getString();
    
                if (!ingredient.isEmpty() && !measure.isEmpty()) {
                    cocktail.addIngredientAndMeasure(ingredient, measure);
                }
            }
        }
        return cocktail;
    }

    public List<Cocktail> searchCocktails(String searchTerm) {
        List<Cocktail> searchResults = new ArrayList<>();

        String url = UriComponentsBuilder
            .fromUriString(search_url)
            .queryParam("s", searchTerm)
            .build(false)
            .toUriString();

        RequestEntity<Void> request = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = null;

        try {
            response = template.exchange(request, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (response != null && response.getBody() != null) {
            JsonReader reader = Json.createReader(new StringReader(response.getBody()));
            JsonObject result = reader.readObject();

            JsonValue drinksValue = result.get("drinks");
            if (drinksValue != null && drinksValue.getValueType() == JsonValue.ValueType.ARRAY) {
                JsonArray drinksArray = (JsonArray) drinksValue;

                for (int i = 0; i < drinksArray.size(); i++) {
                    JsonObject drinkObject = drinksArray.getJsonObject(i);
                    Cocktail drink = parseDrink(drinkObject);
                    searchResults.add(drink);
                }
            }
        }

        return searchResults;
    }


}
