System Rezerwacji Wizyt w Klinice Medycyny Sportowej

createDB.sql - plik do utworzenia bazy danych (ścieżka: ClinicAppointmentReservation/src/main/resource/)
initDB.sql - plik z danymi (ścieżka: ClinicAppointmentReservation/src/main/resource/)

Dane uwierzytelniające:

login       hasło
admin1      Qwerty1!
patient1    Qwerty1!
reception1  Qwerty1!



System informatyczny umożliwiający dokonywanie
rezerwacji wizyt lekarskich w Klinice Medycyny Sportowej. 
Aplikacja posiada cztery poziomy dostępu. Podstawową funkcją systemu jest swobodne dodawanie
rezerwacji wizyt, których dokonywać może Pacjent.
Funkcjonalność zarządzania specjalizacjami dostępnymi w Klinice oraz specjalistami, do których mogą
umawiać się pacjenci została wydzielona dla roli pracownika Recepcji. Użytkownik ten
ma możliwość zarządzania rezerwacjami specjalistów oraz usuwać rezerwacje
poszczególnych pacjentów. Zgodnie z założeniami, system przewiduje samodzielne
rejestrowanie kont przez użytkowników, jednak konta stają się aktywne dopiero
po autoryzowaniu ich przez Administratora. Przed autoryzacją administrator zobowiązany
jest osobiście potwierdzić tożsamość nowo zarejestrowanego użytkownika.
Nieuwierzytelniony użytkownik może zapoznać się z ofertą Kliniki.
System umożliwia dostęp do danych wielu uwierzytelnionym użytkownikom, zachowując przy
tym spójność przetwarzania danych