package co.edu.unbosque.empareja2.config;

import co.edu.unbosque.empareja2.repository.StudentRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link AdminSeeder}.
 */
@Generated
public class AdminSeeder__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'adminSeeder'.
   */
  private static BeanInstanceSupplier<AdminSeeder> getAdminSeederInstanceSupplier() {
    return BeanInstanceSupplier.<AdminSeeder>forConstructor(StudentRepository.class, PasswordEncoder.class)
            .withGenerator((registeredBean, args) -> new AdminSeeder(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'adminSeeder'.
   */
  public static BeanDefinition getAdminSeederBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AdminSeeder.class);
    beanDefinition.setInstanceSupplier(getAdminSeederInstanceSupplier());
    return beanDefinition;
  }
}
