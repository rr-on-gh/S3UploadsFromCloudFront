# Upload to S3 Origin through a CloudFront distribution

## Why?
Uploads to Cloudfront uses the edge network of AWS to connect to S3. This results in faster uploads by bringing clients on slower network or geographically distributed clients onto AWS' backbone sooner.
AWS also provides S3 Transfer Acceleration as an alternate to accelerate uploads and downloads from S3. In fact this should be considerd first if ease of use is of priority. S3TA will seamlessly work with your existing clients and bringing in the benefits of using AWS' network backbone.
 
However, purely from cost perspective of uploads, uploading via cloudfront may work out cheaper than S3 Transfer Acceleration. I will leave you to do the math as there are several discounts that AWS provides to its customers that need to be considered.

The following makes sense only if CloudFront cost benefit outweigh the simplicity of S3 Transfer Acceleration

## How?
### Step 1: Create a CF distribution
 - Follow the steps in this [Stackoverflow answer](https://stackoverflow.com/a/68310786/771149) to create a CloudFront distribution
 - Make sure to set the `s3:PutObject` and `s3:GetObject` actions in the bucket policy

### Step 2: Create a signed url
 - The `UrlSigner` class in the `SignedUrlUpload` maven project has the Java code to build the CloudFront signed URL
 - Set the CloudFront details in the `cloudfront.properties` file
 - Place the private key that you used in CloudFront in `src/main/resources/private.pem`
 - Copy the generated signed URL and use it in the next step

### Step 3: Do the upload
 - There is no AWS SDK for CloudFront to perform the upload
 - This `curl` command demonstrates the required options that need to be set and a Java client should be easy enough to write
```shell
curl -v -X PUT -F file=@file.txt -H "x-amz-content-sha256: UNSIGNED-PAYLOAD" -H "x-amz-acl: bucket-owner-full-control" "https://xxxxxxx.cloudfront.net/file_2.txt?Expires=1635873307&Signature=<signature>&Key-Pair-Id=K2Q53ZMRXXXXX"
```
 - Set the `x-amz-content-sha256` header to the SHA256 hash of the request content, or `UNSIGNED-PAYLOAD` if you are too lazy to do that [Reference](https://forums.aws.amazon.com/thread.jspa?threadID=258104)
 - Set the `x-amz-acl: bucket-owner-full-control` to switch the ownership to bucket owner instead of the uploader, which is the OAI user of the cloudfront [Reference](https://stackoverflow.com/a/39130925/771149)
