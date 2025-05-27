package studioyoga.project.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import studioyoga.project.model.BlogPost;
import studioyoga.project.service.BlogService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogPostController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
public class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Test
    void testManageBlog() throws Exception {
        when(blogService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/admin/blog/manageblog"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/manageblog"));
    }

    @Test
    void testNewPostForm() throws Exception {
        mockMvc.perform(get("/admin/blog/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/blogForm"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void testEditPostFormFound() throws Exception {
        BlogPost post = new BlogPost();
        post.setId(1);
        when(blogService.findById(1)).thenReturn(Optional.of(post));
        mockMvc.perform(get("/admin/blog/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/blogForm"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void testEditPostFormNotFound() throws Exception {
        when(blogService.findById(99)).thenReturn(Optional.empty());
        mockMvc.perform(get("/admin/blog/edit/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/blog/manageblog"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void testSavePost() throws Exception {
        when(blogService.save(any(BlogPost.class))).thenReturn(new BlogPost());
        mockMvc.perform(post("/admin/blog/save")
                .param("title", "Test Title")
                .param("content", "Test Content")
                .param("publishedDate", LocalDate.now().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/blog/manageblog"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(post("/admin/blog/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/blog/manageblog"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testConfirmDeleteFound() throws Exception {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("Test Post");
        when(blogService.findById(1)).thenReturn(Optional.of(post));
        mockMvc.perform(get("/admin/blog/confirm-delete/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/confirm-delete"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("action"))
                .andExpect(model().attributeExists("cancelUrl"));
    }

    @Test
    void testConfirmDeleteNotFound() throws Exception {
        when(blogService.findById(99)).thenReturn(Optional.empty());
        mockMvc.perform(get("/admin/blog/confirm-delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/blog/manageblog"))
                .andExpect(flash().attributeExists("error"));
    }
}