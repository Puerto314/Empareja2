package co.edu.unbosque.empareja2.security;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link JwtService}.
 */
@Generated
public class JwtService__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static JwtService apply(RegisteredBean registeredBean, JwtService instance) {
    AutowiredFieldValueResolver.forRequiredField("secret").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("expirationMs").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
