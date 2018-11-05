package com.karaush.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karaush.demo.controllers.RecordController;
import com.karaush.demo.models.Record;
import com.karaush.demo.repositories.RecordRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableJpaRepositories(basePackageClasses = RecordRepository.class)
@ContextConfiguration(classes = {WebAppContext.class})
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/data-h2.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/clean_db.sql")
})
@WebAppConfiguration
public class DemoApplicationTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void shouldLoadContext() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean(RecordController.class));
	}

	@Test
    public void shouldInitDBfromScript() throws Exception {

    	    mockMvc.perform(get("/records")).andExpect(status().isOk()).andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].latitude").value("59.9343° 1.1′ 3.63″ N"))
                .andExpect(jsonPath("$[0].longitude").value("30.3351° 3.22″ E"))
                .andExpect(jsonPath("$[0].temperature").value(12.4))
                .andExpect(jsonPath("$[7].latitude").value("2″ S"))
                .andExpect(jsonPath("$[7].longitude").value("51° 28′ 38″ E"))
                .andExpect(jsonPath("$[7].temperature").value(22.5));
    }

    @Test
    public void shouldAddNewRecordAtFirstPosition() throws Exception {

	    Record testRecord = new Record("-51° 28′ 40″ N","+120° 38″ W",0);
	    mockMvc.perform(MockMvcRequestBuilders.post("/records")
            .content(asJsonString(testRecord))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/records")).andExpect(status().isOk()).andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$[0].latitude").value("-51° 28′ 40″ N"))
            .andExpect(jsonPath("$[0].longitude").value("+120° 38″ W"))
            .andExpect(jsonPath("$[0].temperature").value(0));
    }

    @Test
    public void shouldAcceptValidLatitude() throws Exception{
        final Set<String> validLatitudeSet = new HashSet<>();

        //baseline case
        validLatitudeSet.add("51° 28′ 1″ N");

        //floating point numbers allowed
        validLatitudeSet.add("51.32° 28′ 1″ N");
        validLatitudeSet.add("51° 28.421′ 1″ N");
        validLatitudeSet.add("51° 28′ 1.412″ N");
        validLatitudeSet.add("51° 28.′ 1″ N");

        //can omit degrees, minutes, seconds and direction
        validLatitudeSet.add("28′ 1″ N");
        validLatitudeSet.add("51° 1″ N");
        validLatitudeSet.add("51° 28′ N");
        validLatitudeSet.add("51° 28′ 1″ ");

        //bounds for degrees are [-90; 90]
        validLatitudeSet.add("90° ");
        validLatitudeSet.add("+90° ");
        validLatitudeSet.add("-90° ");

        //bounds for minutes and seconds are [0; 60]
        validLatitudeSet.add("60′ 0″ ");
        validLatitudeSet.add("0′ 60″ ");

        Record validLatitude = new Record("","2° 38″ E",0);

        for(String latitude: validLatitudeSet){
            validLatitude.setLatitude(latitude);
            System.out.println(latitude);
            mockMvc.perform(MockMvcRequestBuilders.post("/records").content(asJsonString(validLatitude))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

    }

    @Test
    public void shouldRejectInvalidLatitude() throws Exception{
        final Set<String> invalidLatitudeSet = new HashSet<>();

        //cannot omit °, " and ′
        invalidLatitudeSet.add("51");
        invalidLatitudeSet.add("51° 28 1″ N");
        invalidLatitudeSet.add("51° 28′ 1 N");


        //bounds for degrees are [-90; 90]
        invalidLatitudeSet.add("+91° ");
        invalidLatitudeSet.add("-91° ");

        //bounds for minutes and seconds are [0; 60]
        invalidLatitudeSet.add("61′ ");
        invalidLatitudeSet.add("-1′ ");
        invalidLatitudeSet.add("61″ ");
        invalidLatitudeSet.add("-1″ ");

        Record validLatitude = new Record("","2° 38″ E",0);

        for(String latitude: invalidLatitudeSet){
            validLatitude.setLatitude(latitude);
            mockMvc.perform(MockMvcRequestBuilders.post("/records").content(asJsonString(validLatitude))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
        }

    }

    @Test
    public void shouldAcceptValidLongitude() throws Exception{
        final Set<String> validLongitudeSet = new HashSet<>();

        //baseline case
        validLongitudeSet.add("120° 43′ 12″ E");

        //can omit degrees, minutes, seconds and direction
        validLongitudeSet.add("43′ 12″ E");
        validLongitudeSet.add("120° 12″ E");
        validLongitudeSet.add("120° 43′ E");
        validLongitudeSet.add("120° 43′ 12″ ");

        //floating point numbers allowed
        validLongitudeSet.add("120.867° 43′ 12″ ");
        validLongitudeSet.add("120° 43.123′ 12″ ");
        validLongitudeSet.add("120° 43′ 12.111″ ");

        //bounds for degrees are [-180; 180]
        validLongitudeSet.add("180° ");
        validLongitudeSet.add("+180° ");
        validLongitudeSet.add("-180° ");

        //bounds for minutes and seconds are [0; 60]
        validLongitudeSet.add("60′ 0″ ");
        validLongitudeSet.add("0′ 60″ ");

        Record validLatitude = new Record("","2° 38″ E",0);

        for(String longitude: validLongitudeSet){
            validLatitude.setLongitude(longitude);
            mockMvc.perform(MockMvcRequestBuilders.post("/records").content(asJsonString(validLatitude))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

    }

    @Test
    public void shouldRejectInvalidLongitude() throws Exception{
        final Set<String> invalidLongitudeSet = new HashSet<>();

        //cannot omit °, " and ′
        invalidLongitudeSet.add("120 43′ 12″ E");
        invalidLongitudeSet.add("120° 43 12″ E");
        invalidLongitudeSet.add("120° 43′ 12 E");

        //bounds for degrees are [-180; 180]
        invalidLongitudeSet.add("+181° ");
        invalidLongitudeSet.add("-181° ");
        invalidLongitudeSet.add("181° ");

        //bounds for minutes and seconds are [0; 60]
        invalidLongitudeSet.add("61′ ");
        invalidLongitudeSet.add("-1′ ");
        invalidLongitudeSet.add("61″ ");
        invalidLongitudeSet.add("-1″ ");

        Record validLatitude = new Record("51° 28′ 38″ N","",0);

        for(String longitude: invalidLongitudeSet){
            validLatitude.setLongitude(longitude);
            mockMvc.perform(MockMvcRequestBuilders.post("/records").content(asJsonString(validLatitude))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
        }

    }

}
