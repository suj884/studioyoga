package studioyoga.project.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import studioyoga.project.model.Classes;
import studioyoga.project.service.ClassesService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
public class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassesService classesService;

    @Test
    void testManageClasses() throws Exception {
        when(classesService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/admin/classes/manageclasses"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/manageclasses"))
                .andExpect(model().attributeExists("classesList"));
    }

    @Test
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/admin/classes/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/formClasses"))
                .andExpect(model().attributeExists("classes"));
    }

    @Test
    void testShowEditForm() throws Exception {
        Classes classes = new Classes();
        classes.setId(1);
        when(classesService.findById(1)).thenReturn(Optional.of(classes));
        mockMvc.perform(get("/admin/classes/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/formClasses"))
                .andExpect(model().attributeExists("classes"));
    }

    @Test
    void testShowEditFormNotFound() throws Exception {
        when(classesService.findById(99)).thenReturn(Optional.empty());
        mockMvc.perform(get("/admin/classes/edit/99"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testSaveClassAllowedAndNotExists() throws Exception {
        when(classesService.isAllowedSchedule(anyString(), anyString(), anyString())).thenReturn(true);
        when(classesService.existsByDateTime(any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        when(classesService.save(any(Classes.class))).thenReturn(new Classes());

        mockMvc.perform(post("/admin/classes/save")
                .param("title", "Yoga Suave")
                .param("eventDate", "2025-06-01")
                .param("timeInit", "10:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/classes/manageclasses"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testSaveClassNotAllowedSchedule() throws Exception {
        when(classesService.isAllowedSchedule(anyString(), anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/admin/classes/save")
                .param("title", "Yoga Suave")
                .param("eventDate", "2025-06-01")
                .param("timeInit", "10:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/classes/new"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void testSaveClassExists() throws Exception {
        when(classesService.isAllowedSchedule(anyString(), anyString(), anyString())).thenReturn(true);
        when(classesService.existsByDateTime(any(LocalDate.class), any(LocalTime.class))).thenReturn(true);

        mockMvc.perform(post("/admin/classes/save")
                .param("title", "Yoga Suave")
                .param("eventDate", "2025-06-01")
                .param("timeInit", "10:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/classes/new"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void testDeleteClass() throws Exception {
        mockMvc.perform(get("/admin/classes/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/classes/manageclasses"))
                .andExpect(flash().attributeExists("success"));
    }
}