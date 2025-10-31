# Kotlin PID Controller (KMM)

A lightweight and extensible PID controller library written in Kotlin Multiplatform (KMM) — designed to bring precise control algorithms to Android, iOS, and desktop systems with minimal effort.

This project is a refactored and extended version of the original [Simple PID Controller by Mark Lundberg](https://github.com/MarkLundberg/simple-pid).  
While the core logic was inspired by Lundberg’s work, this implementation was rebuilt from scratch in Kotlin, focusing on cross-platform performance, type safety, and developer experience.

---

## ✨ Features

- 🧠 Clean, lightweight, and multiplatform-ready.
- ⚙️ Fully customizable PID gains (`Kp`, `Ki`, `Kd`).
- 🕒 Supports time-based integration and derivative control.
- 🧩 Designed for easy embedding in control loops (motors, sensors, processes, etc.).
- 🔄 Immutable data flow for functional or reactive architectures.
- 📱 Works seamlessly across Android, iOS, and JVM targets.

---

## 🚀 Installation

Available on Maven Central:

```kotlin
dependencies {
    implementation("io.github.murilo-milgiati:kmm-pid-controller:1.1.0")
}
```

Gradle (Kotlin DSL):

```kotlin
implementation("io.github.murilo-milgiati:kmm-pid-controller:1.1.0")
```

---

## 🧮 Example Usage

```kotlin
import com.murilo-migliati.PIDController

fun main() {
    val pid = PIDController(
                        kp = 1.2,
                        ki = 0.8,
                        kd = 0.4,
                        setpoint = 100
)

    var humidity = 80.0
    var pid.call(humidity)

    repeat(5) {
        val output = pid.call(humidity)
        println("PID output: $output")
    }
}
```

---

## ⚖️ License

This project is licensed under the MIT License.  
It includes conceptual work derived from [Mark Lundberg’s Simple PID Controller](https://github.com/MarkLundberg/simple-pid).

```
MIT License

Copyright (c) 2025 ...

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files ...
```

---

## 🧩 Platforms Supported

| Platform | Status | Notes |
|-----------|--------|--------|
| Android | ✅ | Fully supported |
| JVM | ✅ | Compatible with any JVM project |
| iOS | ✅ | Build with KMM native targets |
| macOS / Linux | ✅ | Via Kotlin/Native |

---

## 🧠 Design Philosophy

This library focuses on:
- Cross-platform compatibility through KMM.
- Type safety over float/double ambiguity.
- Deterministic math, for predictable control behavior.
- Modular architecture, enabling extensions or custom backends.

---

## 🧰 Roadmap

- [ ] Unit tests for all platforms  
- [ ] Add time-based tuning utilities  
- [ ] Integrate simulation helpers for embedded systems  
- [ ] Provide Compose multiplatform demo  

---

## 🙌 Credits

- Original idea by Mark Lundberg — [Simple PID Controller](https://github.com/MarkLundberg/simple-pid)  
- Refactored and modernized for Kotlin Multiplatform by Your Name  

---

## 🧑‍💻 Contributing

Contributions are welcome!  
Feel free to open issues or submit pull requests to improve performance, portability, or API clarity.
