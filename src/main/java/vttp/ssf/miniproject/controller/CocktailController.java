package vttp.ssf.miniproject.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.ssf.miniproject.model.Cocktail;
import vttp.ssf.miniproject.service.CocktailService;

@Controller
@RequestMapping
public class CocktailController {

    @Autowired
    CocktailService ctSvc;

    @GetMapping("/search")
    public String searchCocktails(@RequestParam(name = "searchTerm", required = false) String searchTerm, Model model) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            List<Cocktail> searchResults = ctSvc.searchCocktails(searchTerm);

            // Filter the results to include only cocktails that start with the search term
            List<Cocktail> filteredResults = searchResults.stream()
                    .filter(cocktail -> cocktail.getName().toLowerCase().startsWith(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());

            if (!filteredResults.isEmpty()) {
                model.addAttribute("searchResults", filteredResults);
                model.addAttribute("searchTerm", searchTerm);
            } else {
                model.addAttribute("noResultsMessage", "No results found for the search term: " + searchTerm);
            }
        } else {
            return "redirect:/home";
        }

        return "searchresult";
    }
    
}
