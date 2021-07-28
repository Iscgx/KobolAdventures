/*
 * This file was generated by the Gradle 'init' task.
 */
plugins {
    id("KobolAdventures.java-application-conventions")
    java
}

dependencies {
    implementation("org.apache.commons:commons-text")
    val lwjglVersion = "2.9.3"
    implementation("org.lwjgl.lwjgl:lwjgl:$lwjglVersion")
    implementation("org.lwjgl.lwjgl:lwjgl_util:$lwjglVersion")
    implementation("org.lwjgl.lwjgl:lwjgl-platform:$lwjglVersion:natives-windows")
    implementation("org.lwjgl.lwjgl:lwjgl-platform:$lwjglVersion:natives-linux")
    implementation("org.lwjgl.lwjgl:lwjgl-platform:$lwjglVersion:natives-osx")
    implementation("org.slick2d:slick2d-core:1.0.0")
}

application {
    // Define the main class for the application.
    mainClass.set("finalproject.Main")
}
