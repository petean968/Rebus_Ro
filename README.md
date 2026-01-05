Rebus pentru Seniori - Aplicație Android

Magazin Oficial : Google Play Store - Rebus_Ro

https://play.google.com/store/apps/details?id=sandpa.vafex.rebus_ro

Descriere Generală
Această aplicație este un joc de tip rebus dedicat seniorilor, conceput cu o arhitectură care permite actualizarea conținutului fără a necesita publicarea unei noi versiuni în Google Play Store.

Arhitectură și Tehnologii
Proiectul este dezvoltat pentru platforma Android, utilizând următoarele concepte și biblioteci:

Retrofit: Utilizat pentru gestionarea apelurilor API către sursele de date externe.

Manual Dependency Injection: Implementarea manuală a injecției de dependențe pentru menținerea unui control strict asupra ciclului de viață al obiectelor.

Arhitectură decuplată: Logica este separată pentru a asigura testabilitatea și mentenanța codului.

Gestionarea Datelor
Pentru a optimiza fluxul de actualizare a careurilor de rebus, datele sunt stocate extern sub formă de fișiere JSON găzduite pe GitHub Gist.

Avantaj: Permite adăugarea de conținut nou (rebusuri) în timp real, eliminând latența cauzată de procesul de review al Google Play Store.

Proces: Aplicația interoghează Gist-ul prin Retrofit la pornire sau la accesarea ecranului de selecție.

Module și Componente Principale
Codul sursă este structurat în funcție de responsabilități:

MainActivity: Punctul de intrare în aplicație și coordonatorul principal al fluxurilor de navigare.

SelectionScreen: Modulul responsabil pentru afișarea listei de rebusuri disponibile și gestionarea selecției utilizatorului.

RebusScreenModel: Componenta de logică (ViewModel/Model) care gestionează starea jocului, validarea răspunsurilor și procesarea datelor primite din Gist.

Experiența Utilizatorului (UX)
O atenție deosebită a fost acordată feedback-ului vizual și auditiv:

Reward Screen: Un ecran dedicat afișat la finalizarea corectă a unui rebus.

Elemente Culturale: Integrarea unui motiv tricolor românesc în interfața grafică.

Feedback Auditiv: Redarea unei melodii inspirate din folclorul românesc pentru a marca succesul utilizatorului.

Instalare și Rulare
Clonează depozitul.

Configurează URL-ul către Gist-ul JSON în clasa de configurare a rețelei.

Build & Run folosind Android Studio.
