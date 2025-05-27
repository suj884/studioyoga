package studioyoga.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import studioyoga.project.service.TicketService;

@Controller
@RequestMapping("/admin/support")
public class AdminSupportController {
    private final TicketService ticketService;

    public AdminSupportController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "admin/support"; // admin/support.html
    }

    @PostMapping("/status")
    public String changeStatus(@RequestParam Long id, @RequestParam String estado) {
        ticketService.changeStatus(id, estado);
        return "redirect:/admin/support";
    }
    
}

