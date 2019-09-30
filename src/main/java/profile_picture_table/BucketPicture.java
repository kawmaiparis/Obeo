package profile_picture_table;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import user.ObeoUserBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BucketPicture {
    public static Boolean storeImage(long user_id, byte[] profileBytes) {
        if (profileBytes == null) {
            return false;

        }
        Path path = Paths.get("/tmp/" + user_id + ".jpg");
        try {
            Files.write(path, profileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        File file = new File("/tmp/" + user_id + ".jpg");

        /* check if file is valid */
        if (!file.exists()&& file.isDirectory()) {
            System.out.println("Picture is empty! Default picture is set!");
            return false;
        }


        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials("AKIAJYWDV5ANIKADOCNA", "OXJgfPZIKr9fzSPkFfY6pffrG6fSfIlBXoBWhIXL");
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.EU_WEST_2)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();



            String bucketName = "obeoimages";
            String key = Long.toString(user_id);
            PutObjectRequest request = new PutObjectRequest(bucketName, key, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(request);
            if (file.delete()) {
                System.out.println("Deleted successfully");
            } else {
                System.out.println("Not deleted");
            }
            return true;

        } catch(Exception e) {
//             Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3.
            System.out.println("Error in image");
            e.printStackTrace();
        }

        return false;
    }

    public static File getImageFromUserID(Long user_id, boolean image_exists) {
        if (user_id == -1) {
            System.out.println("Error: No user has id -1");
            return null;
        }

        if (!ObeoUserBuilder.userExists(user_id)) {
            System.out.println("Error: User does not exist!");
            return null;
        }

        /* CHECK IF IMAGE EXISTS FOR THIS USER*/

        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials("AKIAJYWDV5ANIKADOCNA", "OXJgfPZIKr9fzSPkFfY6pffrG6fSfIlBXoBWhIXL");
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.EU_WEST_2)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();

            String bucketName = "obeoimages";
            String key;

            // IF IMAGE DOESNT EXIST RETURN DEFAULT IMAGE
            if (image_exists) {
                // user_id = (long) -1;
                key = Long.toString(user_id);
            } else {
                key = "default.jpg";
            }

            File file = new File("/tmp/downloaded.jpg");

            S3Object object = s3Client.getObject(bucketName, key);
//            InputStream reader = new BufferedInputStream(object.getObjectContent());
            InputStream in = object.getObjectContent();
            System.out.println("here");
            Files.copy(in, Paths.get("/tmp/downloaded.jpg"), StandardCopyOption.REPLACE_EXISTING);
            return file;

        } catch(SdkClientException | IOException e) {
            // Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3.
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] bytesFromBucket(Long user_id, boolean image_exists) {
        if (user_id == -1) {
            System.out.println("Error: No user has id -1");
            return null;
        }

        if (!ObeoUserBuilder.userExists(user_id)) {
            System.out.println("Error: User does not exist!");
            return null;
        }

        /* CHECK IF IMAGE EXISTS FOR THIS USER*/

        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials("AKIAJYWDV5ANIKADOCNA", "OXJgfPZIKr9fzSPkFfY6pffrG6fSfIlBXoBWhIXL");
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.EU_WEST_2)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();

            String bucketName = "obeoimages";
            String key;

            // IF IMAGE DOESNT EXIST RETURN DEFAULT IMAGE
            if (image_exists) {
                // user_id = (long) -1;
                key = Long.toString(user_id);
            } else {
                key = "default.jpg";
            }


            S3Object object = s3Client.getObject(bucketName, key);
            byte[] bytes = IOUtils.toByteArray(object.getObjectContent());
            return bytes;

        } catch(SdkClientException | IOException e) {
            // Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3.
            e.printStackTrace();
            return null;
        }
    }
}
