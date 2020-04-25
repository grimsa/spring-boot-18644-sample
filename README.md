# Purpose

This is a small application that can be used to reproduce [Spring Boot #18644](https://github.com/spring-projects/spring-boot/issues/18644) issue.

# Getting Started

To reproduce the issue locally:
- Run `com.example.demo.Application`
- Open Postman, import `spring-boot-18644.postman_collection.json`
- Open "Upload file" request, select some file to be sent in request body as a "file" parameter, execute request

# Expectation

- HTTP 204 response is returned
- Spring app logs show something like `0:0:0:0:0:0:0:1 - - [25/Apr/2020:16:05:54 +0300] "POST /file HTTP/1.1" 204 -`

# Actual results

- HTTP 500 response is returned with the following body:
```
{
    "timestamp": "2020-04-25T13:11:02.367+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Failed to parse multipart servlet request; nested exception is java.io.IOException: org.apache.tomcat.util.http.fileupload.FileUploadException: Stream closed",
    "path": "/file"
}
```
- Spring app logs show stack trace

# Making it work

There are two changes that make it run successfully (one of them is enough):

* A) Go to `build.gradle` and downgrade Spring Boot to 2.1
* B) Leave Spring Boot version as-is (2.2), but go to `com.example.demo.FilterConfiguration` and enable workaround