# Fileswim Service

A library for handling Fileswim security and token, etc.

## Usage

Add dependency:

```
<dependency>
    <groupId>com.github.jiesu12</groupId>
    <artifactId>service-parent</artifactId>
    <version>1.1.0</version>
</dependency>
```

Add beans to a spring configuration class:

```kotlin
@Configuration
@EnableScheduling
class AppConfig(val discoveryClient: DiscoveryClient) {
    @Bean
    fun publicKeyHolder(): PublicKeyHolder =
        PublicKeyHolder(null)

    @Bean
    fun tokenAuthenticationFilter(objectMapper: ObjectMapper): TokenAuthenticationFilter =
        TokenAuthenticationFilter(objectMapper, publicKeyHolder())

    @Bean
    fun publicKeyUpdater(): PublicKeyUpdater =
        PublicKeyUpdater(discoveryClient,  publicKeyHolder())

    @Scheduled(fixedRate = 30000)
    fun refreshPublicKey() =
        publicKeyUpdater().refreshPublicKey()
}
```
