package com.blt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Macon on 6/30/2016.
 */
@RestController
@RequestMapping("/s3")
public class S3Resource {

    @RequestMapping(value="/list", produces = "application/json")
    public ResponseEntity<List<String>> getFileList() {
        final List<String> files = new ArrayList<>();

        files.add("Macon");
        files.add("Alex");

        return ResponseEntity.ok(files);
    }
}
