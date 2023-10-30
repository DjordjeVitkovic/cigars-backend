package com.futing.diary.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Data
@ConfigurationProperties(prefix = "rsa")
@NoArgsConstructor
@AllArgsConstructor
public class RSAKeyProperties {

  private RSAPublicKey publicKey;
  private RSAPrivateKey privateKey;

}
