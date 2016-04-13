# puppy-io
puppy-io provides easy way to develop reactive microservice applications on the jvm.

### Prerequisities
  * JDK 1.8.X
  * Maven 3.3.X

### Getting Started
 * Download the [puppy-io](https://github.com/loviworld/puppy-io) from GitHub
 * Install puppy-io dependency using **mvn install**
 
### Running the sample application
 * Download the [user-mgr](https://github.com/loviworld/puppy-io) application from GitHub
 * Build the application using **mvn package**
 * Run the application using **java -jar target/use.jar**

#How to use puppy-io
###Starting the application
```java
@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
        PuppyApp.create(App.class, "user-mgr", args).run(81);
    }
}
```
* ```PuppyApp.run()``` default port is 8080
* ```PuppyApp.run(int webAppPort)```
* ```PuppyApp.runWebApp()``` run only the web application
* ```PuppyApp.runServiceApp()``` run only the service application
* sample application is based on spring-boot.if you want you can use puppy-io without spring-boot

###Web App
```java
@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private ServiceCaller serviceCaller;
	
	@ResponseBody
	@RequestMapping(produce="application/json")
	public void findAll(HttpResponseResult responseResult) throws ServiceCallerException{
		
		Result<List<User>> result = Result.create();
		FailResult failResult = FailResult.create();
		
		serviceCaller.call("UserService.findAll", result);
		
		result.process(r->{
			responseResult.complete(new ResponseMessage(1, r));
		}, failResult);
		
		failResult.setHandler(fail->{
			responseResult.complete(new ResponseMessage(-1, fail.getMessage()),500);
		});
	}
	.......
}
```
####@Controller
* If you use spring @Controller, puppy-io unable to identify the class as a controller.so use ```com.lovi.puppy.annotation.Controller```
* Implementation of the controllers are similar to the spring-mvc but consider internal architecture is totally different than spring-mvc

####@RequestMapping
* value = The primary mapping expressed by this annotation
* method = The HTTP request methods
* consumes = The consumable media types of the mapped request
* produce = The producible media types of the mapped request

####HttpResponseResult
* ```HttpResponseResult.complete(Object value)``` set response value
* ```HttpResponseResult.complete(Object value, int statusCode)``` set response value with statusCode
