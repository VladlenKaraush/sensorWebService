package com.karaush.demo.controllers;

import com.karaush.demo.models.Record;
import com.karaush.demo.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
public class RecordController {

    private static final int recordsToKeep = 10;

    private final RecordRepository repository;

    @Autowired
    public RecordController(RecordRepository repository) {
        this.repository = repository;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @Transactional
    @PostMapping("/records")
    public void saveRecord(@Valid @RequestBody Record record){

        repository.saveAndFlush(record);
        if(repository.count() > recordsToKeep){
            repository.dropLast(recordsToKeep);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/records")
    public List<Record> getRecords(){
        return repository.fetchAllSortedByDate();
    }
}
