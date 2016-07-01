package com.blt.controller;

import com.blt.model.BucketItem;
import com.blt.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Macon on 6/30/2016.
 */
@RestController
@RequestMapping("/storage")
public class StorageResource {

    private final StorageService storageService;

    /**
     * Configure the resource.
     *
     * @param storageService
     *      A service that orchestrates and translates between our backend storage provider (S3)
     *      and the front
     */
    @Autowired
    public StorageResource(final StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Gets a list of files from the S3 Bucket.
     *
     * @return A list of files from the S3 Bucket.
     */
    @RequestMapping(value="/list", produces = "application/json")
    public ResponseEntity<List<BucketItem>> getFileList() {

        return ResponseEntity.ok(storageService.list());
    }

    /**
     * Gets a list of files from the S3 Bucket using a Resource lookup.  This is the more "Spring" like way
     * of retrieving this information.
     *
     * @return A list of files from the S3 Bucket.
     */
    @RequestMapping(value="/resources", produces = "application/json")
    public ResponseEntity<List<BucketItem>> getFilesByResource() {

        return ResponseEntity.ok(storageService.listByResources());
    }
}
