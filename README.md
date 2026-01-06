
RebusRebus for Seniors - Android Application
Official Store: Google Play Store - Rebus_Ro
https://play.google.com/store/apps/details?id=sandpa.vafex.rebus_ro

Quick Access to Source Code:

NetworkModule.kt — Networking configuration and Retrofit setup:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/network/NetworkModule.kt

RebusApiService.kt — API interface for fetching data from GitHub Gist:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/network/NetworkModule.kt

RebusScreen.kt — The main game screen where the grid is displayed:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/ui/RebusScreen.kt

RebusViewModel.kt — Logic for game state, validation, and data processing:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/ui/RebusViewModel.kt

RewardScreen.kt — The completion screen with cultural motifs and music:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/ui/RewardScreen.kt

SelectionScreen.kt — UI and logic for the crossword selection screen:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/ui/SelectionScreen.kt

RebusData.kt — Data models for parsing JSON content:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/vafex/rebus_ro/data/RebusData.kt

RebusRepository.kt — Manages data operations and acts as a bridge between the API and ViewModel:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/vafex/rebus_ro/data/RebusRepository.kt

MainActivity.kt — The main entry point of the application:
https://github.com/petean968/Rebus_Ro/blob/main/app/src/main/java/sandpa/vafex/rebus_ro/MainActivity.kt

General Description
This application is a crossword (rebus) game specifically designed for seniors. It features a dynamic 
architecture that allows content updates without the need to publish a new version to the 
 Google Play Store.

Architecture & Technologies
The project is developed for the Android platform using the following concepts and libraries:

Retrofit: Used for managing API calls to external data sources.

Manual Dependency Injection: Implemented manually to maintain strict control over object lifecycles.

Decoupled Architecture: Business logic is separated from the UI to ensure code testability and 
maintainability.

Data Management
To optimize the update flow for crossword grids, data is stored externally as JSON files hosted on 
GitHub Gist.

Advantage: Allows for the addition of new content (crosswords) in real-time, eliminating the latency 
caused by the Google Play Store review process.

Process: The app queries the Gist via Retrofit upon startup or when accessing the selection screen.

Main Modules and Components
The source code is structured by responsibilities:

MainActivity: The application's entry point and primary coordinator for navigation flows.

SelectionScreen: The module responsible for displaying the list of available crosswords and handling 
user selection.

RebusScreenModel: The logic component (ViewModel/Model) that manages game state, answer validation, 
and processing data received from the Gist.

User Experience (UX)
Special attention was paid to visual and auditory feedback:

Reward Screen: A dedicated screen displayed upon the successful completion of a crossword.

Cultural Elements: Integration of a Romanian tricolor motif within the graphical interface.

Auditory Feedback: Playback of a melody inspired by Romanian folklore to mark the user's success.

Configure the JSON Gist URL in the network configuration class.

Build & Run using Android Studio. for Seniors - Android Application




Rebus pentru Seniori - Aplicație Android

Magazin Oficial : Google Play Store - Rebus_Ro



Descriere Generală
Această aplicație este un joc de tip rebus dedicat seniorilor, conceput cu o arhitectură care
permite actualizarea conținutului fără a necesita publicarea unei noi versiuni în Google Play Store.

Arhitectură și Tehnologii
Proiectul este dezvoltat pentru platforma Android, utilizând următoarele concepte și biblioteci:

Retrofit: Utilizat pentru gestionarea apelurilor API către sursele de date externe.

Manual Dependency Injection: Implementarea manuală a injecției de dependențe pentru menținerea 
unui control strict asupra ciclului de viață al obiectelor.

Arhitectură decuplată: Logica este separată pentru a asigura testabilitatea și mentenanța codului.

Gestionarea Datelor
Pentru a optimiza fluxul de actualizare a careurilor de rebus, datele sunt stocate extern sub 
formă de fișiere JSON găzduite pe GitHub Gist.

Avantaj: Permite adăugarea de conținut nou (rebusuri) în timp real, eliminând latența cauzată de 
procesul de review al Google Play Store.

Proces: Aplicația interoghează Gist-ul prin Retrofit la pornire sau la accesarea ecranului 
de selecție.

Module și Componente Principale
Codul sursă este structurat în funcție de responsabilități:

MainActivity: Punctul de intrare în aplicație și coordonatorul principal al fluxurilor de navigare.

SelectionScreen: Modulul responsabil pentru afișarea listei de rebusuri disponibile și gestionarea 
selecției utilizatorului.

RebusScreenModel: Componenta de logică (ViewModel/Model) care gestionează starea jocului, validarea 
răspunsurilor și procesarea datelor primite din Gist.

Experiența Utilizatorului (UX)
O atenție deosebită a fost acordată feedback-ului vizual și auditiv:

Reward Screen: Un ecran dedicat afișat la finalizarea corectă a unui rebus.

Elemente Culturale: Integrarea unui motiv tricolor românesc în interfața grafică.

Feedback Auditiv: Redarea unei melodii inspirate din folclorul românesc pentru a marca succesul 
utilizatorului.



Configurează URL-ul către Gist-ul JSON în clasa de configurare a rețelei.

Build & Run folosind Android Studio.
