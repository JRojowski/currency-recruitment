<h1 align="center"> Currency Exchange Task</h1> <br>

<p align="center">
  Jan Rojowski
</p>


## Table of Contents

- [Requirements](#requirements)
- [Security](#security)
- [Quick Start](#quick-start)
- [API](#api)
- [Niezrealizowane plany](#Niezrealizowane-plany)


## Requirements
Cześć!

Spełniłem wszystkie założenia funkcjonalne, jak i niefunkcjonalne przedstawione w mejlu.
Dodałem parę wymagań od siebie, żeby było ciekawie, mianowicie:
* użytkownik może mieć więcej niż jedno konto walutowe, ale tylko jedno dla danej waluty,
* aplikacja obsługuje nie tylko USD, ale również EUR i CHF (dodanie obsługi kolejnej waluty wiąże się wyłącznie z dodaniem jej do Currency.enum i zaktualizowaniem kolumny w bazie o dodatkową opcje (specyficznie dla MySQL i kolumny typu enum)),
* dostęp do kont jest zablokowany, szczegóły opisane są w akapicie [Security](#security).
* aplikacja wspiera tylko konta PLN <-> inna waluta, tzn. konto EUR <-> USD jest nieobsługiwane.

## Security
Aplikacja jest zabezpieczona poprzez httpBasic w następujący sposób:
* tworzenia kont jest dostępne bez zabezpieczenia dla wszystkich użytkowników,
* przy tworzeniu konta użytkownik podaje personalId (może to być PESEL albo username),
* wszystkie inne endpointy (wymiana walut oraz podgląd kont) są dostępne tylko dla zalogowanych użytkowników,
* logowanie odbywa się poprzez Basic Auth, gdzie użytkownik podaje personalId oraz hasło,
* hasło na ten moment nie jest walidowane, sprawdzane jest tylko istnienie użytkownika,
* po zalogowaniu użytkownik ma dostęp tylko i wyłącznie do swoich kont.

## Quick Start
Aplikację można uruchomić na 2 sposoby:

### Run Using Docker Image
Będąc w katalogu aplikacji wystarczy wpisać poniższą komendę:
```bash
$ docker compose up
```
Docker postawi bazę MySQL na porcie lokalnym `3306` i aplikację na porcie `8080`.

Nie implementowałem żadnych liveness probe dla tych serwisów,
także aplikacja się zrestartuje 1-2 razy przy próbie podłączenia do bazy,
która jeszcze nie zdążyła się zainicjalizować. Wszystko zadzieje się automatycznie
i za 3 razem podłączy się do niej i uruchomi prawidłowo.

### Run Using main() method

Najpierw wymagane jest postawienie bazy z obraza dockerowego poniższą komendą:
```bash
$ docker run --name currency-task-db \
-e MYSQL_ROOT_PASSWORD=admin \
-e MYSQL_DATABASE=bankDB \
-e MYSQL_USER=admin \
-e MYSQL_PASSWORD=admin \
-p 3306:3306 \
-d mysql:latest
```

Kiedy baza się zainicjalizuje wystarczy uruchomić metodę main() w CurrencyRecruitmentApplication.


## API
Polecam testować API na Swaggerze: http://localhost:8080/swagger-ui/index.html

W RequestBody metody PUT użytkownik podaje walutę oraz kwotę jaką chce wymienić. Np. dla konta PLN <-> USD, jeśli użytkownik posiada 100PLN  i chce wymienić je na USD, to podaje 
{
"currency": "PLN",
"amount": 100
}.

Metoda POST tworzy jednocześnie użytkownika oraz konto walutowe. Użytkownik podaje dane osobowe, walutę konta walutowego
oraz początkowy depozyt w PLN. Jeśli użytkownik o danym personalId już istnieje,
to aplikacja tworzy tylko nowe konto walutowe przypisane do tego użytkownika.


## Niezrealizowane plany
Kolejność przypadkowa:
* implementacja custom exceptions, żeby zwracać poprawne kody wyjątków,
* dodanie walidacji hasła do security,
* test integracyjne sprawdzające rzucane wyjątki,
* dodanie Liquibase lub Flyway do zarządzania zmianami w schemacie bazy danych,
* fix na 1 security vulnerability -> znając username, użytkownik może stworzyć konto walutowe dla innego usera
(trzeba by rozdzielić UserAccountController na User- i AccountController),
* zmniejszenie wielkości obrazu dockerowego używając "chudego" obrazu JDK,
* fix buga -> użytkownik może stworzyć konto PLN <-> PLN, jednak aktualna implementacja NBP nie obsługuje kursu PLN <-> PLN