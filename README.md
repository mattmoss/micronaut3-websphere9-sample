# Notes on Micronaut War Deployed to IBM WebSphere 9

### About

Currently, building a Micronaut application as a war and attempting to run it on IBM WebSphere 9 fails with the following exception.

```
io.micronaut.context.exceptions.NoSuchBeanException: No bean of type [io.micronaut.context.event.ApplicationEventPublisher<io.micronaut.context.event.StartupEvent>] exists.

Make sure the bean is not disabled by bean requirements (enable trace logging for 'io.micronaut.context.condition' to check) and if the bean is enabled then ensure the class is declared a bean and annotation processing is enabled (for Java and Kotlin the 'micronaut-inject-java' dependency should be configured as an annotation processor)
```

This is similar to a [previous issue with Oracle WebLogic](https://github.com/micronaut-projects/micronaut-core/issues/8636).
The primary fault in both cases comes from unexpected quirks in the application server class loaders.
This leads to failures to find and load `BeanDefinitionReference` resources, which leads to a failure to load the `ApplicationEventPublisherFactory` class.
And without the factory class, the `ApplicationEventPublisher` is not available.

### Testing

To test this out, first set `useNetty` in build.gradle to `true` and verify that you can access [the hello world greeting](http://localhost:8080/sample).

Next, set `useNetty` to false and run gradle task `:war` to generate `build/libs/sample-0.1.war`.
Load and prepare the war file on an IBM WebSphere 9 server, and start the application.
Navigating to the same URL (on port 9080, or whatever port you have exposed) should generate the same response.

If you get the above exception, then your version of Micronaut is not patched to work on IBM WebSphere 9.

This project is configured for Micronaut 3.8.10; it is expected to be fixed in 3.8.11.
Once it is, rebuild the project for 3.8.11 (i.e. update gradle.properties), then rebuild and retest the war.

