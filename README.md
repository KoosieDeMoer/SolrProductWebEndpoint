# SolrProductWebEndpoint
REST wrapper for SOLR - implementation of https://www.topcoder.com/challenges/30098526?tab=details

+ **Continuous Integration** - [![Build Status](https://api.travis-ci.org/KoosieDeMoer/SolrProductWebEndpoint.svg?branch=master)](https://travis-ci.com/KoosieDeMoer/SolrProductWebEndpoint)

+ **Test/Code Coverage** - [![codecov.io Code Coverage](https://img.shields.io/codecov/c/gh/KoosieDeMoer/SolrProductWebEndpoint.svg?maxAge=2592000)](https://codecov.io/gh/KoosieDeMoer/SolrProductWebEndpoint/branch/master)

+ ***Security*** - [![Known Vulnerabilities](https://snyk.io/test/github/KoosieDeMoer/SolrProductWebEndpoint/badge.svg)](https://snyk.io/test/github/KoosieDeMoer/SolrProductWebEndpoint)
 - this is all a problem with com.fasterxml.jackson.core:jackson-databind@2.9.8


## Overview
This server was generated by the [swagger-codegen](https://github.com/swagger-api/swagger-codegen) project. By using the 
[OpenAPI-Spec](https://github.com/swagger-api/swagger-core/wiki) from a remote server, you can easily generate a server stub.  This
is an example of building a swagger-enabled JAX-RS server.

This example uses the [JAX-RS](https://jax-rs-spec.java.net/) framework.

To run the server, please execute the following:

```
mvn clean package jetty:run
```

You can then view the swagger listing here:

```
Swagger / OpenAPI v2: http://localhost:8080/swagger.json
```

Note that if you have configured the `host` to be something other than localhost, the calls through
swagger-ui will be directed to that host and not localhost!
