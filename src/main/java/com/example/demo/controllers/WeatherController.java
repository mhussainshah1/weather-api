package com.example.demo.controllers;

import com.example.demo.models.FormCityAttribute;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.models.Weather;
import com.example.demo.models.WeatherUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@ComponentScan("com.spring.restapi.config")
@Controller
public class WeatherController {

    @Autowired
    RestTemplate restTemp;

    @Autowired
    private WeatherUrl weatherData;

    @GetMapping("/weather")
    public String CityForm(Model model) {
        model.addAttribute("city", new FormCityAttribute());
        return "formData";
    }

    @PostMapping("/weather")
    public String getWeather(Model model, @ModelAttribute FormCityAttribute city)
            throws JsonParseException, JsonMappingException, IOException {

        UriComponents uriComponents = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host(weatherData.getUrl())
                .path("")
                .query("q={keyword}&appid={appid}")
                .buildAndExpand(city.getCity(), weatherData.getApiKey());

        String uri = uriComponents.toUriString();

        ResponseEntity<String> resp = restTemp.exchange(uri, HttpMethod.GET, null, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Weather weather = mapper.readValue(resp.getBody(), Weather.class);
        model.addAttribute("weatherData", weather);

        return "weatherDetails";
    }

}