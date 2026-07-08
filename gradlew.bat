@rem
@rem Gradle startup script for Windows
@rem

@if "%DEBUG%"=="" @echo off
@setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME%\bin\java.exe
) else (
    set JAVA_EXE=java.exe
    if exist "%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe" set JAVA_EXE=%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe
)

if exist "%JAVA_EXE%" goto execute
echo.
echo ERROR: Java was not found. Set JAVA_HOME or install/open Android Studio, then run this command again.
echo.
exit /b 1

:execute
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% -classpath "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*

@endlocal
