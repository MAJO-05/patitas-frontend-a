package pe.edu.cibertec.patitas_frontend_a.controller;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.cibertec.patitas_frontend_a.viewmodel.LoginModel;
import pe.edu.cibertec.patitas_frontend_a.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontend_a.dto.LoginResponseDTO;


@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/inicio")
    public String inicio(Model model) {
        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {

        // Validar campos de entrada
        if (tipoDocumento.isEmpty() || numeroDocumento.isEmpty() || password.isEmpty()) {
            model.addAttribute("loginModel", new LoginModel("01", "Error: Debe completar correctamente sus credenciales", ""));
            return "inicio";
        }

        // Crear un objeto de solicitud
        LoginRequestDTO loginRequest = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);

        // Llamar al backend
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/autenticacion/login";
        LoginResponseDTO response = restTemplate.postForObject(url, loginRequest, LoginResponseDTO.class);

        if (response != null && "00".equals(response.codigo())) {
            model.addAttribute("loginModel", new LoginModel("00", "", response.nombreUsuario()));
            return "principal";
        } else {
            model.addAttribute("loginModel", new LoginModel("01", response.mensaje(), ""));
            return "inicio";
        }
    }



}
