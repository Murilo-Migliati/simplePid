# Kotlin PID Controller (KMP)

A lightweight and extensible PID controller library written in **Kotlin Multiplatform (KMP)** — designed to bring precise control algorithms to Android, iOS, and desktop systems with minimal effort.

This project is a refactored and extended version of the original **Simple PID Controller** by *Mark Lundberg*.  
While the core logic was inspired by Lundberg’s work, this implementation was rebuilt from scratch in Kotlin, focusing on cross-platform performance, type safety, and developer experience.

---

## ✨ Features

- 🧠 Clean, lightweight, and multiplatform-ready.  
- ⚙️ Fully customizable PID gains (`kp`, `ki`, `kd`).  
- 🕒 Supports time-based integration and derivative control.  
- 🧩 Designed for easy embedding in control loops (motors, sensors, processes, etc.).  
- 🔄 Built-in support for anti-windup, output limits, and auto-mode.  
- 📱 Works seamlessly across Android, iOS, and JVM targets.

---

## 🚀 Installation

Available on **Maven Central**:

### Maven
```xml
<dependency>
    <groupId>io.github.murilo-migliati</groupId>
    <artifactId>controllerPidKmp</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Gradle (Groovy DSL)
```groovy
implementation "io.github.murilo-migliati:controllerPidKmp:1.0.1"
```

### Gradle (Kotlin DSL)
```kotlin
implementation("io.github.murilo-migliati:controllerPidKmp:1.0.1")
```

> ✅ **Note**: Always check the latest version on [Maven Central](https://central.sonatype.com/search?q=io.github.murilo-migliati).

---

## 🧮 Example Usage

```kotlin
// The correct import (based on your 'namespace')
import com.murilomigliati.pid.PIDController

fun main() {
    val pid = PIDController(
        kp = 1.2,
        ki = 0.8,
        kd = 0.4,
        setpoint = 100.0
    )

    var humidity = 80.0
    
    // Simulates a control loop
    repeat(5) {
        val output = pid.call(humidity)
        println("PID output: $output")
        
        // Simulates humidity changing based on the output
        if (output != null) {
            humidity += output / 10.0 
        }
    }
}
```

---

## ⚖️ License

This project is licensed under the **MIT License**.  
It includes conceptual work derived from Mark Lundberg’s Simple PID Controller.

```
MIT License

Copyright (c) 2025 Murilo Migliati

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files ...
```

---

## 🧩 Platforms Supported

| Platform | Status | Notes |
|-----------|---------|-------|
| Android | ✅ | Fully supported |
| JVM | ✅ | Compatible with any JVM project |
| iOS | ✅ | Build with KMM native targets |
| macOS / Linux | ✅ | Via Kotlin/Native |

---

## 🧠 Design Philosophy

This library focuses on:

- Cross-platform compatibility through KMM.  
- Type safety using `Double`.  
- Deterministic math, for predictable control behavior.  
- Modular architecture, enabling extensions or custom backends.

---

## 🛣️ Roadmap

- [x] Unit tests for all platforms  
- [ ] Add time-based tuning utilities  
- [ ] Integrate simulation helpers for embedded systems  
- [ ] Provide Compose multiplatform demo  

---

## 🙌 Credits

Original idea by **Mark Lundberg** — *Simple PID Controller*  
Refactored and modernized for Kotlin Multiplatform by **Murilo Migliati**

---

## 🧑‍💻 Contributing

Contributions are welcome!  
Feel free to open issues or submit pull requests to improve performance, portability, or API clarity.
