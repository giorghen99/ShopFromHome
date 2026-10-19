# ShopFromHome 🛒

**ShopFromHome** è un'applicazione Android sviluppata nell'ambito di un progetto universitario di Ingegneria del Software. Permette agli utenti di consultare i prodotti disponibili, organizzare la propria spesa e inviare ordini da ritirare successivamente.

Il progetto è composto da un'applicazione mobile Android e da un backend REST sviluppato con Spring Boot, con persistenza dei dati tramite MySQL.

## ✨ Funzionalità principali

### Utente
- Registrazione e accesso all'applicazione.
- Consultazione dei prodotti e delle categorie.
- Visualizzazione dei dettagli dei prodotti.
- Gestione del carrello.
- Creazione e consultazione degli ordini.
- Visualizzazione dei dettagli degli ordini e del proprio account.

### Gestione del negozio
- Gestione del catalogo dei prodotti.
- Gestione delle categorie.
- Consultazione e gestione degli ordini.
- Visualizzazione dei dettagli degli ordini.

## 🛠️ Tecnologie utilizzate

| Tecnologia | Utilizzo |
|---|---|
| Java | Sviluppo dell'app Android e del backend |
| Android SDK | Applicazione mobile |
| XML | Interfacce e risorse Android |
| Spring Boot | Backend e API REST |
| Spring Data JPA / Hibernate | Persistenza e accesso ai dati |
| MySQL | Database relazionale |
| Spring Security / JWT | Componenti di sicurezza e autenticazione |
| Maven | Gestione delle dipendenze e compilazione del backend |
| Gradle | Configurazione e compilazione dell'app Android |
| Git e GitHub | Versionamento del codice |

## 🏗️ Architettura del progetto

Il repository contiene due componenti principali:

- `app/` — codice sorgente, interfacce e risorse dell'applicazione Android.
- `src/` — backend Spring Boot, organizzato in controller, servizi, repository, modelli e componenti di sicurezza.

File di configurazione principali:

- `pom.xml` — configurazione Maven del backend.
- `app/build.gradle` — configurazione Gradle dell'app Android.
- `src/main/resources/application.properties` — configurazione del backend.

## ⚙️ Avvio del backend

### Requisiti

- JDK 17.
- MySQL.
- Maven Wrapper incluso nel repository.

### 1. Configurare il database

Crea un database MySQL per l'applicazione e configura le seguenti variabili d'ambiente prima di avviare il backend:

- `DB_URL` — URL JDBC del database.
- `DB_USERNAME` — utente MySQL.
- `DB_PASSWORD` — password MySQL.
- `JWT_SECRET` — chiave segreta utilizzata per la firma dei token JWT.

**Non inserire password o chiavi segrete nei file versionati o nel repository pubblico.**

### 2. Avviare il backend su Windows

Apri PowerShell nella cartella principale del progetto e imposta le variabili d'ambiente nella sessione corrente, sostituendo i valori di esempio con quelli del tuo ambiente locale:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/NOME_DATABASE"
$env:DB_USERNAME="TUO_UTENTE"
$env:DB_PASSWORD="TUA_PASSWORD"
$env:JWT_SECRET="INSERISCI_UNA_CHIAVE_SEGRETA_FORTE"
```

Quindi avvia il backend:

```powershell
.\mvnw.cmd spring-boot:run
```

Il backend utilizza la porta `8080`, salvo diversa configurazione.

> L'avvio richiede un database configurato correttamente e le variabili d'ambiente valorizzate. Gli endpoint disponibili e le relative autorizzazioni dipendono dall'implementazione del progetto.

## 📱 Applicazione Android

Il progetto Android si trova nella cartella `app/` e può essere aperto con Android Studio, dopo aver installato una versione compatibile di Android SDK e configurato l'ambiente di sviluppo.

L'applicazione deve poter raggiungere il backend tramite un indirizzo di rete compatibile con il dispositivo o l'emulatore utilizzato. L'indirizzo API potrebbe quindi richiedere una configurazione specifica per l'ambiente locale.

## 🧪 Stato del progetto

Il backend è stato compilato con successo utilizzando Maven e Java 17.

La compilazione dell'app Android non è stata ancora verificata nell'ambiente corrente, perché richiede Android SDK.

## 🎯 Obiettivi formativi

Il progetto ha permesso di lavorare su:

- Sviluppo di applicazioni Android.
- Progettazione e integrazione di API REST.
- Organizzazione di un backend a livelli.
- Interazione con un database relazionale.
- Gestione di autenticazione e autorizzazioni.
- Utilizzo di Git e Maven/Gradle.

---

*Progetto universitario a scopo didattico e di portfolio personale.*