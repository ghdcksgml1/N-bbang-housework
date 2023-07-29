tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    // 내부 모듈
    implementation(project(":heachi-support:logging"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}