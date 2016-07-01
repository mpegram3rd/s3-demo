package com.blt.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.blt.model.BucketItem;
import com.blt.model.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This acts as a bridge between our client, and the gritty details of accessing S3.
 */
@Service
public class StorageService {

    private final AmazonS3Client amazonS3Client;
    private final ResourcePatternResolver resourcePatternResolver;

    /** The base bucket to work with. */
    @Value("${cloud.aws.s3.bucket}")
    private String baseBucket;

    /**
     * Configure the service.
     *
     * @param amazonS3Client
     *      The S3 Client.
     */
    @Autowired
    public StorageService(final AmazonS3Client amazonS3Client, final ResourcePatternResolver resourcePatternResolver) {
        this.amazonS3Client = amazonS3Client;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * Retrieves a list of items from the S3 base bucket.  This is basically using AWS's libraries to do the
     * heavy lifting.
     *
     * @return a list of items in the bucket.
     */
    public List<BucketItem> list() {
        final ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(baseBucket));
        final List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        final List<BucketItem> items = new ArrayList<>();

        for (S3ObjectSummary s3Summary : s3ObjectSummaries) {
            items.add(transformS3ObjectSummary(s3Summary));
        }

        return items;
    }

    /**
     * Transform from the S3 representation to our front end model.
     *
     * @param s3Summary
     *      An S3 Object reference.
     *
     * @return The details of an item in the bucket.
     */
    private BucketItem transformS3ObjectSummary(final S3ObjectSummary s3Summary) {

        final BucketItem bucketItem = new BucketItem();
        bucketItem.setUid(s3Summary.getETag());
        bucketItem.setFilename(s3Summary.getKey());
        bucketItem.setSize(s3Summary.getSize());

        // Is it a file or folder?  S3 really doesn't treat them differently other than with a convention like this.
        final ItemType type = s3Summary.getKey().endsWith("/") ? ItemType.folder : ItemType.file;
        bucketItem.setType(type);

        return bucketItem;
    }

    /**
     * This uses more of a Spring like way of looking up files by treating them as resources.
     *
     * @return A list of "Bucket Items" that match the wild-carded query.
     */
    public List<BucketItem> listByResources() {

        final List<BucketItem> items = new ArrayList<>();
        try {
            final Resource[] allBucketItems = resourcePatternResolver.getResources("s3://" + baseBucket + "/**");

            for (Resource resource : allBucketItems) {
                items.add(transformResource(resource));
            }
        }
        catch (IOException ex) {
            System.out.println("Error retrieving data from S3");
            ex.printStackTrace(System.out);
        }
        return items;
    }

    /**
     * <p><Converts a Resource into a {@link BucketItem}</p>
     *
     * Note:<br/>
     * While the metadata exists internally in a
     * {@link org.springframework.cloud.aws.core.io.s3.SimpleStorageResource} the
     * Spring libraries don't, at this time, give you access to the more S3 centric metadata.
     *
     * @param resource
     *      A resource to convert to a {@link BucketItem}
     *
     * @return A Bucket Item.
     * @throws IOException
     */
    private BucketItem transformResource(final Resource resource) throws IOException {
        final BucketItem item = new BucketItem();
        item.setUid(resource.getURI().toString());
        item.setFilename(resource.getFilename());
        item.setSize(resource.contentLength());

        // All resources returned by the search are of type "file".
        item.setType(ItemType.file);

        return item;
    }

}
