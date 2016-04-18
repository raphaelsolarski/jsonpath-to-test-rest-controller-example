package com.raphaelsolarski.jsonpath.controller;

import com.jayway.jsonpath.JsonPath;
import com.raphaelsolarski.jsonpath.Application;
import net.minidev.json.JSONArray;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class}, loader = AnnotationConfigWebContextLoader.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
@WebAppConfiguration
public class UserControllerTest {

    private static final int USER_ID = 123;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldReturnUserWithId() throws Exception {
        String responseContent = mockMvc.perform(get("/user/" + USER_ID).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer loginFromResponse = JsonPath.parse(responseContent).read("$.id");
        Assert.assertEquals(USER_ID, loginFromResponse.intValue());
    }

    @Test
    public void shouldReturnArrayOfUsers() throws Exception {
        String responseContent = mockMvc.perform(get("/user").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Integer> ids = JsonPath.parse(responseContent).read("$[*].id");
        MatcherAssert.assertThat(ids, Matchers.hasSize(3));
        MatcherAssert.assertThat(ids, Matchers.containsInAnyOrder(1,2,3));
    }

    @Test
    public void shouldReturnsUsersWithListsOfPosts() throws Exception {
        String responseContent = mockMvc.perform(get("/user").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<String> postTitles = JsonPath.parse(responseContent).read("$[*].posts[0].title");
        MatcherAssert.assertThat(postTitles, Matchers.hasSize(3));
        MatcherAssert.assertThat(postTitles, Matchers.everyItem(Matchers.is("post1")));
    }

}