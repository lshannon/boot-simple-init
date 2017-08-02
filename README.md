# boot-simple-init

Shows a simple sample of how a Boot app can be initialized with data upon start up (Great for Demos and Samples)

## CommandLine Runner

The first example uses a CommandLineRunner to intiliaze the data:

http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/CommandLineRunner.html

```java

public static Map<String,Customer> customers = new HashMap<String,Customer>();
	
@Bean
	public CommandLineRunner init() {
		return (args) -> {
			log.info("Inserting two customers. Current Map Size: " +  customers.size());
			Customer one = new Customer("gopika", 23, new Date());
			customers.put(one.getName(), one);
			Customer two = new Customer("george", 23, new Date());
			customers.put(two.getName(), two);
			log.info("Done inserting customers. Current Map Size: " +  customers.size());
		};
	}

```
A 'ApplicationArguments' can be passed into the init to get any arguements passed in at start up.

```java

public CommandLineRunner init(ApplicationArguments params) {
		return (args) -> {
			for (String name : args.getOptionNames()){
            		logger.info("arg-" + name + "=" + args.getOptionValues(name));
       			}
		};
	}


```

As you can see, the data is a static member of the class.

Upon start up we can see the data is initialized. There is a short sleep before the data is initialized, this is just out of my curiousity to see how slow loading of data would affect endpoint availability.

```shell

2017-01-26 14:22:17.883  INFO 75941 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 9898 (http)
2017-01-26 14:22:21.890  INFO 75941 --- [           main] c.l.boot.CommandLineRunnerApplication    : Inserting two customers. Current Map Size: 0
2017-01-26 14:22:21.890  INFO 75941 --- [           main] c.l.boot.CommandLineRunnerApplication    : Done inserting customers. Current Map Size: 2
2017-01-26 14:22:21.892  INFO 75941 --- [           main] c.l.boot.CommandLineRunnerApplication    : Started CommandLineRunnerApplication in 6.174 seconds (JVM running for 6.78)

```
Note that the logging appears before the application is started, there might be some life cycle events to explore here

Now to access this data in an endpoint we can do the following:

```java

@RequestMapping("/customer/{name}")
	public ResponseEntity<Customer> customer(@PathVariable String name) {
		log.info("Searching for : " + name);
		Customer returnValue = CommandLineRunnerApplication.customers.get(name);
		if (returnValue  != null) {
			log.info("Found a reference for name: " + name + " = " + returnValue.toString());
			return ResponseEntity.ok(returnValue);
		}
		log.info("Nothing found for name: " + name);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}


```

Now we can hit the endoint (http://localhost:9898/v1/customer/george) and get the JSON for our good friend George.

## PostContruct

For another approach we are going to put the data and loading in a Component.

Here is our init which are members of our Controller class.

```java
	
	private Map<String,Customer> customers = new HashMap<String,Customer>();
	
	@PostConstruct
	public void initData() {
		log.info("Inserting two customers. Current Map Size: " +  customers.size());
		Customer one = new Customer("gopika2", 23, new Date());
		customers.put(one.getName(), one);
		Customer two = new Customer("george2", 23, new Date());
		customers.put(two.getName(), two);
		log.info("Done inserting customers. Current Map Size: " +  customers.size());
	}

```

Upon start up we something like this:

```shell

7-01-26 14:39:08.208  INFO 75965 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1051 ms
2017-01-26 14:39:08.356  INFO 75965 --- [ost-startStop-1] o.s.b.w.servlet.ServletRegistrationBean  : Mapping servlet: 'dispatcherServlet' to [/]
2017-01-26 14:39:08.359  INFO 75965 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'metricsFilter' to: [/*]
2017-01-26 14:39:08.359  INFO 75965 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'characterEncodingFilter' to: [/*]
2017-01-26 14:39:08.359  INFO 75965 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2017-01-26 14:39:08.359  INFO 75965 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'httpPutFormContentFilter' to: [/*]
2017-01-26 14:39:08.359  INFO 75965 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'requestContextFilter' to: [/*]
2017-01-26 14:39:08.360  INFO 75965 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'webRequestLoggingFilter' to: [/*]
2017-01-26 14:39:08.360  INFO 75965 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'applicationContextIdFilter' to: [/*]
2017-01-26 14:39:08.384  INFO 75965 --- [           main] c.l.boot.controller.CustomerController   : Inserting two customers. Current Map Size: 0
2017-01-26 14:39:08.385  INFO 75965 --- [           main] c.l.boot.controller.CustomerController   : Done inserting customers. Current Map Size: 2
2017-01-26 14:39:08.561  INFO 75965 --- [           main] s.w.s.m.m.a.RequestMappingHandlerAdapter : Looking for @ControllerAdvice: org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@515aebb0: startup date [Thu Jan 26 14:39:07 EST 2017]; root of context hierarchy
2017-01-26 14:39:08.608  INFO 75965 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/v1/customer/{name}]}" onto public org.springframework.http.ResponseEntity<com.lukeshannon.boot.model.Customer> com.lukeshannon.boot.controller.CustomerController.customer(java.lang.String)
2017-01-26 14:39:08.610  INFO 75965 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error]}" onto public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
2017-01-26 14:39:08.610  INFO 75965 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],produces=[text/html]}" onto public org.springframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
2017-01-26 14:39:08.640  INFO 75965 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-01-26 14:39:08.640  INFO 75965 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-01-26 14:39:08.661  INFO 75965 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-01-26 14:39:08.848  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/dump || /dump.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.849  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/mappings || /mappings.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.850  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/env/{name:.*}],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EnvironmentMvcEndpoint.value(java.lang.String)
2017-01-26 14:39:08.850  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/env || /env.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.850  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/health || /health.json],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.HealthMvcEndpoint.invoke(java.security.Principal)
2017-01-26 14:39:08.851  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/info || /info.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.852  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/autoconfig || /autoconfig.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.852  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/heapdump || /heapdump.json],methods=[GET],produces=[application/octet-stream]}" onto public void org.springframework.boot.actuate.endpoint.mvc.HeapdumpMvcEndpoint.invoke(boolean,javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse) throws java.io.IOException,javax.servlet.ServletException
2017-01-26 14:39:08.853  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/configprops || /configprops.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.856  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/metrics/{name:.*}],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.MetricsMvcEndpoint.value(java.lang.String)
2017-01-26 14:39:08.856  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/metrics || /metrics.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.857  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/beans || /beans.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.858  INFO 75965 --- [           main] o.s.b.a.e.mvc.EndpointHandlerMapping     : Mapped "{[/trace || /trace.json],methods=[GET],produces=[application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
2017-01-26 14:39:08.924  INFO 75965 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2017-01-26 14:39:08.929  INFO 75965 --- [           main] o.s.c.support.DefaultLifecycleProcessor  : Starting beans in phase 0
2017-01-26 14:39:09.013  INFO 75965 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 9899 (http)
2017-01-26 14:39:09.016  INFO 75965 --- [           main] c.l.boot.BeanPostConstructorApplication  : Started BeanPostConstructorApplication in 2.184 seconds (JVM running for 2.789)

```
Note the logging now appears well before the container is started.

Our endpoint now looks like this:

```java

@RequestMapping("/customer/{name}")
	public ResponseEntity<Customer> customer(@PathVariable String name) {
		log.info("Searching for : " + name);
		Customer returnValue = customers.get(name);
		if (returnValue  != null) {
			log.info("Found a reference for name: " + name + " = " + returnValue.toString());
			return ResponseEntity.ok(returnValue);
		}
		log.info("Nothing found for name: " + name);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

```
Here we no longer need to reference a static member of a different class. Honestly I think its a bit cleaner.

We can see our results for our good friend George just as before, but with a different key and port: http://localhost:9899/v1/customer/george2




