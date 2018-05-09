@echo off
rem Licensed to the Apache Software Foundation (ASF) under one or more
rem contributor license agreements.  See the NOTICE file distributed with
rem this work for additional information regarding copyright ownership.
rem The ASF licenses this file to You under the Apache License, Version 2.0
rem (the "License"); you may not use this file except in compliance with
rem the License.  You may obtain a copy of the License at
rem
rem     http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.

rem system env
rem **************************************
rem VERTX_HOME=
rem **************************************

setlocal enabledelayedexpansion

IF [%1] EQU [] (
	echo USAGE: %0 classname [opts]
	EXIT /B 1
)

rem Using pushd popd to set BASE_DIR to the absolute path
pushd %~dp0..\..
set BASE_DIR=%CD%
popd
set CLASSPATH=
rem the com-github-app-api use the evn of VERTX_HOME
IF ["%VERTX_HOME%"] EQU [""] (
	set VERTX_HOME=%BASE_DIR%
)
set vertx.home=%VERTX_HOME%
echo "env:VERTX_HOME:="%VERTX_HOME%
rem Classpath addition for release
for %%i in (%BASE_DIR%\libs\*.jar) do (
	call :concat %%i
)

rem JMX settings
IF ["%KAFKA_JMX_OPTS%"] EQU [""] (
	set KAFKA_JMX_OPTS=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false
)

rem JMX port to use
IF ["%JMX_PORT%"] NEQ [""] (
	set KAFKA_JMX_OPTS=%KAFKA_JMX_OPTS% -Dcom.sun.management.jmxremote.port=%JMX_PORT%
)

rem Generic jvm settings you want to add
IF ["%KAFKA_OPTS%"] EQU [""] (
	set KAFKA_OPTS=-Duser.timezone=Asia/Shanghai
)

rem Which java to use
IF ["%JAVA_HOME%"] EQU [""] (
	set JAVA=java
) ELSE (
	set JAVA="%JAVA_HOME%/bin/java"
)

rem Memory options
IF ["%KAFKA_HEAP_OPTS%"] EQU [""] (
	set KAFKA_HEAP_OPTS=-Xmx256M
)

rem JVM performance options
IF ["%KAFKA_JVM_PERFORMANCE_OPTS%"] EQU [""] (
	set KAFKA_JVM_PERFORMANCE_OPTS=-server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+DisableExplicitGC -Djava.awt.headless=true
)

IF ["%CLASSPATH%"] EQU [""] (
	echo Classpath is empty. Please build the project first e.g. by running 'gradlew jarAll'
	EXIT /B 2
)

set COMMAND=%JAVA% %KAFKA_HEAP_OPTS% %KAFKA_JVM_PERFORMANCE_OPTS% %KAFKA_JMX_OPTS% -cp %CLASSPATH% %KAFKA_OPTS% %* %EXTRA_ARGS%
rem echo.
rem echo %COMMAND%
rem echo.

%COMMAND%

goto :eof
:concat
IF ["%CLASSPATH%"] EQU [""] (
  set CLASSPATH="%1"
) ELSE (
  set CLASSPATH=%CLASSPATH%;"%1"
)
