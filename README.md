# RobinPay 💸

> **Der satirische Büro-Pausen- & Meeting-Kosten-Tracker für Android**  
> *(Quiet Quitting, Flurfunk & Klogeld in Echtzeit berechnet)*

RobinPay berechnet und visualisiert in Echtzeit (auf die Sekunde und den Cent genau), wie viel Gehalt während inoffizieller Auszeiten am Arbeitsplatz erwirtschaftet wird. 100 % lokal, datenschutzkonform und ohne Cloud-Zwang.

---

## ✨ Features

- **Live-Lohn-Ticker**: Berechnet sekundengenau deinen Auszeit-Verdienst anhand von Monatsbruttogehalt (€) und Wochenarbeitsstunden (`Monatsstunden ≈ Wochenstunden × 4.3333`, `Lohn/s = (Brutto / Monatsstunden) / 3600`).
- **6 Büro-Auszeit-Kategorien**:
  - 💩 **Klogang**: Das stille Örtchen / Royal Flush
  - ☕ **Flurfunk & Kaffee**: Strategisches Koffein-Networking
  - 🚬 **Raucherpause**: Frischluft-Exkursion & Lagebesprechung
  - 🥱 **Sinnlos-Meeting**: Konzern-Koma & Geldverbrenner
  - ⏳ **IT-Streik & Updates**: Windows updatet... (37%)
  - 🛍️ **Life-Admin & Shopping**: Private Marktrecherche & Paketstatus
- **Sinnlos-Meeting-Geldverbrenner 🔥**: Teilnehmer-Multiplikator berechnet das gesamte kollektiv verbrannte Konzernkapital live.
- **Stealth-Modus / Boss-Key (🚨 CHEF KOMMT!)**: Täuschend echtes Microsoft-Excel-Spreadsheet (Q3 CAPEX/OPEX Financial Forecast), das mit einem Klick das Display tarnt.
- **Karriereleiter & Gamification**:
  - 🐣 Praktikant auf Abwegen (< 15 €)
  - ☕ Flurfunk-Spezialist (< 60 €)
  - 🥷 Quiet-Quitting-Manager (< 200 €)
  - 👻 Konzern-Phantom (< 600 €)
  - 👑 Robin Hood der Wirtschaft (ab 600 €)
- **Naturalien- & Sachwert-Äquivalente**: Rechnet Beute in greifbare Güter um (Döner 🥙, Barista-Kaffee ☕, Pizza 🍕, Kasten Bier 🍺, Tankfüllung ⛽, PS5 🎮, Monatsmiete 🏠).
- **Historie & Auswertung**: Lokale Speicherung mit **Room Database**, Verteilungs-Diagramm (Donut-Chart via Compose Canvas), Notizen und CSV-Export.
- **Slacking-Wrapped**: Generiert einen satirischen „Offiziellen Zeitausgleichs-Beleg“ zum Teilen mit Freunden oder Kollegen.

---

## 🛠️ Tech Stack & Architektur

- **Betriebssystem**: Android (Min SDK 24, Target SDK 36)
- **Sprache**: Kotlin
- **UI Toolkit**: Jetpack Compose mit Material Design 3 (Slate/Tailwind Dark-Theme)
- **Persistenz**: Room Database (SQLite) + SharedPreferences (100% lokal, offline-fähig)
- **Architektur**: Clean Architecture / MVVM (`ViewModel`, `StateFlow`, `Repository`)
- **Unit & UI Tests**: JUnit4 + Robolectric

---

## 🚀 Bauen & Starten mit Android Studio

1. **Repository klonen**:
   ```bash
   git clone https://github.com/DEIN-BENUTZERNAME/robinpay.git
   ```
2. In **Android Studio** öffnen (*File > Open...*).
3. Gradle-Projekt synchronisieren lassen.
4. Auf einem physischen Android-Gerät oder Emulator ausführen (`Run 'app'`).

---

## ⚖️ Satire- & Rechtshinweis

RobinPay ist ein humorvoller Pausen- & Meeting-Kosten-Rechner zu Unterhaltungszwecken. Der Entwickler übernimmt keine Haftung für hitzige Diskussionen mit der Personalabteilung oder plötzliche Beförderungen.
