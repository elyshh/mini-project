package vttp.ssf.miniproject.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.ssf.miniproject.model.Cocktail;
import vttp.ssf.miniproject.model.Register;
import vttp.ssf.miniproject.model.User;
import vttp.ssf.miniproject.repository.UserRepository;
import vttp.ssf.miniproject.service.CocktailService;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    CocktailService ctSvc;

    @GetMapping(path = "/about")
    public String getAbout(Model model) {
        return "about";
    }
    
    @GetMapping(path = "/login")
    public String showLogin(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    @PostMapping(path = "/login")
    public String postLogin(@Valid @ModelAttribute User user, BindingResult result, HttpSession sess) {
        if (result.hasErrors()) {
            return "login";
        }

        if (!userRepo.isUserValid1(user.getUsername())) {
            FieldError err = new FieldError("user", "username", "Login failed. Try again!");
            result.addError(err);
            return "login";
        }

        if (!userRepo.isUserValid2(user.getUsername(), user.getPassword())) {
            FieldError err = new FieldError("user", "password", "Wrong password. Try again!");
            result.addError(err);
            return "login";
        }

        User retrievedUser = userRepo.getUser(user.getUsername());
        String name = retrievedUser.getName();

        sess.setAttribute("name", name);
        System.out.println("Login successfully!");
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        Register registerUser = new Register();
        model.addAttribute("registerUser", registerUser);
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registerUser") Register registerUser, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userRepo.isUserValid1(registerUser.getUsername())) {
            FieldError err = new FieldError("registerUser", "username", "Username is taken, please input again!");
            result.addError(err);
            return "register";
        }

        if (!userRepo.isPasswordSame(registerUser.getPassword(), registerUser.getPassword_copy())) {
            FieldError err = new FieldError("registerUser", "password_copy", "Passwords do not match, please type the correct password.");
            result.addError(err);
            return "register";
        }

        if (registerUser.getDateOfBirth().isAfter(LocalDate.now().minusYears(18))) {
            FieldError err = new FieldError("registerUser", "dateOfBirth", "You must be 18 years or older.");
            result.addError(err);
            return "register";
        }

        userRepo.register(registerUser);
        redirectAttributes.addFlashAttribute("successMessage", "You have registered successfully!");
        
        return "redirect:/login";
    }

    @GetMapping(path = "/home")
    public String showHome(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        if (name == null) {
            return "redirect:/login";
        }

        List<Cocktail> cocktailsList = ctSvc.getAllCocktails();
        model.addAttribute("name", name);
        model.addAttribute("cocktailsList", cocktailsList);

        return "home";
    }

    @GetMapping(path = "/logout")
    public String showLogout(HttpSession sess, RedirectAttributes redirectAttributes) {
        sess.invalidate();
        redirectAttributes.addFlashAttribute("message", "Logged out successfully.");
        return "redirect:/login";
    }
    
}

