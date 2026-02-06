package com.minhthien.web.shop.config.payos;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

public class PayOSWebhookVerifier {

    public static boolean verify(
            Map<String, Object> data,
            String signature,
            String checksumKey
    ) {
        try {

            // 🔥 Sort key ASC
            Map<String, Object> sorted =
                    new TreeMap<>(data);

            // 🔥 Build query string
            StringBuilder sb =
                    new StringBuilder();

            for (Map.Entry<String, Object> e
                    : sorted.entrySet()) {

                if (sb.length() > 0)
                    sb.append("&");

                sb.append(e.getKey())
                        .append("=")
                        .append(
                                e.getValue() == null
                                        ? ""
                                        : e.getValue()
                        );
            }

            String signData = sb.toString();

            // 🔥 HMAC SHA256
            Mac mac =
                    Mac.getInstance("HmacSHA256");

            SecretKeySpec key =
                    new SecretKeySpec(
                            checksumKey.getBytes(
                                    StandardCharsets.UTF_8
                            ),
                            "HmacSHA256"
                    );

            mac.init(key);

            byte[] raw =
                    mac.doFinal(
                            signData.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            String expected =
                    bytesToHex(raw);

            return expected.equalsIgnoreCase(
                    signature
            );

        } catch (Exception e) {
            return false;
        }
    }

    private static String bytesToHex(
            byte[] bytes
    ) {
        StringBuilder hex =
                new StringBuilder();

        for (byte b : bytes) {
            hex.append(
                    String.format("%02x", b)
            );
        }

        return hex.toString();
    }
}
