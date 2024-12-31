## 笔记
### 依赖管理
**server模块**  
![image](https://github.com/user-attachments/assets/19fa0154-6534-4839-9bfa-4f64b462ebb6)
![image](https://github.com/user-attachments/assets/0369aab3-e7fc-4ae7-bd01-2125485316f7)

**Pojo模块**  
![image](https://github.com/user-attachments/assets/a41b256e-9d57-4cb8-8442-f1c980931711)  

 
![image](https://github.com/user-attachments/assets/16b1fae4-6481-475d-994b-6560cda62a9a)

**entity是实体类  
DTO是前端传给后端的对象  
VO是后端返回给前端的对象**  

**获取当前线程id**  
![image](https://github.com/user-attachments/assets/f5742bec-dcd9-462b-a3c0-a56028abc535)  
**通过BaseContext.getCurrentId()获取当前员工id**  
![image](https://github.com/user-attachments/assets/3fc43cd6-d4f5-4f69-9bb4-0bba5cd6a428)

### 数据库表公共字段自动填充  
![image](https://github.com/user-attachments/assets/03e2855e-2c32-47db-9c39-0ac9a8faa1e6)  
（1）自定义注解AutoFill，用于标识需要进行公共字段自动填充的方法  
（2）自定义切面类AutoFillAspect，同意拦截加入了AutoFill注解的方法，通过反射为公共字段赋值
（3）在Mapper的方法上加入AutoFill注解，即可实现公共字段的自动填充

### 防止用户端频繁请求数据库，使用redis缓存菜品  
使用Spring Cache 框架来实现基于注解的缓存功能
Spring Cache 提供了一层抽象，底层可以切换不同的缓存实现  
@EnableCaching：开启缓存注解功能，通常加在启动类上   
@Cacheable：在方法执行前先查询缓存中是否有数据，如果有数据，则直接返回缓存数据；如果没有，则调用方法并将方法返回值放到缓存中  
@CachePut：将方法的返回值放到缓存中  
@CacheEvict：将一条或多条数据从缓存中删除  









