# Fileswim Service

A library for handling Fileswim security and token, etc.

## Usage

Add this to `Application.java`

```
@ComponentScan("jiesu.fileswim.service")
```

Add this to `SecurityConfig.java`

```kotlin
class SecurityConfig(val tokenAuthenticationFilter: TokenAuthenticationFilter): WebSecurityConfigurerAdapter() {
  override fun configure(http: HttpSecurity) {
    http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
  }
}
```
