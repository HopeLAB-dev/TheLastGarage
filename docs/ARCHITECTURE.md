# The Last Garage - Technical Architecture Document

## Overview
**The Last Garage** is a tower defense game built using the **LibGDX** framework. It follows a standard Object-Oriented architecture, leveraging inheritance to manage different types of game entities (Towers, Enemies) and LibGDX's Screen system for state management.

## Project Structure
The project is divided into standard LibGDX modules, with the core logic residing in the `core` module.

### Core Package: `com.kouceng.prolab2`

#### 1. Game Lifecycle (`Prolab2.java`)
- **Role:** The main entry point extending `com.badlogic.gdx.Game`.
- **Responsibility:** Manages the global assets and switches between screens (MainMenu vs GameScreen).

#### 2. User Interface & State (`gui` package)
- **MainMenuScreen:** Handles the initial menu, start/exit buttons, and background rendering.
- **GameScreen:** The main game loop resides here. It manages:
  - The map rendering.
  - Wave management (spawning enemies).
  - Tower placement logic.
  - Collision detection and updating entity states.

#### 3. Game Entities

##### Towers (`kuleler` package)
All towers inherit from the abstract base class `kule.java`.
- **Inheritance Hierarchy:**
  - `kule` (Base Class)
    - `AnahtarMakinesi` (Standard projectile tower)
    - `CiviAgAtar` (Slow effect tower)
    - `YagSizdirici` (AoE damage tower)
- **Mechanics:** Each tower manages its own cooldown, range checking, and projectile (`Mermi`) spawning.

##### Enemies (`dusmanlar` package)
All enemies inherit from the abstract base class `dusman.java`.
- **Inheritance Hierarchy:**
  - `dusman` (Base Class)
    - `MotorluCapulcu` (Basic ground unit)
    - `ZirhliKamyon` (Tanky ground unit)
    - `GozcuUcagi` (Flying unit, ignores some terrain/effects)
- **Pathfinding:** Enemies follow a pre-defined path on the map.

#### 4. Utilities
- **CombatLog (`log` package):** Handles logging of combat events for debugging or UI feedback.

## Key Design Patterns used
- **Game Loop Pattern:** Implemented via LibGDX `render()` method in Screens.
- **Inheritance:** Extensively used for sharing logic between different types of Towers and Enemies.
- **Component-based UI:** Uses LibGDX Scene2D (implied by button handling in screens) or direct SpriteBatch drawing.

## Build System
- **Gradle:** Used for dependency management and building the project across different platforms (Desktop).
- **Java Version:** Java 8 compatibility.
