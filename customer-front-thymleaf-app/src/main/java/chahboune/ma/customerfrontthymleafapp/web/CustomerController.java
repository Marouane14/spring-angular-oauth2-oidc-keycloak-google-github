package chahboune.ma.customerfrontthymleafapp.web;

import chahboune.ma.customerfrontthymleafapp.entities.Customer;
import chahboune.ma.customerfrontthymleafapp.model.Product;
import chahboune.ma.customerfrontthymleafapp.repositories.CustomerRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    private CustomerRepository customerRepository;
    private ClientRegistrationRepository clientRegistrationRepository;

    public CustomerController(CustomerRepository customerRepository, ClientRegistrationRepository clientRegistrationRepository) {
        this.customerRepository = customerRepository;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }


    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String customers(Model model){
        List<Customer> customersList = customerRepository.findAll();
        model.addAttribute("customers",customersList);
        return "customers";
    }

    @GetMapping("/products")
    public String products(Model model) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        OAuth2AuthenticationToken oAuth2AuthenticationToken= (OAuth2AuthenticationToken) authentication;
        DefaultOidcUser oidcUser = (DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal();
        String jwtTokenValue=oidcUser.getIdToken().getTokenValue();
        RestClient restClient = RestClient.create("http://localhost:8098");
        List<Product> products = restClient.get()
                .uri("/products")
                .headers(httpHeaders -> httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer "+jwtTokenValue))
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
        model.addAttribute("products",products);
        return "products";
    }


    @GetMapping("/auth")
    @ResponseBody
    public Authentication authentication(Authentication authentication){
        return authentication;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized(){
        return "notAuthorized";
    }

    @GetMapping("/oauth2Login")
    public String oauth2Login(Model model){
        //Retourner Iterable de clientRegistrations
        Iterable<ClientRegistration> clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        //Créer un Map qui va recevoir les clientRegistrations
        // et leur URL (Google,Keycloak,GitHub)
        Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
        //Comme quoi chaque URL soit de Google soit Keycloak soit GitHub
        //Contient au debut de l'URL "oauth2/authorization", donc on crée
        //un String avec cette chaine de caracteres
        String authorizationRequestBaseUri = "oauth2/authorization";
        //Maintenant parcourir la liste des clientRegistrations et les mettre
        //dans la Map qu'on a créé avec clé (nom de registrationClient)
        //valeur (début de URL + '/' +le id registrationClient)
        clientRegistrations.forEach(registration ->{
            oauth2AuthenticationUrls.put(registration.getClientName(),
                    authorizationRequestBaseUri + "/" + registration.getRegistrationId());
        });
        //Dans le model on ajoute la Map sous le nom "urls" qu'on a créé
        // pour les récupérer dans la page "oauth2Login"
        model.addAttribute("urls", oauth2AuthenticationUrls);
        return "oauth2Login";
    }



}
