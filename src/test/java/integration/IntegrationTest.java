package integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.DatabaseConnection.DatabaseConnectionApplication;
import com.example.DatabaseConnection.model.MySpringBootDataModel;
import com.example.DatabaseConnection.repository.MySpringBootRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DatabaseConnectionApplication.class})
@AutoConfigureMockMvc
public class IntegrationTest {
	
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private MySpringBootRepository repository;
	
	@Before
	public void clearDB() {
		repository.deleteAll();
	}
	
	@Test
	public void findingAndRetrievingPersonFromDatabase() throws Exception {
		repository.save(new MySpringBootDataModel("Dale","Salford", 2));
		mvc.perform(get("/api/person")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content()
		.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$[0].name",is("Dale")));
	}
	
	@Test
	public void addApersonToDatabaseTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Robert\",\"address\":\"Atlantis\",\"age\":200}"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name",is("Robert")));
	}
	@Test
	public void editApersonInDatabaseTest() throws Exception {
		MySpringBootDataModel model1 = new MySpringBootDataModel("Tim","Somewhere",3);
		repository.save(model1);
		Long id = model1.getId();
		mvc.perform(MockMvcRequestBuilders.put("/api/person/"+id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Robert3\",\"address\":\"test\",\"age\":100}"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name",is("Robert3")));
	}
	@Test
	public void deletePersonFromDatabaseTest() throws Exception {
		MySpringBootDataModel model1 = new MySpringBootDataModel("Tim","Somewhere",3);
		repository.save(model1);
		Long id = model1.getId();
		mvc.perform(MockMvcRequestBuilders.delete("/api/person/"+id)
				.content(""))
				.andExpect(status().isOk());
	}
}
