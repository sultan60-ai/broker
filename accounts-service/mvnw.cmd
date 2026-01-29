@ECHO OFF
SETLOCAL
SET BASEDIR=%~dp0
SET WRAPPER_DIR=%BASEDIR%\.mvn\wrapper
SET WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar
SET PROPS_FILE=%WRAPPER_DIR%\maven-wrapper.properties

FOR /F "usebackq tokens=1,* delims==" %%A IN ("%PROPS_FILE%") DO (
  IF "%%A"=="wrapperUrl" SET WRAPPER_URL=%%B
)

IF NOT DEFINED WRAPPER_URL (
  SET WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar
)

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO Downloading Maven Wrapper...
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "(New-Object Net.WebClient).DownloadFile('%WRAPPER_URL%','%WRAPPER_JAR%')"
)

IF DEFINED JAVA_HOME (
  SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
) ELSE (
  SET JAVA_EXE=java
)

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory="%BASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
ENDLOCAL
