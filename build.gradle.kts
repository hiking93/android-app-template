buildscript {
    extra.apply {
        set("kotlinVersion", "1.5.31")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val kotlinVersion: String by rootProject.extra
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.create<Delete>("clean") {
    delete(rootProject.buildDir)
}