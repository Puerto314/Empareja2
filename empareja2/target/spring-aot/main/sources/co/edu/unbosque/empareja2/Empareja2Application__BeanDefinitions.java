package co.edu.unbosque.empareja2;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link Empareja2Application}.
 */
@Generated
public class Empareja2Application__BeanDefinitions {
  /**
   * Get the bean definition for 'empareja2Application'.
   */
  public static BeanDefinition getEmparejaApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(Empareja2Application.class);
    beanDefinition.setInstanceSupplier(Empareja2Application::new);
    return beanDefinition;
  }
}
