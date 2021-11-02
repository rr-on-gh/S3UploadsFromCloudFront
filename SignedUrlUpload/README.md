# Upload to S3 Origin through a CloudFront distribution

## Why?
 - Speed up uploads
 - vs S3 Transfer Acceleration
   - List price is cheaper to upload
   - Special CloudFront pricing

## How?
### Step 1: Create a CF distribution
 - https://stackoverflow.com/a/68310786/771149

### Step 2: Create a signed url
 - Reference: https://aws.amazon.com/blogs/developer/accessing-private-content-in-amazon-cloudfront/
 - Run UrlSigner class

### Step 3: Do the upload
 - Set the `x-amz-content-sha256` header to the SHA256 hash of the request content, or `UNSIGNED-PAYLOAD` if you are too lazy to do that [Reference](https://forums.aws.amazon.com/thread.jspa?threadID=258104)
 - Set the `x-amz-acl: bucket-owner-full-control` to switch the ownership to bucket owner instead of the uploader, which is the OAI user of the cloudfront [Reference](https://stackoverflow.com/a/39130925/771149)
```shell
curl -v -X PUT -F file=@file.txt -H "x-amz-content-sha256: UNSIGNED-PAYLOAD" -H "x-amz-acl: bucket-owner-full-control" "https://xxxxxxx.cloudfront.net/file_2.txt?Expires=1635873307&Signature=<signature>&Key-Pair-Id=K2Q53ZMRXXXXX"  

```