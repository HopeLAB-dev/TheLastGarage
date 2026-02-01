# The Last Garage - Technical Architecture Document

[**English**](#english) | [**Türkçe**](#türkçe)

---

<a name="english"></a>
## 🇬🇧 English

### Overview
**The Last Garage** is a tower defense game built using the **LibGDX** framework. It follows a standard Object-Oriented architecture, leveraging inheritance to manage different types of game entities (Towers, Enemies) and LibGDX's Screen system for state management.

### Project Structure
The project is divided into standard LibGDX modules, with the core logic residing in the `core` module.

#### Core Package: `com.kouceng.prolab2`

##### 1. Game Lifecycle (`Prolab2.java`)
- **Role:** The main entry point extending `com.badlogic.gdx.Game`.
- **Responsibility:** Manages the global assets and switches between screens (MainMenu vs GameScreen).

##### 2. User Interface & State (`gui` package)
- **MainMenuScreen:** Handles the initial menu, start/exit buttons, and background rendering.
- **GameScreen:** The main game loop resides here. It manages:
  - The map rendering.
  - Wave management (spawning enemies).
  - Tower placement logic.
  - Collision detection and updating entity states.

##### 3. Game Entities

###### Towers (`kuleler` package)
All towers inherit from the abstract base class `kule.java`.
- **Inheritance Hierarchy:**
  - `kule` (Base Class)
    - `AnahtarMakinesi` (Standard projectile tower)
    - `CiviAgAtar` (Slow effect tower)
    - `YagSizdirici` (AoE damage tower)
- **Mechanics:** Each tower manages its own cooldown, range checking, and projectile (`Mermi`) spawning.

###### Enemies (`dusmanlar` package)
All enemies inherit from the abstract base class `dusman.java`.
- **Inheritance Hierarchy:**
  - `dusman` (Base Class)
    - `MotorluCapulcu` (Basic ground unit)
    - `ZirhliKamyon` (Tanky ground unit)
    - `GozcuUcagi` (Flying unit, ignores some terrain/effects)
- **Pathfinding:** Enemies follow a pre-defined path on the map.

##### 4. Utilities
- **CombatLog (`log` package):** Handles logging of combat events for debugging or UI feedback.

### Key Design Patterns used
- **Game Loop Pattern:** Implemented via LibGDX `render()` method in Screens.
- **Inheritance:** Extensively used for sharing logic between different types of Towers and Enemies.
- **Component-based UI:** Uses LibGDX Scene2D (implied by button handling in screens) or direct SpriteBatch drawing.

### Build System
- **Gradle:** Used for dependency management and building the project across different platforms (Desktop).
- **Java Version:** Java 8 compatibility.

---

<a name="türkçe"></a>
## 🇹🇷 Türkçe

### Genel Bakış
**The Last Garage**, **LibGDX** çatısı kullanılarak geliştirilmiş bir kule savunma oyunudur. Standart Nesne Yönelimli (Object-Oriented) mimariyi takip eder; farklı oyun varlıklarını (Kuleler, Düşmanlar) yönetmek için kalıtımdan ve durum yönetimi için LibGDX'in Screen sisteminden yararlanır.

### Proje Yapısı
Proje standart LibGDX modüllerine ayrılmıştır ve temel mantık `core` modülünde bulunur.

#### Çekirdek Paketi: `com.kouceng.prolab2`

##### 1. Oyun Yaşam Döngüsü (`Prolab2.java`)
- **Rol:** `com.badlogic.gdx.Game` sınıfını genişleten ana giriş noktasıdır.
- **Sorumluluk:** Global varlıkları (assets) yönetir ve ekranlar arası geçişi (Ana Menü vs Oyun Ekranı) sağlar.

##### 2. Kullanıcı Arayüzü ve Durum (`gui` paketi)
- **MainMenuScreen:** Başlangıç menüsünü, başlat/çıkış butonlarını ve arka plan çizimini yönetir.
- **GameScreen:** Ana oyun döngüsü burada bulunur. Şunları yönetir:
  - Harita çizimi.
  - Dalga (Wave) yönetimi (düşmanların oluşturulması).
  - Kule yerleştirme mantığı.
  - Çarpışma tespiti ve varlık durumlarının güncellenmesi.

##### 3. Oyun Varlıkları

###### Kuleler (`kuleler` paketi)
Tüm kuleler soyut `kule.java` temel sınıfından türer.
- **Kalıtım Hiyerarşisi:**
  - `kule` (Temel Sınıf)
    - `AnahtarMakinesi` (Standart mermi atan kule)
    - `CiviAgAtar` (Yavaşlatma etkili kule)
    - `YagSizdirici` (Alan hasarı - AoE veren kule)
- **Mekanikler:** Her kule kendi bekleme süresini (cooldown), menzil kontrolünü ve mermi (`Mermi`) oluşturma işlemini yönetir.

###### Düşmanlar (`dusmanlar` paketi)
Tüm düşmanlar soyut `dusman.java` temel sınıfından türer.
- **Kalıtım Hiyerarşisi:**
  - `dusman` (Temel Sınıf)
    - `MotorluCapulcu` (Temel kara birimi)
    - `ZirhliKamyon` (Dayanıklı kara birimi)
    - `GozcuUcagi` (Uçan birim, bazı zemin/etkileri yok sayar)
- **Yol Bulma (Pathfinding):** Düşmanlar harita üzerinde önceden tanımlanmış bir yolu takip eder.

##### 4. Araçlar
- **CombatLog (`log` paketi):** Hata ayıklama veya kullanıcı arayüzü geri bildirimi için savaş olaylarının kaydını tutar.

### Kullanılan Temel Tasarım Desenleri
- **Oyun Döngüsü Deseni (Game Loop Pattern):** Ekranlardaki (Screens) LibGDX `render()` metodu ile uygulanmıştır.
- **Kalıtım (Inheritance):** Farklı Kule ve Düşman türleri arasında mantık paylaşımı için kapsamlı bir şekilde kullanılmıştır.
- **Bileşen Tabanlı UI:** LibGDX Scene2D (ekranlardaki buton kullanımı ile ima edilmektedir) veya doğrudan SpriteBatch çizimi kullanır.

### Derleme Sistemi
- **Gradle:** Bağımlılık yönetimi ve projenin farklı platformlarda (Masaüstü) derlenmesi için kullanılır.
- **Java Sürümü:** Java 8 uyumluluğu.