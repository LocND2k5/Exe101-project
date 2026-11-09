@echo off
rem Chay demo "Day to di" qua localhost, uu tien mo bang Google Chrome (nhan giong noi on dinh nhat)
cd /d "%~dp0"
set URL=http://127.0.0.1:8080/
if exist "%ProgramFiles%\Google\Chrome\Application\chrome.exe" (
  start "" "%ProgramFiles%\Google\Chrome\Application\chrome.exe" %URL%
) else if exist "%ProgramFiles(x86)%\Google\Chrome\Application\chrome.exe" (
  start "" "%ProgramFiles(x86)%\Google\Chrome\Application\chrome.exe" %URL%
) else if exist "%LocalAppData%\Google\Chrome\Application\chrome.exe" (
  start "" "%LocalAppData%\Google\Chrome\Application\chrome.exe" %URL%
) else (
  start "" %URL%
)
echo Dang chay "Day to di" tai %URL%  - dong cua so nay de tat.
java Serve.java . 8080
