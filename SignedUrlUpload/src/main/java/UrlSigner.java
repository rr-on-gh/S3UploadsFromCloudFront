import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Properties;

public class UrlSigner {
    public static void main(String[] args) throws InvalidKeySpecException, IOException {

        Properties props = new Properties();
        props.load(UrlSigner.class.getResourceAsStream("cloudfront.properties"));
        String distributionDomainName = props.getProperty("distributionDomainName");
        File cloudFrontPrivateKeyFile = new File("SignedUrlUpload/src/main/resources/private.pem");
        String cloudFrontKeyPairId = props.getProperty("cloudFrontKeyPairId");
        Date expirationDate = new Date(System.currentTimeMillis() + 60 * 1000);

        String signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                SignerUtils.Protocol.https,
                distributionDomainName,
                cloudFrontPrivateKeyFile,
                "file_4.txt",
                cloudFrontKeyPairId,
                expirationDate);
        System.out.println(signedUrl);
    }
}
