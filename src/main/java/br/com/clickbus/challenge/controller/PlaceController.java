package br.com.clickbus.challenge.controller;


import br.com.clickbus.challenge.dto.PlaceDTO;
import br.com.clickbus.challenge.entity.Place;
import br.com.clickbus.challenge.exception.PlaceNotFoundException;
import br.com.clickbus.challenge.service.PlaceService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api("places")
@RestController
@RequestMapping("places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService service;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid PlaceDTO dto) {
        return new ResponseEntity(service.save(dto.buildPlace()).convertToDTO(), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        return service.findById(id)
                      .map(place -> ResponseEntity.ok(place.convertToDTO()))
                      .orElseThrow(() -> new PlaceNotFoundException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity findAll(@Param("name") String name) {
        List<Place> places = name != null ? service.findByName(name) : service.findAll();

        if (places.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(PlaceDTO.convertToList(places));
    }

    @PutMapping("/{id}")
    public ResponseEntity alter(@PathVariable Long id, @RequestBody @Valid PlaceDTO placeDTO) {
        Place place = service.findById(id).orElseThrow(null);
        return new ResponseEntity(service.alter(place, placeDTO).convertToDTO(), HttpStatus.OK);
    }
}
