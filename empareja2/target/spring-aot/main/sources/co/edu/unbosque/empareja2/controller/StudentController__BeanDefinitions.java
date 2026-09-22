package co.edu.unbosque.empareja2.controller;

import co.edu.unbosque.empareja2.service.StudentService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StudentController}.
 */
@Generated
public class StudentController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'studentController'.
   */
  private static BeanInstanceSupplier<StudentController> getStudentControllerInstanceSupplier() {
    return BeanInstanceSupplier.<StudentController>forConstructor(StudentService.class)
            .withGenerator((registeredBean, args) -> new StudentController(args.get(0)));
  }

  /**
   * Get the bean definition for 'studentController'.
   */
  public static BeanDefinition getStudentControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StudentController.class);
    beanDefinition.setInstanceSupplier(getStudentControllerInstanceSupplier());
    return beanDefinition;
  }
}
