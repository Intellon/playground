package tutorials.base64converter.java;

import java.util.Base64;

public class Encoder_Decoder {

    /**
     * Encoded ein String
     *
     * @param originalString
     * @return
     * @throws Exception
     */
    public String encodeString(String originalString) throws Exception {
        // Encode into Base64 format
         String basicBase64format = Base64.getEncoder().encodeToString(originalString.getBytes());
         return basicBase64format;
    }


    /**
     * Decoded ein String
     *
     * @param decodedString
     * @return
     * @throws Exception
     */
    public String decodeString(String decodedString) {
        // decode into String from encoded format
         byte[] actualByte = Base64.getDecoder().decode(decodedString);
         String basicBase64Encode = new String(actualByte);
         return basicBase64Encode;
    }

}
