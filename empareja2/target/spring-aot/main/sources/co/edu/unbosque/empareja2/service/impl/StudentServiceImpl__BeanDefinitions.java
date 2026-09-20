package co.edu.unbosque.empareja2.service.impl;

import co.edu.unbosque.empareja2.mapper.StudentMapper;
import co.edu.unbosque.empareja2.repository.StudentRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link StudentServiceImpl}.
 */
@Generated
public class StudentServiceImpl__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'studentServiceImpl'.
   */
  private static BeanInstanceSupplier<StudentServiceImpl> getStudentServiceImplInstanceSupplier() {
    return BeanInstanceSupplier.<StudentServiceImpl>forConstructor(StudentRepository.class, StudentMapper.class, PasswordEncoder.class)
            .withGenerator((registeredBean, args) -> new StudentServiceImpl(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'studentServiceImpl'.
   */
  public static BeanDefinition getStudentServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StudentServiceImpl.class);
    beanDefinition.setInstanceSupplier(getStudentServiceImplInstanceSupplier());
    return beanDefinition;
  }
}
