package ssm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.regex.Pattern;

@Configuration
// 引入配置数据源的类
@Import(DataConfig.class)
/**
 * 扫描除了（excludeFilters）controller包以外的所有包
 * type=FilterType.CUSTOM代表自定义规则
 */
@ComponentScan(basePackages={"ssm"},
    excludeFilters={
        @Filter(type=FilterType.CUSTOM, value= RootConfig.ControllerPackage.class)
    })
/**
 * 用正则表达式选中controller包
 * @author 肥宅快乐码
 * @date 2019/2/20
 */
public class RootConfig {
  public static class ControllerPackage extends RegexPatternTypeFilter {
    public ControllerPackage() {
      super(Pattern.compile("ssm\\.controller"));
    }    
  }
}
