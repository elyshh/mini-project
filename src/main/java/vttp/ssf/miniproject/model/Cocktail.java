package vttp.ssf.miniproject.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cocktail {
    
    private String id;
    private String name;
    private String category;
    private boolean ifAlcoholic;
    private String instructions;
    private String thumbnail;
    private Map<String, String> ingredientsAndMeasures;

    public void addIngredientAndMeasure(String ingredient, String measure) {
        if (ingredientsAndMeasures == null) {
            ingredientsAndMeasures = new HashMap<>();
        }
        ingredientsAndMeasures.put(ingredient, measure);
    }
    
}
