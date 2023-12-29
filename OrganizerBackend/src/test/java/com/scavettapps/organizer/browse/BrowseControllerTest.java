/**
 * Copyright 2019 Vincent Scavetta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scavettapps.organizer.browse;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasNoJsonPath;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Vincent Scavetta.
 */
@RunWith(MockitoJUnitRunner.class)
public class BrowseControllerTest {
   
   private static ObjectMapper mapper = new ObjectMapper();
   
   private BrowseService browseService;

   private MockMvc mockMvc;
   
   public BrowseControllerTest() {
      this.browseService = new BrowseService();
      mockMvc = standaloneSetup(new BrowseController(browseService)).build();
   }

   @Test
   public void browseRootTest() throws Exception {
      String response = mockMvc.perform(MockMvcRequestBuilders
          .get("/browse/root")
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();
      
      assertThat(response, hasNoJsonPath("$.error"));
      assertThat(response, hasJsonPath("$.data[*]"));
   }

   @Test
   public void browseDirectoryTest_noHidden() throws Exception {
      String response = mockMvc.perform(MockMvcRequestBuilders
          .post("/browse")
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(new BrowseRequest("./", false))))
          .andExpect(status().isOk())
          .andReturn().getResponse()
          .getContentAsString();
      
      assertThat(response, hasNoJsonPath("$.error"));
      assertThat(response, hasJsonPath("$.data[*]"));
   }
   
   @Test
   public void browseDirectoryTest_showHidden() throws Exception {
      String response = mockMvc.perform(MockMvcRequestBuilders
          .post("/browse")
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(new BrowseRequest("./", true))))
          .andExpect(status().isOk())
          .andReturn().getResponse()
          .getContentAsString();
      
      assertThat(response, hasNoJsonPath("$.error"));
      assertThat(response, hasJsonPath("$.data[*]"));
   }
   
}
