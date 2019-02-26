package ssm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * @author 肥宅快乐码
 * @date 2019/2/20
 */
public class SsmWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    /**
     * 配置过滤器
     * @return
     */
    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {
                // 字符编码过滤器
                new CharacterEncodingFilter("UTF-8",true,true),
                // 将这些请求转换为标准的http方法，使得支持GET、POST、PUT与DELETE请求的过滤器
                new HiddenHttpMethodFilter(),
                // 获取put表单的值，并将它重新打包传递到Controller中标注了method为RequestMethod.put的方法中的过滤器
                new HttpPutFormContentFilter()
        };
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        // 配置使用占位符${}
        PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        //PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //placeholderConfigurer.setLocation(resolver.getResource("classpath:app.properties"));
        return placeholderConfigurer;
    }
}
