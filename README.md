#  Weather

A clean, lightweight Android weather application built in Java.


[![GitHub Release](https://img.shields.io/github/v/release/Shikusu/Weather?style=flat-square)](https://github.com/Shikusu/Weather/releases/latest)

## Download

Grab the latest APK from the [Releases page](https://github.com/Shikusu/Weather/releases/latest).

## ScreenShots
<div align="center">
  <table>
    <tr>
      <td><img width="180" src="https://github.com/user-attachments/assets/76d0c8d6-e660-4d84-a922-e2be593ceb13" /></td>
      <td><img width="180" src="https://github.com/user-attachments/assets/27931108-e598-4406-91c9-b071146e653e" /></td>
      <td><img width="180" src="https://github.com/user-attachments/assets/72d8cbd3-7608-45e7-ab95-dc1787e9174e" /></td>
      <td><img width="180" src="https://github.com/user-attachments/assets/e6382ffa-c7fc-4ea3-a33d-dcc53cdcd2b5" /></td>
      <td><img width="180" src="https://github.com/user-attachments/assets/7ba1b242-0f92-4a7d-94d9-d7870639ca9a" /></td>
    </tr>
  </table>
</div>
##  Features

- Current weather conditions for any location
- Temperature, humidity, wind speed, and more for the user's specific location
- Clean and intuitive user interface

##  Tech Stack

- **Language:** Java
- **Platform:** Android
- **Build System:** Gradle (Kotlin DSL)
- **Weather Data:** Open-meteo

##  Requirements

- Android Studio (Hedgehog or later recommended)
- Android SDK Min SDK 21, Target SDK 36
- Java 11+

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Shikusu/Weather.git
cd Weather
```

### 2. Open in Android Studio

Open the project folder in Android Studio and let Gradle sync automatically.

### 3. Run the app

Select a device or emulator and click **Run ▶**.

## Project Structure

```
Weather/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/         # Java source files
│       │   ├── res/          # Layouts, drawables, strings
│       │   └── AndroidManifest.xml
│       └── test/             # Unit tests
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Contributing

Contributions are welcome! Feel free to open an issue or submit a pull request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

