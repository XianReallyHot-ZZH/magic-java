# magic-java
java小型组件手写系列

## 组件简介
* mini-schedule：定时任务组件，模拟实现本地定时任务提交与调度
* mini-ThreadPool：线程池组件，模拟实现线程池提交与调度,支持设置核心线程数、最大线程数、拒绝策略，内置两个默认拒绝策略
* mini-jvm：模拟实现hotspot启动时，根据配置的主类和类加载路径，加载主类的class文件，并进行解析，然后根据字节码指令进行逐行解释执行。模拟实现了如下核心组件：
  * 启动类加载器
  * 虚拟机线程栈
  * 线程栈的栈帧
  * pc寄存器
  * 操作数栈
* mini-spring: mini版spring，模拟实现了ioc容器、springMVC的核心逻辑
  * spring-ioc: 模拟实现了spring容器启动、扫描、BeanDefinition管理，bean管理，自动注入Autowired，生命周期接口BeanPostProcessor的管理与回调，初始化注解PostConstruct扫描与回调
    * 支持注解Component
    * 支持注解Autowired
    * 支持注解PostConstruct
    * 支持接口BeanPostProcessor
    * 二级缓存解决自动注入循环依赖
  * spring-mvc: 模拟实现了一个内嵌的TomcatServer，启动阶段自动扫描管理接口路径映射（uri-WebHandler），运行阶段自动处理映射请求
    * 支持注解Controller
    * 支持注解RequestMapping
    * 支持注解RequestBody,支持三种数据类型：HTML，JSON，LOCAL（ModelAndView）
    * 支持注解PathParam

## 设计模式实践
* decorator-装饰器设计模式：不需要继承，基于组合的方式实现，有更高的灵活性，在实现功能任意组合、扩展、追加等需求场景下，该设计模式很合适
  * 案例1：带有删除记录功能的set类；
  * 案例2：带有缓存功能的文件输入流；
  * 案例3：增强spring的request注解，实现对入参requestBody的属性增强（Map类型入参，默认增加一个时间戳字段）
* iterator-迭代器设计模式：
* composite-组合设计模式：
* chain-责任链设计模式：
* flyweight-蝇量（享元）设计模式：
