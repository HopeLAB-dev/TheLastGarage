# The Last Garage 🛡️🚛

[![Build LibGDX Project](https://github.com/HopeLAB-dev/TheLastGarage/actions/workflows/gradle.yml/badge.svg)](https://github.com/HopeLAB-dev/TheLastGarage/actions/workflows/gradle.yml)
[![CodeQL](https://github.com/HopeLAB-dev/TheLastGarage/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/HopeLAB-dev/TheLastGarage/actions/workflows/github-code-scanning/codeql)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Windows%20|%20Linux%20|%20MacOS-lightgrey.svg)
![Status](https://img.shields.io/badge/Status-Playable-brightgreen.svg)

[**English Documentation**](#-english-documentation) | [**Türkçe Dokümantasyon**](#-türkçe-dokümantasyon)

---

## 🇬🇧 English Documentation

> **"Protect the last haven of humanity. Tighten the bolts, start the engines, and begin the defense!"**

![Gameplay Demo](assets/gameplay.gif)
*If the GIF doesn't load, please check the 'assets' folder.*

**The Last Garage** is a strategic Tower Defense game set in a post-apocalyptic world. Players must use limited scrap resources to build defensive towers and protect the garage against relentless mechanical enemies.

### 🚀 Download & Play (No Installation Required)

Our game comes with a **Bundled JRE**, meaning you can play it even if you don't have Java installed on your computer!

👉 **[⬇️ Download Latest Version (GitHub Releases)](https://github.com/HopeLAB-dev/TheLastGarage/releases)**

#### 🎮 How to Run?

* **Windows:** Extract `TheLastGarage-Windows.zip` and run **`Prolab2.exe`**.
* **Linux:** Extract `TheLastGarage-Linux.zip` and run **`Prolab2`**. (Use `chmod +x Prolab2` if needed).
* **MacOS:** Extract `TheLastGarage-MacOS.zip` and run **`Prolab2.app`**.

### ✨ Key Features

* **Nail Launcher:** High damage and slows down enemies.
* **Wrench Thrower:** Balanced fire rate and range.
* **Oil Spiller:** Deals Area of Effect (AoE) damage to ground units.
* **Diverse Enemies:** Scouts (Flying), Raiders (Ground), and Heavy Armored Trucks.

> 📘 **Technical Details:** Curious about how it works? Check out our [Technical Architecture Document](docs/ARCHITECTURE.md).

---

## 🇹🇷 Türkçe Dokümantasyon

> **"İnsanlığın son sığınağını koru. Vidaları sık, motorları çalıştır ve savunmaya başla!"**

**The Last Garage**, kıyamet sonrası bir dünyada geçen, stratejik derinliğe sahip bir Kule Savunma (Tower Defense) oyunudur. Oyuncular, ellerindeki sınırlı hurda kaynaklarını kullanarak savunma kuleleri inşa etmeli ve garajı acımasız mekanik düşmanlara karşı korumalıdır.

> 📘 **Teknik Detaylar:** Projenin mimarisi ve çalışma mantığı için [Teknik Mimari Dokümanı](docs/ARCHITECTURE.md) sayfasını inceleyebilirsiniz.

### 🚀 İndir ve Oyna (Kurulumsuz)

Oyunumuz **kendi içinde Java motoruyla (Bundled JRE)** birlikte gelir. Yani bilgisayarınızda Java yüklü olmasa bile indirip hemen oynayabilirsiniz!

👉 **[⬇️ Son Sürümü İndir (GitHub Releases)](https://github.com/HopeLAB-dev/TheLastGarage/releases)**

#### 🎮 Nasıl Çalıştırılır?

* **Windows:** `TheLastGarage-Windows.zip` dosyasını çıkartın ve **`Prolab2.exe`** dosyasını çalıştırın.
* **Linux:** `TheLastGarage-Linux.zip` dosyasını çıkartın ve **`Prolab2`** dosyasını çalıştırın.
* **MacOS:** `TheLastGarage-MacOS.zip` dosyasını çıkartın ve **`Prolab2.app`** uygulamasını başlatın.

---

## 🏰 Game Mechanics / Oyun Mekanikleri

### 🛠️ Defensive Towers / Savunma Kuleleri

| | Name / İsim | Feature / Özellik |
| :---: | :--- | :--- |
| 🔩 | **Wrench Thrower / Anahtar Makinesi** | Balanced Damage & Speed / Dengeli Hasar ve Hız |
| 🕸️ | **Nail Launcher / Çivi Ağ Atar** | Slows down enemies / Düşmanları yavaşlatır |
| 🛢️ | **Oil Spiller / Yağ Sızdırıcı** | Area Damage (AoE) / Alan Hasarı |

### 🤖 Enemy Units / Düşman Birimleri

| Enemy / Düşman | Type / Tip | Weakness / Zayıflık |
| :--- | :--- | :--- |
| **Motorized Raider** | Ground / Kara | Nail Launcher (Slows them down) |
| **Armored Truck** | Tank | High Damage Towers / Yüksek Hasarlı Kuleler |
| **Scout Drone** | Flying / Hava | Cannot be hit by Oil Spiller / Yağ Sızdırıcı vuramaz |

---

## 👨‍💻 Developers / Geliştiriciler

| Name | Role | GitHub |
|---|---|---|
| **Emir Bera Soğuk** | Lead Developer | [@emirberasoguk](https://github.com/emirberasoguk) |
| **Emre Acar** | Lead Developer | [@emreacr](https://github.com/emreacr) |

---

## 🎨 Credits & Attributions

This project uses assets from various sources. Special thanks to:

* **Graphics:** [Gemini Nano Banana Pro]
* **Libraries:** LibGDX Framework

---
*Developed for Kocaeli University - Programming Laboratory II Project.*
