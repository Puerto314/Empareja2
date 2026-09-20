package co.edu.unbosque.empareja2.service.impl;

import co.edu.unbosque.empareja2.mapper.StudentMapper;
import co.edu.unbosque.empareja2.repository.StudentRepository;
import co.edu.unbosque.empareja2.security.CustomUserDetailsService;
import co.edu.unbosque.empareja2.security.JwtService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link AuthServiceImpl}.
 */
@Generated
public class AuthServiceImpl__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'authServiceImpl'.
   */
  private static BeanInstanceSupplier<AuthServiceImpl> getAuthServiceImplInstanceSupplier() {
    return BeanInstanceSupplier.<AuthServiceImpl>forConstructor(StudentRepository.class, StudentMapper.class, PasswordEncoder.class, JwtService.class, AuthenticationManager.class, CustomUserDetailsService.class)
            .withGenerator((registeredBean, args) -> new AuthServiceImpl(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5)));
  }

  /**
   * Get the bean definition for 'authServiceImpl'.
   */
  public static BeanDefinition getAuthServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AuthServiceImpl.class);
    beanDefinition.setInstanceSupplier(getAuthServiceImplInstanceSupplier());
    return beanDefinition;
  }
}
