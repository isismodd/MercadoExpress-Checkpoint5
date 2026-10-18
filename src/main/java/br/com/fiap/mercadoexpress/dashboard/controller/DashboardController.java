package br.com.fiap.mercadoexpress.dashboard.controller;

import br.com.fiap.mercadoexpress.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** Dashboard da gerência (somente ADMIN, via /admin/**). */
@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("d", dashboardService.montar());
        return "admin/dashboard";
    }
}
